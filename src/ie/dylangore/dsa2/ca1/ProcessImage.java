package ie.dylangore.dsa2.ca1;

import ie.dylangore.dsa2.ca1.sets.DisjointSets;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class ProcessImage {

    public static WritableImage makeBW(Image image, double width, double height) {
        WritableImage wImage = new WritableImage((int) width, (int) height);
        PixelReader pReader = image.getPixelReader();
        PixelWriter pWriter = wImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color currColor = pReader.getColor(x, y);
//                        System.out.println("RED: " + currColor.getRed() + " GREEN: " + currColor.getGreen() + " BLUE: " + currColor.getBlue());
                if (currColor.getRed() >= 0.5 || currColor.getGreen() >= 0.5 || currColor.getBlue() >= 0.5) {
                    pWriter.setColor(x, y, new Color(1, 1, 1, 1));
                } else {
                    pWriter.setColor(x, y, new Color(0, 0, 0, 1));
                }
            }
        }

        return wImage;
    }

    public static void processSets(Image image, int width, int height){
        DisjointSets.createSets(width, height);
        int[][] imageSet = DisjointSets.getImageSet();

        System.out.println("Array Size: " + imageSet.length + "\n");

        WritableImage wImage = new WritableImage((int) width, (int) height);
        PixelReader pReader = image.getPixelReader();
        PixelWriter pWriter = wImage.getPixelWriter();

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color currColor = pReader.getColor(x, y);
//                System.out.println("currColor: " + currColor);
                if(currColor.equals(Color.WHITE)){
                    imageSet[x][y] = 0;
                }else{
                    imageSet[x][y] = 1234;
                }
            }
        }

        DisjointSets.setImageSet(imageSet);
//        System.out.println(Arrays.deepToString(imageSet));
    }
}
