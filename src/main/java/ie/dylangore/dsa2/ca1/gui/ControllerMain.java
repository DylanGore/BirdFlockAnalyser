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
import java.util.Set;

/**
 * Main JavaFX Controller class
 */
public class ControllerMain {

    @FXML
    MenuItem menuOpen;
    @FXML
    ImageView imageMain;

    @FXML
    Button btnAnalyse, btnOriginal, btnBW;

    @FXML
    Label lblMsg;

    private static Stage primaryStage;
    private File currImage;
    private Image image;
    private static double iWidth;
    private static double iHeight;

    /**
     * Handles making the imaage black and white and running analysis
     *
     * @param event JavaFX action event
     */
    @FXML
    public void manipulateImage(ActionEvent event) {
        if (currImage != null) {
            if (event.getSource() == btnOriginal || event.getSource() == menuOpen) {
                imageMain.setImage(image);
            } else if (event.getSource() == btnBW) {
                imageMain.setImage(ProcessImage.makeBW(image, iWidth, iHeight));
            } else if (event.getSource() == btnAnalyse) {
                Image bwImage = ProcessImage.makeBW(image, iWidth, iHeight);
                ProcessImage.processSets(bwImage);
                drawBoxes();
            } else {
                // If the event source is not handled elsewhere, set the image view to the current image
                imageMain.setImage(image);
            }
        } else {
            displayNoImageAlert();
        }
    }

    /**
     * Get image using a file chooser window
     *
     * @param event JavaFX action evemt
     */
    @FXML
    public void openImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        // Limit to only common image files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Select an image");
        // Set to open in working directory
        fileChooser.setInitialDirectory(new File(Paths.get("").toAbsolutePath().toString()));

        // Show dialog and get image
        currImage = fileChooser.showOpenDialog(primaryStage);
        if (currImage != null) {
            image = new Image(currImage.toURI().toString());
            iWidth = image.getWidth();
            iHeight = image.getHeight();
            System.out.println(currImage);
            lblMsg.setText("Current File: " + currImage.getName());
        }

        // Call manipulate image to display currImage
        manipulateImage(event);
    }

    /***
     * Runs on app exit
     */
    @FXML
    public void close() {
        System.out.println("Exiting...");
        Platform.exit();
    }

    /**
     * Used to get stage from Main class
     *
     * @param newStage main stage
     */
    public static void setStage(Stage newStage) {
        primaryStage = newStage;
    }

    /**
     * Draw boxes around birds in a given image
     */
    private void drawBoxes() {
        Set<Integer> birdSet = ProcessImage.getBirdSet();
        // TODO
    }

    /**
     * Display an alert box if not image has been selected
     */
    private void displayNoImageAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("An Error Has Occurred!");
        alert.setContentText("No image selected!");
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
        System.out.println("No Image Selected!");
    }
}
