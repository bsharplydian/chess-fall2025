package websocket.commands;

import chess.ChessGame.TeamColor;

public class ConnectCommand extends UserGameCommand {
    public ConnectCommand(CommandType commandType, String authToken, TeamColor color, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}
