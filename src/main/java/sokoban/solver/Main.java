package sokoban.solver;

import puzzle.solver.BreadthFirstSearch;
import sokoban.state.Direction;
import sokoban.state.SokobanState;

public class Main {

    public static void main(String[] args) {
        var bfs = new BreadthFirstSearch<Direction>();
        bfs.solveAndPrintSolution(new SokobanState());
    }

}
