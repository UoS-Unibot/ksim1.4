package org.evors.vision;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.BitSet;

import org.evors.core.EvoRSLib;
import org.evors.core.Programmable;
import org.evors.core.geometry.Vec2;
import org.evors.processing.MinimumThreshold;
import org.evors.processing.ProgrammableThreshold;

public class VisualSensorGroup implements Programmable {
	
	protected ProcessedMultiChannelImageSource imgSource;
	public ImageSource debugImgSource = null;
	protected BufferedImage debugImage = null;
	public static final int DEFAULT_SENSOR_COUNT = 5;
	protected VisualSensor[] sensors;
	protected ProgrammableThreshold[] thresholds;
	
	public static final int BITS_FILTER_TYPE = 3;
	public static final int BITS_CENTRE_X = 6;
	public static final int BITS_CENTRE_Y = 5;
	public static final int BITS_HEIGHT = 5;
	public static final int BITS_MIN_THRESHOLD = 4;
	public static final int BITS_CHANNEL = 1;
	
	public static final int BITS_SENSOR = BITS_FILTER_TYPE + BITS_CENTRE_X + BITS_CENTRE_Y + BITS_HEIGHT + BITS_CHANNEL;
	public static final int BITS_BLOCK = BITS_SENSOR + BITS_MIN_THRESHOLD;
	
	public static int IMG_WIDTH = 752, IMG_HEIGHT = 480, IMG_GUESS_CENTRE_X = 393/*404*/, IMG_GUESS_CENTRE_Y = 249/*260*/, IMG_DISC_RADIUS = 170, IMG_OUTER_RADIUS = 220; // 120,258 **** old img disc radius
	protected static int IMG_CENTRE_RING_THRESHOLD = 0; // How dark the ring is
	
	public VisualSensorGroup( ProcessedMultiChannelImageSource imgSource )  {
		this.imgSource = imgSource;
		init();
	}
		
	protected void init()
	{
		sensors = new VisualSensor[ DEFAULT_SENSOR_COUNT ];
		thresholds = new ProgrammableThreshold[ DEFAULT_SENSOR_COUNT ];
		for( int sl = 0; sl < DEFAULT_SENSOR_COUNT; sl++ )
		{
			sensors[ sl ] = new VisualSensor( this, BITS_FILTER_TYPE, BITS_CENTRE_X, BITS_CENTRE_Y, BITS_HEIGHT, BITS_CHANNEL );
			thresholds[ sl ] = new MinimumThreshold();
		}
	}
	
	public int getSensorCount()
	{
		return DEFAULT_SENSOR_COUNT;
	}

	public void program(BitSet bits) {
		for( int sl = 0; sl < DEFAULT_SENSOR_COUNT; sl++ )
		{
			BitSet sensorBits = EvoRSLib.getChunk( bits, BITS_BLOCK * sl, BITS_BLOCK * sl + BITS_SENSOR );
			BitSet thresholdBits = EvoRSLib.getChunk( bits, BITS_BLOCK * sl + BITS_SENSOR, BITS_BLOCK * ( sl + 1 ) );
			sensors[ sl ].program( sensorBits );
			thresholds[ sl ].program( thresholdBits );
			
			if( this.debugImgSource != null )
			{
				System.out.println("Sensor " + sl + " = " + sensors[ sl ] );
				System.out.println("Threshold " + sl + " = " + thresholds[ sl ] );
			}
		}
	}
	
	public double[] getReadings()
	{
		// 1. Fetch image
		// 2. Crop, grayscale, find centre
		// 3. Read sensors
		// 4. Apply thresholds
		
		double[] rv = new double[ DEFAULT_SENSOR_COUNT ];
		
		// 1.
		short[][][] pImg = imgSource.getProcessedMultiChannelImage();
		double rotation = imgSource.getRotation();
		
		// 2.
		
		// 2iii. Find centre
		Vec2 imgCentre = new Vec2( this.IMG_GUESS_CENTRE_X, this.IMG_GUESS_CENTRE_Y );
		
		// 3 & 4.
		
		BufferedImage debugWorkImage = this.debugImgSource == null ? null : debugImgSource.getImage();
		for( int sl = 0; sl < DEFAULT_SENSOR_COUNT; sl++ )
		{
			rv[ sl ] = thresholds[ sl ].threshold( sensors[ sl ].getValue( pImg, rotation, imgCentre, debugWorkImage ) );
			rv[ sl ] += EvoRSLib.uniformNoise( 0.10 ); // *** was 0.15
			rv[ sl ] = Math.max( 0.0, Math.min( 1.0, rv[ sl ]) );
		}
		debugImage = debugWorkImage;
		
		return rv;
	}
	
	public BufferedImage getDebugImage()
	{
		return debugImage;
	}
	
	
	public static Vec2 findCentre( BufferedImage greyimg )
	{
		int ringLeft=IMG_GUESS_CENTRE_X-IMG_DISC_RADIUS, ringRight=IMG_GUESS_CENTRE_X+IMG_DISC_RADIUS,
				ringTop=IMG_GUESS_CENTRE_Y-IMG_DISC_RADIUS, ringBottom=IMG_GUESS_CENTRE_Y+IMG_DISC_RADIUS, centreX, centreY;
		
		// 2iiiA. Scan left right from:
		int scanLeftX = ( int )( IMG_GUESS_CENTRE_X - IMG_DISC_RADIUS * 1.2 );
		int scanLength = ( int ) ( IMG_DISC_RADIUS * 2 * 1.2 );
		
		int[] pixelValues = new int[ scanLength ];
		greyimg.getRGB(scanLeftX, IMG_GUESS_CENTRE_Y, scanLength, 1, pixelValues, 0, scanLength);
		
		// Find dark areas - Should be scanLength values - Start halfway left, halfway right from guessed centre
		// 2iiiAI. Find left dark ring
		int startX = scanLength / 4;
		for( int pl = startX; pl > 0; pl-- )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			
			
			if( pixelValue < IMG_CENTRE_RING_THRESHOLD )
			{
				ringLeft = scanLeftX + pl;
				break;
			}
		}
		// 2iiiAII. Find right
		startX = scanLength * 3 / 4;
		for( int pl = startX; pl < scanLength; pl++ )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			
			if( pixelValue < IMG_CENTRE_RING_THRESHOLD )
			{
				ringRight = scanLeftX + pl;
				break;
			}
		}
		
		
		// 2iiiB. Scan top to bottom
		int scanTopY = ( int ) ( IMG_GUESS_CENTRE_Y - IMG_DISC_RADIUS * 1.2 );
		greyimg.getRGB(IMG_GUESS_CENTRE_X, scanTopY, 1, scanLength, pixelValues, 0, 1);
		
		// 2iiiBI. Find top
		int startY = scanLength / 4;
		for( int pl = startY; pl > 0; pl-- )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			
			if( pixelValue < IMG_CENTRE_RING_THRESHOLD )
			{
				ringTop = scanTopY + pl;
				break;
			}
		}
		// 2iiiBII. Find bottom
		startY = scanLength * 3 / 4;
		for( int pl = startY; pl < scanLength; pl++ )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			System.out.println( pixelValue );
			if( pixelValue < IMG_CENTRE_RING_THRESHOLD )
			{
				ringBottom = scanTopY + pl;
				break;
			}
		}
		
		// 2iiiC. Calculate centre
		Vec2 imgCentre = new Vec2( ringLeft + ( ringRight - ringLeft ) / 2, ringTop + ( ringBottom - ringTop ) / 2 );
		return imgCentre;
	}
	
	public String toString()
	{
		StringBuffer rv= new StringBuffer();
		rv.append( "Visual Sensor Group with:");
		rv.append("\n\t\t\tSensor count = " + this.getSensorCount() );
		rv.append("\n\t\t\tConfiguration bits per block = " + this.BITS_BLOCK );
		rv.append("\n\t\t\tProcessed Image Source: " + this.imgSource.toString() );
		return rv.toString();
	}

}
