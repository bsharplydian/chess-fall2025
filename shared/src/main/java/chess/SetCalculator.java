package chess;

import java.util.Collection;
import java.util.HashSet;

public abstract class SetCalculator implements MovementCalculator {

    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    protected Collection<ChessMove> getMovesBySet(ChessBoard board, ChessPosition myPosition,
                                                int[][] relativePositions, ChessGame.TeamColor myColor) {
        Collection<ChessMove> legalMoves = new HashSet<>();

        for(int[] position : relativePositions) {
            int addX = position[0];
            int addY = position[1];
            ChessPosition square = new ChessPosition(myPosition.getRow() + addX,
                    myPosition.getColumn() + addY);
            if(ChessPiece.isLegalSquare(board, square, myColor)){
                legalMoves.add(new ChessMove(myPosition, square, null));
            }
        }
        return legalMoves;
    }
}
