package org.evors.vision;

import java.awt.image.BufferedImage;

/**
 * Assumes that getImage will be called after getProcessedImage.  The image will likely be modified downstream.
 * @author michaelgarvie
 *
 */
public class MultipleProcessedMultiChannelImageSourceDebug extends MultipleProcessedMultiChannelImageSource implements ImageSource {

	protected ImageSource[] debugSources;
	
	public MultipleProcessedMultiChannelImageSourceDebug( ProcessedMultiChannelImageSource[] sources, int resolutionMethod, ImageSource[] debugSources ) {
		super(sources, resolutionMethod);
		this.debugSources = debugSources;
	}

	public MultipleProcessedMultiChannelImageSourceDebug( ProcessedMultiChannelImageSource[] sources, ImageSource[] debugSources) {
		super(sources);
		this.debugSources = debugSources;
	}

	public BufferedImage getImage()
	{
		return debugSources[ lastSource ].getImage();
	}
	
}
