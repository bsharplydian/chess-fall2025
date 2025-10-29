package chess;

import chess.movecalculators.CheckCalculator;

import java.util.Collection;
import java.util.HashSet;

public class CastleTracker {
    private final ChessGame.TeamColor teamColor;
    private ChessPosition kingPosition;
    private final int kingRow;
    boolean kingSideMoved = false;
    boolean queenSideMoved = false;
    static final int[] QUEEN_EMPTY_COLUMNS = new int[]{2, 3, 4};
    static final int[] KING_EMPTY_COLUMNS = new int[]{6, 7};

    public CastleTracker(ChessGame game, ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
        updateKingPosition(game);
        this.kingRow = switch(teamColor) {
            case WHITE -> 1;
            case BLACK -> 8;
        };
    }

    public Collection<ChessMove> getCastleMoves(ChessGame game) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition destinationQueenSide = new ChessPosition(kingRow, 3);
        ChessPosition destinationKingSide = new ChessPosition(kingRow, 7);

        if(canCastleQueenSide(game)) {
            moves.add(new ChessMove(kingPosition, destinationQueenSide, null));
        }
        if(canCastleKingSide(game)) {
            moves.add(new ChessMove(kingPosition, destinationKingSide, null));
        }

        return moves;
    }

    private boolean canCastleQueenSide(ChessGame game) {
        return canCastle(queenSideMoved, QUEEN_EMPTY_COLUMNS, game);
    }

    private boolean canCastleKingSide(ChessGame game) {
        return canCastle(kingSideMoved, KING_EMPTY_COLUMNS, game);
    }

    private boolean canCastle(boolean sideMoved, int[] emptyColumns, ChessGame game) {
        // neither piece has moved
        if (sideMoved) {
            return false;
        }
        if(kingRow != 1 && kingRow != 8) {
            return false;
        }
        if(kingPosition.getColumn() != 5) {
            return false;
        }
        for (int col : emptyColumns) {
            //all intermediate spaces empty
            ChessPosition intermediatePosition = new ChessPosition(kingRow, col);
            if (!game.getBoard().isEmptyAt(intermediatePosition)) {
                return false;
            }
            //not in check currently
            CheckCalculator checkCalculator = new CheckCalculator(game.getBoard(), teamColor, kingPosition);
            if (checkCalculator.isInCheck()) {
                return false;
            }
            //not in check for intermediate spaces
            ChessGame previewGame = new ChessGame(game);
            previewGame.getBoard().movePiece(new ChessMove(kingPosition, intermediatePosition, null), game.getBoard().getPiece(kingPosition));
            checkCalculator = new CheckCalculator(previewGame.getBoard(), teamColor, previewGame.getBoard().getKingPosition(teamColor));
            if (checkCalculator.isInCheck()) {
                return false;
            }
        }
        return true;
    }
    public void handleRookMove(int column) {
        if (column == 1) {
            queenSideMoved = true;
        } else if(column == 8) {
            kingSideMoved = true;
        }
    }

    public void handleKingMove() {
        queenSideMoved = true;
        kingSideMoved = true;
    }

    public void updateKingPosition(ChessGame game) {
        kingPosition = game.getBoard().getKingPosition(teamColor);
    }
}
