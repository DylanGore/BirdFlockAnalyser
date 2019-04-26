package ie.dylangore.dsa2.ca1;

import ie.dylangore.dsa2.ca1.gui.ControllerMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Main method
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * JavaFX start method
     *
     * @param primaryStage main JavaFX stage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox vb = FXMLLoader.load(getClass().getResource("/gui/main.fxml"));
            Scene scene = new Scene(vb);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/gui/icon.png")));
            scene.getStylesheets().add(getClass().getResource("/gui/main.css").toExternalForm());
            primaryStage.setResizable(false);
            primaryStage.setTitle("Bird Flock Analyser");
            System.out.println("Loading...");
            ControllerMain.setStage(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}