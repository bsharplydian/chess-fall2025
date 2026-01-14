package server.websocket;

public class BadAuthException extends RuntimeException {
    public BadAuthException(String message) {
        super(message);
    }
}
