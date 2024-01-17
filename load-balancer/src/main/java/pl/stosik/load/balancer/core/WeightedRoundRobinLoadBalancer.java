package pl.stosik.load.balancer.core;

import pl.stosik.load.balancer.configuration.Configuration;
import pl.stosik.load.balancer.exception.MaxNumberOfServersReachedException;
import pl.stosik.load.balancer.server.Request;
import pl.stosik.load.balancer.server.Server;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeightedRoundRobinLoadBalancer extends RoundRobinLoadBalancer implements LoadBalancingStrategy {

    public WeightedRoundRobinLoadBalancer(Configuration configuration, Map<String, Server> servers) throws MaxNumberOfServersReachedException {
        super(configuration, createWeightedMap(servers));
    }

    @Override
    public Server getServer(Request request) {
        return super.getServer(request);
    }

    private static Map<String, Server> createWeightedMap(Map<String, Server> servers) {
        return servers
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .flatMap(entry -> IntStream.rangeClosed(1, entry.getValue().weight()).mapToObj(i -> createEntry(entry, i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private static Map.Entry<String, Server> createEntry(Map.Entry<String, Server> entry, int index) {
        return Map.entry(entry.getValue().ipAddress() + "-" + index, new Server(entry.getValue().ipAddress(), entry.getValue().weight()));
    }
}
