package chess.movecalculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public abstract class LineCalculator implements MovementCalculator {
    public Collection<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        return new HashSet<>(getMovesByLines(board, myPosition,
                getDirections(),
                myColor));
    }
    public abstract Direction[] getDirections();
    public enum Direction {
        N,
        NE,
        E,
        SE,
        S,
        SW,
        W,
        NW
    }
    public static final Direction[] DIAGONALS = new Direction[]{
            Direction.NE,
            Direction.SE,
            Direction.SW,
            Direction.NW
    };
    public static final Direction[] CARDINALS = new Direction[]{
            Direction.N,
            Direction.E,
            Direction.S,
            Direction.W
    };

    private Collection<ChessMove> getMovesByLine(ChessBoard board, ChessPosition myPosition,
                                                 Direction dir, ChessGame.TeamColor myColor) {
        int addX = switch(dir) {
            case N, S -> 0;
            case NE, E, SE -> 1;
            case SW, W, NW -> -1;
        };
        int addY = switch(dir) {
            case N, NE, NW -> 1;
            case E, W -> 0;
            case SE, S, SW -> -1;
        };
        Collection<ChessMove> moveSet = new HashSet<>();

        ChessPosition nextSquare = new ChessPosition(myPosition.getRow() + addX,
                myPosition.getColumn() + addY);
        while(ChessPiece.isLegalSquare(board, nextSquare, myColor)) {
            moveSet.add(new ChessMove(myPosition, nextSquare, null));
            ChessPiece otherPiece = board.getPiece(nextSquare);
            if(otherPiece != null && ChessPiece.areEnemies(otherPiece.getTeamColor(), myColor)) {
                break; // ensures that this only gets the first opposing piece in the line
            }
            nextSquare = new ChessPosition(nextSquare.getRow() + addX, nextSquare.getColumn()+addY);
        }

        return moveSet;
    }

    protected Collection<ChessMove> getMovesByLines(ChessBoard board, ChessPosition myPosition,
                                                  Direction[] dirs, ChessGame.TeamColor myColor) {
        Collection<ChessMove> moveSet = new HashSet<>();
        for (Direction dir : dirs) {
            moveSet.addAll(getMovesByLine(board, myPosition, dir, myColor));
        }
        return moveSet;
    }
}

