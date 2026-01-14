package server.websocket;

public class BadGameIDException extends RuntimeException {
    public BadGameIDException(String message) {
        super(message);
    }
}
