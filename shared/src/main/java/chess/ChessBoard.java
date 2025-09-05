package chess;
import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] pieces =  new ChessPiece[8][8];
    public ChessBoard() {

    }
    public ChessBoard(ChessBoard copyBoard) {
        for(int row = 0; row < copyBoard.pieces.length; row++) {
            for(int col = 0; col < copyBoard.pieces.length; col++) {
                if (copyBoard.pieces[row][col] != null) {
                    this.pieces[row][col] = new ChessPiece(copyBoard.pieces[row][col]);
                } else {
                    this.pieces[row][col] = null;
                }
            }
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }

    @Override
    public String toString() {
        StringBuilder piecesOut = new StringBuilder("");
        for(int i = 7; i >= 0; i--){
            for(int j = 0; j < 8; j++) {
                piecesOut.append("|");
                ChessPiece piece = pieces[i][j];
                if(piece == null) {
                    piecesOut.append(" ");
                } else {
                    piecesOut.append(piece);
                }
            }
            piecesOut.append("|\n");
        }

        return piecesOut.toString();
//        return Arrays.toString(pieces);
    }
    public boolean isEmptyAt(ChessPosition square) {
        if(square.outOfBounds()){
            return false;
        }
        return getPiece(square) == null;
    }
    private int coord1to0(int coord) {
        return coord - 1;
    }
    private int coord0to1(int coord) {
        return coord + 1;
    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = coord1to0(position.getRow());
        int col = coord1to0(position.getColumn());
        pieces[row][col] = piece;
    }
    /**
     * without checking for validity, moves a piece from one square to another, replacing the piece at the end.
     * @param move move to be executed
     * @param piece piece to be moved
     */
    public void movePiece(ChessMove move, ChessPiece piece) {
        addPiece(move.getEndPosition(), piece);
        addPiece(move.getStartPosition(), null);
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = coord1to0(position.getRow());
        int col = coord1to0(position.getColumn());
        return pieces[row][col];
    }
    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces.length; col++) {
                if(pieces[row][col] == null) {
                    continue;
                }
                if(pieces[row][col].getPieceType() == ChessPiece.PieceType.KING
                && pieces[row][col].getTeamColor() == teamColor) {
                    return new ChessPosition(coord0to1(row), coord0to1(col));
                }
            }
        }
        return null;
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        pieces[0][0] = new ChessPiece(WHITE, ROOK);
        pieces[0][1] = new ChessPiece(WHITE, KNIGHT);
        pieces[0][2] = new ChessPiece(WHITE, BISHOP);
        pieces[0][3] = new ChessPiece(WHITE, QUEEN);
        pieces[0][4] = new ChessPiece(WHITE, KING);
        pieces[0][5] = new ChessPiece(WHITE, BISHOP);
        pieces[0][6] = new ChessPiece(WHITE, KNIGHT);
        pieces[0][7] = new ChessPiece(WHITE, ROOK);
        for(int i = 0; i < 8; i++){
            pieces[1][i] = new ChessPiece(WHITE, PAWN);
        }

        pieces[7][0] = new ChessPiece(BLACK, ROOK);
        pieces[7][1] = new ChessPiece(BLACK, KNIGHT);
        pieces[7][2] = new ChessPiece(BLACK, BISHOP);
        pieces[7][3] = new ChessPiece(BLACK, QUEEN);
        pieces[7][4] = new ChessPiece(BLACK, KING);
        pieces[7][5] = new ChessPiece(BLACK, BISHOP);
        pieces[7][6] = new ChessPiece(BLACK, KNIGHT);
        pieces[7][7] = new ChessPiece(BLACK, ROOK);
        for(int i = 0; i < 8; i++){
            pieces[6][i] = new ChessPiece(BLACK, PAWN);
        }
    }
}
