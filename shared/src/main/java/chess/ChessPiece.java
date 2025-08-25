package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    public String toString() {
        return pieceColor.toString() + " " + pieceType.toString();
    }
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

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

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor myColor = piece.getTeamColor();
        Collection<ChessMove> moveSet = new HashSet<>();
        final Direction[] DIAGONALS = new Direction[]{Direction.NE, Direction.SE, Direction.SW, Direction.NW};
        final Direction[] CARDINALS = new Direction[]{Direction.N, Direction.E, Direction.S, Direction.W};
        switch(piece.getPieceType()) {
            case QUEEN -> {
                moveSet.addAll(lineMovesMultiple(board, myPosition,
                        DIAGONALS,
                        myColor));
                moveSet.addAll(lineMovesMultiple(board, myPosition,
                        CARDINALS,
                        myColor));
            }
            case BISHOP -> moveSet.addAll(lineMovesMultiple(board, myPosition,
                    DIAGONALS,
                    myColor));
            case ROOK -> moveSet.addAll(lineMovesMultiple(board, myPosition,
                    CARDINALS,
                    myColor));
        }
        return moveSet;
    }

    /**
     * Given a board state, starting point, direction, and piece color
     * calculates all moves in a straight line,
     * being blocked by a same-color piece
     * and capturing a different-color piece
     */
    private Collection<ChessMove> lineMoves(ChessBoard board, ChessPosition myPosition,
                                            Direction dir,  ChessGame.TeamColor myColor) {
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
        while(legalSquare(board, nextSquare, myColor)) {
            moveSet.add(new ChessMove(myPosition, nextSquare, null));
            ChessPiece otherPiece = board.getPiece(nextSquare);
            if(otherPiece != null && areEnemies(otherPiece.getTeamColor(), myColor)) {
                break; // ensures that this only gets the first opposing piece in the line
            }
            nextSquare = new ChessPosition(nextSquare.getRow() + addX, nextSquare.getColumn()+addY);
        }

        return moveSet;
    }
    private Collection<ChessMove> lineMovesMultiple(ChessBoard board, ChessPosition myPosition,
                                                    Direction[] dirs, ChessGame.TeamColor myColor) {
        Collection<ChessMove> moveSet = new HashSet<>();
        for (Direction dir : dirs) {
            moveSet.addAll(lineMoves(board, myPosition, dir, myColor));
        }
        return moveSet;
    }
    private boolean legalSquare(ChessBoard board, ChessPosition nextSquare, ChessGame.TeamColor myColor) {
        if(nextSquare.outOfBounds()) {
            return false;
        }
        ChessPiece otherPiece = board.getPiece(nextSquare);
        if(otherPiece == null) {
            return true;
        } else {
            return areEnemies(otherPiece.getTeamColor(), myColor);
        }
    }
    private boolean areEnemies(ChessGame.TeamColor color1, ChessGame.TeamColor color2) {
        return color1 != color2;
    }
}
