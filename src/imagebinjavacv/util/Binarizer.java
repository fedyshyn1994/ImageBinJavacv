/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagebinjavacv.util;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_legacy.*;

/**
 *
 * @author Tsendee
 */
public class Binarizer {

    static int numberOfBins = 256;
    static float minRange = 0.0f;
    static float maxRange = 255.0f;

    /**
     * Binarizes an image with Otsu's method (opencv)
     *
     * @param image
     * @return
     */
    public static IplImage paperOtsuBinarizerCv(IplImage image) {
        IplImage dst = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
        cvThreshold(image, dst, 128, 255, CV_THRESH_OTSU);
        return dst;
    }

    /**
     * Binarizes an image with Otsu's method
     *
     * @param image
     * @return
     */
    public static IplImage paperOtsuBinarizer(IplImage image) {
        IplImage dst = image.clone(); //cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
        int[] histArray = getHistogramAsArray(image);
        int thresh = otsu(histArray);
        cvThreshold(image, dst, thresh, 255, CV_THRESH_BINARY);
        return dst;
    }

    /**
     * Binarization with Momentum Preserving threshold [10]
     * @param image
     * @return 
     */
    public static IplImage paperMoment(IplImage image) {
        IplImage dst = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
        int[] histArray = getHistogramAsArray(image);
        int thresh = moments(histArray);
        cvThreshold(image, dst, thresh, 255, CV_THRESH_BINARY);
        return dst;
    }
    
    /**
     * Adaptive Binarization of Document Images [6]
     * @param image
     * @return 
     */
    public static IplImage paperAdaptiveThresh(IplImage image){
        IplImage dst = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
        cvAdaptiveThreshold(image, dst, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY, 3, 0);
        return dst;
    }

    public static int[] getHistogramAsArray(IplImage image) {
        CvHistogram hist = getHistogram(image, null);
        // extract values to an array
        int dest[] = new int[numberOfBins];
        for (int i = 0; i < numberOfBins; i++) {
            dest[i] = (int) cvQueryHistValue_1D(hist, i);
        }
//        cvReleaseHist(hist);
        return dest;
    }

    public static CvHistogram getHistogram(IplImage image, IplImage mask) {
        int dims = 1;
        final int sizes[] = new int[]{numberOfBins};
        int histType = CV_HIST_ARRAY;
        float ranges[][] = {{minRange, maxRange}};
        CvHistogram hist = cvCreateHist(dims, sizes, histType, ranges, 1);
        int acculate = 0;
        cvCalcHist(new IplImage[]{image}, hist, acculate, mask);
        return hist;
    }

    public static int moments(int[] data) {
        //  W. Tsai, "Moment-preserving thresholding: a new approach," Computer Vision,
        // Graphics, and Image Processing, vol. 29, pp. 377-393, 1985.
        // Ported to ImageJ plugin by G.Landini from the the open source project FOURIER 0.8
        // by  M. Emre Celebi , Department of Computer Science,  Louisiana State University in Shreveport
        // Shreveport, LA 71115, USA
        //  http://sourceforge.net/projects/fourier-ipal
        //  http://www.lsus.edu/faculty/~ecelebi/fourier.htm
        double total = 0;
        double m0 = 1.0, m1 = 0.0, m2 = 0.0, m3 = 0.0, sum = 0.0, p0 = 0.0;
        double cd, c0, c1, z0, z1;	/*
         * auxiliary variables
         */
        int threshold = -1;

        double[] histo = new double[256];

        for (int i = 0; i < 256; i++) {
            total += data[i];
        }

        for (int i = 0; i < 256; i++) {
            histo[i] = (double) (data[i] / total); //normalised histogram
        }
        /*
         * Calculate the first, second, and third order moments
         */
        for (int i = 0; i < 256; i++) {
            m1 += i * histo[i];
            m2 += i * i * histo[i];
            m3 += i * i * i * histo[i];
        }
        /*
         * First 4 moments of the gray-level image should match the first 4
         * moments of the target binary image. This leads to 4 equalities whose
         * solutions are given in the Appendix of Ref. 1
         */
        cd = m0 * m2 - m1 * m1;
        c0 = (-m2 * m2 + m1 * m3) / cd;
        c1 = (m0 * -m3 + m2 * m1) / cd;
        z0 = 0.5 * (-c1 - Math.sqrt(c1 * c1 - 4.0 * c0));
        z1 = 0.5 * (-c1 + Math.sqrt(c1 * c1 - 4.0 * c0));
        p0 = (z1 - m1) / (z1 - z0);  /*
         * Fraction of the object pixels in the target binary image
         */

        // The threshold is the gray-level closest  
        // to the p0-tile of the normalized histogram 
        sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += histo[i];
            if (sum > p0) {
                threshold = i;
                break;
            }
        }
        return threshold;
    }

    public static int otsu(int[] data) {
        // Otsu's threshold algorithm
        // C++ code by Jordan Bevik <Jordan.Bevic@qtiworld.com>
        // ported to ImageJ plugin by G.Landini
        int k, kStar;  // k = the current threshold; kStar = optimal threshold
        int N1, N;    // N1 = # points with intensity <=k; N = total number of points
        double BCV, BCVmax; // The current Between Class Variance and maximum BCV
        double num, denom;  // temporary bookeeping
        int Sk;  // The total intensity for all histogram points <=k
        int S, L = 256; // The total intensity of the image

        // Initialize values:
        S = N = 0;
        for (k = 0; k < L; k++) {
            S += k * data[k];	// Total histogram intensity
            N += data[k];		// Total number of data points
        }

        Sk = 0;
        N1 = data[0]; // The entry for zero intensity
        BCV = 0;
        BCVmax = 0;
        kStar = 0;

        // Look at each possible threshold value,
        // calculate the between-class variance, and decide if it's a max
        for (k = 1; k < L - 1; k++) { // No need to check endpoints k = 0 or k = L-1
            Sk += k * data[k];
            N1 += data[k];

            // The float casting here is to avoid compiler warning about loss of precision and
            // will prevent overflow in the case of large saturated images
            denom = (double) (N1) * (N - N1); // Maximum value of denom is (N^2)/4 =  approx. 3E10

            if (denom != 0) {
                // Float here is to avoid loss of precision when dividing
                num = ((double) N1 / N) * S - Sk; 	// Maximum value of num =  255*N = approx 8E7
                BCV = (num * num) / denom;
            } else {
                BCV = 0;
            }

            if (BCV >= BCVmax) { // Assign the best threshold found so far
                BCVmax = BCV;
                kStar = k;
            }
        }
        // kStar += 1;	// Use QTI convention that intensity -> 1 if intensity >= k
        // (the algorithm was developed for I-> 1 if I <= k.)
        return kStar;
    }
}
