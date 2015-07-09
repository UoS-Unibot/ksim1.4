package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.evors.core.EvoRSLib;
import org.evors.core.PositionOrientationSource;
import org.evors.core.geometry.Vec2;

public class StoredImageSource implements ImageSource {

	protected double CAMERA_FROM_CENTRE = 4; // *** radius of camera from centre of robot
	protected double STORED_Y_MAX = 20;
	protected double STORED_X_MAX = 12;
	
	protected String imagePath;
	protected final String imageExtension = ".jpg";
	protected PositionOrientationSource locationSource; // Assuming KSIM coordinate system
	
	public StoredImageSource( String imagePath, PositionOrientationSource locationSource ) {
		this.imagePath = imagePath;
		this.locationSource = locationSource;
	}

	public BufferedImage getImage() {
		// 1. Convert position into image coordinate system
		Vec2 imgFileCoords = getImageFileCoordinates();
		
		// 1b. Find stored photo position
		int storedPhoto_x = ( int ) imgFileCoords.x;
		int storedPhoto_y = ( int ) imgFileCoords.y;
		
		// 2. Read image
		String imageFile = imagePath + storedPhoto_y + "_" + storedPhoto_x + ".jpg"; // [sic] bad day when captured!..
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File( imageFile ));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit( 0 );
		}
		
		return img;
	}
	
	public Vec2 getImageFileCoordinates()
	{
		// 1. Convert position into image coordinate system
		Vec2 robotPosition = locationSource.getPosition();
		double robotHeading = locationSource.getOrientation();
		
		// 1a. Find simulated camera position
		Vec2 cameraPosition = robotPosition.translatePolar( EvoRSLib.headingToPolar(robotHeading), CAMERA_FROM_CENTRE );
		
		// 1b. Find stored photo position
		double storedPhoto_x = Math.min(STORED_X_MAX, Math.max(0, Math.round( ( cameraPosition.x - 6.35 ) / 5 ) ) );
		double storedPhoto_y = Math.min(STORED_Y_MAX, Math.max(0, Math.round( ( cameraPosition.y - 10 - CAMERA_FROM_CENTRE ) / 5 ) ) );
		
		return new Vec2( storedPhoto_x, storedPhoto_y );
	}

	public double getRotation() {
		return locationSource.getOrientation();
	}

}
