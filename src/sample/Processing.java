package sample;

import java.awt.image.BufferedImage;

public class Processing {

    public static BufferedImage shiftingAddition(BufferedImage img,int shift) {
        BufferedImage res = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<img.getWidth()-shift;i++) {
            for(int j=0;j<img.getHeight();j++) {
                int rgb = img.getRGB(i, j);

                int img_red = (rgb>>16)&0xff;
                int img_green = (rgb>>8)&0xff;
                int img_blue = (rgb)&0xff;

                int rgb_shift = img.getRGB(i+shift, j);

                int imgShift_red = (rgb_shift>>16)&0xff;
                int imgShift_green = (rgb_shift>>8)&0xff;
                int imgShift_blue = (rgb_shift)&0xff;



                int res_red = (img_red+imgShift_red)/2;
                int res_green = (img_green+imgShift_green)/2;
                int res_blue = (img_blue+imgShift_blue)/2;




                int res_rgb = ((res_red&0x0ff)<<16)|((res_green&0x0ff)<<8)|(res_blue&0x0ff);
                res.setRGB(i, j, res_rgb);



            }

        }
        return res;

    }

    public static BufferedImage shiftingSubtract(BufferedImage img,int shift) {
        BufferedImage res = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<img.getWidth()-shift;i++) {
            for(int j=0;j<img.getHeight();j++) {
                int rgb = img.getRGB(i, j);

                int img_red = (rgb>>16)&0xff;
                int img_green = (rgb>>8)&0xff;
                int img_blue = (rgb)&0xff;

                int rgb_shift = img.getRGB(i+shift, j);

                int imgShift_red = (rgb_shift>>16)&0xff;
                int imgShift_green = (rgb_shift>>8)&0xff;
                int imgShift_blue = (rgb_shift)&0xff;

                int res_red = (img_red-imgShift_red)/2 + (255/2);
                int res_green = (img_green-imgShift_green)/2 + (255/2);
                int res_blue = (img_blue-imgShift_blue)/2 + (255/2);

                int res_rgb = ((res_red&0x0ff)<<16)|((res_green&0x0ff)<<8)|(res_blue&0x0ff);
                res.setRGB(i, j, res_rgb);



            }

        }
        return res;

    }
    public static BufferedImage filtering(BufferedImage img, int filterVal) {
        BufferedImage res = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);

        int filter = 255 - filterVal;
        for(int i=0;i<img.getWidth();i++) {
            for(int j=0;j<img.getHeight();j++) {
                int rgb = img.getRGB(i, j);

                int img_red = (rgb>>16)&0xff;
                int img_green = (rgb>>8)&0xff;
                int img_blue = (rgb)&0xff;



                int res_gray = (img_red+img_green+img_blue)/3;
//				System.out.print(res_gray+"\t");
                res_gray = (res_gray<filter)? 0 :255;
                //System.out.print(res_gray +"\t");

                int res_rgb = ((res_gray&0x0ff)<<16)|((res_gray&0x0ff)<<8)|(res_gray&0x0ff);
                res.setRGB(i, j, res_rgb);

            }

        }
        return res;
    }
    public static BufferedImage XOR(BufferedImage img1, BufferedImage img2) {
        BufferedImage newImg = new BufferedImage(img1.getWidth(),img1.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<img1.getWidth();i++) {
            for(int j=0;j<img1.getHeight();j++) {
                int rgb_img1 = img1.getRGB(i, j);
                int rgb_img2 = img2.getRGB(i, j);

                int rgb_xor = rgb_img1 ^ rgb_img2;
                newImg.setRGB(i, j, rgb_xor);
            }
        }
        return newImg;
    }
    public static BufferedImage AND(BufferedImage img1, BufferedImage img2) {
        BufferedImage newImg = new BufferedImage(img1.getWidth(),img1.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<img1.getWidth();i++) {
            for(int j=0;j<img1.getHeight();j++) {
                int rgb_img1 = img1.getRGB(i, j);
                int rgb_img2 = img2.getRGB(i, j);

                int rgb_xor = rgb_img1 & rgb_img2;
                newImg.setRGB(i, j, rgb_xor);
            }
        }
        return newImg;
    }
    public static BufferedImage OR(BufferedImage img1, BufferedImage img2) {
        BufferedImage newImg = new BufferedImage(img1.getWidth(),img1.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<img1.getWidth();i++) {
            for(int j=0;j<img1.getHeight();j++) {
                int rgb_img1 = img1.getRGB(i, j);
                int rgb_img2 = img2.getRGB(i, j);

                int rgb_xor = rgb_img1 | rgb_img2;
                newImg.setRGB(i, j, rgb_xor);
            }
        }
        return newImg;
    }

}

