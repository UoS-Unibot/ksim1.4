package org.evors.core;

import java.util.BitSet;
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
    	double rv = ( 2 * Math.PI - polar ) + Math.PI / 2;
    	if( rv < 0 ) rv += Math.PI * 2; // % is remainder, not modulus in java
    	return rv % ( 2 * Math.PI );
    }
    
    public static double headingToPolar( double heading )
    {
    	double rv = ( Math.PI / 2 ) - heading;
    	if( rv < 0 ) rv += Math.PI * 2; // % is remainder, not modulus in java
    	return rv % ( 2 * Math.PI );
    }
    
    /** From jaga.BitSet: Takes two integers defining a range in the bitset
     * and returns the integer represented by these bits.  LSB last.
     * @param bitset to get bits from
     * @param first inclusive first bit in sequence to be converted
     * @param last exclusive last bit in sequence to be converted
     */
    public static int bitsToInt( BitSet bitset, int first, int last )
    {
        int rv = 0;
        for( int bitLoop = first; bitLoop < last; bitLoop++ )
        {
            if( bitset.get( bitLoop ) )
            {
                rv += 1 << ( last - 1 - bitLoop);
            }
        }
        return rv;
    }
    
    public static double getProportionValue( BitSet bits, int first, int last )
    {
    	double top = bitsToInt( bits, first, last );
    	double bottom = 1 << ( last - first );
    	return top / bottom;
    }
}
