package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LiveImageSource implements ImageSource {

	protected String URL = "http://192.168.0.101:8080/stream.html";
	
	public LiveImageSource() {
	}

	public BufferedImage getImage() {
		BufferedImage image = null;
		try {
		    URL url = new URL( URL );
		    image = ImageIO.read(url);
		} catch (IOException e) {
			System.err.println(e);
			System.out.println(e);
		}
		return image;
	}

	public double getRotation() {
		return 0;
	}

}
