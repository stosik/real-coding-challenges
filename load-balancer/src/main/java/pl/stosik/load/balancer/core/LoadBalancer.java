package pl.stosik.load.balancer.core;

import pl.stosik.load.balancer.configuration.Configuration;
import pl.stosik.load.balancer.exception.MaxNumberOfServersReachedException;
import pl.stosik.load.balancer.server.Request;
import pl.stosik.load.balancer.server.Server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class LoadBalancer {

    private final Configuration configuration;

    protected ConcurrentMap<String, Server> servers;

    public LoadBalancer(Configuration configuration, Map<String, Server> servers) throws MaxNumberOfServersReachedException {
        if (servers.size() > configuration.maxServers()) {
            throw new MaxNumberOfServersReachedException("Max servers limit reached");
        }
        this.configuration = configuration;
        this.servers = new ConcurrentHashMap<>(servers);
    }

    public void registerServer(Server server) throws MaxNumberOfServersReachedException {
        if (servers.size() < configuration.maxServers()) {
            servers.putIfAbsent(server.ipAddress(), server);
        } else {
            throw new MaxNumberOfServersReachedException("Max servers limit reached");
        }
    }

    public void deregisterServer(Server server) {
        servers.remove(server.ipAddress());
    }

    public List<Server> getServers() {
        return servers.values().stream().toList();
    }

    public abstract Server getServer(Request request);
}
