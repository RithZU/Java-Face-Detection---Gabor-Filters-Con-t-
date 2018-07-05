package sample;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ImageView gaborImageView;
    @FXML
    private ImageView beforeImage;
    @FXML
    private ImageView afterImage;
    @FXML
    private Slider thresholdSlider;
    @FXML
    private ComboBox<String> gaborComboBox;
    @FXML
    private Label thresholdLabel;
    @FXML
    private TextField thresholdVal1;
    @FXML
    private TextField thresholdVal2;
    @FXML
    private Button btn_arrow;
    private String currentBeforePath = null;
    private String currentBeforeImageName = null;
    private String currentAfterPath = null;
    private String currentAfterImageName = null;
    private File beforeFile, afterFile;
    private String filePath = null;
    private String fileName = null;
    private String grayScaledFilePath = null;
    private String grayScaledFileName = null;
    private String gaboredImageFilePath = null;
    private String currentThresholdPath = null;
    private String XORFilePath = null;
    private String ANDFilePath = null;
    private String ORFilePath = null;
    private Image defaultImg = null;
    private boolean thresholded = false;

    private int currentThresholdValue = 0;
    private File defaultImageFile = null;
    ArrayList<BufferedImage> gabors = new ArrayList<BufferedImage>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i=0;i<40;i++) {
			BufferedImage perGabor = null;
			try {
				perGabor = ImageIO.read(new File("./res/gabors/gabor"+i+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gabors.add(perGabor);
		}
        File file = new File("./res/gabors/gabor0.jpg");
        Image image = new Image(file.toURI().toString());
        gaborImageView.setImage(image);

        defaultImageFile = new File("./res/default.jpg");
        defaultImg = new Image(defaultImageFile.toURI().toString());
        beforeImage.setImage(defaultImg);
        afterImage.setImage(defaultImg);

        Image arrow = new Image(new File("./res/arrow.png").toURI().toString());
        btn_arrow.setGraphic(new ImageView(arrow));

        thresholdSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            currentThresholdValue = (newValue.intValue());
           thresholdLabel.setText(Integer.toString(currentThresholdValue));




                    File gaborFile = new File("./res/results/"+getFileName(new File(currentBeforePath))+"_threshold" + currentThresholdValue+".jpg");
                    currentAfterPath = gaborFile.getAbsolutePath();
                    Image thresholdImage = new Image(gaborFile.toURI().toString());
                    afterImage.setImage(thresholdImage);



        });



    }
    public void onLoadImage(){
        FileChooser.ExtensionFilter imageFilter
                = new FileChooser.ExtensionFilter("Image Files", "*.jpg");
        FileChooser imgChooser = new FileChooser();
        imgChooser.getExtensionFilters().add(imageFilter);
        File selectedImage = imgChooser.showOpenDialog(null);
        if(selectedImage!=null){
            currentBeforePath = selectedImage.getAbsolutePath();
            beforeImage.setImage(new Image(selectedImage.toURI().toString()));


        }

    }
    public void applyGrayScale(){
        if(currentBeforePath!=null) {
            BufferedImage grayImage = Utils.convertToGrayScaled(Utils.readImage(currentBeforePath));
            beforeFile = new File(currentBeforePath);
            String grayScaledName = getFileName(beforeFile)+"_grayScaled";
            //System.out.println(grayScaledName);
            Utils.writeImage(grayImage,grayScaledName);
            File grayFile = new File("./res/results/"+grayScaledName+".jpg");
            currentAfterPath = grayFile.getAbsolutePath();
            currentAfterImageName = getFileName(grayFile);
            //System.out.println(currentAfterPath);
            afterImage.setImage(new Image(grayFile.toURI().toString()));
            //Utils.writeImage(grayImage,);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please choose an image");


            alert.showAndWait();
        }
    }
    @FXML public void comboListener() {


        //Show the scene here
        String selectedValue = gaborComboBox.getSelectionModel().getSelectedItem();

        for(int i=0;i<40;i++){
            if(selectedValue.equals("Gabor "+i)){
                File gaborFile = new File("./res/gabors/gabor"+i+".jpg");
                Image image = new Image(gaborFile.toURI().toString());
                gaborImageView.setImage(image);
            }
        }

    }
    public void applyGabor(){
        if(currentBeforePath!=null){
            for(int i=0;i<40;i++){
                if(gaborComboBox.getSelectionModel().getSelectedItem().equals("Gabor "+i)){

                    GaborFiltering.applyGabor(gabors.get(i),Utils.readImage(currentBeforePath),currentBeforeImageName,"gabor_"+i);
                    gaboredImageFilePath = "./res/results/"+currentBeforeImageName+"_"+"gabor_"+i+".jpg";
                    File gaboredImageFile = new File(gaboredImageFilePath);
                    currentAfterPath = gaboredImageFile.getAbsolutePath();
                    currentAfterImageName = getFileName(gaboredImageFile);
                    afterImage.setImage(new Image(gaboredImageFile.toURI().toString()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done");
                    alert.setHeaderText("Successfully Applying " + "Gabor "+i);


                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No image found");


            alert.showAndWait();
        }
    }
    public void applyThreshold(){

        if(currentBeforePath!=null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Threshold");
            alert.setHeaderText("Apply thresholding");


            alert.showAndWait();

            for (int i = 0; i <= 255; i++) {

                Utils.writeImage(Processing.filtering(Utils.readImage(currentBeforePath), i), getFileName(new File(currentBeforePath)) + "_threshold" + i);


            }
            thresholded = true;
            Alert alertDone = new Alert(Alert.AlertType.INFORMATION);
            alertDone.setTitle("Thresholding");
            alertDone.setHeaderText("Thresholds Applied");


            alert.showAndWait();
        }
        else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Error");
            alert1.setHeaderText("Apply gabor filtering first");


            alert1.showAndWait();
        }
    }
    public void onReset(){
        beforeImage.setImage(defaultImg);
        afterImage.setImage(defaultImg);
    }
    public void XOR(){

        String TValue1 = thresholdVal1.getText().toString();
        String TValue2 = thresholdVal2.getText().toString();
//        if(!TValue1.equals("")&&!TValue2.equals("")){
            //BufferedImage img_t1 = Utils.readImage("./res/results/"+fileName+"_threshold" + Integer.parseInt(TValue1)+".jpg");
            //BufferedImage img_t2 = Utils.readImage("./res/results/"+fileName+"_threshold" + Integer.parseInt(TValue2)+".jpg");
            //BufferedImage XOR_image = Processing.XOR(img_t1,img_t2);



            BufferedImage img_t1 = Utils.readImage("./res/results/"+getFileName(new File(currentBeforePath))+"_threshold"+TValue1+".jpg");
            BufferedImage img_t2 = Utils.readImage("./res/results/"+getFileName(new File(currentBeforePath))+"_threshold"+TValue2+".jpg");
            BufferedImage XOR_image = Processing.XOR(img_t1,img_t2);

            Utils.writeImage(XOR_image,"XOR182_222");
            XORFilePath = "./res/results/XOR182_222.jpg";
            File xorFile = new File(XORFilePath);
            Image xorImg = new Image(xorFile.toURI().toString());
            currentAfterPath = xorFile.getAbsolutePath();

            afterImage.setImage(xorImg);
       // }
    }
    public void AND(){
        String TValue1 = thresholdVal1.getText().toString();
        String TValue2 = thresholdVal2.getText().toString();
//        if(!TValue1.equals("")&&!TValue2.equals("")){
        //BufferedImage img_t1 = Utils.readImage("./res/results/"+fileName+"_threshold" + Integer.parseInt(TValue1)+".jpg");
        //BufferedImage img_t2 = Utils.readImage("./res/results/"+fileName+"_threshold" + Integer.parseInt(TValue2)+".jpg");
        //BufferedImage XOR_image = Processing.XOR(img_t1,img_t2);

        BufferedImage img_t1 = Utils.readImage("./res/results/faces20_threshold182.jpg");
        BufferedImage img_t2 = Utils.readImage("./res/results/faces20_threshold222.jpg");
        BufferedImage AND_image = Processing.AND(img_t1,img_t2);

        Utils.writeImage(AND_image,"AND182_222");
        ANDFilePath = "./res/results/AND_182_222";
        afterImage.setImage(new Image(new File(XORFilePath).toURI().toString()));
        // }
    }
    public void OR(){
        String TValue1 = thresholdVal1.getText().toString();
        String TValue2 = thresholdVal2.getText().toString();
//        if(!TValue1.equals("")&&!TValue2.equals("")){
        //BufferedImage img_t1 = Utils.readImage("./res/results/"+fileName+"_threshold" + Integer.parseInt(TValue1)+".jpg");
        //BufferedImage img_t2 = Utils.readImage("./res/results/"+fileName+"_threshold" + Integer.parseInt(TValue2)+".jpg");
        //BufferedImage XOR_image = Processing.XOR(img_t1,img_t2);

        BufferedImage img_t1 = Utils.readImage("./res/results/faces20_threshold182.jpg");
        BufferedImage img_t2 = Utils.readImage("./res/results/faces20_threshold222.jpg");
        BufferedImage OR_image = Processing.OR(img_t1,img_t2);



        Utils.writeImage(OR_image,"OR182_222");
        ORFilePath = "./res/results/OR_182_222.jpg";
        afterImage.setImage(new Image(new File(XORFilePath).toURI().toString()));
        // }
    }
    public void edgeDetection(){
        if(currentBeforePath!=null){
            BufferedImage currentImg = Utils.readImage(currentBeforePath);
            Utils.writeImage(EdgeDetection.sobelOperation(currentImg),"sobeled"+getFileName(new File(currentBeforePath)));
            currentAfterPath = "./res/results/sobeled"+ getFileName(new File(currentBeforePath))+".jpg";
            System.out.println(currentAfterPath);
            Utils.setImageWithPath(afterImage,currentAfterPath);
        }
    }
    public void onSwitch(){
        currentBeforePath = currentAfterPath;
        currentBeforeImageName = getFileName(new File(currentBeforePath));
        currentAfterImageName = null;
        currentAfterPath = null;
        setImage(beforeImage,new File(currentBeforePath));
        setImage(afterImage,defaultImageFile);

    }
    public void setImage(ImageView imgView, File file){

        Image img = new Image(file.toURI().toString());
        imgView.setImage(img);
    }
    public String getFileName(File file){
        return file.getName().substring(0,file.getName().length()-4);
    }



}
