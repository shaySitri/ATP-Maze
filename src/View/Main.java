package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Welcome.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Welcome to a-MAZE-ing!");
        primaryStage.setScene(new Scene(root, 500, 350));
        primaryStage.show();
        WelcomeWindow welcomeWindow = fxmlLoader.getController();
        welcomeWindow.stage = primaryStage;
        primaryStage.resizableProperty().setValue(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
