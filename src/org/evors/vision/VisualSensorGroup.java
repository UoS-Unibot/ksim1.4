package org.evors.vision;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.BitSet;

import org.evors.core.Programmable;
import org.evors.core.geometry.Vec2;
import org.evors.processing.MinimumThreshold;
import org.evors.processing.ProgrammableThreshold;

public class VisualSensorGroup implements Programmable {
	
	protected ImageSource imgSource;
	protected int sensorCount = 5;
	protected VisualSensor[] sensors;
	protected ProgrammableThreshold[] thresholds;
	
	protected int BITS_FILTER_TYPE = 3;
	protected int BITS_CENTRE_X = 6;
	protected int BITS_CENTRE_Y = 5;
	protected int BITS_HEIGHT = 5;
	protected int BITS_MIN_THRESHOLD = 4;
	
	protected int bitsSensor = BITS_FILTER_TYPE + BITS_CENTRE_X + BITS_CENTRE_Y + BITS_HEIGHT;
	protected int bitsBlock = bitsSensor + BITS_MIN_THRESHOLD;
	
	public static int IMG_WIDTH = 752, IMG_HEIGHT = 480, IMG_GUESS_CENTRE_X = 402, IMG_GUESS_CENTRE_Y = 265, IMG_DISC_RADIUS = 120, IMG_OUTER_RADIUS = 258;
	protected int IMG_CENTRE_RING_THRESHOLD = 10;
	
	public VisualSensorGroup( ImageSource imgSource )  {
		this.imgSource = imgSource;
		init();
	}
	
	protected void init()
	{
		sensors = new VisualSensor[ sensorCount ];
		for( int sl = 0; sl < sensorCount; sl++ )
		{
			sensors[ sl ] = new VisualSensor( this, BITS_FILTER_TYPE, BITS_CENTRE_X, BITS_CENTRE_Y, BITS_HEIGHT );
			thresholds[ sl ] = new MinimumThreshold();
		}
	}

	public void program(BitSet bits) {
		for( int sl = 0; sl < sensorCount; sl++ )
		{
			BitSet sensorBits = bits.get( bitsBlock * sl, bitsBlock * sl + bitsSensor );
			BitSet thresholdBits = bits.get( bitsBlock * sl + bitsSensor, bitsBlock * ( sl + 1 ) );
			sensors[ sl ].program( sensorBits );
			thresholds[ sl ].program( thresholdBits );
		}
	}
	
	public double[] getReadings()
	{
		// 1. Fetch image
		// 2. Crop, grayscale, find centre
		// 3. Read sensors
		// 4. Apply thresholds
		
		double[] rv = new double[ sensorCount ];
		
		// 1.
		BufferedImage img = imgSource.getImage();
		double rotation = imgSource.getRotation();
		
		// 2.
		// 2ii. Grayscale
		
		BufferedImage greyimg = new BufferedImage(752, 480, BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = greyimg.getGraphics();  
		g.drawImage(img, 0, 0, null);  
		g.dispose();
		
		// 2iii. Find centre
		int ringLeft=IMG_GUESS_CENTRE_X-IMG_DISC_RADIUS, ringRight=IMG_GUESS_CENTRE_X+IMG_DISC_RADIUS,
				ringTop=IMG_GUESS_CENTRE_Y-IMG_DISC_RADIUS, ringBottom=IMG_GUESS_CENTRE_Y+IMG_DISC_RADIUS, centreX, centreY;
		
		// 2iiiA. Scan left right from:
		int scanLeftX = ( int )( this.IMG_GUESS_CENTRE_X - this.IMG_DISC_RADIUS * 1.2 );
		int scanLength = ( int ) ( IMG_DISC_RADIUS * 2 * 1.2 );
		
		int[] pixelValues = greyimg.getRGB(scanLeftX, IMG_GUESS_CENTRE_Y, scanLength, 1, null, 0, 0);
		
		// Find dark areas - Should be scanLength values - Start halfway left, halfway right from guessed centre
		// 2iiiAI. Find left dark ring
		int startX = scanLength / 4;
		for( int pl = startX; pl > 0; pl-- )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			if( pixelValue < this.IMG_CENTRE_RING_THRESHOLD )
			{
				ringLeft = pl;
				break;
			}
		}
		// 2iiiAII. Find right
		startX = scanLength * 3 / 4;
		for( int pl = startX; pl < scanLength; pl++ )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			if( pixelValue < this.IMG_CENTRE_RING_THRESHOLD )
			{
				ringRight = pl;
				break;
			}
		}
		
		
		// 2iiiB. Scan top to bottom
		int scanTopY = ( int ) ( this.IMG_GUESS_CENTRE_Y - this.IMG_DISC_RADIUS * 1.2 );
		pixelValues = greyimg.getRGB(IMG_GUESS_CENTRE_X, scanTopY, 1, scanLength, null, 0, 0);
		
		// 2iiiBI. Find top
		int startY = scanLength / 4;
		for( int pl = startY; pl > 0; pl-- )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			if( pixelValue < this.IMG_CENTRE_RING_THRESHOLD )
			{
				ringTop = pl;
				break;
			}
		}
		// 2iiiAII. Find right
		startY = scanLength * 3 / 4;
		for( int pl = startY; pl < scanLength; pl++ )
		{
			// Use blue (all same anyway)
			int pixelValue = pixelValues[ pl ] & 0xff;
			if( pixelValue < this.IMG_CENTRE_RING_THRESHOLD )
			{
				ringBottom = pl;
				break;
			}
		}
		
		// 2iiiC. Calculate centre
		Vec2 imgCentre = new Vec2( ringLeft + ( ringRight - ringLeft ) / 2, ringTop + ( ringBottom - ringTop ) / 2 );
		
		// 3 & 4.
		
		for( int sl = 0; sl < sensorCount; sl++ )
		{
			rv[ sl ] = thresholds[ sl ].threshold( sensors[ sl ].getValue( greyimg, rotation, imgCentre ) );
		}
		
		return rv;
	}

}
