package service.requests;

public record JoinGameRequest(String authToken, chess.ChessGame.TeamColor playerColor, int gameID) {
}
