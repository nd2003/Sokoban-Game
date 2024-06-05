package sokoban.state;

/**
 * Represents the map of the game.
 */
public class Map {

    public Character[][] board;

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