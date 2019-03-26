package ie.dylangore.dsa2.ca1.gui;

import ie.dylangore.dsa2.ca1.ProcessImage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.nio.file.Paths;

public class ControllerMain {

    @FXML MenuItem menuOpen;
    @FXML ImageView imageMain;

    @FXML Button btnAnalyse;
    @FXML Button btnOriginal;
    @FXML Button btnBW;

    @FXML Label lblMsg;

    private static Stage primaryStage;
    private File currImage;
    private Image image;
    private static double iWidth;
    private static double iHeight;

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
            }else if(event.getSource() == btnAnalyse){
                Image bwImage = ProcessImage.makeBW(image, iWidth, iHeight);
                ProcessImage.processSets(bwImage, (int)iWidth, (int)iHeight);
            }
            else{
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

    public static double getWidth(){
        return iWidth;
    }

    public static double getHeight(){
        return iHeight;
    }

}
