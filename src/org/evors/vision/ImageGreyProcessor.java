package org.evors.vision;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageGreyProcessor implements ProcessedGreyImageSource {

	protected ImageSource imgSrc;
	
	public ImageGreyProcessor( ImageSource imgSrc ) {
		this.imgSrc = imgSrc;
	}

	public int[][] getProcessedGreyImage() {
		// 1. Convert to greyscale
		BufferedImage greyimg = new BufferedImage(752, 480, BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = greyimg.getGraphics();  
		g.drawImage( getImageSourceImage(), 0, 0, null);  
		g.dispose();
		
		int[][] rv = new int[ greyimg.getWidth() ][ greyimg.getHeight() ];
		for( int xl = 0; xl < greyimg.getWidth(); xl++ )
		{
			for( int yl = 0; yl < greyimg.getHeight(); yl++ )
			{
				rv[ xl ][ yl ] = greyimg.getRGB( xl, yl ) & 0xff;
			}
		}
		
		return rv;
	}
	
	protected BufferedImage getImageSourceImage()
	{
		return imgSrc.getImage();
	}

	public double getRotation() {
		return imgSrc.getRotation();
	}

}
