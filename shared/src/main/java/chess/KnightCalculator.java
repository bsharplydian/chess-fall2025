package chess;

import java.util.Collection;
import java.util.HashSet;


public class KnightCalculator extends SetCalculator {
    final int[][] KNIGHT_MOVES = new int[][]{
            {1, 2},
            {2, 1},
            {2, -1},
            {1, -2},
            {-1, -2},
            {-2, -1},
            {-2, 1},
            {-1, 2}
    };
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        return new HashSet<>(getMovesBySet(board, myPosition, KNIGHT_MOVES, myColor));
    }
}
