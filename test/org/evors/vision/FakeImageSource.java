package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FakeImageSource implements ImageSource {

	protected String path = "/Users/michaelgarvie/git/ksim1.4/test/org/evors/vision/img/";
	protected String ext = ".png";
	protected int id = 0;
	
	public FakeImageSource() {
		// TODO Auto-generated constructor stub
	}
	
	public void setID( int id ){ this.id = id; }

	public BufferedImage getImage() {
		String imageFile = path + id + ext; // [sic] bad day when captured!..
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File( imageFile ));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit( 0 );
		}
		return img;
	}

	public double getRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

}
