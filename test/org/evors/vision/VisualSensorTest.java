package org.evors.vision;

import java.util.BitSet;

import org.evors.TestUtils;
import org.evors.core.EvoRSLib;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class VisualSensorTest extends TestCase {
	
	VisualSensorGroup vsg = new VisualSensorGroup( new ImageWhiteBalanceBaseGreyRedProcessor( VisionTestLib.getImageSource() ) );
	
	public VisualSensorTest( String name) {
		super( name );
	}
	
	int[] filterType = { 3, 2, 1, 6, 4 };
	Vec2[] centrePerc = {
			new Vec2( .5, .5 ),
			new Vec2( .25+1.0/31, .25+1.0/31 ),
			new Vec2( .125,.25 ),
			new Vec2( 7.0/8, .5 ),
			new Vec2( 5.0/8, 30.0/31 )
	};
	double[] heightPerc = { 1, .25+1.0/16, .25, .5, .5 };
	
	String[] encoded = {
			"01101000001000100000",
			"01001100101100011010",
			"00100010000100001000",
			"11010110001000010000",
			"10011010010001010000"
	};
	
	public void testEncode()
	{
		BitSet bits = new BitSet( VisualSensorGroup.BITS_SENSOR );
		for( int fl = 0; fl < filterType.length; fl++ )
		{
			int bitPos = 0;
			EvoRSLib.intToBits(bits, filterType[ fl ], bitPos, VisualSensorGroup.BITS_FILTER_TYPE );
			bitPos += VisualSensorGroup.BITS_FILTER_TYPE;
			int xGrey = EvoRSLib.toGreyCode( ( int ) ( centrePerc[ fl ].x * ( ( 1 << VisualSensorGroup.BITS_CENTRE_X  ) - 1 ) ) );
			EvoRSLib.intToBits(bits, xGrey, bitPos, VisualSensorGroup.BITS_CENTRE_X );
			bitPos += VisualSensorGroup.BITS_CENTRE_X;
			int yGrey = EvoRSLib.toGreyCode( ( int ) ( centrePerc[ fl ].y * ( ( 1 << VisualSensorGroup.BITS_CENTRE_Y  ) - 1 ) ) );
			EvoRSLib.intToBits(bits, yGrey, bitPos, VisualSensorGroup.BITS_CENTRE_Y );
			bitPos += VisualSensorGroup.BITS_CENTRE_Y;
			int hGrey = EvoRSLib.toGreyCode( ( int ) ( heightPerc[ fl ] * ( ( 1 << VisualSensorGroup.BITS_HEIGHT  ) - 1 ) ) );
			EvoRSLib.intToBits(bits, hGrey, bitPos, VisualSensorGroup.BITS_HEIGHT );
			
			System.out.println( EvoRSLib.bitsToString(bits,VisualSensorGroup.BITS_SENSOR) );
			for( int bl = 0; bl < VisualSensorGroup.BITS_SENSOR; bl++ )
			{
				assertEquals( encoded[fl],EvoRSLib.bitsToString(bits,VisualSensorGroup.BITS_SENSOR) );
			}			
		}
	}
	
	public void testDecode()
	{	
		VisualSensor vs = new VisualSensor( vsg, VisualSensorGroup.BITS_FILTER_TYPE, VisualSensorGroup.BITS_CENTRE_X, VisualSensorGroup.BITS_CENTRE_Y, VisualSensorGroup.BITS_HEIGHT, VisualSensorGroup.BITS_CHANNEL );
		
		for( int fl = 0; fl < encoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < encoded[fl ].length(); bl++ )
			{
				if( encoded[fl].charAt(bl)=='1') bits.set(bl);
			}
			vs.program( bits );
			
			assertEquals( vs.FILTER_MAPPING[ filterType[fl] ], vs.getFilter() );
						
			assertEquals( centrePerc[ fl ].x, vs.getCentrePerc().x, 0.1 );
			assertEquals( centrePerc[ fl ].y, vs.getCentrePerc().y, 0.1 );
			
			assertEquals( heightPerc[ fl ], vs.getHeightPerc(), 0.1 );
			
		}

	}

}
