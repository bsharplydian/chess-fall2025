package serverfacade;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    String url;
    MessageHandler messageHandler;
    public WebSocketFacade(String url, MessageHandler messageHandler) {
        this.url = url.replace("http", "ws");
        this.messageHandler = messageHandler;
    }

    public void startServerConnection() throws HttpResponseException {
        try {
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new jakarta.websocket.MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String s) {
                    ServerMessage message = new Gson().fromJson(s, ServerMessage.class);
                    switch(message.getServerMessageType()) {
                        case ServerMessage.ServerMessageType.NOTIFICATION -> {
                            NotificationMessage notificationMessage = new Gson().fromJson(s, NotificationMessage.class);
                            messageHandler.handleMessage(notificationMessage);
                        }
                        case ServerMessage.ServerMessageType.LOAD_GAME -> {
                            LoadGameMessage loadGameMessage = new Gson().fromJson(s, LoadGameMessage.class);
                            messageHandler.handleMessage(loadGameMessage);
                        }
                        case ServerMessage.ServerMessageType.ERROR -> {
                            ErrorMessage errorMessage = new Gson().fromJson(s, ErrorMessage.class);
                            messageHandler.handleMessage(errorMessage);
                        }
                    };
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new HttpResponseException(ex.getMessage());
        }
    }
    public void connectToGame(String authToken, int gameID) throws HttpResponseException {
        try {
            UserGameCommand command = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new HttpResponseException(e.getMessage());
        }
    }
    public void makeMove() {

    }
    public void leave() {

    }
    public void resign() {

    }

    public void closeServerConnection() throws HttpResponseException {
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
