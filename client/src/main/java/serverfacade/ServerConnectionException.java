package serverfacade;

public class ServerConnectionException extends RuntimeException {
    public String message;
    public ServerConnectionException(String message) {
        super(message);
        this.message = message;
    }
}
