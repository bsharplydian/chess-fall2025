package serverfacade;

import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
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

    public void startServerConnection() throws HttpResponseException {
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
    public void connectToGame(String authToken, int gameID, ChessGame.TeamColor color) throws HttpResponseException {
        try {
            UserGameCommand command = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, color, gameID);
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
