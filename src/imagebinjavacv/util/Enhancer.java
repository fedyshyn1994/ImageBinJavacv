/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagebinjavacv.util;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

/**
 *
 * @author Tsendee
 */
public class Enhancer {

    /**
     * BM Filtering (Brightness correction + Mean filter) method from the paper
     *
     * @param image
     */
    public static IplImage paperBM(IplImage image) {
        IplImage nImage = Enhancer.brigthtNessAdjust(1, 10, image);
        IplImage nImage1 = meanFilter(nImage);
        return nImage1;
    }

    /**
     * Applies the following transformer to the given image g(i,j) = alpha *
     * f(i,j) + beta int alpha = 1; // [1 - 3] int beta = 50; // 0 - 100
     *
     * @param alpha
     * @param beta
     */
    public static IplImage brigthtNessAdjust(int alpha, int beta, IplImage image) {
        int nChannel = image.nChannels();
        CvMat cvMat = image.asCvMat();
        CvMat nCvMat = cvMat.clone();
        for (int y = 0; y < cvMat.rows(); y++) {
            for (int x = 0; x < cvMat.cols(); x++) {
                for (int c = 0; c < nChannel; c++) {
                    nCvMat.put(y, x, c, alpha * cvMat.get(y, x, c) + beta);
                }
            }
        }
        return nCvMat.asIplImage();
    }

    /**
     * Mean filter implemented using cvSmooth with CV_BLUR option
     *
     * @param image
     * @return
     */
    public static IplImage meanFilter(IplImage image) {
        IplImage dst = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
        cvSmooth(image, dst, CV_BLUR, 3);
        return dst;
    }

    /**
     * Performs Cubic operator using the equation given in the paper
     *
     * @param image
     * @return
     */
    public static IplImage paperCubicOperator(IplImage image) {
        int nChannel = image.nChannels();
        CvMat cvMat = image.asCvMat();
        CvMat nCvMat = cvMat.clone();
        for (int y = 0; y < cvMat.rows(); y++) {
            for (int x = 0; x < cvMat.cols(); x++) {
                for (int nc = 0; nc < nChannel; nc++) {
                    double p1, p2, p3, p4;
                    p1 = p2 = p3 = p4 = 0;
                    int a = 0, b = 1, c = 2, d = 3, e = 4, f = 5, g = 6, h = 7, i = 8;
                    double[] R = new double[9]; // 0-a, 1-b, 2-c, 3-d, 4-e, 5-f, 6-g, 7-h, 8-i
                    int Ri = 0;
                    for (int s = y - 1; s <= y + 1; s++) {
                        for (int z = x - 1; z <= x + 1; z++) {
                            try {
                                R[Ri] = cvMat.get(s, z, nc);
                            } catch (Exception ex) {
                                R[Ri] = 0;
                            }
                            Ri++;
                        }
                    }
                    double Zij = Math.pow(R[a] - R[c], 2) * (2 * R[b] - R[a] - R[c])
                            + Math.pow(R[d] - R[e], 2) * (2 * R[b] - R[d] - R[e])
                            + Math.pow(R[f] - R[h], 2) * (2 * R[b] - R[f] - R[h])
                            + Math.pow(R[g] - R[i], 2) * (2 * R[b] - R[g] - R[i]);
                    nCvMat.put(y, x, nc, cvMat.get(y, x, nc) + Zij);
                }
            }
        }
        return nCvMat.asIplImage();
    }

    /**
     * Performs 3x3 weighted filtering, it uses filter2d with given kernel in the paper
     * Another option is to use Laplacian filter (not sure about it)
     * @param image
     * @param dkernelp
     * @return 
     */
    public static IplImage paperWeightedFilter(IplImage image, double[][] dkernelp) {
        IplImage dst = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
        double[][] dkernel = {
            {-2, 1, -2},
            {1, 6, 1},
            {-2, 1, -2}
        };
        if (dkernelp != null) {
            dkernel = dkernelp;
        }
        CvMat kernel = CvMat.create(dkernel.length, dkernel[0].length);
        for (int i = 0; i < dkernel.length; i++) {
            for (int j = 0; j < dkernel[i].length; j++) {
                kernel.put(i, j, dkernel[i][j]);
            }
        }
        cvFilter2D(image, dst, kernel, CvPoint.ZERO);
        return dst;
    }
    
    /**
     * Filters image with opencv Erode function 
     * @param image
     * @return 
     */
    public static IplImage paperErosionFilter(IplImage image){
        IplImage dst = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
        cvErode(image, dst, null, 1);
        return dst;
    }
}
