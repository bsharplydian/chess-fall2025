package service.requests;

public record JoinGameRequest(chess.ChessGame.TeamColor playerColor, int gameID) {
}
