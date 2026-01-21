package server.websocket.messagedata;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import chess.ChessMove;

public record NotificationData(String username, TeamColor color, ChessGame game, ChessMove move, String opponentUsername) {
}
