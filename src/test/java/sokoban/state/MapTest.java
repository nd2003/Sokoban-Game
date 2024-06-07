package sokoban.state;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
    }

    @Test
    void testWidth() {
        assertEquals(9, map.width(), "The width of the board should be 9.");
    }

    @Test
    void testGetPosition() {
        assertEquals('#', map.getPosition(0, 0), "The position (0, 0) should contain '#'.");
        assertEquals(' ', map.getPosition(1, 1), "The position (1, 1) should contain ' '.");
        assertEquals('.', map.getPosition(3, 7), "The position (3, 7) should contain '.'.");
        assertEquals('-', map.getPosition(5, 0), "The position (5, 0) should contain '-'.");
    }

    @Test
    void testGetPositionOutOfBounds() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.getPosition(-1, 0), "Accessing out of bounds should throw an exception.");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.getPosition(0, -1), "Accessing out of bounds should throw an exception.");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.getPosition(9, 0), "Accessing out of bounds should throw an exception.");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.getPosition(0, 9), "Accessing out of bounds should throw an exception.");
    }
}
