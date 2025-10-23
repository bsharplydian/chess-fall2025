package model;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName) {
    // need to make a Simple Game Data object to store this info, then change Game Data to also include the ChessGame
}
