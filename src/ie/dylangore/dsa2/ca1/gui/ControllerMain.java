package ie.dylangore.dsa2.ca1.gui;

import ie.dylangore.dsa2.ca1.ProcessImage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.nio.file.Paths;

public class ControllerMain {

    @FXML MenuItem menuOpen;
    @FXML ImageView imageMain;

    @FXML Button btnOriginal;
    @FXML Button btnBW;

    @FXML Label lblMsg;

    private static Stage primaryStage;
    private File currImage;
    private Image image;
    private double iWidth;
    private double iHeight;

//    @FXML
//    public void initialize(){
//    }

    @FXML
    public void manipulateImage(ActionEvent event){
        if(currImage != null){
            if(event.getSource() == btnOriginal || event.getSource() == menuOpen){
                imageMain.setImage(image);
            }else if(event.getSource() == btnBW){
                imageMain.setImage(ProcessImage.makeBW(image, iWidth, iHeight));
            }else{
                imageMain.setImage(image);
            }
        }else{
            displayNoImageAlert();
        }
    }

    @FXML
    public void openImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        // Set to open in working directory
        fileChooser.setInitialDirectory(new File(Paths.get("").toAbsolutePath().toString()));
        currImage = fileChooser.showOpenDialog(primaryStage);
        image = new Image(currImage.toURI().toString());
        iWidth = image.getWidth();
        iHeight = image.getHeight();
        System.out.println(currImage);

        if(currImage != null){
            lblMsg.setText("Current File: " + currImage.getName());
        }

        manipulateImage(event);
    }

    @FXML
    public void close(){
        System.out.println("Exiting...");
        Platform.exit();
    }

    public static void setStage(Stage newStage){
        primaryStage = newStage;
    }

    private void displayNoImageAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("An Error Has Occurred!");
        alert.setContentText("No image selected!");
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
        System.out.println("No Image Selected!");
    }

}
