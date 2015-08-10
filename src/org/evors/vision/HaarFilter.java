package org.evors.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.evors.core.geometry.Vec2;

/**
 * This Haar filter class is written to work on circular (conical mirror) images
 * as per our specific setup.
 * 
 * @author michaelgarvie
 *
 */
public class HaarFilter implements VisualFilter {

	protected static final boolean debug = false; // TODO should be false *** Should optimise away
	protected String filter;
	protected Vec2 proportionalDimension;
	protected boolean[][] filterMap; // in top left 0,0 coordinates, true is light
	
	public HaarFilter( String filter ) {
		this.filter = filter;
		int lines = 1;
		for( int cl = 0; cl < filter.length(); cl++ ) if( filter.charAt( cl ) == '\n' ) lines++;
		
		int cols = filter.indexOf( "\n" ) < 0 ? filter.length() : ( filter.substring(0, filter.indexOf("\n") ) ).length();
		
		this.proportionalDimension = new Vec2( cols, lines );
		filterMap = new boolean[ cols ][ lines ];
		
		int row = 0;
		
		// Scan line by line
		int stringPos = 0;
		boolean end = false;
		while( !end )
		{
			int nextLine = filter.indexOf('\n', stringPos);
			if( nextLine < 0 )
			{
				end = true;
				nextLine = filter.length();
			}
			String line = filter.substring(stringPos, nextLine );
			stringPos = nextLine+1;
			
			for( int cl = 0; cl < line.length(); cl++ )
			{
				  filterMap[ cl ][ row ] = line.charAt( cl ) != 'x';
			}
		  row++;
		}
	}

	public double getValue( int[][] img, double rotation, Vec2 imgCentre, Vec2 filterCentrePerc, double heightPerc )
	{
		return getValue( img, rotation, imgCentre, filterCentrePerc, heightPerc, null );
	}
	
	public double getValue( int[][] img, double rotation, Vec2 imgCentre, Vec2 filterCentrePerc, double heightPerc, BufferedImage debugImage )
	{
		// Setup variables
		int r_in = VisualSensorGroup.IMG_DISC_RADIUS, r_out = VisualSensorGroup.IMG_OUTER_RADIUS;
		double shrinkFactor = 1; // *** Need to shrink centre, radiuses etc..
		
		// Bias height so in 0.5 -> 2.0 range
		heightPerc = heightPerc * 1.5 + 0.5;
		
		// ***
		if( debugImage != null ) debugImage.setRGB( (int) (imgCentre.x/shrinkFactor), (int) (imgCentre.y/shrinkFactor), Color.WHITE.getRGB() );
		// ***
		
		// Use polar coordinates (orientation is "heading")
		
		// 1. Find sensor centre
		
		// Image is vertical inverted, so left is right, right is left
		double sensorPolarCentreAngle = ( filterCentrePerc.x - 0.25 ) * 2 * Math.PI;
		
		// Add orientation adjustment
		sensorPolarCentreAngle += rotation;
		
		double sensorCentreRadius = r_in + ( r_out - r_in ) * filterCentrePerc.y;
		
		// Need to find r_max, r_min, theta_max, theta_min
		double y_min = filterCentrePerc.y - heightPerc / 2; double y_max = filterCentrePerc.y + heightPerc / 2; // may be outside range
		double r_min = r_in + ( r_out - r_in ) * y_min; double r_max = r_in + ( r_out - r_in ) * y_max;
		double magicConstant = 0.5; // Experimentally derived
		double widthAngle = magicConstant * heightPerc * proportionalDimension.x / ( proportionalDimension.y * Math.PI ); 
		
		double theta_min = sensorPolarCentreAngle - widthAngle / 2;
		double theta_max = sensorPolarCentreAngle + widthAngle / 2;
		
		// Ray trace and count buckets - Top Left to Bottom Right in real world
		double pixelCount = 0;
		double valueCountRaw = 0;
		
		// Iterate over angles from theta_min to theta_max
		double thetaStep = 1 / r_max;
		
		for( double rayTheta = theta_min; rayTheta < theta_max; rayTheta += thetaStep )
		{
			int column = ( int ) ( ( rayTheta - theta_min ) * proportionalDimension.x / widthAngle );
			
			// then along line from r_min (near ceiling) to r_max (near floor)
			for( double rayR = r_min; rayR < r_max; rayR++ )
			{
				if( rayR > VisualSensorGroup.IMG_DISC_RADIUS && rayR < VisualSensorGroup.IMG_OUTER_RADIUS )
				{
					int row = ( int ) ( ( rayR - r_min ) * proportionalDimension.y / ( r_max - r_min ) );
				
					// Find pixel coordinates
					// Image coordinates are y-inverted so angle
					Vec2 offset = new Vec2( 0,0 ).translatePolar(rayTheta, rayR);
					Vec2 invOffset = new Vec2( offset.x, -1 * offset.y );
					Vec2 rayPoint = imgCentre.add( invOffset );
					rayPoint = new Vec2( rayPoint.x, Math.max(0, Math.min( img[0].length - 1, rayPoint.y)) );
					int blue = img[ (int) ( rayPoint.x / shrinkFactor ) ][ (int) ( rayPoint.y / shrinkFactor ) ];
					
					// Add to totals
					pixelCount++;
					if( filterMap[ column ][ row ] )
					{
						valueCountRaw += blue; // light area
						if( debugImage != null ) { debugImage.setRGB((int) (rayPoint.x/shrinkFactor), (int) (rayPoint.y/shrinkFactor), Color.WHITE.getRGB() ); }
						//blue = img.getRGB( (int) rayPoint.x, (int) rayPoint.y ) & 0xff;
						//System.out.println( "post light area " + blue + " rawTotal " + valueCountRaw );
					}
					else
					{
						valueCountRaw += ( 255 - blue ); // dark area
						if( debugImage != null ) { debugImage.setRGB((int) (rayPoint.x/shrinkFactor), (int) (rayPoint.y/shrinkFactor), Color.BLACK.getRGB() ); }
						//blue = img.getRGB( (int) rayPoint.x, (int) rayPoint.y ) & 0xff;
					}
				}
			}
		}
		
		double zeroToOneValue = pixelCount == 0 ? 0 : valueCountRaw / ( 255.0 * pixelCount );
		
		//double zeroToOneValue = minusOneToOneValue / 2 + 1;
		
		return Math.min(1.0, zeroToOneValue);
	}
	
	/**
	 * Mainly for testing
	 * @return
	 */
	public Vec2 getProportionalDimension()
	{
		return this.proportionalDimension;
	}
	
	/**
	 * Mainly for testing
	 * @return
	 */
	public boolean[][] getMap()
	{
		return this.filterMap;
	}

	public String toString()
	{
		StringBuffer rv = new StringBuffer();
		rv.append( "\tHaarFilter with:" );
		rv.append("\n\t\tfilter = " + filter);
		return rv.toString();
	}
}
