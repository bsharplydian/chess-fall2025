package ui;

import chess.ChessPiece;
import chess.ChessPosition;

public class InGameValidator extends Validator {
    public ChessPosition readPosition(String position) throws SyntaxException{
        int col = switch(position.substring(0, 1)) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default ->
                    throw new SyntaxException("column not formatted properly: " + position);
        };
        int row;
        try {
            row = Integer.parseInt(position.substring(1, 2));
        } catch (NumberFormatException e) {
            throw new SyntaxException("row not formatted properly: " + position);
        }
        if(row < 1 || row > 8) {
            throw new SyntaxException("row should be between 1 and 8: " + position);
        }
        return new ChessPosition(row, col);
    }

    public ChessPiece.PieceType readPiece(String piece) throws SyntaxException {
        return switch(piece.toLowerCase()){
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new SyntaxException("not a valid piece type");
        };
    }
}
