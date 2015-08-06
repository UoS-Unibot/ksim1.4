package org.evors.vision;

import java.awt.image.BufferedImage;

public class CacheLastImageGreyProcessor extends ImageGreyProcessor implements ImageSource {

	protected BufferedImage cachedImage;
	
	public CacheLastImageGreyProcessor(ImageSource imgSrc) {
		super(imgSrc);
	}
	
	public synchronized int[][] getProcessedGreyImage() // No real need for synchronisation right now as a single thread calls these one after the other
	{
		return super.getProcessedGreyImage();
	}

	public synchronized BufferedImage getImage() {
		return cachedImage;
	}
	
	protected BufferedImage getImageSourceImage()
	{
		cachedImage = imgSrc.getImage();
		return cachedImage;
	}

}
