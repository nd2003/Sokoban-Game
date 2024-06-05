package sokoban.state;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.State;

import java.util.EnumSet;
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
        return switch (direction) {
            case UP -> canMoveUp();
            case RIGHT -> canMoveRight();
            case DOWN -> canMoveDown();
            case LEFT -> canMoveLeft();
        };
    }

    private boolean canMoveUp() {
        Position playerPosition = getPosition(PLAYER_POSITION);
        if (! isOnBoard(playerPosition.moveUp())) {
            return false;
        }

        var up = playerPosition.moveUp();
        return isEmpty(up) ||
                (playerPosition.row() > 2 && isInDirection(Direction.UP) != -1 && isEmpty(up.moveUp()));
    }


    private boolean canMoveRight() {
        Position playerPosition = getPosition(PLAYER_POSITION);
        if (! isOnBoard(playerPosition.moveRight())) {
            return false;
        }

        var right = playerPosition.moveRight();
        return isEmpty(right) ||
                ( playerPosition.col() < 7 && isInDirection(Direction.RIGHT) != -1  && isEmpty(right.moveRight()));
    }

    private boolean canMoveDown() {
        Position playerPosition = getPosition(PLAYER_POSITION);
        if (! isOnBoard(playerPosition.moveDown())) {
            return false;
        }

        var down = playerPosition.moveDown();
        return isEmpty(down) ||
                ( playerPosition.row() < 7 && isInDirection(Direction.DOWN) !=-1  && isEmpty(down.moveDown()));
    }

    private boolean canMoveLeft() {
        Position playerPosition = getPosition(PLAYER_POSITION);
        if (! isOnBoard(playerPosition.moveLeft())){
            return false;
        }

        var left = playerPosition.moveLeft();
        return isEmpty(left) ||
                ( playerPosition.col() > 2 && isInDirection(Direction.LEFT) != -1 && isEmpty(left.moveLeft()));
    }

    private int isInDirection(Direction direction) {
        Position playerPosition = getPosition(PLAYER_POSITION).move(direction);
        for (int i = 1; i < 4 ; i++) {
            if(playerPosition.col() == getPosition(i).col() &&
                    playerPosition.row() == getPosition(i).row()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isEmpty(Position position) {
        for (var p : positions) {
            if(p.get().equals(position)) {
                return false;
            }
        }
        return board.getPosition(position.row(), position.col()) != WALL;
    }



    /**
     * Moves the block and the player to the direction specified.
     *
     * @param direction the direction to which the block and the player are moved
     */
    @Override
    public void makeMove(Direction direction) {
        switch (direction) {
            case UP -> moveUp();
            case RIGHT -> moveRight();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
        }
    }

    private void moveUp() {
        movePlayer(Direction.UP);

    }

    private void moveRight() {
        movePlayer(Direction.RIGHT);

    }

    private void moveDown() {
        movePlayer(Direction.DOWN);

    }

    private void moveLeft() {
        movePlayer(Direction.LEFT);

    }

    private void movePlayer(Direction direction) {

        int index = isInDirection(direction);
        if (index != -1) {

            Position position = getPosition(index);
            positions[index].set(position.move(direction));
        }

        Position newPosition = getPosition(PLAYER_POSITION).move(direction);
        positions[PLAYER_POSITION].set(newPosition);

    }

    /**
     * {@return the set of directions to which the block can be moved}
     */
    @Override
    public Set<Direction> getLegalMoves() {
        var legalMoves = EnumSet.noneOf(Direction.class);
        for (var direction : Direction.values()) {
            if (isLegalMove(direction)) {
                legalMoves.add(direction);
            }
        }
        return legalMoves;
    }

    @Override
    public SokobanState clone() {
        return new SokobanState(getPosition(PLAYER_POSITION), getPosition(FIRST_BOX_POSITION),
                getPosition(SECOND_BOX_POSITION), getPosition(THIRD_BOX_POSITION));
    }
}
