package uk.co.sunbyte;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import uk.co.sunbyte.controller.Controller;
import uk.co.sunbyte.model.ImageReader;

public class SunbyteApp {
	public static void main(String[] args) throws IOException, InterruptedException {	

//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					waitForPictureReception();
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//		});
//		t.setName("Waits for image reception");
//		t.setDaemon(true);
//		t.setPriority(Thread.NORM_PRIORITY);
//		t.start();
		
		Controller c = new Controller();
	}

	private static void waitForPictureReception() throws IOException {
    	ServerSocket welcomeSocket = new ServerSocket(9999);

		Socket connectionSocket = welcomeSocket.accept();
		
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

		ImageReader imageReader = new ImageReader(connectionSocket.getInputStream());
		
		try {
			imageReader.read();
			byte[] imageBytes = imageReader.getData();
			// now do something with the image bytes....
			InputStream in = new ByteArrayInputStream(imageBytes);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "png", new File("test.png"));
			
		} catch (IOException ioe) {
			System.out.println("There was an error while reading from the stream: " + ioe.getMessage());
			ioe.printStackTrace();
		}		
	}
}
