package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LiveImageSource implements ImageSource {

	protected String URL = "http://172.19.4.226:8080/?action=snapshot";
	
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
