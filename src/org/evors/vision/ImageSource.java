package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Source for robot images.
 * @author michaelgarvie
 *
 */
public interface ImageSource extends Serializable {

	/**
	 * 
	 * @return The image as seen by the robot at that moment in time
	 */
	public BufferedImage getImage();
	
	/**
	 * 
	 * @return How much the image should be rotated to take into account robot orientation.  
	 * Angle returned is clockwise angle difference from angle image was taken at.  ie. if 
	 * image taken pointing north and we are now heading east, this will return PI/2
	 */
	public double getRotation();
}
