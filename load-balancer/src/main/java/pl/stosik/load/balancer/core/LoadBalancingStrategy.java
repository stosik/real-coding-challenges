package pl.stosik.load.balancer.core;

import pl.stosik.load.balancer.server.Request;
import pl.stosik.load.balancer.server.Server;

public interface LoadBalancingStrategy {

    Server getServer(Request request);
}
