package chess;

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
    private int coord0to1(int coord) {
        return coord + 1;
    }
    private int coord1to0(int coord) {
        return coord - 1;
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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

    }
}
