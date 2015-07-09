package org.evors.vision;

import java.util.BitSet;

import org.evors.core.EvoRSLib;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class VisualSensorTest extends TestCase {

	public VisualSensorTest( String name) {
		super( name );
	}
	
	public void testDecode()
	{
		int[] filterType = { 3, 2, 1, 6, 4 };
		Vec2[] centrePerc = {
				new Vec2( .5, .5 ),
				new Vec2( .25+1.0/32, .25+1.0/32 ),
				new Vec2( .125,.25 ),
				new Vec2( 7.0/8, .5 ),
				new Vec2( 5.0/8, 31.0/32 )
		};
		double[] heightPerc = { 1, .25+1.0/16, .25, .5, .5 };
		
		BitSet bits = new BitSet( VisualSensorGroup.BITS_SENSOR );
		for( int fl = 0; fl < filterType.length; fl++ )
		{
			int filterGrey = EvoRSLib.toGreyCode( filterType[ fl ] );
			//EvoRSLib.
		}
	}

}
