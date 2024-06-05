package sokoban.state;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.State;

import java.util.Set;

public class SokobanState implements State<Direction> {

    /**
     * Instantiate the board.
     */
    public static Map board = new Map();

    public static final int BOARD_SIZE = board.width();

    public static final char WALL = '#';
    public static final char TARGET = '.';
    public static final char OUT_OF_BOARD = '-';

    public static final int PLAYER_POSITION = 0;

    public static final int FIRST_BOX_POSITION = 1;
    public static final int SECOND_BOX_POSITION = 2;
    public static final int THIRD_BOX_POSITION = 3;

    private ReadOnlyObjectWrapper<Position>[] positions;

    private final ReadOnlyBooleanWrapper solved;

    public SokobanState() {
        this(new Position(1, 1),
                new Position(2, 2),
                new Position(3, 2),
                new Position(2, 3));
        board = new Map();
    }

    public static final Position[] finishPositions =  {
            new Position(3,7), new Position(4,7), new Position(5,7)
    };

    public SokobanState(Position... positions) {
        checkPositions(positions);
        this.positions = new ReadOnlyObjectWrapper[4];
        for (var i = 0; i < 4; i++) {
            this.positions[i] = new ReadOnlyObjectWrapper<>(positions[i]);
        }
        solved = new ReadOnlyBooleanWrapper();

        solved.bind((this.positions[1].isEqualTo(finishPositions[0]).
                or(this.positions[1].isEqualTo(finishPositions[1])).
                or(this.positions[1].isEqualTo(finishPositions[2]))).

                and(this.positions[2].isEqualTo(finishPositions[0]).
                        or(this.positions[2].isEqualTo(finishPositions[1])).
                        or(this.positions[2].isEqualTo(finishPositions[2]))).

                and(this.positions[3].isEqualTo(finishPositions[0]).
                        or(this.positions[3].isEqualTo(finishPositions[1])).
                        or(this.positions[3].isEqualTo(finishPositions[2]))));
    }

    private void checkPositions(Position[] positions) {
        if (positions == null || positions.length != 4) {
            throw new IllegalArgumentException("Exactly four positions are required.");
        }

        for (var position : positions) {
            if(!isOnBoard(position))
            {
                throw new IllegalArgumentException("Position is not on the board.");
            }
        }
    }

    private boolean isOnBoard(Position position) {
        return position.row() > 0 && position.row() < BOARD_SIZE - 1 &&
                position.col() > 0 && position.col() < BOARD_SIZE - 1 &&
                board.getPosition(position.row(), position.col()) != WALL &&
                board.getPosition(position.row(), position.col()) != OUT_OF_BOARD;

    }

    public Position getPosition(int index) {
        return positions[index].get();
    }

    public ReadOnlyObjectProperty<Position> positionProperty(int index) {
        return positions[index].getReadOnlyProperty();
    }

    @Override
    public boolean isSolved() {
        return solved.get();
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
