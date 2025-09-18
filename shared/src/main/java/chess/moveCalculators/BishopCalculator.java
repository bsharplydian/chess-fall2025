package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopCalculator extends LineCalculator {
    @Override
    public Direction[] getDirections() {
        return DIAGONALS;
    }
}
