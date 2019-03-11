package ie.dylangore.dsa2.ca1;

import ie.dylangore.dsa2.ca1.sets.DisjointSets;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProcessImage {

    /**
     * Make image black and white
     * @param image input image in color
     * @param width image width
     * @param height image height
     * @return B&W image
     */
    public static WritableImage makeBW(Image image, double width, double height) {
        WritableImage wImage = new WritableImage((int) width, (int) height);
        PixelReader pReader = image.getPixelReader();
        PixelWriter pWriter = wImage.getPixelWriter();

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

        saveFile(wImage, "imgOut/b&w");
        return wImage;
    }

    /**
     * Count all black and white pixels in an image
     * @param image input image in B&W
     * @param width image width
     * @param height image height
     */
    public static void processSets(Image image, int width, int height){
        DisjointSets.createSets(width, height);
        int[] imageSet = DisjointSets.getImageSet();
        int whiteCount = 0;
        int blackCount = 0;

        System.out.println("Array Size: " + imageSet.length);

        WritableImage wImage = new WritableImage((int) width, (int) height);
        PixelReader pReader = image.getPixelReader();
        int currPixel = 0;

//        if(currPixel <= imageSet.length){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    Color currColor = pReader.getColor(x, y);
                    if(currColor.equals(Color.WHITE)){
                        imageSet[currPixel] = 0;
                        whiteCount++;
                    }else{
                        imageSet[currPixel] = 1;
                        blackCount++;
                    }
                    currPixel++;
                }
            }
//        }

        // Print Results
        DisjointSets.setImageSet(imageSet);
        System.out.println("Image Width: " + width + " Image Height: " + height);
        System.out.println("Black: " + blackCount);
        System.out.println("White: " + whiteCount);
    }

    /**
     * Save image to file
     * @param image image to save
     * @param fileName file name to save as
     */
    private static void saveFile(WritableImage image, String fileName){
        try {
            Files.createDirectories(Paths.get("imgOut"));
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(fileName + ".png") );
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
