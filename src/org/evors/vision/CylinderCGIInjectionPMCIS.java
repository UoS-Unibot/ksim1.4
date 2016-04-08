package org.evors.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.evors.core.EvoRSLib;
import org.evors.core.PositionOrientationSource;
import org.evors.core.geometry.Circle;
import org.evors.core.geometry.Vec2;

public class CylinderCGIInjectionPMCIS implements ProcessedMultiChannelImageSource, ImageSource {

	protected ProcessedMultiChannelImageSource yokePMCIS;
	protected ColourCollectionSource colourSource;
	protected CircleCollectionSource circleSource;
	protected PositionOrientationSource locationSource;
	protected short[][][] lastImg;
	protected boolean staticWorld = false;
	
	public CylinderCGIInjectionPMCIS( ProcessedMultiChannelImageSource yokePMCIS, CircleCollectionSource circleSource, ColourCollectionSource colourSource, PositionOrientationSource locationSource, boolean staticWorld ) {
		this.yokePMCIS = yokePMCIS;
		this.colourSource = colourSource;
		this.circleSource = circleSource;
		this.locationSource = locationSource;
		this.staticWorld = staticWorld;
	}

	java.util.Hashtable done = new java.util.Hashtable();
	
	public short[][][] getProcessedMultiChannelImage() {
		// 1. Copy image
		short[][][] img = staticWorld ? yokePMCIS.getProcessedMultiChannelImage() :
										( short[][][] ) yokePMCIS.getProcessedMultiChannelImage().clone();
		
		if( staticWorld && done.get( img ) != null )
		{
			lastImg = img;
			return img;
		}else
		{
			done.put(img, this);
		}

		// Iterate over cylinders
		Vector ccds = new Vector();
		
		Iterator colours = colourSource.getColours().iterator();
		Color colour = null;
		for( Iterator circles = circleSource.getCircles().iterator(); circles.hasNext(); )
		{
			Circle circle = ( Circle ) circles.next();
			if( colours.hasNext() ) colour = ( Color ) colours.next();
			double distance = locationSource.getPosition().distance( circle.getCenter() );
			ccds.add( new CircleColourDistance( circle, colour, distance ) );
		}
		
		Collections.sort( ccds );
		
		for( Iterator ccdsI = ccds.iterator(); ccdsI.hasNext(); )
		{
			CircleColourDistance ccd = ( CircleColourDistance ) ccdsI.next();
			Circle circle = ccd.getCircle();
			colour = ccd.getColour();
			
			// 2. Calculate phi - angle of field of view obscured by cylinder
			// 2a. Calculate x - distance from camera to center of cylinder
			// 2ai. Calculate camera location in sim (*** this will be slightly different to that at the time of sampling***).
			Vec2 robotCentre = locationSource.getPosition();
			double robotHeading = locationSource.getOrientation();
			double imageSourceRotation = yokePMCIS.getRotation();
			double worldSampledHeading = EvoRSLib.twoPIRange( imageSourceRotation - robotHeading );
			Vec2 worldSampledCameraOffset = new Vec2( VisionLib.CAMERA_FROM_CENTRE * Math.sin( worldSampledHeading ), VisionLib.CAMERA_FROM_CENTRE * Math.cos( worldSampledHeading ) );
			Vec2 cameraLocation = robotCentre.add( worldSampledCameraOffset );
	
			double x = cameraLocation.distance( circle.getCenter() );
			
			// 2b. Calculate phi
			double d = circle.getRadius() * 2;
			double phi = 2 * Math.atan( d / ( 2 * x ) );
			
			// 3. Calculate w - angle of location of cylinder in field of view
			// 3a. From N 
			double w = Math.atan2( circle.getCenter().x - cameraLocation.x, circle.getCenter().y - cameraLocation.y );
			
			// 3b. Adjust according to world sample offset
			w = EvoRSLib.twoPIRange( w + worldSampledHeading );
			w = 2 * Math.PI - w; // Inverted in 360 mirror
			w = EvoRSLib.headingToPolar( w ); // Standard trigonometry
			
			// 4. Overlay shape
			int r_in = VisualSensorGroup.IMG_DISC_RADIUS, r_out = VisualSensorGroup.IMG_OUTER_RADIUS;
			Vec2 imgCentre = new Vec2( VisualSensorGroup.IMG_GUESS_CENTRE_X, VisualSensorGroup.IMG_GUESS_CENTRE_Y );
			
			double shrinkFactor = 1;
			if( img[0].length != VisualSensorGroup.IMG_WIDTH )
			{
				shrinkFactor = ( ( double ) VisualSensorGroup.IMG_WIDTH ) / img[0].length;
				r_in /= shrinkFactor;
				r_out /= shrinkFactor;
				imgCentre = new Vec2( imgCentre.x / shrinkFactor, imgCentre.y / shrinkFactor );
			}
			
			double thetaStep = 1.0 / r_out;
			
			for( double rayTheta = w - phi/2; rayTheta < w + phi/2; rayTheta += thetaStep )
			{
				// then along line from r_min (near ceiling) to r_max (near floor)
				for( double rayR = r_in; rayR < r_out; rayR++ )
				{
					if( rayR > r_in && rayR < r_out )
					{
						Vec2 offset = new Vec2( 0,0 ).translatePolar(rayTheta, rayR);
						Vec2 invOffset = new Vec2( offset.x, -1 * offset.y );
						Vec2 rayPoint = imgCentre.add( invOffset );
						rayPoint = new Vec2( rayPoint.x, Math.max(0, Math.min( img[0][0].length - 1, rayPoint.y)) );
						for( int chIx = 0; chIx < Math.min(3,img.length ); chIx++ )
						{ 
							int channelValue;
							switch( chIx )
							{
								case 1: channelValue = colour.getGreen(); break;
								case 2: channelValue = colour.getBlue(); break;
								default: channelValue = colour.getRed();break;
							}
							img[ chIx ][ (int) ( rayPoint.x ) ][ (int) ( rayPoint.y ) ] = ( short ) channelValue;
						}
					}
				}
			}
		}
		lastImg = img;
		return lastImg;
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage( lastImg[ 0 ].length, lastImg[ 0 ][ 0 ].length, BufferedImage.TYPE_INT_RGB );
		for( int x = 0; x < lastImg[ 0 ].length; x++ )
		{
			for( int y = 0; y < lastImg[ 0 ][ 0 ].length; y++ )
			{
				Color pixel = new Color( lastImg[ ImageWhiteBalanceBaseGreyRedProcessor.IX_RED ][ x ][ y ], lastImg[ ImageWhiteBalanceBaseGreyRedProcessor.IX_GREY ][ x ][ y ], lastImg[ ImageWhiteBalanceBaseGreyRedProcessor.IX_GREY ][ x ][ y ] );
				img.setRGB(x, y, pixel.getRGB());
			}
		}
		return img;
	}

	public double getRotation() {
		return yokePMCIS.getRotation();
	}
	
	public String toString()
	{
		StringBuffer rv= new StringBuffer();
		rv.append("Cylinder CGI Injection Processed Multi Channel Image Source with:");
		rv.append("\n\t\t\t\tColour Source = " + this.colourSource.getClass().getName() );
		rv.append("\n\t\t\t\tCircle Source = " + this.circleSource.getClass().getName() );
		rv.append("\n\t\t\t\tLocation Source = " + this.locationSource.getClass().getName() );
		rv.append("\n\t\t\t\tStatic World = " + this.staticWorld );
		rv.append("\n\t\t\t\tYoke PMCIS = " + this.yokePMCIS );
		return rv.toString();
	}

	protected class CircleColourDistance implements Comparable
	{
		Circle circle;
		Color colour;
		double distance;
		
		public CircleColourDistance( Circle circle, Color colour, double distance )
		{
			this.circle = circle;
			this.colour = colour;
			this.distance = distance;
		}
		
		public int compareTo( Object o )
		{
			CircleColourDistance occd = ( CircleColourDistance ) o;
			// Descending order
			return Double.compare( occd.distance, distance );
		}
		
		public Circle getCircle(){ return circle; }
		
		public Color getColour(){ return colour; }
	}
	
}
