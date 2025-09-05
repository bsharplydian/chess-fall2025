package chess;

import chess.moveCalculators.*;

import java.util.Collection;
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
    public ChessPiece(ChessPiece copy) {
        this.pieceColor = copy.pieceColor;
        this.pieceType = copy.pieceType;
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
//        return pieceColor.toString() + " " + pieceType.toString();
        String str = switch(getPieceType()) {
            case KING -> "k";
            case QUEEN -> "q";
            case BISHOP -> "b";
            case KNIGHT -> "n";
            case ROOK -> "r";
            case PAWN -> "p";
        };
        if(getTeamColor() == ChessGame.TeamColor.WHITE){
            str = str.toUpperCase();
        }
        return str;
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
        PieceType pieceType = board.getPiece(myPosition).getPieceType();
        MovementCalculator pieceCalculator = switch(pieceType) {
            case QUEEN -> new QueenCalculator();
            case BISHOP -> new BishopCalculator();
            case ROOK -> new RookCalculator();
            case KING -> new KingCalculator();
            case KNIGHT -> new KnightCalculator();
            case PAWN -> new PawnCalculator();
        };
        return pieceCalculator.getPieceMoves(board, myPosition);
    }

    public static boolean isLegalSquare(ChessBoard board, ChessPosition square, ChessGame.TeamColor myColor) {
        if(square.outOfBounds()) {
            return false;
        }
        ChessPiece otherPiece = board.getPiece(square);
        if(otherPiece == null) {
            return true;
        } else {
            return areEnemies(otherPiece.getTeamColor(), myColor);
        }
    }

    public static boolean areEnemies(ChessGame.TeamColor color1, ChessGame.TeamColor color2) {
        return color1 != color2;
    }
}
