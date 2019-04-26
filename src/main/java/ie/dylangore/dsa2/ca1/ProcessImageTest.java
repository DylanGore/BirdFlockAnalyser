package ie.dylangore.dsa2.ca1;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/***
 * Junit testing for main methods
 */
class ProcessImageTest extends ApplicationTest {

    /**
     * Setup dummy Java FX application to allow testing of Java FX components
     *
     * @param stage primary stage
     */
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new VBox());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Setup tests
     */
    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    /**
     * Cleanup after tests
     */
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    /**
     * Test making image B&W and counting pixels
     */
    @org.junit.jupiter.api.Test
    void processSets() {
        File imgFile = new File("sample/sample1.jpg");
        Image image = new Image(imgFile.toURI().toString());
        image = ProcessImage.makeBW(image, (int) image.getWidth(), (int) image.getHeight());
        ProcessImage.processSets(image);
        int[] imageSet = ProcessImage.getImageSet();

        int whiteCount = 0;
        int blackCount = 0;

        for (int pixel : imageSet) {
            if (pixel == 0) {
                whiteCount++;
            } else {
                blackCount++;
            }
        }

        // Check if numbers of black and white pixels matches the values from Photoshop for sample1.jpg
        assertEquals(1601, blackCount);
        assertEquals(252499, whiteCount);
    }
}