package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import static chess.moveCalculators.LineCalculator.CARDINALS;
import static chess.moveCalculators.LineCalculator.DIAGONALS;

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

    private boolean isThreatenedDiagonals() {
        for(LineCalculator.Direction dir : DIAGONALS) {
            if(isThreatenedLine(dir)) {
                return true;
            }
        }
        return false;
    }
    private boolean isThreatenedCardinals() {
        for(LineCalculator.Direction dir : CARDINALS) {
            if(isThreatenedLine(dir)) {
                return true;
            }
        }
        return false;
    }

    private boolean isThreatenedLine(LineCalculator.Direction dir) {
        throw new RuntimeException("Not Implemented");
    }

}
