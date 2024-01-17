package pl.stosik.load.balancer;

import pl.stosik.load.balancer.configuration.Configuration;
import pl.stosik.load.balancer.configuration.LoadBalancingMode;
import pl.stosik.load.balancer.core.LoadBalancer;
import pl.stosik.load.balancer.core.RandomLoadBalancer;
import pl.stosik.load.balancer.core.RoundRobinLoadBalancer;
import pl.stosik.load.balancer.core.WeightedRoundRobinLoadBalancer;
import pl.stosik.load.balancer.exception.MaxNumberOfServersReachedException;
import pl.stosik.load.balancer.server.Server;

import java.util.Map;

public class LoadBalancerFactory {

    private LoadBalancerFactory() {
    }

    public static class LoadBalancerBuilder {
        private int maxAcceptedServers;

        private LoadBalancingMode balancingMode;

        private Map<String, Server> servers;

        public LoadBalancerBuilder mode(LoadBalancingMode balancingMode) {
            this.balancingMode = balancingMode;
            return this;
        }

        public LoadBalancerBuilder maxAcceptedServers(int maxAcceptedServers) {
            this.maxAcceptedServers = maxAcceptedServers;
            return this;
        }

        public LoadBalancerBuilder servers(Map<String, Server> servers) {
            this.servers = servers;
            return this;
        }

        public LoadBalancer build() throws MaxNumberOfServersReachedException {
            return switch (balancingMode) {
                case RANDOM -> new RandomLoadBalancer(
                        new Configuration(maxAcceptedServers), servers
                );
                case ROUND_ROBIN -> new RoundRobinLoadBalancer(
                        new Configuration(maxAcceptedServers), servers
                );
                case WEIGHTED_ROUND_ROBIN -> new WeightedRoundRobinLoadBalancer(
                        new Configuration(maxAcceptedServers), servers
                );
            };
        }
    }

    public static LoadBalancerBuilder builder() {
        return new LoadBalancerBuilder();
    }
}
