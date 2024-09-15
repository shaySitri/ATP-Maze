package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public interface IView {
    void setViewModel(MyViewModel myViewModel);
    String getUpdatePlayerRow();
    void setUpdatePlayerRow(int updatePlayerRow);
    String getUpdatePlayerCol();
    void setUpdatePlayerCol(int updatePlayerCol);
    void setStage(Stage newStage);
    void silence(ActionEvent actionEvent);
    void solveMaze(ActionEvent actionEvent);
    void mazeLeft(ActionEvent actionEvent);
    void mazeRight(ActionEvent actionEvent);
    void mazeDown(ActionEvent actionEvent);
    void mazeUp(ActionEvent actionEvent);
    void openFile(ActionEvent actionEvent);
    void saveMaze(ActionEvent actionEvent);
    void keyPressed(KeyEvent keyEvent);
    void setPlayerPosition(int row, int col);
    void mouseClicked(MouseEvent mouseEvent);
    void pressed(MouseEvent event);
    void dragged(MouseEvent event);
    void released();
    void newMaze() throws IOException;
    void about(Event event) throws IOException;
    void changeNameItai(ActionEvent actionEvent);
    void changeNameShay(ActionEvent actionEvent);
    void changeSong();
    void properties() throws IOException;
    void exit(Event event);
    void help() throws IOException;

}
