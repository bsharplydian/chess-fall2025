package chess;

import java.util.Collection;

public interface MovementCalculator {
    Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition);
}

