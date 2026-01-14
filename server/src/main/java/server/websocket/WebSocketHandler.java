package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.UserService;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    ConnectionManager connectionManager;
    UserService userService;
    GameService gameService;
    GameDAO gameDAO;
    UserDAO userDAO;
    AuthDAO authDAO;


    public WebSocketHandler(UserService userService, GameService gameService, GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.userService = userService;
        this.gameService = gameService;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.connectionManager = new ConnectionManager(authDAO, gameDAO);
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
        System.out.println("trying to connect to game " + command.getGameID());
        connectionManager.addToGame(command, context.session);
    }
    private void makeMove(WsMessageContext context) throws IOException, DataAccessException {

    }


}
