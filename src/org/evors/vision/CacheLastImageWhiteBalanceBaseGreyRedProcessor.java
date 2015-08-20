package org.evors.vision;

import java.awt.image.BufferedImage;

public class CacheLastImageWhiteBalanceBaseGreyRedProcessor extends ImageWhiteBalanceBaseGreyRedProcessor implements ImageSource {

	protected BufferedImage cachedImage;
	
	public CacheLastImageWhiteBalanceBaseGreyRedProcessor(ImageSource imgSrc) {
		super(imgSrc);
	}
	
	public synchronized short[][][] getProcessedMultiChannelImage() // No real need for synchronisation right now as a single thread calls these one after the other
	{
		return super.getProcessedMultiChannelImage();
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
