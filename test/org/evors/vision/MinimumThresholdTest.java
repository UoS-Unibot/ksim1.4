package org.evors.vision;

import java.util.BitSet;

import org.evors.core.EvoRSLib;
import org.evors.processing.MinimumThreshold;

import junit.framework.TestCase;

public class MinimumThresholdTest extends TestCase {

	int[] values = { 0, 15, 7, 1, 12 };
	double[] threshold = { 0, 1.0, 0.467, 0.067, 0.8 };
	double[][] testValues = { 
			{ 0, 0.5 },
			{ 0.5, 1.0 },
			{ 0.3, 0.5 },
			{ 0.01, 0.1 },
			{ 0.3, 0.9 }
	};
	
	double[][] testResults = {
			{ 0, 0.5 },
			{ 0, 1.0 },
			{ 0, 0.5 },
			{ 0, 0.1 },
			{ 0, 0.9 }
	};
	
	BitSet[] configs = new BitSet[ values.length ];
	
	public MinimumThresholdTest( String name ) {
		super( name );
		for( int vl = 0; vl < values.length; vl++ )
		{
			int greyValue = EvoRSLib.toGreyCode( values[ vl ] );
			configs[ vl ] = new BitSet( 4 );
			EvoRSLib.intToBits(configs[ vl ], greyValue, 0, 4 ); 
		}
	}
	
	public void testProgram()
	{
		for( int vl = 0; vl < values.length; vl++ )
		{
			MinimumThreshold minTh = new MinimumThreshold();
			minTh.setThreshold( EvoRSLib.getProportionGreyValue( configs[ vl ], 0, 4 ) );
			
			assertEquals( threshold[ vl ], minTh.getThreshold(), 0.01 );
		}
	}
	
	public void testTreshold()
	{
		for( int vl = 0; vl < values.length; vl++ )
		{
			MinimumThreshold minTh = new MinimumThreshold();
			minTh.setThreshold( EvoRSLib.getProportionGreyValue( configs[ vl ], 0, 4 ) );
			
			for( int tl = 0; tl < testValues[ vl ].length; tl++ )
			{
				assertEquals( testResults[ vl ][ tl ], minTh.threshold( testValues[ vl ][ tl ] ), 0.01 );
			}
		}
	}

}
