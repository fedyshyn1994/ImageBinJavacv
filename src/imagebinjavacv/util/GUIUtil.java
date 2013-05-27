/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagebinjavacv.util;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import java.io.File;
import java.io.FileNotFoundException;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

/**
 *
 * @author Tsendee
 */
public class GUIUtil {

    public static opencv_core.IplImage loadAndShowOrExit(File file, int flags) {
        try {
            opencv_core.IplImage image = loadOrExit(file, flags);
            show(image, file.getName());
            return image;
        } catch (Exception ex) {
            System.out.println("Couldn't load image: " + file.getAbsolutePath());
            System.exit(1);
        }
        return null;
    }

    public static opencv_core.IplImage loadOrExit(File file, int flags) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Image file does not exist!" + file.getAbsolutePath());
        }
        return cvLoadImage(file.getAbsolutePath(), flags);
    }

    public static void show(opencv_core.IplImage image, String title) {
        final CanvasFrame canvas = new CanvasFrame(title);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvas.showImage(image);

    }

    public static IplImage scale(int scale, IplImage img) {
        int h = img.cvSize().height();
        int w = img.cvSize().width();
        int nh = h - (h / 100) * scale;
        int nw = w - (w / 100) * scale;
        CvSize cs = new CvSize(nw, nh);
        IplImage dst = cvCreateImage(cs, img.depth(), img.nChannels());
        cvResize(img, dst, CV_INTER_LINEAR);
        return dst;
    }
}
