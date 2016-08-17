package org.evors.vision;

import java.awt.image.BufferedImage;

import org.evors.core.Queue;

public class DelayedVisualSensorGroup extends VisualSensorGroup
{
	protected double delay = 0.6;
	protected int delaySteps;
	
	protected Queue cachedValues = new Queue();
	protected Queue cachedDebugImages = new Queue();
	
	public DelayedVisualSensorGroup(ProcessedMultiChannelImageSource imgSource, double dT )
	{
		super(imgSource);
		delaySteps = ( int ) Math.round( ( delay / dT ) );
	}

	public double[] getReadings()
	{
		double[] readingsNow = super.getReadings();
		double[] rv;
		
		cachedValues.enq( readingsNow );
		
		if( cachedValues.size() <= delaySteps )
		{
			rv = readingsNow;
		}else
		{
			rv = ( double[] ) cachedValues.deq();
		}
		
		if( debugImgSource != null )
		{
			BufferedImage debugImageNow = debugImage;
			
			cachedDebugImages.enq( debugImageNow );
			
			if( cachedDebugImages.size() > delaySteps )
			{
				debugImage = ( BufferedImage ) cachedDebugImages.deq();
			}
		}
		
		return rv;
	}
	
	
}
