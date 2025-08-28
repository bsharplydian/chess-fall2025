package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingCalculator extends SetCalculator {
    final int[][] KING_MOVES = new int[][]{
            {0, 1},
            {1, 1},
            {1, 0},
            {1, -1},
            {0, -1},
            {-1, -1},
            {-1, 0},
            {-1, 1}
    };
    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        return new HashSet<>(getMovesBySet(board, myPosition, KING_MOVES, myColor));
    }
}
