package pl.stosik.load.balancer;

import org.junit.jupiter.api.Test;
import pl.stosik.load.balancer.configuration.LoadBalancingMode;
import pl.stosik.load.balancer.core.LoadBalancer;
import pl.stosik.load.balancer.core.RandomLoadBalancer;
import pl.stosik.load.balancer.exception.MaxNumberOfServersReachedException;
import pl.stosik.load.balancer.server.Request;
import pl.stosik.load.balancer.server.Server;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LoadBalancerTest {

    @Test
    public void shouldProperlyBuildLoadBalancer() throws MaxNumberOfServersReachedException {
        // given
        Server server = new Server("127.0.0.1", 1);

        // when
        LoadBalancer loadBalancer = new LoadBalancerFactory
                .LoadBalancerBuilder()
                .maxAcceptedServers(10)
                .servers(Map.of(server.ipAddress(), server))
                .mode(LoadBalancingMode.RANDOM)
                .build();

        // then
        assertTrue(loadBalancer instanceof RandomLoadBalancer);
        assertEquals(loadBalancer.getServers(), List.of(server));
    }

    @Test
    public void shouldNotAllowToBuildLoadBalancerWithMoreThanMaxServers() {
        // when
        MaxNumberOfServersReachedException exception = assertThrows(MaxNumberOfServersReachedException.class, () -> {
            new LoadBalancerFactory
                    .LoadBalancerBuilder()
                    .maxAcceptedServers(1)
                    .servers(
                            Map.of(
                                    "127.0.0.1", new Server("127.0.0.1", 1),
                                    "127.0.0.2", new Server("127.0.0.2", 1)
                            )
                    )
                    .mode(LoadBalancingMode.RANDOM)
                    .build();
        });

        // then
        assertTrue(exception.getMessage().contains("Max servers limit reached"));
    }

    @Test
    public void shouldNotAllowToRegisterForLoadBalancerServerWhenMaxServerAmountReached() throws MaxNumberOfServersReachedException {
        // given
        LoadBalancer loadBalancer = new LoadBalancerFactory
                .LoadBalancerBuilder()
                .maxAcceptedServers(1)
                .servers(Map.of("127.0.0.1", new Server("127.0.0.1", 1)))
                .mode(LoadBalancingMode.RANDOM)
                .build();

        // when
        MaxNumberOfServersReachedException exception = assertThrows(MaxNumberOfServersReachedException.class, () -> {
            loadBalancer.registerServer(new Server("127.0.0.2", 1));
        });

        // then
        assertTrue(exception.getMessage().contains("Max servers limit reached"));
    }

    @Test
    public void shouldNotAllowToRegisterSameServerMultipleTimes() throws MaxNumberOfServersReachedException {
        // given
        LoadBalancer loadBalancer = LoadBalancerFactory
                .builder()
                .maxAcceptedServers(10)
                .servers(Map.of("127.0.0.1", new Server("127.0.0.1", 1)))
                .mode(LoadBalancingMode.RANDOM)
                .build();

        // when
        loadBalancer.registerServer(new Server("127.0.0.1", 1));

        // then
        assertEquals(loadBalancer.getServers().size(), 1);
    }

    @Test
    public void shouldRandomlyRouteRequestToServersWhenUsingRandomBalancingStrategy() throws MaxNumberOfServersReachedException {
        // given
        LoadBalancer loadBalancer = LoadBalancerFactory
                .builder()
                .maxAcceptedServers(10)
                .servers(Map.of("127.0.0.1", new Server("127.0.0.1", 1)))
                .mode(LoadBalancingMode.RANDOM)
                .build();

        Request request = new Request("payload");

        // when
        Server server = loadBalancer.getServer(request);

        // then
        assertEquals(server.ipAddress(), "127.0.0.1");
    }

    @Test
    public void shouldRouteRequestInRoundRobinFashionWhenUsingRoundRobingBalancingStrategy() throws MaxNumberOfServersReachedException {
        // given
        LoadBalancer loadBalancer = LoadBalancerFactory
                .builder()
                .maxAcceptedServers(10)
                .servers(
                        Map.of(
                                "127.0.0.1", new Server("127.0.0.1", 1),
                                "127.0.0.2", new Server("127.0.0.2", 1)
                        )
                )
                .mode(LoadBalancingMode.ROUND_ROBIN)
                .build();

        Request request = new Request("payload");

        // when
        Server firstServer = loadBalancer.getServer(request);
        Server secondServer = loadBalancer.getServer(request);
        Server thirdServer = loadBalancer.getServer(request);

        // then
        assertEquals(firstServer.ipAddress(), "127.0.0.2");
        assertEquals(secondServer.ipAddress(), "127.0.0.1");
        assertEquals(thirdServer.ipAddress(), "127.0.0.2");

    }

    @Test
    public void shouldRouteRequestAccordingToServerWeightsWhenUsingWRRBalancingStrategy() throws MaxNumberOfServersReachedException {
        // given
        LoadBalancer loadBalancer = LoadBalancerFactory
                .builder()
                .maxAcceptedServers(10)
                .servers(
                        Map.of(
                                "127.0.0.1", new Server("127.0.0.1", 1),
                                "127.0.0.2", new Server("127.0.0.2", 2)
                        )
                )
                .mode(LoadBalancingMode.WEIGHTED_ROUND_ROBIN)
                .build();

        Request request = new Request("payload");

        // when
        Server firstServer = loadBalancer.getServer(request);
        Server secondServer = loadBalancer.getServer(request);
        Server thirdServer = loadBalancer.getServer(request);

        // then
        assertEquals("127.0.0.2", firstServer.ipAddress());
        assertEquals("127.0.0.2", secondServer.ipAddress());
        assertEquals("127.0.0.1", thirdServer.ipAddress());
    }
}
