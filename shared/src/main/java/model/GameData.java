package model;

public record GameData(int gameID, String whiteUser, String blackUser, String gameName, chess.ChessGame game) {
}
