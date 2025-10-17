package passoff.chess.game;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.chess.TestUtilities;

import java.util.ArrayList;
import java.util.List;

public class IsInCheckTests {

    @Test
    @DisplayName("Checked by Bishop")
    public void bishopCheck() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | |B| | | | | | |
                    | | | | | |K| | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |k| | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.BLACK);
    }

    @Test
    @DisplayName("Checked by Rook")
    public void rookCheck() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | |R| | | |k| | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.BLACK);
    }

    @Test
    @DisplayName("Checked by Queen (Diagonal)")
    public void queenCheckDiag() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | | |Q| | | | |
                    | | | | | | | | |
                    | | | | | |k| | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.BLACK);
    }

    @Test
    @DisplayName("Checked by Queen (Cardinal)")
    public void queenCheckCard() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | |Q| | | |k| | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.BLACK);
    }

    @Test
    @DisplayName("White checked by pawn (left)")
    public void whiteCheckedPawnLeft() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | |p| | | |
                    | | | | | |K| | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |k| | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.WHITE);
    }

    @Test
    @DisplayName("White checked by pawn (right)")
    public void whiteCheckedPawnRight() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | |p| |
                    | | | | | |K| | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |k| | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.WHITE);
    }

    @Test
    @DisplayName("Black Checked by Pawn (left)")
    public void blackCheckedPawnLeft() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | |k| | | | | |
                    | |P| | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.BLACK);
    }

    @Test
    @DisplayName("Black Checked by Pawn (right))")
    public void blackCheckedPawnRight() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | |k| | | | | |
                    | | | |P| | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    """));

        assert game.isInCheck(ChessGame.TeamColor.BLACK);
    }

    @Test
    @DisplayName("Can capture queen")
    public void queenCheckCapture() {

        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(TestUtilities.loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | |Q|k| | |
                    | | | | | | | | |
                    """));

        ChessPosition kingPosition = new ChessPosition(2, 6);
        var validMoves = TestUtilities.loadMoves(kingPosition, new int[][]{
                {2, 5}, {1, 7}, {3, 7}
        });

        var generatedMoves = game.validMoves(kingPosition);
        var actualMoves = new ArrayList<>(generatedMoves);
        TestUtilities.validateMoves(validMoves, actualMoves);
    }
}
