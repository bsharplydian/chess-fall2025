package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenCalculator extends LineCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
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
