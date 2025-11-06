package serverfacade;

public class HttpResponseException extends Exception {
    public String message;
    public HttpResponseException(String message) {
        super(message);
        this.message = message;
    }
}
