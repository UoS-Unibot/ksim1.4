package org.evors.vision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageWhiteBalanceBaseGreyRedProcessor implements ProcessedMultiChannelImageSource {

	protected ImageSource imgSrc;
	public static final int IX_GREY = 0, IX_RED = 1;
	protected BufferedImage debugImage;
	protected int processedCount = 0;
	protected boolean debugMode = false;
	
	public ImageWhiteBalanceBaseGreyRedProcessor( ImageSource imgSrc ) {
		this.imgSrc = imgSrc;
	}

	public int[][][] getProcessedMultiChannelImage() {
		
		BufferedImage img = getImageSourceImage();
		if( debugMode ) { debugImage = img; processedCount++; }
		
		int[][][] rv = new int[ img.getWidth() ][ img.getHeight() ][ 2 ];
		
		int totalRed = 0, totalBlue = 0, totalGreen = 0;
		for( int x = 0; x < img.getWidth(); x++ )
		{
			for( int y = 0; y < img.getHeight(); y++ )
			{
				int rgb = img.getRGB(x, y);
				Color pixel = new Color( rgb );
				totalRed += pixel.getRed();
				totalBlue += pixel.getBlue();
				totalGreen += pixel.getGreen();
			}
		}
		double totalPixels = img.getHeight() * img.getWidth();
		double averageRed = totalRed / totalPixels, averageBlue = totalBlue / totalPixels, averageGreen = totalGreen / totalPixels;
		double averageGrey = ( averageRed + averageBlue + averageGreen ) / 3;
		double redRatio = averageGrey / averageRed, blueRatio = averageGrey / averageBlue, greenRatio = averageGrey / averageGreen;

		// Manual
		double redBase = 40, greenBase = 45, blueBase = 30;
		double extraRatio = 3;
		
		for( int x = 0; x < img.getWidth(); x++ )
		{
			for( int y = 0; y < img.getHeight(); y++ )
			{
				Color pixel = new Color( img.getRGB(x, y));
				double red = Math.max(0,Math.min(255, (( pixel.getRed() - redBase )* redRatio * extraRatio) ));
				double green = Math.max(0,Math.min(255, ((pixel.getGreen() - greenBase) * greenRatio * extraRatio)));
				double blue = Math.max(0,Math.min(255, ((pixel.getBlue() - blueBase) * blueRatio * extraRatio)));
				if( debugMode ) debugImage.setRGB(x, y, new Color( (int)red,(int)green,(int)blue).getRGB() );
				rv[ x ][ y ][ IX_GREY ] = (int)( red * 0.2989 + green * 0.587 + blue * 0.1140 );
				rv[ x ][ y ][ IX_RED ] = (int)( red * ( Math.min( 1, red / ( green + blue ) ) ) );
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

	public BufferedImage getDebugImage()
	{
		return debugImage;
	}
	
	public String toString()
	{
		return "Image White Balance Base Grey Red Processor with Inner Source = " + this.imgSrc.toString();
	}
	
}
