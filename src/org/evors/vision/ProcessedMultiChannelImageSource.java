package org.evors.vision;

/**
 * All images are represented by a three-dimensional int array
 * matching the x,y coordinates of the original image and each sub array value representing
 * the 0-255 intensity in each channel
 * 
 * @author michaelgarvie
 *
 */
public interface ProcessedMultiChannelImageSource {

	/**
	 * 
	 * @return The image as seen by the robot at that moment in time 
	 * represented as a three dimensional int array with identical coordinates
	 * to the original image and the 0-255 intensity in each channel
	 */
	public int[][][] getProcessedMultiChannelImage();
	
	/**
	 * 
	 * @return How much the image should be rotated to take into account robot orientation.  
	 * Angle returned is clockwise angle difference from angle image was taken at.  ie. if 
	 * image taken pointing north and we are now heading east, this will return PI/2
	 */
	public double getRotation();

}
