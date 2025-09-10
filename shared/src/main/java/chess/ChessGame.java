package chess;

import chess.moveCalculators.CheckCalculator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeamTurn;
    private ChessBoard currentBoard = new ChessBoard();
    private boolean whiteCastleAMoved = false;
    private boolean blackCastleAMoved = false;
    private boolean whiteCastleHMoved = false;
    private boolean blackCastleHMoved = false;
    private boolean enPassantOpen = false;
    private ChessPosition enPassantPosition;
    public ChessGame() {
        currentTeamTurn = TeamColor.WHITE;
        this.currentBoard.resetBoard();
    }
    public ChessGame(ChessGame copy) {
        currentTeamTurn = copy.currentTeamTurn;
        currentBoard = new ChessBoard(copy.currentBoard);
        whiteCastleAMoved = copy.whiteCastleAMoved;
        blackCastleAMoved = copy.blackCastleAMoved;
        whiteCastleHMoved = copy.whiteCastleHMoved;
        blackCastleHMoved = copy.blackCastleHMoved;
        enPassantOpen = copy.enPassantOpen;
        enPassantPosition = copy.enPassantPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return whiteCastleAMoved == chessGame.whiteCastleAMoved &&
                blackCastleAMoved == chessGame.blackCastleAMoved &&
                whiteCastleHMoved == chessGame.whiteCastleHMoved &&
                blackCastleHMoved == chessGame.blackCastleHMoved &&
                currentTeamTurn == chessGame.currentTeamTurn &&
                Objects.equals(currentBoard, chessGame.currentBoard) &&
                enPassantOpen == chessGame.enPassantOpen &&
                Objects.equals(enPassantPosition, chessGame.enPassantPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeamTurn, currentBoard, whiteCastleAMoved, blackCastleAMoved, whiteCastleHMoved, blackCastleHMoved, enPassantOpen);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece thisPiece = currentBoard.getPiece(startPosition);
        Collection<ChessMove> moves = thisPiece.pieceMoves(currentBoard, startPosition);
        if(enPassantOpen) {
            ChessPosition leftPawnPosition = new ChessPosition(enPassantPosition.getRow(), enPassantPosition.getColumn()-1);
            ChessPosition rightPawnPosition = new ChessPosition(enPassantPosition.getRow(), enPassantPosition.getColumn()+1);
            moves.addAll(getEnPassantMove(leftPawnPosition));
            moves.addAll(getEnPassantMove(rightPawnPosition));
        }
        if(thisPiece.getPieceType() == ChessPiece.PieceType.KING) {
            moves.addAll(getCastleMoves(thisPiece.getTeamColor()));
        }
        Collection<ChessMove> invalidMoves = new HashSet<>();
        for(ChessMove move : moves) {
            ChessGame previewGame = new ChessGame(this);
            previewGame.currentBoard.movePiece(move, thisPiece);
            if(previewGame.isInCheck(thisPiece.getTeamColor())) {
                invalidMoves.add(move);
            }
        }
        for(ChessMove move : invalidMoves) {
            moves.remove(move);
        }
        return moves;
    }

    private Collection<ChessMove> getEnPassantMove(ChessPosition capturingPawnPosition) {
        Collection<ChessMove> move = new HashSet<>();
        ChessPiece capturingPawn = currentBoard.getPiece(capturingPawnPosition);
        if(capturingPawn == null ||
                capturingPawn.getPieceType() != ChessPiece.PieceType.PAWN ||
                capturingPawn.getTeamColor() != currentTeamTurn) {
            return move;
        }
        ChessPosition capturePosition = switch(currentTeamTurn) {
            case WHITE -> new ChessPosition(enPassantPosition.getRow()+1, enPassantPosition.getColumn());
            case BLACK -> new ChessPosition(enPassantPosition.getRow()-1, enPassantPosition.getColumn());
        };
        move.add(new ChessMove(capturingPawnPosition, capturePosition, null));
        return move;
    }

    private Collection<ChessMove> getCastleMoves(TeamColor teamColor) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition kingPosition = currentBoard.getKingPosition(teamColor);
        ChessPosition castlePositionA = new ChessPosition(kingPosition.getRow(), kingPosition.getColumn()-2);
        ChessPosition castlePositionH = new ChessPosition(kingPosition.getRow(), kingPosition.getColumn()+2);
        if(canCastleA(kingPosition)) {
            moves.add(new ChessMove(kingPosition, castlePositionA, null));
        }
        if(canCastleH(kingPosition)) {
            moves.add(new ChessMove(kingPosition, castlePositionH, null));
        }
        return moves;
    }

    private boolean canCastleA(ChessPosition kingPosition) {
        TeamColor teamColor = currentBoard.getPiece(kingPosition).getTeamColor();
        // neither piece has moved
        switch(teamColor) {
            case WHITE -> {
                if(whiteCastleAMoved) {
                    return false;
                }
            }
            case BLACK -> {
                if(blackCastleAMoved) {
                    return false;
                }
            }
        }
        for(int i = 2; i <= 4; i++) {
            //all intermediate spaces empty
            ChessPosition intermediatePosition = new ChessPosition(kingPosition.getRow(), i);
            if(!currentBoard.isEmptyAt(intermediatePosition)) {
                return false;
            }
            //not in check currently
            CheckCalculator checkCalculator = new CheckCalculator(this.currentBoard, teamColor, kingPosition);
            if(checkCalculator.isInCheck()) {
                return false;
            }
            //not in check for intermediate spaces
            ChessGame previewGame = new ChessGame(this);
            previewGame.currentBoard.movePiece(new ChessMove(kingPosition, intermediatePosition, null), currentBoard.getPiece(kingPosition));
            checkCalculator = new CheckCalculator(previewGame.currentBoard, teamColor, previewGame.currentBoard.getKingPosition(teamColor));
            if(checkCalculator.isInCheck()) {
                return false;
            }
        }


        return true;
    }
    private boolean canCastleH(ChessPosition kingPosition) {
        TeamColor teamColor = currentBoard.getPiece(kingPosition).getTeamColor();
        // neither piece has moved
        switch(teamColor) {
            case WHITE -> {
                if(whiteCastleHMoved) {
                    return false;
                }
            }
            case BLACK -> {
                if(blackCastleHMoved) {
                    return false;
                }
            }
        }
        // all spaces between empty
        for(int i = 6; i <= 7; i++) {
            ChessPosition intermediatePosition = new ChessPosition(kingPosition.getRow(), i);
            if(!currentBoard.isEmptyAt(intermediatePosition)) {
                return false;
            }
            //not in check currently
            CheckCalculator checkCalculator = new CheckCalculator(this.currentBoard, teamColor, kingPosition);
            if(checkCalculator.isInCheck()) {
                return false;
            }
            ChessGame previewGame = new ChessGame(this);
            previewGame.currentBoard.movePiece(new ChessMove(kingPosition, intermediatePosition, null), currentBoard.getPiece(kingPosition));
            checkCalculator = new CheckCalculator(previewGame.currentBoard, teamColor, previewGame.currentBoard.getKingPosition(teamColor));
            if(checkCalculator.isInCheck()) {
                return false;
            }
        }


        return true;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece thisPiece = currentBoard.getPiece(move.getStartPosition());
        if(thisPiece == null) {
            throw new InvalidMoveException("no piece at start position");
        }
        if(thisPiece.getTeamColor() != currentTeamTurn) {
            throw new InvalidMoveException("wrong turn");
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if(!moves.contains(move)) {
            throw new InvalidMoveException("illegal move");
        }
        currentBoard.movePiece(move, currentBoard.getPiece(move.getStartPosition()));
        if(move.getPromotionPiece() != null) {
            ChessPiece promotionPiece = new ChessPiece(thisPiece.getTeamColor(), move.getPromotionPiece());
            currentBoard.addPiece(move.getEndPosition(), promotionPiece);
        }
        if(thisPiece.getPieceType() == ChessPiece.PieceType.KING) {
            if(move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == 2) {
                currentBoard.addPiece(new ChessPosition(move.getStartPosition().getRow(), 8), null);
                currentBoard.addPiece(new ChessPosition(move.getStartPosition().getRow(), 6), new ChessPiece(thisPiece.getTeamColor(), ChessPiece.PieceType.ROOK));
            }
            if(move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == -2) {
                currentBoard.addPiece(new ChessPosition(move.getStartPosition().getRow(), 1), null);
                currentBoard.addPiece(new ChessPosition(move.getStartPosition().getRow(), 4), new ChessPiece(thisPiece.getTeamColor(), ChessPiece.PieceType.ROOK));
            }
            switch(currentTeamTurn) {
                case WHITE -> {
                    whiteCastleAMoved = true;
                    whiteCastleHMoved = true;
                }
                case BLACK -> {
                    blackCastleAMoved = true;
                    blackCastleHMoved = true;
                }
            }
        }
        if(thisPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            int column = move.getStartPosition().getColumn();
            switch(currentTeamTurn) {
                case WHITE -> {
                    if (column == 1) {
                        whiteCastleAMoved = true;
                    } else if(column == 8) {
                        whiteCastleHMoved = true;
                    }
                }
                case BLACK -> {
                    if (column == 1) {
                        blackCastleAMoved = true;
                    } else if(column == 8) {
                        blackCastleHMoved = true;
                    }
                }
            }
        }
        makeEnPassantMove(move);

        currentTeamTurn = switch(currentTeamTurn) {
            case BLACK -> TeamColor.WHITE;
            case WHITE -> TeamColor.BLACK;
        };
    }
    private void makeEnPassantMove(ChessMove move) {
        // capture
        if(enPassantPosition != null) {
            ChessPosition enPassantCapturePosition = switch (currentTeamTurn) {
                case WHITE -> new ChessPosition(enPassantPosition.getRow() + 1, enPassantPosition.getColumn());
                case BLACK -> new ChessPosition(enPassantPosition.getRow() - 1, enPassantPosition.getColumn());
            };
            if (enPassantOpen && Objects.equals(move.getEndPosition(), enPassantCapturePosition)) {
                currentBoard.addPiece(enPassantPosition, null);
            }
        }
        enPassantOpen = false;

        // left open
        int startRow = move.getStartPosition().getRow();
        int endRow = move.getEndPosition().getRow();
        if(((startRow == 2 && endRow == 4) || (startRow == 7 && endRow == 5)) &&
                currentBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
            enPassantOpen = true;
            enPassantPosition = move.getEndPosition();
        }
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        CheckCalculator checkCalculator = new CheckCalculator(currentBoard, teamColor,
                currentBoard.getKingPosition(teamColor));
        return checkCalculator.isInCheck();
        //
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            return false;
        }
        return teamCannotPlay(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            return false;
        }
        return teamCannotPlay(teamColor);
    }

    private boolean teamCannotPlay(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(position);
                if(piece == null) {
                    continue;
                }
                if(piece.getTeamColor() == teamColor) {
                    possibleMoves.addAll(validMoves(position));
                }
                if(!possibleMoves.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }
}
