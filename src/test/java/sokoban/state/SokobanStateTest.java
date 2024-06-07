package sokoban.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SokobanStateTest {

    private SokobanState state;

    @BeforeEach
    void setUp() {
        state = new SokobanState();
    }

    @Test
    void testInitialPositions() {
        assertEquals(new Position(1, 1), state.getPosition(SokobanState.PLAYER_POSITION), "Initial player position should be (1, 1).");
        assertEquals(new Position(2, 2), state.getPosition(SokobanState.FIRST_BOX_POSITION), "Initial first box position should be (2, 2).");
        assertEquals(new Position(3, 2), state.getPosition(SokobanState.SECOND_BOX_POSITION), "Initial second box position should be (3, 2).");
        assertEquals(new Position(2, 3), state.getPosition(SokobanState.THIRD_BOX_POSITION), "Initial third box position should be (2, 3).");
    }

    @Test
    void testIsSolvedInitial() {
        assertFalse(state.isSolved(), "The game should not be solved initially.");
    }

    @Test
    void testIsOnBoard() {
        Position validPosition = new Position(1, 1);
        Position invalidPosition = new Position(0, 0);
        assertTrue(state.isOnBoard(validPosition), "Position (1, 1) should be on the board.");
        assertFalse(state.isOnBoard(invalidPosition), "Position (0, 0) should not be on the board.");
    }

    @Test
    void testIsLegalMove() {
        assertTrue(state.isLegalMove(Direction.RIGHT), "Moving right should be a legal move.");
        assertFalse(state.isLegalMove(Direction.LEFT), "Moving left should not be a legal move initially.");
    }

    @Test
    void testMakeMove() {
        state.makeMove(Direction.RIGHT);
        assertEquals(new Position(1, 2), state.getPosition(SokobanState.PLAYER_POSITION), "Player position should be (1, 2) after moving right.");
    }

    @Test
    void testGetLegalMoves() {
        Set<Direction> legalMoves = state.getLegalMoves();
        assertTrue(legalMoves.contains(Direction.RIGHT), "Legal moves should contain RIGHT.");
        assertTrue(legalMoves.contains(Direction.DOWN), "Legal moves should contain DOWN.");
    }

    @Test
    void testClone() {
        SokobanState clonedState = state.clone();
        assertEquals(state, clonedState, "Cloned state should be equal to the original state.");
    }

    @Test
    void testEqualsAndHashCode() {
        SokobanState sameState = new SokobanState(new Position(1, 1), new Position(2, 2), new Position(3, 2), new Position(2, 3));
        SokobanState differentState = new SokobanState(new Position(1, 1), new Position(2, 3), new Position(3, 2), new Position(2, 2));

        assertEquals(state, sameState, "States with the same positions should be equal.");
        assertNotEquals(state, differentState, "States with different positions should not be equal.");
        assertEquals(state.hashCode(), sameState.hashCode(), "Hash codes of equal states should be the same.");
        assertNotEquals(state.hashCode(), differentState.hashCode(), "Hash codes of different states should be different.");
    }

    @Test
    void testToString() {
        String expectedString = "[Player: (1,1), BOX1: (2,2), Box2: (3,2), BOX3: (2,3)]";
        assertEquals(expectedString, state.toString(), "toString method should return the correct string representation.");
    }

    @Test
    void testMoveUp() {
        // Player at initial position (1,1)
        assertFalse(state.isLegalMove(Direction.UP), "Moving up should not be legal at initial position.");

        // Move player down to position (2,1)
        state.makeMove(Direction.DOWN);
        // Move player down to position (3,1)
        state.makeMove(Direction.DOWN);
        // Move player right to position (3,2)
        state.makeMove(Direction.RIGHT);
        assertTrue(state.isLegalMove(Direction.UP), "Moving up should be legal from position (3,2).");

        // Move player left to position (3,1)
        state.makeMove(Direction.LEFT);
        // Move player up to position (2,1)
        state.makeMove(Direction.UP);
        // Move player up to position (1,1)
        state.makeMove(Direction.UP);
        // Move player right to (1,2)
        state.makeMove(Direction.RIGHT);
        assertFalse(state.isLegalMove(Direction.UP), "Moving up should not be legal from position (1,2).");

        // Move player right to (1,3)
        state.makeMove(Direction.RIGHT);
        // Move player down to (2,3)
        state.makeMove(Direction.DOWN);
        assertTrue(state.isLegalMove(Direction.UP), "Moving up should be legal from position (2,3).");
    }

    @Test
    void testMoveDown() {
        // Move player right to (1,2)
        state.makeMove(Direction.RIGHT);
        assertFalse(state.isLegalMove(Direction.DOWN), "Moving down should not be legal from position (1,2)");

        // Move player left to (1,1)
        state.makeMove(Direction.LEFT);
        // Move player down to (2,1)
        state.makeMove(Direction.DOWN);
        assertTrue(state.isLegalMove(Direction.DOWN), "Moving down should be legal from position (2,1).");

        // Move player down to (3,1)
        state.makeMove(Direction.DOWN);
        assertFalse(state.isLegalMove(Direction.DOWN), "Moving down should not be legal from position (3,1).");

    }

    @Test
    void testMoveLeft() {
        // Player at initial position (1,1)
        assertFalse(state.isLegalMove(Direction.LEFT), "Moving left should not be legal at initial position.");

        // Move player right to (1,2)
        state.makeMove(Direction.RIGHT);
        assertTrue(state.isLegalMove(Direction.LEFT), "Moving left should be legal from position (1,2).");

        // Move player right to (1,3)
        state.makeMove(Direction.RIGHT);
        // Move player down to (2,3)
        state.makeMove(Direction.DOWN);
        assertTrue(state.isLegalMove(Direction.LEFT), "Moving left should be legal from position (2,3).");
    }

    @Test
    void testMoveRight() {
        // Player at initial position (1,1)
        assertTrue(state.isLegalMove(Direction.RIGHT), "Moving right should be legal at initial position.");


        // Move player down to (2,1)
        state.makeMove(Direction.DOWN);
        // Move player down to (3,1)
        state.makeMove(Direction.DOWN);
        assertTrue(state.isLegalMove(Direction.RIGHT), "Moving right should be legal from position (3,1).");

        // Move player up to (2,1)
        state.makeMove(Direction.UP);
        // Move player up to (1,1)
        state.makeMove(Direction.UP);
        // Move player right to (1,2)
        state.makeMove(Direction.RIGHT);
        assertTrue(state.isLegalMove(Direction.RIGHT), "Moving right should be legal from position (1,2).");

        // Move player right to (1,3)
        state.makeMove(Direction.RIGHT);
        assertFalse(state.isLegalMove(Direction.RIGHT), "Moving right should not be legal from position (1,3).");
    }

    @Test
    void testSolvingGame() {
        // Move player to (5,6) and boxes to finish positions
        state = new SokobanState(new Position(5, 5), new Position(3, 7), new Position(4, 7), new Position(5, 6));
        state.makeMove(Direction.RIGHT);
        assertTrue(state.isSolved(), "The game should be solved when all boxes are in the finish positions.");
    }

}
