package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface MovementCalculator {
    Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition);
}

