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
import javafx.scene.layout.AnchorPane;
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
    private MenuItem menuOpen, menuShowOriginal, menuShowBW;
    @FXML
    private ImageView imageMain;

    @FXML
    private AnchorPane imageContainer;

    @FXML
    private Button btnAnalyse;

    @FXML
    private Label lblMsg;

    private static Stage primaryStage;
    private File currImage;
    private Image image;
    private static double iWidth;
    private static double iHeight;

    /**
     * Runs on app load
     */
    @FXML
    public void initialize(){
        // Load default image
        currImage = new File("sample/sample1.jpg");
        image = new Image(currImage.toURI().toString());
        setImage(image);
        lblMsg.setText("Current File: " + currImage.getName());
    }

    /**
     * Handles making the imaage black and white and running analysis
     *
     * @param event JavaFX action event
     */
    @FXML
    public void manipulateImage(ActionEvent event) {
        imageContainer.getChildren().clear();
        if (currImage != null) {
            if (event.getSource() == menuShowOriginal || event.getSource() == menuOpen) {
                setImage(image);
            } else if (event.getSource() == menuShowBW) {
                ProcessImage.makeBW(image, iWidth, iHeight);
                setImage(new Image(new File("imgOut/b&w.png").toURI().toString()));
            } else if (event.getSource() == btnAnalyse) {
                Image bwImage = ProcessImage.makeBW(image, iWidth, iHeight);
                ProcessImage.processSets(bwImage);
                drawBoxes();
            } else {
                // If the event source is not handled elsewhere, set the image view to the current image
                setImage(image);
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
            // If image is over the maximum viewport resolution, recursively call file chooser again until it is below that value
            if(iWidth > 1280 || iHeight > 660){
                System.out.println("Image too large! Maximum resolution is 1280px x 660px.");
                openImage(event);
            }
            // If image is below the minimum viewport resolution, recursively call file chooser again until it is below that value
            if(iWidth < 256 || iHeight < 256){
                System.out.println("Image too small! Minimum resolution is 256px x 256px.");
                openImage(event);
            }
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
        imageContainer.getChildren().clear();
        int birdCount = 0;

        for (int bird: birdSet){
            birdCount++;
            int y = (int) (bird/iWidth);
            int x = (int) (bird%iWidth);
            System.out.println("Bird " + birdCount + ": x: " + x + " y: " + y);

            Button btn = new Button();
            btn.setText(String.valueOf(birdCount));
            btn.setLayoutX(x);
            btn.setLayoutY(y);
            btn.getStyleClass().add("btnBird");
            imageContainer.getChildren().add(btn);
        }
        System.out.println("Container: " + imageContainer.getWidth() + " " + imageContainer.getHeight());
//        imageMain.setImage(wImage);
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

    /**
     * Display about dialog window
     */
    @FXML
    private void displayAboutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Bird Flock Analyser");
        alert.setContentText("Data Structures & Algorithms Assignment 1\nBy Dylan Gore (20081224)\n\n\nIcon made by Freepik from www.flaticon.com.\nLicensed by CC 3.0 BY.");
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
        System.out.println("No Image Selected!");
    }

    /**
     * Sets the background of the image container to the required image
     * @param image image to use
     */
    private void setImage(Image image){
        imageMain.setImage(image);
        iWidth = image.getWidth();
        iHeight = image.getHeight();
        System.out.println("iWidth: " + iWidth + " iHieght: " + iHeight);

        imageContainer.setStyle("-fx-background-image: url('" + image.getUrl() + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;");
        imageContainer.setPrefSize(iWidth, iHeight);
        imageContainer.setMaxSize(iWidth, iHeight);
        imageContainer.setMinSize(iWidth, iHeight);
    }
}
