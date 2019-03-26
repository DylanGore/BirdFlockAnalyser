package ie.dylangore.dsa2.ca1.sets;

public class DisjointSets {

    private static int[] imageSet;

    public static void createSets(int width, int height){
        imageSet = new int[width*height];
    }

    public static int[] getImageSet() {
        return imageSet;
    }

    public static void setImageSet(int[] imageSet) {
        DisjointSets.imageSet = imageSet;
    }

//    public static void printImageSet(){
//        for(int i = 0; i < imageSet[0].length; i++){
//            for(int j = 0; i < imageSet.length; j++){
//                System.out.println(imageSet[j][i]);
//            }
//        }
//    }
}
