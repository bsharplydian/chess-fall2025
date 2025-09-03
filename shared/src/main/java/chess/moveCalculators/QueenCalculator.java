package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenCalculator extends LineCalculator {
    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> moveSet = new HashSet<>();
        moveSet.addAll(getMovesByLines(board, myPosition,
                DIAGONALS,
                myColor));
        moveSet.addAll(getMovesByLines(board, myPosition,
                CARDINALS,
                myColor));
        return moveSet;
    }
}
