package sokoban.state;


/**
 * Represents the board of the game.
 */
public class Map {

    /**
     *  Represents the game board.
     */
    public Character[][] board;

    /**
     * Constructs a Map instance and initializes the board with a predefined layout.
     * The layout is represented as a 2D array of characters.
     */
    public Map() {
        board= new Character[][]{
                {'#', '#', '#', '#', '#', '-', '-', '-', '-'},
                {'#', ' ', ' ', ' ', '#', '-', '-', '-', '-'},
                {'#', ' ', ' ', ' ', '#', '-', '#', '#', '#'},
                {'#', ' ', ' ', ' ', '#', '-', '#', '.', '#'},
                {'#', '#', '#', ' ', '#', '#', '#', '.', '#'},
                {'-', '#', '#', ' ', ' ', ' ', ' ', '.', '#'},
                {'-', '#', ' ', ' ', ' ', '#', ' ', ' ', '#'},
                {'-', '#', ' ', ' ', ' ', '#', '#', '#', '#'},
                {'-', '#', '#', '#', '#', '#', '-', '-', '-'},
        };
    }

    /**
     * @return the size of the board.
     */
    public int width() {
        return board.length;
    }

    /**
     * @param x the x-coordinate of the position.
     * @param y the y-coordinate of the position.
     * @return the value at the specified position.
     */
    public Character getPosition(int x, int y) {
        return board[x][y];
    }
}
