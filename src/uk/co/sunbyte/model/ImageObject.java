package uk.co.sunbyte.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageObject {
	private BufferedImage img = null;
	
	public ImageObject(InputStream in) {
		try {
		    img = ImageIO.read(in);
		} catch (IOException e) {
		}
	}
}
