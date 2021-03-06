/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import static com.googlecode.javacv.cpp.opencv_core.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Tsendee
 */
public class FrmImage extends javax.swing.JFrame {

    ArrayList<ImageProcess> imageProcesses;

    /**
     * Creates new form FrmImage
     */
    public FrmImage() {
        initComponents();
    }

    /**
     * Creates new form FrmImage
     */
    public FrmImage(ArrayList<ImageProcess> imageProcesses) {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.imageProcesses = imageProcesses;
        showImages();
    }

    public void showImages() {
        GridLayout gridLayout = new GridLayout(imageProcesses.size(), 3, 5, 5);
        jPanel2.setLayout(gridLayout);
        for (Iterator<ImageProcess> it = imageProcesses.iterator(); it.hasNext();) {
            ImageProcess imageProcess = it.next();
            jPanel2.add(getImageShow(imageProcess.getOrgImage(), imageProcess.getOrgImageTitle()));
            jPanel2.add(getImageShow(imageProcess.getEnhImage(), imageProcess.getEnhImageTitle()));
            jPanel2.add(getImageShow(imageProcess.getBinImage(), imageProcess.getBinImageTitle()));
        }
        
    }

    private JPanel putInPanel(ImageProcess impProcess) {
        JPanel jPanel = new JPanel();
        jPanel.add(getImageShow(impProcess.getOrgImage(), impProcess.getOrgImageTitle()));
        jPanel.add(getImageShow(impProcess.getEnhImage(), impProcess.getEnhImageTitle()));
        jPanel.add(getImageShow(impProcess.getBinImage(), impProcess.getBinImageTitle()));
        return jPanel;
    }

    private JLabel getImageShow(IplImage img, String title) {
        JLabel imageView = new JLabel();
        if (img != null) {
            imageView.setIcon((new ImageIcon(img.getBufferedImage())));
            imageView.setText(title);
            
        } else {
            imageView.setText("No image available");
        }
        imageView.setFont(new java.awt.Font("Tahoma", 1, 16));
        imageView.setForeground(Color.blue);
        imageView.setHorizontalTextPosition(JLabel.CENTER);
        imageView.setVerticalTextPosition(JLabel.TOP);
        return imageView;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Output");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmImage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FrmImage().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
