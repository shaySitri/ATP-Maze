package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observer;

public interface IModel {
    void mazeGenerating(int[] mazeDetails) throws UnknownHostException;
    Maze getMaze();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void solveMaze();
    Solution getSolution();
    void saveMaze(String path) throws IOException;
    void openMaze(String path) throws IOException, ClassNotFoundException;
    void closeServer();
}
