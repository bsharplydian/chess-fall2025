package serverfacade;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    String url;
    public WebSocketFacade(String url) {
        this.url = url.replace("http", "ws");
    }

    public void makeConnection() throws HttpResponseException {
        try {
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String s) {
                    ServerMessage message = new Gson().fromJson(s, ServerMessage.class);
                    System.out.println(message.toString());
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new HttpResponseException(ex.getMessage());
        }
    }

    public void closeConnection() throws HttpResponseException {
        try {
            this.session.close();
        } catch (IOException e) {
            throw new HttpResponseException(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
