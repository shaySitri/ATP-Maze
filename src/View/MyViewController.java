package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements Initializable, Observer ,IView{
    public MyViewModel myViewModel;
    public Pane PaneTry;

    public void setViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
        this.myViewModel.addObserver(this);
    }

    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public AnchorPane borderPane;
    public MediaPlayer mediaPlayerShay;
    public MediaPlayer mediaPlayerItai;
    public MediaPlayer mediaPlayerEnd;
    public Stage stage;
    public boolean musicOn = true;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }
    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        String songPath = "resources/songs/" + mazeDisplayer.getName() + ".mp3";
        Media media = new Media((new File(songPath)).toURI().toString());
        mediaPlayerShay = new MediaPlayer(media);

        mediaPlayerShay.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayerShay.seek(Duration.ZERO);
            }
        });
        mediaPlayerShay.play();

        borderPane.setMinSize(0, 0);
        PaneTry.prefHeightProperty().bind(borderPane.heightProperty());
        PaneTry.prefWidthProperty().bind(borderPane.prefWidthProperty());
        mazeDisplayer.widthProperty().bind(PaneTry.widthProperty());
        mazeDisplayer.heightProperty().bind(PaneTry.heightProperty());
        mazeDisplayer.heightProperty().addListener(event -> mazeDisplayer.drawMaze(myViewModel.getMaze()));
        mazeDisplayer.widthProperty().addListener(event -> mazeDisplayer.drawMaze(myViewModel.getMaze()));

    }

    public void setStage(Stage newStage) {
        stage = new Stage();
    }

    public void silence(ActionEvent actionEvent) {
        if(musicOn){
            musicOn = false;
            if(mediaPlayerEnd != null){
                mediaPlayerEnd.pause();
                return;
            }
            if(MazeDisplayer.name.equals("itai")){
                if(mediaPlayerItai != null)
                    mediaPlayerItai.pause();
            }
            else
                mediaPlayerShay.pause();
        }
        else {
            musicOn = true;
            if(mediaPlayerEnd != null){
                mediaPlayerEnd.play();
                return;
            }
            if(MazeDisplayer.name.equals("itai")){
                if(mediaPlayerItai != null)
                    mediaPlayerItai.play();
            }
            else
                mediaPlayerShay.play();
        }
    }

    public void solveMaze(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (myViewModel.getMaze() != null && myViewModel.getSolution() == null) {
            alert.setContentText("Solving maze...");
            alert.show();
            myViewModel.solveMaze();
        } else if (myViewModel.getSolution() == null) {
            alert.setContentText("You might want to generate maze first.");
            alert.show();
        }
    }

    public void mazeLeft(ActionEvent actionEvent) {
        PaneTry.getChildren().get(0).setTranslateX(PaneTry.getChildren().get(0).getTranslateX() - 10);
    }

    public void mazeRight(ActionEvent actionEvent) {
        PaneTry.getChildren().get(0).setTranslateX(PaneTry.getChildren().get(0).getTranslateX() + 10);
    }

    public void mazeDown(ActionEvent actionEvent) {

        PaneTry.getChildren().get(0).setTranslateY(PaneTry.getChildren().get(0).getTranslateY() + 10);
    }

    public void mazeUp(ActionEvent actionEvent) {
        PaneTry.getChildren().get(0).setTranslateY(PaneTry.getChildren().get(0).getTranslateY() - 10);
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File file = fc.showOpenDialog(stage);

        try {
            String path = file.getPath();
            myViewModel.openMaze(path);
        } catch (Exception ex) {
            Alert fileAlert = new Alert(Alert.AlertType.ERROR);
        }
    }

    public void saveMaze(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File file = fc.showSaveDialog(stage);

        try {
            String path = file.getPath();
            myViewModel.saveMaze(path);
        } catch (Exception ex) {
            Alert fileAlert = new Alert(Alert.AlertType.ERROR);
        }
    }


    public void keyPressed(KeyEvent keyEvent) {
        myViewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }


    public void setPlayerPosition(int row, int col) {
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
        mouseEvent.consume();
    }


    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change) {
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "Game Over" -> gameOver();
            default -> System.out.println("Not implemented change: " + change);
        }
    }


    private void gameOver() {
        if(musicOn){
            if(mazeDisplayer.getName().equals("shay"))
                mediaPlayerShay.pause();
            else
                if(mediaPlayerItai!=null)
                    mediaPlayerItai.pause();
            Media media = new Media((new File("resources/songs/queen-we-are-the-champions.mp3")).toURI().toString());
            mediaPlayerEnd = new MediaPlayer(media);

            mediaPlayerEnd.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayerEnd.seek(Duration.ZERO);
                }
            });
            mediaPlayerEnd.play();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("You are a-MAZE-ing!");
        Optional<ButtonType> ok = alert.showAndWait();
    }

    private void mazeSolved() {
        mazeDisplayer.setSolution(myViewModel.getSolution());
    }

    private void playerMoved() {
        setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
    }

    private void mazeGenerated() {
        MazeDisplayer.setDelta(0);
        mazeDisplayer.drawMaze(myViewModel.getMaze());
        if(musicOn){
            if (mediaPlayerEnd != null) {
                mediaPlayerEnd.pause();
                if(mazeDisplayer.getName().equals("shay"))
                    mediaPlayerShay.play();
                else
                    mediaPlayerItai.play();
            }
        }
    }

    @FXML
    private void scrollEvent(ScrollEvent sE) {
        double delta = sE.getDeltaY();
        if (sE.isControlDown()) {
            MazeDisplayer.changeDelta(delta);
        }
        mazeDisplayer.drawMazeScroll(myViewModel.getMaze());
    }

    private int clickedInRow, clickedInCol, releasedInRow, releasedInCol;

    public void pressed(MouseEvent event) {


        clickedInRow = (int) (event.getScreenX() - mazeDisplayer.getHeight());
        clickedInCol = (int) (event.getScreenY() - mazeDisplayer.getWidth());


    }

    public void dragged(MouseEvent event) {

        releasedInCol = (int) (event.getScreenY() - mazeDisplayer.getHeight() - clickedInCol);

        releasedInRow = (int) (event.getScreenX()- mazeDisplayer.getWidth() - clickedInRow);
    }

    public void released() {
        if(myViewModel.getMaze() != null){
            double cellH = mazeDisplayer.getHeight()  / myViewModel.getMaze().getMazeRows();
            double cellW = mazeDisplayer.getWidth() / myViewModel.getMaze().getMazeCols();
            System.out.println(cellH + " , " + cellW);
            if (releasedInRow < 2 * cellW && releasedInCol < 2 * cellH)
                myViewModel.movePlayerMouse(( releasedInRow / cellW), (releasedInCol / cellH));
            System.out.println(( releasedInRow / cellW) + " , " + releasedInCol / cellH);

        }
    }


    public void newMaze() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewMaze.fxml"));
        Parent newMenu = fxmlLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("New Maze");
        newStage.setScene(new Scene(newMenu, 400, 200));
        newStage.resizableProperty().setValue(false);
        newStage.show();
        NewMaze newMaze = fxmlLoader.getController();
        newMaze.setStage(newStage);
        newMaze.setMazeDisplayer(mazeDisplayer);
        newMaze.setMyViewModel(myViewModel);
        stage.close();
    }
    @FXML
    public void about(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
        Parent about = fxmlLoader.load();
        Stage aboutStage = new Stage();
        aboutStage.setTitle("About");
        aboutStage.setScene(new Scene(about, 300, 500));
        aboutStage.resizableProperty().setValue(false);
        aboutStage.alwaysOnTopProperty();
        AboutWindow aboutWindow = fxmlLoader.getController();
        aboutStage.show();
    }
    public void changeNameItai(ActionEvent actionEvent) {
        MazeDisplayer.changeName("itai");
        mazeDisplayer.drawMazeScroll(myViewModel.getMaze());
        if (musicOn) {
            changeSong();
        }
    }
    public void changeNameShay(ActionEvent actionEvent) {
        MazeDisplayer.changeName("shay");

        mazeDisplayer.drawMazeScroll(myViewModel.getMaze());
        if (musicOn){
            changeSong();
        }
    }
    public void changeSong() {
        if (mediaPlayerEnd == null ) {
            //mediaPlayerEnd.pause();
            if(mazeDisplayer.getName().equals("itai")){
                mediaPlayerShay.pause();
                if (mediaPlayerItai == null){
                    String songPath = "resources/songs/" + mazeDisplayer.getName() + ".mp3";
                    Media media = new Media((new File(songPath)).toURI().toString());
                    mediaPlayerItai = new MediaPlayer(media);

                    mediaPlayerItai.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayerItai.seek(Duration.ZERO);
                        }
                    });
                }
                mediaPlayerItai.play();

            }
            else{
                if(mediaPlayerItai!=null)
                    mediaPlayerItai.pause();
                mediaPlayerShay.play();
            }

        }
    }

    public void properties() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SettingsWindow.fxml"));
        Parent settingsFXML = fxmlLoader.load();
        Stage settings = new Stage();
        settings.setTitle("Properties");
        settings.setScene(new Scene(settingsFXML, 344, 245));
        settings.resizableProperty().setValue(false);
        SettingsWindow settingsWindow = fxmlLoader.getController();
        settingsWindow.setStage(settings);
        settingsWindow.setSettings();
        settings.show();
    }

    public void exit(Event event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> ok = alert.showAndWait();
        if(ok.get() != ButtonType.OK){
            event.consume();
            return;
        }
        myViewModel.closeServer();
        System.exit(0);
    }

    public void help() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
        Parent settingsFXML = fxmlLoader.load();
        Stage help = new Stage();
        help.setTitle("Help");
        help.setScene(new Scene(settingsFXML, 300, 436));
        help.resizableProperty().setValue(false);
        help.show();
    }
}
