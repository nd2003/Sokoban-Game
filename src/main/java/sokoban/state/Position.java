package sokoban.state;

/**
 * Represents a 2D position.
 */
public record Position(int row, int col) {

    /**
     * {@return the position whose vertical and horizontal distances from this
     * position are equal to the coordinate changes of the direction given}
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public Position move(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.UP)}.
     *
     * @return the position above this position
     */
    public Position moveUp() {
        return move(Direction.UP);
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.RIGHT)}.
     *
     * @return the position to the right of this position
     */
    public Position moveRight() {
        return move(Direction.RIGHT);
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.DOWN)}.
     *
     * @return the position below this position
     */
    public Position moveDown() {
        return move(Direction.DOWN);
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.LEFT)}.
     *
     * @return the position to the left of this position
     */
    public Position moveLeft() {
        return move(Direction.LEFT);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}