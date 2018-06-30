package sample;

import javafx.scene.control.Alert;

import java.awt.image.BufferedImage;

public class GaborFiltering {
    public static void applyGabor(BufferedImage gabor, BufferedImage grayScale, String imageName,String gaborName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Start");
        alert.setHeaderText("Applying Gabor");


        alert.showAndWait();

        int factor = 9;
        int gaborWidth = gabor.getWidth();
        int gaborHeight = gabor.getHeight();
       // int stepSize = gabor.getWidth()/factor;
        int stepSize = 1;
        double[][] result = new double[(grayScale.getWidth()/stepSize)][(grayScale.getHeight()/stepSize)];
        int n=0;
        for (int i = 0; i <= result.length - stepSize + 1; i++) {

            for (int j = 0; j <= result[0].length - stepSize + 1; j++) {
                //System.out.println(i+"\t"+j);


                double tempResult = 0;
                BufferedImage subGrayScale = null;
                try {
                    subGrayScale = grayScale.getSubimage(stepSize * i, j * stepSize, gaborWidth, gaborHeight);
                }
                catch(Exception e){
                    break;
                }

                tempResult = Matrix.dotProduct(Utils.imageToPixels(subGrayScale), Utils.imageToPixels(gabor), 20);
                //System.out.print(tempResult+"\t");
                result[i][j] = tempResult;
                //Utils.writeImage(subLena, "subLena"+(n++));


                //Utils.writeImage(Utils.pixelsToImage(subLenaPixels), "subGabored"+(n++));


            }
            if(i%100==0)
            System.out.println(i);
            //System.out.println();


        }
        Utils.writeImage(Utils.pixelsToImage(result), imageName+"_"+gaborName);



    }
}
