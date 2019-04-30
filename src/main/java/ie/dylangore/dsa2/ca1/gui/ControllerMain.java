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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    private Label lblMsg, lblInfo;

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
                try {
                    BufferedImage buffImage = ImageIO.read(currImage);
                    int type = (buffImage.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : buffImage.getType();

                    BufferedImage resizeImageJpg = resizeImage(buffImage, type, 1280, 600);
                    Files.createDirectories(Paths.get("imgOut"));
                    ImageIO.write(resizeImageJpg, "jpg", new File(Paths.get("imgOut") + "/resize-too_big.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Image too large! Maximum resolution is 1280px x 660px - Resizing");
                openImage(event);
            }
            // If image is below the minimum viewport resolution, recursively call file chooser again until it is below that value
            if(iWidth < 256 || iHeight < 256){
                try {
                    BufferedImage buffImage = ImageIO.read(currImage);
                    int type = (buffImage.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : buffImage.getType();

                    BufferedImage resizeImageJpg = resizeImage(buffImage, type, 256, 256);
                    Files.createDirectories(Paths.get("imgOut"));
                    ImageIO.write(resizeImageJpg, "jpg", new File(Paths.get("imgOut") + "/resize-too_small.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Image too small! Minimum resolution is 256px x 256px.");
                openImage(event);
            }
            System.out.println(currImage);
            lblMsg.setText("Current File: " + currImage.getName());
            lblInfo.setText("");
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
        int[] imageSet = ProcessImage.getImageSet();
        imageContainer.getChildren().clear();
        int birdCount = 0;

        for (int bird: birdSet){
            birdCount++;
            int birdX = (int) (bird % iWidth);
            int birdY = (int) (bird / iWidth);
            int startX = birdX, startY = birdY;
            int endX = birdX, endY = birdY;

            // Loop through the imageSet and birdSet, check if the current pixel is part of the bird and size the box accordingly
            for (int i = 0; i < imageSet.length; i++) {
                if (ProcessImage.find(imageSet, i) == bird) {
                    int currX = (int) (i % iWidth);
                    int currY = (int) (i / iWidth);

                    if (currX < startX) {
                        startX = currX;
                    } else {
                        endX = currX;
                    }

                    if (currY < startY) {
                        startY = currY;
                    } else {
                        endY = currY;
                    }
                }
            }

            System.out.println("Bird " + birdCount + ": x: " + startX + " y: " + startY);

            // Create the box (button used for simplicity as it has text features as standard)
            Button btn = new Button();
            btn.setText(String.valueOf(birdCount));
            btn.setLayoutX(startX);
            btn.setLayoutY(startY);
            // Set the size to be the difference between the start and end coordinates + 5px padding
            btn.setPrefSize(endX - startX + 5, endY - startY + 5);
            btn.getStyleClass().add("btnBird");
            imageContainer.getChildren().add(btn);
        }
//        System.out.println("Container: " + imageContainer.getWidth() + " " + imageContainer.getHeight());
        lblInfo.setText(birdCount + " birds have been found in this image.");
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

    /***
     * Resize image to a specific width and height
     * @param originalImage image to be resized
     * @param type image type
     * @param width desired width
     * @param height desired height
     * @return resized image
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
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
