package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.SimpleDecompressorInputStream;
import Server.Server;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;
import Server.Configurations;

import org.apache.log4j.Logger;


public class MyModel extends Observable implements IModel {
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private Maze maze;
    private Server _generateServer;
    private Server _solverServer;
    private static Logger logger = Logger.getLogger(MyModel.class);

    public MyModel() {
        _generateServer = new Server(5400, 1000, new ServerStrategyGenerateMazeModel());
        logger.info("Generator server: info- new maze generator server created.");
        _solverServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        logger.info("Solver server: info- new maze solver server created.");
        _generateServer.start();
        logger.info("Generator server: info- server start.");
        _solverServer.start();
        logger.info("Solver server: info- server start.");
    }

    public void mazeGenerating(int[] mazeDimensions) throws UnknownHostException {
        Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                    @Override
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        logger.info("Generator server: info- client try to create a maze");
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            toServer.writeObject(mazeDimensions); //send maze dimensions to server
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                            InputStream is = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            int size = mazeDimensions[0] * mazeDimensions[1];
                            byte[] decompressedMaze = new byte[size + 12]; //allocating byte[] for the decompressed maze -
                            is.read(decompressedMaze); //Fill decompressedMaze with bytes
                            maze = new Maze(decompressedMaze);
//                            Configurations.instance()._GeneratorAlgorithm;
                            logger.info("Generator server: info- new maze in size " +"["+ mazeDimensions[0] + "," + mazeDimensions[1] + "]"+ " creates by server.");
                        } catch (Exception e) {
                            logger.info("Generator server: error- " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
        client.communicateWithServer();

        if(maze != null){
            solution = null;
            setChanged();
            notifyObservers("maze generated");
            logger.info("Generator server: info- maze generated");

            // start position:
            Position p = maze.getStartPosition();
            movePlayer(p.getRowIndex(), p.getColumnIndex());
        }
    }

    @Override
    public Maze getMaze() {
        return maze;
    }


    @Override
    public void updatePlayerLocation(MovementDirection direction) {

        if(playerRow == maze.getGoalPosition().getRowIndex() && playerCol == maze.getGoalPosition().getColumnIndex()){
            return;
        }

        switch (direction) {
            case UP -> {
                if (playerRow > 0 && maze.getCellVal(playerRow - 1,playerCol) == 0){
                    movePlayer(playerRow - 1, playerCol);
                }
            }
            case DOWN -> {
                if (playerRow < maze.getMazeRows() - 1 && maze.getCellVal(playerRow + 1, playerCol) == 0){
                    movePlayer(playerRow + 1, playerCol);
                }
            }
            case LEFT -> {
                if (playerCol > 0 && maze.getCellVal(playerRow,playerCol - 1) == 0){
                    movePlayer(playerRow, playerCol - 1);
                }
            }
            case RIGHT -> {
                if (playerCol < maze.getMazeCols()-1 && maze.getCellVal(playerRow,playerCol + 1) == 0){
                    movePlayer(playerRow, playerCol + 1);
                }
            }

            case UP_LEFT -> {
                if(maze.getCellVal(playerRow,playerCol - 1) == 0 || maze.getCellVal(playerRow - 1,playerCol) == 0){
                    if (playerRow > 0 && playerCol > 0 && maze.getCellVal(playerRow - 1,playerCol - 1) == 0){
                        movePlayer(playerRow - 1, playerCol- 1);
                    }
                }
            }
            case UP_RIGHT -> {
                if(maze.getCellVal(playerRow - 1,playerCol) == 0 || maze.getCellVal(playerRow,playerCol + 1) == 0){
                    if (playerCol < maze.getMazeCols()-1 && playerRow > 0  && maze.getCellVal(playerRow - 1,playerCol + 1) == 0){
                        movePlayer(playerRow - 1, playerCol + 1);
                    }
                }
            }
            case DOWN_LEFT -> {
                if(maze.getCellVal(playerRow,playerCol - 1) == 0 || maze.getCellVal(playerRow + 1,playerCol) == 0) {
                    if (playerCol > 0 && playerRow < maze.getMazeRows() - 1 && maze.getCellVal(playerRow + 1, playerCol - 1) == 0){
                        movePlayer(playerRow + 1, playerCol - 1);
                    }
                }
            }
            case DOWN_RIGHT -> {
                if(maze.getCellVal(playerRow,playerCol + 1) == 0 || maze.getCellVal(playerRow + 1,playerCol) == 0){
                    if (playerCol < maze.getMazeCols()-1 && playerRow < maze.getMazeRows()-1 && maze.getCellVal(playerRow + 1,playerCol + 1) == 0){
                        movePlayer(playerRow + 1, playerCol + 1);
                    }
                }
            }

        }
        inEndPosition(playerRow,playerCol);
    }

    private void inEndPosition(int i, int j){
        if(i == maze.getGoalPosition().getRowIndex() && j == maze.getGoalPosition().getColumnIndex()){
            setChanged();
            notifyObservers("Game Over");
        }
    }


    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        if(maze==null){
            return;
        }
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                            try {
                                logger.info("Solver server: info- client try to solve a maze");
                                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                                toServer.flush();
                                toServer.writeObject(maze); //send maze to server
                                toServer.flush();
                                solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                                logger.info("Solver server: info- solver found solution with " + Configurations.instance().getSearchingAlg().getName());
                                logger.info("Solver server: info- solution has" + solution.getSolutionPath().size() + " steps");
                            } catch (Exception e) {
                                logger.info("Solver server: error- " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            logger.info("Solver server: error- " + e.getMessage());
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void saveMaze(String path) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(maze);
        out.flush();
        out.close();
    }

    @Override
    public void openMaze(String patha) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(patha));
        maze = (Maze) in.readObject();
        setChanged();
        notifyObservers("maze generated");
        // start position:
        Position p = maze.getStartPosition();
        movePlayer(p.getRowIndex(), p.getColumnIndex());
    }

    public void closeServer(){
        _generateServer.stop();
        _solverServer.stop();
        logger.info("Generator server: info- server stopped");
        logger.info("Solver server: info- server stopped");
    }
}
