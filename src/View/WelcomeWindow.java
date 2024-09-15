package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.VolatileImage;
import java.io.IOException;

public class WelcomeWindow {
    public Button _newGame;
    Stage stage;

    public void startNewGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Welcome to a-MAZE-ing!");
        primaryStage.setScene(new Scene(root, 1000, 700));

        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        MyViewController myViewController = fxmlLoader.getController();
        myViewController.setStage(primaryStage);
        myViewController.setViewModel(myViewModel);

        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                myViewController.exit(windowEvent);
            }
        });

        stage.close();
    }

}
