package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopCalculator extends LineCalculator {
    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        return new HashSet<>(getMovesByLines(board, myPosition,
                DIAGONALS,
                myColor));
    }
}
