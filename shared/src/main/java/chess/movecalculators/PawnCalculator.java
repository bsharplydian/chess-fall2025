package chess.movecalculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnCalculator implements MovementCalculator {

    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        return new HashSet<>(getPawnMoves(board, myPosition, myColor));
    }
    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition,
                                               ChessGame.TeamColor myColor) {
        Collection<ChessMove> moveSet = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int pawnDirection = switch(myColor) {
            case WHITE -> 1;
            case BLACK -> -1;
        };
        ChessPosition firstSquare = new ChessPosition(myRow + pawnDirection, myCol);
        ChessPosition secondSquare = new ChessPosition(myRow + 2*pawnDirection, myCol);
        ChessPosition westCaptureSquare = new ChessPosition(myRow + pawnDirection, myCol - 1);
        ChessPosition eastCaptureSquare = new ChessPosition(myRow + pawnDirection, myCol + 1);

        // forward 1
        if(board.isEmptyAt(firstSquare)){
            moveSet.addAll(getPawnMovesByRank(myPosition, firstSquare, myColor));

            // forward 2
            if((myRow == 2 || myRow == 7) && board.isEmptyAt(secondSquare)) {
                moveSet.add(new ChessMove(myPosition, secondSquare, null));
            }
        }
        // capture west
        if(!westCaptureSquare.outOfBounds() &&
                !board.isEmptyAt(westCaptureSquare) &&
                ChessPiece.areEnemies(myColor, board.getPiece(westCaptureSquare).getTeamColor())) {
            moveSet.addAll(getPawnMovesByRank(myPosition, westCaptureSquare, myColor));
        }
        // capture east
        if(!eastCaptureSquare.outOfBounds() &&
                !board.isEmptyAt(eastCaptureSquare) &&
                ChessPiece.areEnemies(myColor, board.getPiece(eastCaptureSquare).getTeamColor())) {
            moveSet.addAll(getPawnMovesByRank(myPosition, eastCaptureSquare, myColor));
        }
        return moveSet;
    }

    private Collection<ChessMove> getPawnMovesByRank(ChessPosition myPosition,
                                                     ChessPosition newPosition, ChessGame.TeamColor myColor) {
        Collection<ChessMove> moveSet = new HashSet<>();
        if((newPosition.getRow() == 8 && myColor == ChessGame.TeamColor.WHITE) ||
                (newPosition.getRow() == 1 && myColor == ChessGame.TeamColor.BLACK)){
            moveSet.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
            moveSet.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
            moveSet.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
            moveSet.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        } else {
            moveSet.add(new ChessMove(myPosition, newPosition, null));
        }
        return moveSet;
    }
}
