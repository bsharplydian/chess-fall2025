package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class BoardPrinter {
    private String lightSquareColor = SET_BG_COLOR_LIGHT_GREY;
    private String darkSquareColor = SET_BG_COLOR_DARK_GREEN;
    private String whitePieceColor = SET_TEXT_COLOR_WHITE;
    private String blackPieceColor = SET_TEXT_COLOR_BLACK;
    private String coordBackground = SET_BG_COLOR_BLACK;
    private String coordForeground = SET_TEXT_COLOR_BLUE;

    public String printBoard(ChessBoard board, ChessGame.TeamColor color) {
        return switch(color) {
            case WHITE -> printBoardWhite(board);
            case BLACK -> printBoardBlack(board);
        };
    }

    private String printBoardBlack(ChessBoard board) {
        StringBuilder boardString = new StringBuilder();
        boardString.append(coordForeground + coordBackground + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n");
        for(int row = 1; row <= 8; row++) {
            boardString.append(String.format("%s%s %d ", coordForeground, coordBackground, row));
            for(int col = 8; col >=1; col--) {
                boardString.append(printCell(board.getPiece(new ChessPosition(row, col)), row, col));
//                boardString.append(new ChessPosition(row, col));
            }
            boardString.append(String.format("%s%s %d ", coordForeground, coordBackground, row));
            boardString.append(RESET_BG_COLOR + "\n");
        }
        boardString.append(coordForeground + coordBackground + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n");
        return boardString.toString();
    }

    private String printBoardWhite(ChessBoard board) {
        StringBuilder boardString = new StringBuilder();
        boardString.append(coordForeground + coordBackground + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n");
        for(int row = 8; row >= 1; row--) {
            boardString.append(String.format("%s%s %d ", coordForeground, coordBackground, row));
            for(int col = 1; col <=8; col++) {
                boardString.append(printCell(board.getPiece(new ChessPosition(row, col)), row, col));
//                boardString.append(new ChessPosition(row, col));
            }
            boardString.append(String.format("%s%s %d ", coordForeground, coordBackground, row));
            boardString.append(RESET_BG_COLOR + "\n");
        }
        boardString.append(coordForeground + coordBackground + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n");
        return boardString.toString();
    }

    private String printCell(ChessPiece piece, int row, int col) {
        StringBuilder cell = new StringBuilder();
        if((row + col) % 2 == 1) {
            cell.append(lightSquareColor);
        } else {
            cell.append(darkSquareColor);
        }
        if(piece == null) {
            cell.append("   " + RESET_TEXT_COLOR);
        } else {
            if(piece.getTeamColor() == WHITE) {
                cell.append(whitePieceColor);
            } else if(piece.getTeamColor() == BLACK) {
                cell.append(blackPieceColor);
            }
            cell.append(String.format(" %s ", piece));
        }
        return cell.toString();
    }
}
