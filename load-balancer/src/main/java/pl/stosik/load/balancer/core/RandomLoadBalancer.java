package pl.stosik.load.balancer.core;

import pl.stosik.load.balancer.configuration.Configuration;
import pl.stosik.load.balancer.exception.MaxNumberOfServersReachedException;
import pl.stosik.load.balancer.server.Request;
import pl.stosik.load.balancer.server.Server;

import java.util.Map;
import java.util.Random;

public class RandomLoadBalancer extends LoadBalancer implements LoadBalancingStrategy {

    private final Random random = new Random();

    public RandomLoadBalancer(Configuration configuration, Map<String, Server> servers) throws MaxNumberOfServersReachedException {
        super(configuration, servers);
    }

    @Override
    public Server getServer(Request request) {
        Object[] values = servers.values().toArray();
        return (Server) values[random.nextInt(values.length)];
    }
}
