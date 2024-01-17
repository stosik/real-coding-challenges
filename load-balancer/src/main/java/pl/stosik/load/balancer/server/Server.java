package pl.stosik.load.balancer.server;

import java.util.Optional;

public record Server(String ipAddress, int weight) implements Comparable<Server> {
    public Server(String ipAddress, int weight) {
        this.ipAddress = ipAddress;
        this.weight = Optional.ofNullable(weight).orElse(1);
    }

    @Override
    public int compareTo(Server o) {
        return Integer.compare(this.weight, o.weight());
    }
}
