package pl.stosik.load.balancer.exception;

public class MaxNumberOfServersReachedException extends Throwable {
    public MaxNumberOfServersReachedException() {
    }

    public MaxNumberOfServersReachedException(String message) {
        super(message);
    }
}
