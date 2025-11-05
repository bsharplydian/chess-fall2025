package model.requests;

public record JoinGameRequest(chess.ChessGame.TeamColor playerColor, int gameID) {
}
