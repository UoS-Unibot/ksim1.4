package org.evors.vision;

import java.util.BitSet;

import org.evors.core.Programmable;
import org.evors.processing.MinimumThreshold;
import org.evors.processing.Threshold;

public class VisualSensorGroup implements Programmable {
	
	protected ImageSource imgSource;
	protected int sensorCount = 5;
	protected VisualSensor[] sensors;
	protected Threshold[] thresholds;
	
	protected int BITS_FILTER_TYPE = 3;
	protected int BITS_CENTRE_X = 6;
	protected int BITS_CENTRE_Y = 5;
	protected int BITS_HEIGHT = 5;
	protected int BITS_MIN_THRESHOLD = 4;
	
	protected int bitsSensor = BITS_FILTER_TYPE + BITS_CENTRE_X + BITS_CENTRE_Y + BITS_HEIGHT;
	protected int bitsBlock = bitsSensor + BITS_MIN_THRESHOLD;
	
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

}
