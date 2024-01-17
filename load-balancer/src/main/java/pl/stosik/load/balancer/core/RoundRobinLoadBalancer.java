package pl.stosik.load.balancer.core;

import pl.stosik.load.balancer.configuration.Configuration;
import pl.stosik.load.balancer.exception.MaxNumberOfServersReachedException;
import pl.stosik.load.balancer.server.Request;
import pl.stosik.load.balancer.server.Server;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class RoundRobinLoadBalancer extends LoadBalancer implements LoadBalancingStrategy {
    private int counter;
    private final ReentrantReadWriteLock lock;
    private final WriteLock writeLock;

    public RoundRobinLoadBalancer(Configuration configuration, Map<String, Server> servers) throws MaxNumberOfServersReachedException {
        super(configuration, servers);
        this.counter = 0;
        this.lock = new ReentrantReadWriteLock(true);
        this.writeLock = lock.writeLock();
    }

    @Override
    public Server getServer(Request request) {
        writeLock.lock();
        try {
            Server server = servers.values().stream().toList().get(counter);
            counter += 1;
            if (counter == servers.size()) {
                counter = 0;
            }
            return server;
        } finally {
            writeLock.unlock();
        }
    }
}
