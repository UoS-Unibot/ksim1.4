package org.evors.core;

import java.util.Random;

import org.evors.core.geometry.Line;
import org.evors.core.geometry.Rectangle;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;

/**
 * Library file with shared objects and functions
 * 
 * @author michaelgarvie
 *
 */
public abstract class EvoRSLib {
	
	/**
	 * Shared random number generator to allow control of seed.
	 */
	public static final Random random = new Random();
	
	public static double uniformNoise( double noiseCoeff )
	{
		return ( random.nextDouble() * 2 * noiseCoeff - noiseCoeff );
	}
	
	public static SimulationWorld getStandardKSimPhilWorld()
	{
		SimulationWorld world = new SimulationWorld( new Vec2( 150, 150 ));
		world.createWorldObject( Line.fromCoords( -25, -50, -5, -45 ) );
		world.createWorldObject( Line.fromCoords( -5, -45, -5, 45));
		world.createWorldObject( Line.fromCoords( -5, 45, 15, 47));
		return world;
	}
	
	public static SimulationWorld getGC5Arena()
	{
		SimulationWorld w = new SimulationWorld( new Vec2(172, 117 ) ); // 152,97
		w.createWorldObject( Rectangle.createFromCenter( new Vec2( 10, 0 ), new Vec2( 61, 1.5) , 0 ) );
		w.createWorldObject( Rectangle.createFromCenter( new Vec2( -20.5, 0 ), new Vec2( 1.5, 61 ), 0 ) );
		return w;
	}
	
    public static String arrayToString( double[] arr )
    {
    	StringBuffer sb = new StringBuffer( "{ " );
    	if( arr.length > 0 ) sb.append( arr[ 0 ] ); 
    	for( int i = 1; i < arr.length; i++ )
    	{
    		sb.append(", "); sb.append( arr[ i ] );
    	}
    	sb.append( " }" );
    	return sb.toString();
    }
    
    /**
     * Source http://www2.mpia-hd.mpg.de/~mathar/progs/gray.java
     * @param n
     * @return
     */
    public static int toGreyCode( int n )
    {
    		return n ^ (n >> 1) ;
    }
    
    /**
     * Source http://www2.mpia-hd.mpg.de/~mathar/progs/gray.java
     * @param n
     * @return
     */
    public static int fromGreyCode( int n )
    {
    	for(int i= (n >> 1) ; i != 0 ; n ^= i, i >>= 1);
    	return n;
    }
    
    public static double polarToHeading( double polar )
    {
    	return ( 2 * Math.PI - polar ) + Math.PI / 2;
    }
    
    public static double headingToPolar( double heading )
    {
    	return ( Math.PI / 2 ) - heading;
    }
}
