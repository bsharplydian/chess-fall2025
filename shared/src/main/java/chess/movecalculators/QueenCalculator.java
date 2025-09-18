package chess.movecalculators;

public class QueenCalculator extends LineCalculator {
    @Override
    public Direction[] getDirections() {
        return Direction.values();
    }
}
