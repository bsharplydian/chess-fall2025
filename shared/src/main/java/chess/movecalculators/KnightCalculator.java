package chess.movecalculators;

public class KnightCalculator extends SetCalculator {
    static final int[][] KNIGHT_MOVES = new int[][]{
            {1, 2},
            {2, 1},
            {2, -1},
            {1, -2},
            {-1, -2},
            {-2, -1},
            {-2, 1},
            {-1, 2}
    };

    @Override
    public int[][] getOffsets() {
        return KNIGHT_MOVES;
    }
}
