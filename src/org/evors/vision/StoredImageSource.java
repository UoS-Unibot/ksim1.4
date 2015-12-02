package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.evors.core.EvoRSLib;
import org.evors.core.PositionOrientationSource;
import org.evors.core.geometry.Vec2;

public class StoredImageSource implements ImageSource, KeyGenerator {

	protected int STORED_Y_MAX = 19;
	protected int STORED_X_MAX = 13;
	
	protected String imagePath;
	protected final String imageExtension = ".jpg";
	protected PositionOrientationSource locationSource;
	protected boolean cache = false;

	protected double worldSampledHeading = 0; // Heading 0 = N
	protected String ext = "N";
	protected Vec2 worldSampledCameraOffset;
	
	protected BufferedImage[][] imgCache = new BufferedImage[ STORED_X_MAX + 1 ][ STORED_Y_MAX + 1 ];
	
	public StoredImageSource( String imagePath, PositionOrientationSource locationSource, double worldSampledHeading, String ext )
	{
		this.imagePath = imagePath;
		this.locationSource = locationSource;
		this.worldSampledHeading = worldSampledHeading;
		this.ext = ext;
		worldSampledCameraOffset = new Vec2( VisionLib.CAMERA_FROM_CENTRE * Math.sin( worldSampledHeading ), VisionLib.CAMERA_FROM_CENTRE * Math.cos( worldSampledHeading ) );
	}
	
	public StoredImageSource( String imagePath, PositionOrientationSource locationSource ) {
		this( imagePath, locationSource, 0, "N" );
	}

	public BufferedImage getImage() {
		// 1. Convert position into image coordinate system
		Vec2 imgFileCoords = getImageFileCoordinates();
		
		// 1b. Find stored photo position
		int storedPhoto_x = ( int ) imgFileCoords.x;
		int storedPhoto_y = ( int ) imgFileCoords.y;
		
		BufferedImage rv = null;
		
		// 2a. Check Cache
		if( imgCache[ storedPhoto_x ][ storedPhoto_y ] == null ) 
		{
			// 2b. Read image
			String imageFile = imagePath + storedPhoto_x + "_" + storedPhoto_y + "-" + ext + ".jpg"; // [sic] bad day when captured!..
			
			try {
				rv = ImageIO.read(new File( imageFile ));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit( 0 );
			}
			
			// If caching, store
			if( cache ) imgCache[ storedPhoto_x ][ storedPhoto_y ] = rv;
		}else
		{
			// Cache hit, retrieve
			rv = imgCache[ storedPhoto_x ][ storedPhoto_y ];
		}
		
		return rv;
	}
	
	public Vec2 getImageFileCoordinates()
	{
		// Arena is 85, 114
		// (0,0) is at 6.8, 9 (+4 for N facing camera)
		// There are 14 x and 20 y samples
		// Means there's a gap of 7.2 at the end of x, and 5 at the end of y
		
		double dX = 5, dY = 5;
		Vec2 zeroZeroCentreRobot = new Vec2( 6.8, 9 );
		
		// 1. Convert position into image coordinate system
		Vec2 robotPosition = locationSource.getPosition();
		double robotHeading = locationSource.getOrientation();
		
		// 1a. Find simulated camera position
		Vec2 cameraPosition = robotPosition.translatePolar( EvoRSLib.headingToPolar(robotHeading), VisionLib.CAMERA_FROM_CENTRE );
		
		// 1b. Find stored photo position
		
		double storedPhoto_x = Math.min(STORED_X_MAX + 0.0, Math.max(0, Math.round( ( cameraPosition.x - zeroZeroCentreRobot.x - worldSampledCameraOffset.x ) / dX ) ) );
		double storedPhoto_y = Math.min(STORED_Y_MAX + 0.0, Math.max(0, Math.round( ( cameraPosition.y - zeroZeroCentreRobot.y - worldSampledCameraOffset.y ) / dY ) ) );
		
		return new Vec2( storedPhoto_x, storedPhoto_y );
	}

	public double getRotation() {
		return ( locationSource.getOrientation() + this.worldSampledHeading ) % ( 2 * Math.PI );
	}

	public Object getKey() {
		return getImageFileCoordinates();
	}
	
	public String toString()
	{
		StringBuffer rv = new StringBuffer();
		rv.append("Stored Image Source with:");
		rv.append("\n\t\t\t\t\tPath = " + this.imagePath );
		rv.append("\n\t\t\t\t\tSample Orientation = " + this.worldSampledHeading );
		rv.append("\n\t\t\t\t\tFile name append = " + this.ext );
		return rv.toString();
	}

}
