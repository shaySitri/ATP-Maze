package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import algorithms.search.Solution;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public Maze getMaze(){
        return model.getMaze();
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void generateMaze(int rows, int cols) throws UnknownHostException {
        int[] mazeDetails = new int[]{rows,cols};
        model.mazeGenerating(mazeDetails);
    }

    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case NUMPAD8 -> direction = MovementDirection.UP;
            case NUMPAD2 -> direction = MovementDirection.DOWN;
            case NUMPAD4 -> direction = MovementDirection.LEFT;
            case NUMPAD6 -> direction = MovementDirection.RIGHT;
            case NUMPAD7 -> direction = MovementDirection.UP_LEFT;
            case NUMPAD9 -> direction = MovementDirection.UP_RIGHT;
            case NUMPAD1 -> direction = MovementDirection.DOWN_LEFT;
            case NUMPAD3 -> direction = MovementDirection.DOWN_RIGHT;
            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    public void movePlayerMouse(double x, double y){
        MovementDirection direction = null;
        System.out.println(x + "," + y);
        if ( x<=-1 && x >= -2 && y<=2 && y >=1)
            direction = MovementDirection.DOWN_LEFT;
        else if ( x<=2 && x >= 1 && y<=2 && y >=1)
            direction = MovementDirection.DOWN_RIGHT;
        else if ( x<=2 && x >= 1 && y<=-1 && y >=-2)
            direction = MovementDirection.UP_RIGHT;
        else if ( x<=-1 && x >= -2 && y<=-1 && y >=-2)
            direction = MovementDirection.UP_LEFT;
        else if ( x<=2 && x >= 1 && y<1 && y >-1)
            direction = MovementDirection.RIGHT;
        else if ( x<=-1 && x >= -2 && y<1 && y >-1)
            direction = MovementDirection.LEFT;
        else if ( x<1 && x > -1 && y<=2 && y >=1)
            direction = MovementDirection.DOWN;
        else if ( x<1 && x > -1 && y<=-1 && y >=-2) {
            direction = MovementDirection.UP;
        }
        else {
            return;
        }
        model.updatePlayerLocation(direction);
    }


    public void solveMaze(){
        model.solveMaze();
    }
    public void saveMaze(String path) throws IOException {
        model.saveMaze(path);
    }

    public void openMaze(String path) throws IOException, ClassNotFoundException {
        model.openMaze(path);
    }

    public void closeServer(){
        model.closeServer();
    }
}

