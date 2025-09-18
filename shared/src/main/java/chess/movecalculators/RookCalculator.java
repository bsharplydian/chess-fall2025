package chess.movecalculators;

public class RookCalculator extends LineCalculator {
    @Override
    public Direction[] getDirections() {
        return CARDINALS;
    }
}
