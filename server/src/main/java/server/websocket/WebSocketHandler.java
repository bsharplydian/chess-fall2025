package server.websocket;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.UserService;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import static websocket.messages.ServerMessage.ServerMessageType.*;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    ConnectionManager connectionManager = new ConnectionManager();
    UserService userService;
    GameService gameService;
    GameDAO gameDAO;
    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handleClose(@NotNull WsCloseContext context) throws Exception {
        System.out.println("websocket closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        System.out.println("websocket connected");
        context.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext context) throws Exception {
        try {
            UserGameCommand command = new Gson().fromJson(context.message(), UserGameCommand.class);
            switch(command.getCommandType()) {
                case LEAVE -> leave(context);
                case RESIGN -> resign(context);
                case CONNECT -> connect(context, new Gson().fromJson(context.message(), ConnectCommand.class));
                case MAKE_MOVE -> makeMove(context);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void leave(WsMessageContext context) throws IOException {

    }
    private void resign(WsMessageContext context) throws IOException {

    }
    private void connect(WsMessageContext context, ConnectCommand command) throws IOException {

    }
    private void makeMove(WsMessageContext context) throws IOException, DataAccessException {

    }
}
