package org.evors.vision;

import java.awt.image.BufferedImage;
import java.util.Scanner;

import org.evors.core.geometry.Vec2;

public class HaarFilter implements VisualFilter {

	protected String filter;
	protected Vec2 proportionalDimension;
	protected boolean[][] filterMap; // in top left 0,0 coordinates, true is light
	
	public HaarFilter( String filter ) {
		this.filter = filter;
		int lines = filter.length() - filter.replace("\n", "").length() + 1;
		int cols = filter.indexOf( "\n" ) >= 0 ? filter.length() : ( filter.substring(0, filter.indexOf("\n") ) ).length();
		this.proportionalDimension = new Vec2( cols, lines );
		filterMap = new boolean[ cols ][ lines ];
		
		int row = 0;
		Scanner scanner = new Scanner(filter);
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  for( int cl = 0; cl < line.length(); cl++ )
		  {
			  filterMap[ cl ][ row ] = line.charAt( cl ) != 'x';
		  }
		  row++;
		}
		scanner.close();
	}

	public double getValue( BufferedImage img, double rotation, Vec2 imgCentre, Vec2 filterCentrePerc, double heightPerc )
	{
		//
		int r_in = VisualSensorGroup.IMG_DISC_RADIUS, r_out = VisualSensorGroup.IMG_OUTER_RADIUS;
		
		// Use polar coordinates (orientation is "heading")
		
		// 1. Find centre
		
		// Image is vertical inverted, so left is right, right is left
		double sensorPolarCentreAngle = ( filterCentrePerc.x - 0.25 ) * 2 * Math.PI;
		
		// Add orientation adjustment
		sensorPolarCentreAngle += rotation;
		
		double sensorCentreRadius = r_in + ( r_out - r_in ) * filterCentrePerc.y;
		
		// Need to find r_max, r_min, theta_max, theta_min
		double y_min = filterCentrePerc.y - heightPerc / 2; double y_max = filterCentrePerc.y + heightPerc / 2;
		double r_min = r_in + ( r_out - r_in ) * y_min; double r_max = r_in + ( r_out - r_in ) * y_max;
		double widthAngle = heightPerc * proportionalDimension.x / ( proportionalDimension.y * Math.PI );
		
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
				int row = ( int ) ( ( rayR - r_min ) * proportionalDimension.y / ( r_max - r_min ) );
				
				// Find pixel coordinates
				Vec2 rayPoint = imgCentre.translatePolar( rayTheta, rayR );
				int blue = img.getRGB( (int) rayPoint.x, (int) rayPoint.y ) & 0xff;
				
				// Add to totals
				pixelCount++;
				if( filterMap[ column ][ row ] )
					valueCountRaw += blue; // light area
				else
					valueCountRaw -= blue; // dark area
				
			}
		}
		
		double minusOneToOneValue = valueCountRaw / ( 255.0 * pixelCount );
		
		double zeroToOneValue = minusOneToOneValue / 2 + 1;
		
		return zeroToOneValue;
	}

}
