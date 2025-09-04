package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

public class CheckCalculator {
    private final ChessBoard board;
    private final ChessGame.TeamColor myTeamColor;
    private final ChessPosition kingPosition;

    public CheckCalculator(ChessBoard board, ChessGame.TeamColor myTeamColor, ChessPosition kingPosition) {
        this.board = board;
        this.myTeamColor = myTeamColor;
        this.kingPosition = kingPosition;
    }

    public boolean isInCheck() {
        throw new RuntimeException("Not Implemented");
    }

}
