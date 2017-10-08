package uk.co.sunbyte.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private BufferedImage image = null;
    
    public ImagePanel(String fileName) {
        try {
        	// hot_air_balloon_kidnapping.png
            this.image = ImageIO.read(new File(fileName));
            this.setPreferredSize(new Dimension(this.image.getWidth(), 
            		                            this.image.getHeight()));
//            System.out.println(this.image.getWidth());
//            System.out.println(this.image.getHeight());
        } catch (IOException e) {
    	    System.out.println("Exception occured");
        }
    }
    
    public boolean showOn(Graphics g) {
    	return g.drawImage(this.image,
                0, 0,
                null);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image,
                0, 0,
                null);
    }
}
