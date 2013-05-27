/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagebinjavacv;

import GUI.ImageProcess;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.CanvasFrame;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacv.cpp.opencv_imgproc.*;
import imagebinjavacv.util.Binarizer;
import imagebinjavacv.util.Enhancer;
import imagebinjavacv.util.GUIUtil;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Tsendee
 */
public class ImageBinJavacv {

    public static String[] enhans = new String[]{"BM (Brightness Correction + Mean Filter)",
        "Brightness Correction",
        "Mean Filter",
        "Cubic Operator",
        "Weighted Filter",
        "Erosion Filter"};
    public static String[] bins = new String[]{"Otsu (with CV_THRESH_OTSU)",
        "Otsu (implementation with Histogram)",
        "Momentum Preserving",
        "Adaptive Thresholding"
    };

    public static ImageProcess processImage(File img, int enhan, int bin) {
        ImageProcess imageProcess = new ImageProcess();
        imageProcess.setOrgImage(cvLoadImage(img.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE));
        imageProcess.setOrgImageTitle(img.getName());
        // Prepocessing enhancing image
        switch (enhan) {
            case 0:
                imageProcess.setEnhImage(Enhancer.paperBM(imageProcess.getOrgImage()));
                imageProcess.setEnhImageTitle(enhans[enhan]);
                break;
            case 1:
                imageProcess.setEnhImage(Enhancer.brigthtNessAdjust(1, 50, imageProcess.getOrgImage()));
                imageProcess.setEnhImageTitle(enhans[enhan]);
                break;
            case 2:
                imageProcess.setEnhImage(Enhancer.meanFilter(imageProcess.getOrgImage()));
                imageProcess.setEnhImageTitle(enhans[enhan]);
                break;
            case 3:
                imageProcess.setEnhImage(Enhancer.paperCubicOperator(imageProcess.getOrgImage()));
                imageProcess.setEnhImageTitle(enhans[enhan]);
                break;
            case 4:
                imageProcess.setEnhImage(Enhancer.paperWeightedFilter(imageProcess.getOrgImage(), null));
                imageProcess.setEnhImageTitle(enhans[enhan]);
                break;
            case 5:
                imageProcess.setEnhImage(Enhancer.paperErosionFilter(imageProcess.getOrgImage()));
                imageProcess.setEnhImageTitle(enhans[enhan]);
                break;
            default:
                imageProcess.setEnhImage(imageProcess.getOrgImage());
                imageProcess.setEnhImageTitle(img.getName());
                break;
        }
        // Bin
        switch (bin) {
            case 0:
                imageProcess.setBinImage(Binarizer.paperOtsuBinarizerCv(imageProcess.getEnhImage()));
                imageProcess.setBinImageTitle(bins[bin]);
                break;
            case 1:
                imageProcess.setBinImage(Binarizer.paperOtsuBinarizer(imageProcess.getEnhImage()));
                imageProcess.setBinImageTitle(bins[bin]);
                break;
            case 2:
                imageProcess.setBinImage(Binarizer.paperMoment(imageProcess.getEnhImage()));
                imageProcess.setBinImageTitle(bins[bin]);
                break;
            case 3:
                imageProcess.setBinImage(Binarizer.paperAdaptiveThresh(imageProcess.getEnhImage()));
                imageProcess.setBinImageTitle(bins[bin]);
                break;
            default:
                break;
        }
        return imageProcess;
    }

    public static void main(String[] args) {

        final IplImage image = GUIUtil.loadAndShowOrExit(new File("image-data/TEST1.BMP"), CV_LOAD_IMAGE_GRAYSCALE);
        IplImage nImage;
//        nImage = Enhancer.brigthtNessAdjust(1, 50, image);
//        GUIUtil.show(nImage, "New image");
//        nImage = Enhancer.meanFilter(image);
//        GUIUtil.show(nImage, "New image");
//        Enhancer.paperBM(nImage);
//        nImage = Enhancer.paperCubicOperator(image);
//        nImage = Enhancer.paperWeightedFilter(image, null);
//        nImage = Enhancer.paperErosionFilter(image);
//        nImage = Binarizer.paperOtsuBinarizerCv(image);
//        GUIUtil.show(nImage, "Cubic operator");
//        nImage = Binarizer.paperOtsuBinarizer(image);
//        nImage = Binarizer.paperMoment(image);
        nImage = Binarizer.paperAdaptiveThresh(image);
        GUIUtil.show(nImage, "Cubic operator");

    }
}
