package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Solution solution;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameEmpty = new SimpleStringProperty();
    StringProperty imageFileNameEnd = new SimpleStringProperty();
    StringProperty imageFileNameSol = new SimpleStringProperty();

    //delta for scroll
    public static double delta = 0.0;
    public static void changeDelta(double num){delta += num;}
    public static void setDelta(double num){delta = num;}

    //str for name
    public static String name = "shay";
    public String getName(){
        return name;
    }
    public static void changeName(String currName){
        if(currName.equals(name))
            return;
        if(name.equals("shay"))
            name = "itai";
        else
            name = "shay";
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();

    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get() + name + ".jpg";
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get()  + name + ".jpg";
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {this.imageFileNamePlayer.set(imageFileNamePlayer);}

    public String getImageFileNameEmpty() {
        return imageFileNameEmpty.get() ;
    }
    public String imageFileNameEmptyProperty() {
        return imageFileNameEmpty.get();
    }
    public void setImageFileNameEmpty(String imageFileNameEmpty) {this.imageFileNameEmpty.set(imageFileNameEmpty);}

    public String getImageFileNameEnd() {
        return imageFileNameEnd.get() + name + ".jpg";
    }
    public String imageFileNameEndProperty() {
        return imageFileNameEnd.get();
    }
    public void setImageFileNameEnd(String imageFileNameEnd) {this.imageFileNameEnd.set(imageFileNameEnd);}


    public String getImageFileNameSol() {
        return imageFileNameSol.get() + name + ".jpg";
    }
    public String imageFileNameSolProperty() {
        return imageFileNameSol.get();
    }
    public void setImageFileNameSol(String imageFileNameEnd) {this.imageFileNameSol.set(imageFileNameEnd);}


    public void drawMaze(Maze maze) {
        this.maze = maze;
        this.solution = null;
        draw();
    }

    public void drawMazeScroll(Maze maze) {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight() + delta;//
            double canvasWidth = getWidth() + delta; //
            int rows = maze.getMazeRows();
            int cols = maze.getMazeCols();

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth -delta , canvasHeight -delta);
            drawMazeSpaces(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            if(solution != null)
                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawEnd(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        // need to be implemented
        graphicsContext.setFill(Color.BLUE);
        Image solImage = null;
        try{
            solImage = new Image(new FileInputStream(getImageFileNameSol()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no sol image file");
        }
        for (AState state: solution.getSolutionPath()) {
            MazeState mazeState = (MazeState)state;
            int i = mazeState.getStatePosition().getRowIndex();
            int j = mazeState.getStatePosition().getColumnIndex();
            double x = j * cellWidth;
            double y = i * cellHeight;
            if (solImage == null)
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(solImage, x, y, cellWidth, cellHeight);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze.getCellVal(i, j) == 1) {
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;

                    if (wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }


    private void drawMazeSpaces(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.WHITE);

        Image spaceImage = null;
        try{
            spaceImage = new Image(new FileInputStream(getImageFileNameEmpty()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no empty image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze.getCellVal(i, j) == 0) {
                    double x = j * cellWidth;
                    double y = i * cellHeight;

                    if (spaceImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(spaceImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);
        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }


    private void drawEnd(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        int endR = maze.getGoalPosition().getRowIndex();
        int endC = maze.getGoalPosition().getColumnIndex();
        if(playerCol != endC || playerRow != endR){
            graphicsContext.setFill(Color.BLACK);
            double x = endC * cellWidth;
            double y = endR * cellHeight;
            Image endImage = null;
            try {
                endImage = new Image(new FileInputStream(getImageFileNameEnd()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no end image file");
            }
            if(endImage == null)
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(endImage, x, y, cellWidth, cellHeight);
        }

    }

}
