package ie.dylangore.dsa2.ca1;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to handle image processing - making the image black and white and counting birds
 */
public class ProcessImage {

    /**
     * Value for white pixels to be set to in the imageSet
     */
    private static final int WHITE_VAL = 0;

    private static int imageWidth = 0, imageHeight = 0;
    private static int[] imageSet;
    private static Set<Integer> birdSet = new HashSet<>();


    /**
     * Make image black and white
     *
     * @param image  input image in color
     * @param width  image width
     * @param height image height
     * @return B&W image
     */
    public static WritableImage makeBW(Image image, double width, double height) {
        WritableImage wImage = new WritableImage((int) width, (int) height);
        PixelReader pReader = image.getPixelReader();
        PixelWriter pWriter = wImage.getPixelWriter();
        imageWidth = (int) width;
        imageHeight = (int) height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color currColor = pReader.getColor(x, y);
                if (currColor.getRed() >= 0.5 || currColor.getGreen() >= 0.5 || currColor.getBlue() >= 0.5) {
                    pWriter.setColor(x, y, new Color(1, 1, 1, 1));
                } else {
                    pWriter.setColor(x, y, new Color(0, 0, 0, 1));
                }
            }
        }

        saveImageFile(wImage, "imgOut/b&w");
        return wImage;
    }

    /**
     * Count all black and white pixels in an image
     *
     * @param image input image in B&W
     */
    public static void processSets(Image image) {
        imageSet = new int[imageWidth * imageHeight];
        int whiteCount = 0;
        int blackCount = 0;

        System.out.println("Array Size: " + imageSet.length);

        PixelReader pReader = image.getPixelReader();
        int currPixel = 0;

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Color currColor = pReader.getColor(x, y);
                if (currColor.equals(Color.BLACK)) {
                    imageSet[currPixel] = currPixel;
                    blackCount++;
                } else {
                    imageSet[currPixel] = WHITE_VAL;
                    whiteCount++;
                }
                currPixel++;
            }
        }

        // Print Results
        System.out.println("Image Width: " + imageWidth + " Image Height: " + imageHeight);
        System.out.println("Black: " + blackCount);
        System.out.println("White: " + whiteCount);

        // Print Result from Set
        saveTextFile(Arrays.toString(imageSet), "before");
        countBirds();
    }

    /***
     * Implementation of a union function for a disjoint set
     * @param dSet disjoint set
     * @param val1 value 1
     * @param val2 value 2
     */
    private static void union(int[] dSet, int val1, int val2) {
        dSet[find(dSet, val2)] = find(dSet, val1);
    }

    /***
     * Iterative version of find
     * @param dSet disjoint set
     * @param indexToFind to find
     * @return root
     */
    private static int find(int[] dSet, int indexToFind) {
        /*
            Disregard white pixels as they are not required
            without this line sometimes find returns white
            pixel as the root of a black pixel.
         */
        if(dSet[indexToFind] == WHITE_VAL) {
            //System.out.println(indexToFind);
            return WHITE_VAL;
        }

        while (dSet[indexToFind] != indexToFind) {
            indexToFind = dSet[indexToFind];
        }
        return indexToFind;
    }

    /***W
     * Loop through the imageSet joining all each bird into a disjoint set of black pixels,
     * then loop through imageSet and copy birds (disjoint set of black pixels) to a separate bird set
     */
    private static void countBirds() {
        for (int i = 0; i < imageSet.length; i++) {
            if (imageSet[i] != WHITE_VAL) {
                // If the pixel to the immediate right is black, union it
                if (imageSet[i + 1] != WHITE_VAL) {
                    union(imageSet, i, i + 1);
                }

                // If the pixel to the bottom (current + image width) is black, union it
                if (imageSet[i + imageWidth] != WHITE_VAL) {
                    union(imageSet, i, i + imageWidth);
                }
            }
        }

        /* Loop through imageSet and add all birds to birdSet
          (ie. add any set that have root values of black pixels to master set)
         */
        for (int id = 0; id < imageSet.length; id++) {
            int rootPixel = find(imageSet, id);
            if (rootPixel != WHITE_VAL) {
                birdSet.add(rootPixel);
            }
        }

        System.out.println("Count: " + birdSet.size());
        saveTextFile(birdSet.toString(), "after");
    }


    /**
     * Save image to file
     *
     * @param image    image to save
     * @param fileName file name to save as
     */
    private static void saveImageFile(WritableImage image, String fileName) {
        try {
            Files.createDirectories(Paths.get("imgOut"));
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(fileName + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Save string to text file - used for debug
     *
     * @param toSave   string to save
     * @param fileName file name for new file
     */
    private static void saveTextFile(String toSave, String fileName) {
        try (PrintWriter out = new PrintWriter(fileName + ".txt")) {
            out.print(toSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return image set (set of pixels for a given b&w image)
     *
     * @return int array of pixels
     */
    static int[] getImageSet() {
        return imageSet;
    }

    /**
     * Return bird set
     *
     * @return set of birds
     */
    public static Set<Integer> getBirdSet() {
        return birdSet;
    }
}
