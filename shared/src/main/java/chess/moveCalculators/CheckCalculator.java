package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.moveCalculators.LineCalculator.CARDINALS;
import static chess.moveCalculators.LineCalculator.DIAGONALS;
import static chess.moveCalculators.KnightCalculator.KNIGHT_MOVES;
import static chess.moveCalculators.KingCalculator.KING_MOVES;

public class CheckCalculator {
    private final ChessBoard board;
    private final ChessGame.TeamColor myTeamColor;
    private final ChessPosition kingPosition;

    public CheckCalculator(ChessBoard board, ChessGame.TeamColor myTeamColor, ChessPosition kingPosition) {
        this.board = board;
        this.myTeamColor = myTeamColor;
        this.kingPosition = kingPosition;
    }

    public boolean isInCheck() {
        return isThreatenedCardinals() ||
                isThreatenedDiagonals() ||
                isThreatenedKing() ||
                isThreatenedKnight() ||
                isThreatenedPawn();
    }

    private boolean isThreatenedDiagonals() {
        for(LineCalculator.Direction dir : DIAGONALS) {
            if(isThreatenedLine(dir)) {
                return true;
            }
        }
        return false;
    }
    private boolean isThreatenedCardinals() {
        for(LineCalculator.Direction dir : CARDINALS) {
            if(isThreatenedLine(dir)) {
                return true;
            }
        }
        return false;
    }

    private boolean isThreatenedLine(LineCalculator.Direction dir) {
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
        ChessPiece.PieceType[] threateningPieces = switch(dir) {
            case N, S, E, W -> new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK};
            case NW, SW, SE, NE -> new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP};
        };
        ChessPosition nextPosition = new ChessPosition(kingPosition.getRow() + addX, kingPosition.getColumn() + addY);
        while(ChessPiece.isLegalSquare(board, nextPosition, myTeamColor)) {
            for(ChessPiece.PieceType threateningPiece: threateningPieces) {
                ChessPiece otherPiece = board.getPiece(nextPosition);
                if(otherPiece != null && otherPiece.getPieceType() == threateningPiece) {
                    return true;
                }
            }

            nextPosition = new ChessPosition(nextPosition.getRow() + addX, nextPosition.getColumn() + addY);
        }
        return false;
    }
    private boolean isThreatenedKing() {
        return isThreatenedSet(KING_MOVES, ChessPiece.PieceType.KING);
    }
    private boolean isThreatenedKnight() {
        return isThreatenedSet(KNIGHT_MOVES, ChessPiece.PieceType.KNIGHT);
    }
    private boolean isThreatenedSet(int[][] coordList, ChessPiece.PieceType threateningPiece) {
        for(int[] coords : coordList) {
            int newRow = kingPosition.getRow() + coords[0];
            int newCol = kingPosition.getColumn() + coords[1];
            ChessPosition nextPosition = new ChessPosition(newRow, newCol);
            if(!ChessPiece.isLegalSquare(board, nextPosition, myTeamColor)) {
                continue;
            }
            ChessPiece otherPiece = board.getPiece(nextPosition);
            if(otherPiece == null) {
                continue;
            }
            if(board.getPiece(nextPosition).getPieceType() == threateningPiece) {
                return true;
            }
        }
        return false;
    }
    private boolean isThreatenedPawn() {
        int rowDiff = switch(myTeamColor) {
            case WHITE -> 1;
            case BLACK -> -1;
        };
        ChessPosition leftPos = new ChessPosition(kingPosition.getRow()+rowDiff, kingPosition.getColumn()+1);
        ChessPosition rightPos = new ChessPosition(kingPosition.getRow()+rowDiff, kingPosition.getColumn()-1);
        for(ChessPosition nextPosition : new ChessPosition[]{leftPos, rightPos}) {
            if(!ChessPiece.isLegalSquare(board, nextPosition, myTeamColor)) {
                continue;
            }
            ChessPiece otherPiece = board.getPiece(nextPosition);
            if(otherPiece == null) {
                continue;
            }
            if(board.getPiece(nextPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
                return true;
            }
        }
        return false;
    }
}
