package org.evors.core.geometry;

import junit.framework.TestCase;

import org.evors.core.EvoRSLib;
import org.junit.Test;

public class GeometryLibTest extends TestCase {

	public static double DEFINITION = 0.00001;
	double pi = Math.PI;
	
	 public GeometryLibTest(String testName) {
	        super(testName);
	    }

    public void testPolarToHeading() {
        
    	double[] polarAngles = { pi/4, pi/2, pi, 5*pi/4, 3*pi/2, -1*pi/2, 0 };
    	double[] headingAngles={ pi/4, 0, 3*pi/2,5*pi/4, pi,      pi,     pi/2};
    	for( int al = 0; al < polarAngles.length; al++ )
    	{
    		double calcH = EvoRSLib.polarToHeading( polarAngles[ al ] );
    		double storeH = headingAngles[ al ];
    		assertEquals( calcH, storeH, DEFINITION );
    	}
    }
    
    public void testHeadingToPolar() 
    {
    	double[] headingAngles={ pi/4, 0, 3*pi/2,5*pi/4, pi,      -1*pi/2,     pi/2};
    	double[] polarAngles = { pi/4, pi/2, pi, 5*pi/4, 3*pi/2, pi, 0 };
    	
    	
    	for( int al = 0; al < polarAngles.length; al++ )
    	{
    		double calcP = EvoRSLib.headingToPolar( headingAngles[ al ] );
    		double storeP = polarAngles[ al ];
    		//System.out.println( calcP +" "+storeP );
    		assertEquals( calcP, storeP, DEFINITION );
    	}
    }

}
