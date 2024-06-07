package sokoban.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(0, 0);
    }

    @Test
    void move() {
        assertPosition(-1, 0, position.move(Direction.UP));
        assertPosition(0, 1, position.move(Direction.RIGHT));
        assertPosition(1, 0, position.move(Direction.DOWN));
        assertPosition(0, -1, position.move(Direction.LEFT));
    }

    @Test
    void moveUp() {
        assertPosition(-1, 0, position.moveUp());
    }

    @Test
    void moveRight() {
        assertPosition(0, 1, position.moveRight());
    }

    @Test
    void moveDown() {
        assertPosition(1, 0, position.moveDown());
    }

    @Test
    void moveLeft() {
        assertPosition(0, -1, position.moveLeft());
    }

    @Test
    void testToString() {
        assertEquals("(0,0)", position.toString());
    }

}
