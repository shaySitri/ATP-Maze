package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;

public class AboutWindow {
    public ImageView imgShay;


    public void setImgShay() {
        Image image = new Image(getShayPath().toString());
        imgShay.setImage(image);
    }


    public String getShayPath(){
        // put good path
        return "";
//        return "./resources/images/shay.jpg";

    }


}
