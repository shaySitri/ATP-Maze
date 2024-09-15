package Model;

import IO.MyCompressorOutputStream;
import IO.SimpleCompressorOutputStream;
import Server.Configurations;
import Server.IServerStrategy;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ServerStrategyGenerateMazeModel implements IServerStrategy {
    public ServerStrategyGenerateMazeModel() {
    }

    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream dataIn = new ObjectInputStream(inFromClient);
            ObjectOutputStream dataOut = new ObjectOutputStream(outToClient);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try {
                int[] mazeDetails = (int[])dataIn.readObject();
                int rows = mazeDetails[0];
                int cols = mazeDetails[1];
                IMazeGenerator mg = Configurations.instance()._GeneratorAlgorithm;
                Maze maze = mg.generate(rows, cols);

                try {
                    SimpleCompressorOutputStream comp = new SimpleCompressorOutputStream(byteArrayOutputStream);
                    comp.write(maze.toByteArray());
                    comp.flush();
                    dataOut.writeObject(byteArrayOutputStream.toByteArray());
                    dataOut.flush();
                    byteArrayOutputStream.close();
                    comp.close();
                    dataIn.close();
                    dataOut.close();
                } catch (Exception var12) {
                }
            } catch (EOFException var13) {
            }
        } catch (Exception var14) {
        }

    }
}
