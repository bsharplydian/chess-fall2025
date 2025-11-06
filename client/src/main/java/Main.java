import chess.*;
import model.requests.RegisterRequest;
import serverfacade.ServerFacade;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        facade.register(new RegisterRequest("jim", "jim", "jim@jim.com"));
    }
}