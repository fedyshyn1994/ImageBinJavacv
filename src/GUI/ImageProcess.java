/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import static com.googlecode.javacv.cpp.opencv_core.*;
import imagebinjavacv.util.GUIUtil;

/**
 *
 * @author Tsendee
 */
public class ImageProcess {

    IplImage orgImage = null;
    String orgImageTitle = "";
    IplImage enhImage = null;
    String enhImageTitle = "";
    IplImage binImage = null;
    String binImageTitle = "";

    public ImageProcess() {
    }

    public String getBinImageTitle() {
        return binImageTitle;
    }

    public void setBinImageTitle(String binImageTitle) {
        this.binImageTitle = binImageTitle;
    }

    public String getEnhImageTitle() {
        return enhImageTitle;
    }

    public void setEnhImageTitle(String enhImageTitle) {
        this.enhImageTitle = enhImageTitle;
    }

    public String getOrgImageTitle() {
        return orgImageTitle;
    }

    public void setOrgImageTitle(String orgImageTitle) {
        this.orgImageTitle = orgImageTitle;
    }

    public IplImage getBinImage() {
        return binImage;
    }

    public void setBinImage(IplImage binImage) {
        this.binImage = binImage;
    }

    public IplImage getEnhImage() {
        return enhImage;
    }

    public void setEnhImage(IplImage enhImage) {
        this.enhImage = enhImage;
    }

    public IplImage getOrgImage() {
        return orgImage;
    }

    public void setOrgImage(IplImage orgImage) {
        this.orgImage = orgImage;
    }

    public void scaleImages(int scale) {
        if (scale > 0) {
            try {
                orgImage = GUIUtil.scale(scale, orgImage);
            } catch (NullPointerException ex) {
            }
            try {
                enhImage = GUIUtil.scale(scale, enhImage);
            } catch (NullPointerException ex) {
            }
            try {
                binImage = GUIUtil.scale(scale, binImage);
            } catch (NullPointerException ex) {
            }
        }
    }
}
