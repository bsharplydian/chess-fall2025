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
        final Direction[] DIAGONALS = new Direction[]{
                Direction.NE,
                Direction.SE,
                Direction.SW,
                Direction.NW};
        final Direction[] CARDINALS = new Direction[]{
                Direction.N,
                Direction.E,
                Direction.S,
                Direction.W};
        final int[][] KING_MOVES = new int[][]{
                {0, 1},
                {1, 1},
                {1, 0},
                {1, -1},
                {0, -1},
                {-1, -1},
                {-1, 0},
                {-1, 1}
        };
        final int[][] KNIGHT_MOVES = new int[][]{
                {1, 2},
                {2, 1},
                {2, -1},
                {1, -2},
                {-1, -2},
                {-2, -1},
                {-2, 1},
                {-1, 2}
        };
        switch(piece.getPieceType()) {
            case QUEEN -> {
                MovementCalculator queenCalculator = new QueenCalculator();
                moveSet.addAll(queenCalculator.pieceMoves(board, myPosition));
//                moveSet.addAll(getMovesByLines(board, myPosition,
//                        DIAGONALS,
//                        myColor));
//                moveSet.addAll(getMovesByLines(board, myPosition,
//                        CARDINALS,
//                        myColor));
            }
            case BISHOP -> moveSet.addAll(getMovesByLines(board, myPosition,
                    DIAGONALS,
                    myColor));
            case ROOK -> moveSet.addAll(getMovesByLines(board, myPosition,
                    CARDINALS,
                    myColor));
            case KING -> moveSet.addAll(getMovesBySet(board, myPosition, KING_MOVES, myColor));
            case KNIGHT -> moveSet.addAll(getMovesBySet(board, myPosition, KNIGHT_MOVES, myColor));
            case PAWN -> moveSet.addAll(getMovesByPawn(board, myPosition, myColor));
        }
        return moveSet;
    }

    /**
     * Given a board state, starting point, direction, and piece color
     * calculates all moves in a straight line,
     * being blocked by a same-color piece
     * and capturing a different-color piece
     */
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
        while(isLegalSquare(board, nextSquare, myColor)) {
            moveSet.add(new ChessMove(myPosition, nextSquare, null));
            ChessPiece otherPiece = board.getPiece(nextSquare);
            if(otherPiece != null && areEnemies(otherPiece.getTeamColor(), myColor)) {
                break; // ensures that this only gets the first opposing piece in the line
            }
            nextSquare = new ChessPosition(nextSquare.getRow() + addX, nextSquare.getColumn()+addY);
        }

        return moveSet;
    }

    private Collection<ChessMove> getMovesByLines(ChessBoard board, ChessPosition myPosition,
                                                  Direction[] dirs, ChessGame.TeamColor myColor) {
        Collection<ChessMove> moveSet = new HashSet<>();
        for (Direction dir : dirs) {
            moveSet.addAll(getMovesByLine(board, myPosition, dir, myColor));
        }
        return moveSet;
    }

    private Collection<ChessMove> getMovesBySet(ChessBoard board, ChessPosition myPosition,
                                                int[][] relativePositions, ChessGame.TeamColor myColor) {
        Collection<ChessMove> legalMoves = new HashSet<>();

        for(int[] position : relativePositions) {
            int addX = position[0];
            int addY = position[1];
            ChessPosition square = new ChessPosition(myPosition.getRow() + addX,
                    myPosition.getColumn() + addY);
            if(isLegalSquare(board, square, myColor)){
                legalMoves.add(new ChessMove(myPosition, square, null));
            }
        }
        return legalMoves;
    }

    private Collection<ChessMove> getMovesByPawn(ChessBoard board, ChessPosition myPosition,
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
                areEnemies(myColor, board.getPiece(westCaptureSquare).getTeamColor())) {
            moveSet.addAll(getPawnMovesByRank(myPosition, westCaptureSquare, myColor));
        }
        // capture east
        if(!eastCaptureSquare.outOfBounds() &&
                !board.isEmptyAt(eastCaptureSquare) &&
                areEnemies(myColor, board.getPiece(eastCaptureSquare).getTeamColor())) {
            moveSet.addAll(getPawnMovesByRank(myPosition, eastCaptureSquare, myColor));
        }
        return moveSet;
    }

    private Collection<ChessMove> getPawnMovesByRank(ChessPosition myPosition,
                                                     ChessPosition newPosition, ChessGame.TeamColor myColor) {
        Collection<ChessMove> moveSet = new HashSet<>();
        if((newPosition.getRow() == 8 && myColor == ChessGame.TeamColor.WHITE) ||
                (newPosition.getRow() == 1 && myColor == ChessGame.TeamColor.BLACK)){
             moveSet.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
            moveSet.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
            moveSet.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
            moveSet.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
        } else {
            moveSet.add(new ChessMove(myPosition, newPosition, null));
        }
        return moveSet;
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
