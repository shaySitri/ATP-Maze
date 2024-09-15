package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class NewMaze {

    public TextField txt_Row;
    public TextField txt_Col;
    public Button btnOK;

    MyViewModel myViewModel;
    MazeDisplayer mazeDisplayer;
    Stage stage;

    public TextField getTxtRow()
    {
        return txt_Row;
    }

    public TextField getTxtCol()
    {
        return txt_Col;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void generate()
    {
        try{
            int rows = Integer.parseInt(txt_Row.getText());
            int cols = Integer.parseInt(txt_Col.getText());
            try {
                myViewModel.generateMaze(rows, cols);
            }
            catch (Exception ex){
                Alert errorAlert1 = new Alert(Alert.AlertType.ERROR);
                errorAlert1.setContentText("Fail to generate maze.");
                errorAlert1.show();
            }
        }
        catch (NumberFormatException ex){
            Alert errorAlert2 = new Alert(Alert.AlertType.ERROR);
            errorAlert2.setContentText("Please enter valid numbers.");
            errorAlert2.show();
        }

        stage.close();
    }

    public void setMazeDisplayer(MazeDisplayer maze)
    {
        mazeDisplayer = maze;
    }

    public void setMyViewModel(MyViewModel mvm)
    {
        myViewModel = mvm;
    }
}
