package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookCalculator extends LineCalculator {
    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        return new HashSet<>(getMovesByLines(board, myPosition,
                CARDINALS,
                myColor));
    }
}
