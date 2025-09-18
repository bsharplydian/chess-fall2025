package chess.movecalculators;

public class KingCalculator extends SetCalculator {
    static final int[][] KING_MOVES = new int[][]{
            {0, 1},
            {1, 1},
            {1, 0},
            {1, -1},
            {0, -1},
            {-1, -1},
            {-1, 0},
            {-1, 1}
    };

    @Override
    public int[][] getOffsets() {
        return KING_MOVES;
    }
}
