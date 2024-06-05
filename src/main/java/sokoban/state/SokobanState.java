package sokoban.state;

import puzzle.State;

import java.util.Set;

public class SokobanState implements State<Direction> {
    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public boolean isLegalMove(Direction direction) {
        return false;
    }

    @Override
    public void makeMove(Direction direction) {

    }

    @Override
    public Set<Direction> getLegalMoves() {
        return Set.of();
    }

    @Override
    public State<Direction> clone() {
        return null;
    }
}
