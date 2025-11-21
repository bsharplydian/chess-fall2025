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
    final static int[] FORWARD_COORDS = {1, 2, 3, 4, 5, 6, 7, 8};
    final static int[] BACKWARD_COORDS = {8, 7, 6, 5, 4, 3, 2, 1};


    public String printBoard(ChessBoard board, ChessGame.TeamColor color) {
        int[] rows = switch(color) {
            case WHITE -> BACKWARD_COORDS; // white prints the rows starting at 8 and going down to 1
            case BLACK -> FORWARD_COORDS; // black prints them starting with 1 at the top and going down to 8
        };
        int[] cols = switch(color) {
            case WHITE -> FORWARD_COORDS; // white prints the columns starting at a and going over to h
            case BLACK -> BACKWARD_COORDS; // black starts at h and goes down to a
        };
        StringBuilder boardString = new StringBuilder();
        boardString.append(printColumnMarkers(color));
        for(int row : rows) {
            boardString.append(printRowMarker(row));
            for(int col : cols) {
                boardString.append(printCell(board.getPiece(new ChessPosition(row, col)), row, col));
            }
            boardString.append(printRowMarker(row));
            boardString.append(RESET_BG_COLOR + "\n");
        }
        boardString.append(printColumnMarkers(color));
        return boardString.toString();
    }

    private String printRowMarker(int row) {
        return String.format("%s%s %d ", coordForeground, coordBackground, row);
    }
    private String printColumnMarkers(ChessGame.TeamColor color) {
        String columns = switch(color) {
            case BLACK -> "    h  g  f  e  d  c  b  a    ";
            case WHITE -> "    a  b  c  d  e  f  g  h    ";
        };
        return coordForeground + coordBackground + columns + RESET_BG_COLOR + "\n";
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
