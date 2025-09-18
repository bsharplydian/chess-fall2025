package chess.movecalculators;

public class BishopCalculator extends LineCalculator {
    @Override
    public Direction[] getDirections() {
        return DIAGONALS;
    }
}
