package View;

import Server.Server;
import Server.Configurations;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class SettingsWindow {
    public ComboBox<String> solvingAlgorithms;
    public ComboBox<String> mazeGenerator;
    public Button btnOK;
    Stage stage;


    public void setStage(Stage newStage) {
        stage = newStage;
    }


    public void setSettings() {
        assert false;
        solvingAlgorithms.getItems().addAll("Best First Search", "Breadth First Search", "Depth First Search");
        mazeGenerator.getItems().addAll("Empty Maze Generator", "Simple Maze Generator", "My Maze Generator");
        solvingAlgorithms.getSelectionModel().select(0);
        mazeGenerator.getSelectionModel().select(2);

    }

    public String getMazeGen(String mazeGenerator) {
        return switch (mazeGenerator) {
            case "Empty Maze Generator" -> "EmptyMazeGenerator";
            case "Simple Maze Generator" -> "SimpleMazeGenerator";
            default -> "MyMazeGenerator";
        };
    }

    public String getSolvingAlgorithm(String solver) {
        return switch (solver) {
            case "Breadth First Search" -> "BreadthFirstSearch";
            case "Depth First Search" -> "DepthFirstSearch";
            default -> "BestFirstSearch";
        };
    }


    public void ok() throws IOException {
        try {
            FileOutputStream file = new FileOutputStream("resources/config.properties", false);
            String newSettings = "threadPoolSize=1\n" +
                    "mazeGeneratingAlgorithm=" +getMazeGen(mazeGenerator.getSelectionModel().getSelectedItem())+ "\n"+
                    "mazeSearchingAlgorithm=" +getSolvingAlgorithm(solvingAlgorithms.getSelectionModel().getSelectedItem());
            try
            {
                file.write(newSettings.getBytes(StandardCharsets.UTF_8));
                file.close();
                stage.close();
            }
            catch (Exception ex)
            {

            }


        }
        catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Choose properties first!");
            ex.printStackTrace();
        }

        stage.close();
    }
}

