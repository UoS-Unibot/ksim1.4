package org.evors.core;

import java.util.BitSet;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

import org.evors.core.geometry.Circle;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Rectangle;
import org.evors.core.geometry.Vec2;
import org.evors.rs.kjunior.SimulatedKJunior;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.vision.StaticColourCollection;

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
	
	public static double uniformNoise( double noiseCoeff, Random rnd )
	{
		return ( rnd.nextDouble() * 2 * noiseCoeff - noiseCoeff );
	}
	
	public static double uniformNoise( double noiseCoeff )
	{
		return uniformNoise( noiseCoeff, random );
	}
	
	public static Vector getRightLeftMaze()
	{
		double blockWidth = SimulatedKJunior.ROBOT_RADIUS * 4;
		SimulationWorld world = new SimulationWorld( new Vec2( 15 * blockWidth, 17 * blockWidth ) );
		addBlockyLine( 0,0,9,0,world,blockWidth);
		addBlockyLine( 0,0,0,2,world,blockWidth);
		addBlockyLine( 0,2,2,2,world,blockWidth);
		addBlockyLine( 2,2,2,1,world,blockWidth);
		addBlockyLine( 2,1,5,1,world,blockWidth);
		addBlockyLine( 5,1,5,6,world,blockWidth);
		addBlockyLine( 6,0,6,5,world,blockWidth);
		addBlockyLine( 7,0,7,5,world,blockWidth);
		addBlockyLine( 5,6,8,6,world,blockWidth);
		addBlockyLine( 8,1,8,9,world,blockWidth);
		addBlockyLine( 9,0,9,9,world,blockWidth);
		
		addBlockyLine( 10,2,10,10,world,blockWidth);
		addBlockyLine( 10,2,13,2,world,blockWidth);
		addBlockyLine( 13,2,13,4,world,blockWidth);
		addBlockyLine( 13,4,12,5,world,blockWidth);
		addBlockyLine( 12,5,11,5,world,blockWidth);
		addBlockyLine( 11,5,12,3,world,blockWidth);
		addBlockyLine( 10,0,10,1,world,blockWidth);
		addBlockyLine( 11,0,11,1,world,blockWidth);
		addBlockyLine( 12,0,12,1,world,blockWidth);
		addBlockyLine( 13,0,13,1,world,blockWidth);
		addBlockyLine( 14,0,14,1,world,blockWidth);
		addBlockyLine( 14,3,15,3,world,blockWidth);
		addBlockyLine( 14,4,15,4,world,blockWidth);
		addBlockyLine( 14,5,15,5,world,blockWidth);
		addBlockyLine( 14,6,15,6,world,blockWidth);
		addBlockyLine( 10,7,15,7,world,blockWidth);
		addBlockyLine( 11,7,11,6,world,blockWidth);
		addBlockyLine( 12,7,12,6,world,blockWidth);
		addBlockyLine( 13,7,13,6,world,blockWidth);
		
		addBlockyLine( 10,10,7,10,world,blockWidth);
		addBlockyLine( 7,7,7,15,world,blockWidth);
		addBlockyLine( 7,15,8,15,world,blockWidth);
		addBlockyLine( 8,15,8,17,world,blockWidth);
		addBlockyLine( 8,17,5,17,world,blockWidth);
		addBlockyLine( 5,15,5,17,world,blockWidth);
		addBlockyLine( 5,15,6,15,world,blockWidth);
		addBlockyLine( 6,6,6,15,world,blockWidth);
		
		Vector rv = new Vector();
		rv.add( world );
		
		Vec2 startPosition = blockyTranslate( 6, 0, world, blockWidth );
		startPosition = startPosition.add( new Vec2( blockWidth / 2, blockWidth / 2 ) );
		rv.add( startPosition );
		
		Vec2 targetPosition = blockyTranslate( 6, 17, world, blockWidth );
		targetPosition = targetPosition.add( new Vec2( blockWidth / 2 , 0 ) );
		rv.add( targetPosition );
				
		return rv;
	}
	
	protected static Vec2 blockyTranslate( int x, int y, SimulationWorld world, double blockWidth )
	{
		double xd = blockWidth * x, yd = blockWidth * y;
		xd -= ( world.getBounds().x / 2 );
		yd -= ( world.getBounds().y / 2 );
		return new Vec2( xd, yd );
	}
	
	protected static void addBlockyLine( int x0, int y0, int x1, int y1, SimulationWorld world, double blockWidth )
	{
		Vec2 p0 = blockyTranslate( x0, y0, world, blockWidth );
		Vec2 p1 = blockyTranslate( x1, y1, world, blockWidth );
		
		world.createWorldObject( Line.fromCoords( p0.x, p0.y, p1.x, p1.y ) );
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
	
	public static SimulationWorld getDemoArena()
	{
		SimulationWorld w = new SimulationWorld( new Vec2( 85, 114 ), true );
		return w;
	}
	
	public static void addCylMaze( SimulationWorld world )
	{
		world.createWorldObject( Circle.getFromCenter( new Vec2( 0, 0 ), 40 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 80, 80 ), 20 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 65, 60 ), 10 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 55, 50 ), 10 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 0, 85 ), 7 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 10, 85 ), 7 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 20, 85 ), 6 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 30, 85 ), 8 ) );
		StaticColourCollection circleColours = new StaticColourCollection( new Color[]{ new Color( 100, 100, 0 ), new Color( 250, 0, 0 ), new Color( 250, 0, 0 ), new Color( 250, 0, 0 ), new Color( 100, 100, 0 ), new Color( 0, 0, 0 ) , new Color( 255, 0, 0 ), new Color( 0, 0, 0 ) } );
	}

	public static void add3DTest( SimulationWorld world )
	{
		world.createWorldObject( Circle.getFromCenter( new Vec2( 35, 65 ), 10 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 35, 25 ), 10 ) );
		StaticColourCollection circleColours = new StaticColourCollection( new Color[]{ new Color( 0, 0, 0 ), new Color( 250, 250, 0 ) } );
		//StaticColourCollection circleColours = new StaticColourCollection( new Color[]{ new Color( 250, 250, 0 ), new Color( 0, 0, 0 ) } );
	}
	
	
	public static void addBlackMarkers( SimulationWorld world )
	{
		world.createWorldObject( Circle.getFromCenter( new Vec2( 42, -5 ), 5 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( -5, 38 ), 10 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( -5, 76 ), 5 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 90, 38 ), 15 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 90, 76 ), 5 ) );
		StaticColourCollection circleColours = new StaticColourCollection( new Color[]{ new Color( 0, 0, 0 ) } );
	}
	
	public static void addWhiteMarkers( SimulationWorld world )
	{
		world.createWorldObject( Circle.getFromCenter( new Vec2( 21, -5 ), 5 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 63, -5 ), 10 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( -5, 57 ), 5 ) );
		world.createWorldObject( Circle.getFromCenter( new Vec2( 90, 57 ), 10 ) );
		
		//Only white
		StaticColourCollection circleColours = new StaticColourCollection( new Color[]{ new Color( 255, 50, 0 ) } );
		
		//Black & White
		StaticColourCollection circleColoursBW = new StaticColourCollection( new Color[]{ new Color( 0, 0, 0 ), new Color( 0, 0, 0 ), new Color( 0, 0, 0 ), new Color( 0, 0, 0 ), new Color( 0, 0, 0 ), new Color( 255, 50, 0 ) } );
	}
	
	public static String arrayToString( double[] arr )
	{
		return arrayToString( arr, true );
	}
	
    public static String arrayToString( double[] arr, boolean showBrackets )
    {
    	StringBuffer sb = new StringBuffer();
    	if( showBrackets ) sb.append("{ " );
    	if( arr.length > 0 ) sb.append( arr[ 0 ] ); 
    	for( int i = 1; i < arr.length; i++ )
    	{
    		sb.append(", "); sb.append( arr[ i ] );
    	}
    	if( showBrackets )sb.append( " }" );
    	return sb.toString();
    }
    
    public static String arrayToStringInputs( double[] arr )
    {
    	DecimalFormat df1 = new DecimalFormat("0000");
    	DecimalFormat df2 = new DecimalFormat("#.00"); 
    	
    	StringBuffer sb = new StringBuffer( "{ " );
    	if( arr.length > 0 ) sb.append( df1.format(arr[ 0 ]) ); 
    	for( int i = 1; i < arr.length; i++ )
    	{
    		DecimalFormat df = i < 6 ? df1 : df2;
    		sb.append(", "); sb.append( df.format( arr[ i ] ) );
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
    
    public static int toGreyCodeFake( int n )
    {
    		return n ;
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
    	return twoPIRange( rv );
    }
    
    public static double headingToPolar( double heading )
    {
    	double rv = ( Math.PI / 2 ) - heading;
    	return twoPIRange( rv );
    }
    
    public static double twoPIRange( double rv )
    {
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
    
    public static double getProportionGreyValue( BitSet bits, int first, int last )
    {
    	return getProportionGreyValue( bits, first, last, true );
    }
    
    /**
     * Gets value of grey code value encoded by bits as proportional to maximum encodable value.
     * @param bits
     * @param first bit
     * @param last bit
     * @param fullRange if 0 and 1 should be included in result
     * @return Value from 0 to 1 for fullRange = true, otherwise range in between excluding ends.
     */
    public static double getProportionGreyValue( BitSet bits, int first, int last, boolean fullRange )
    {
    	double top = fromGreyCode( bitsToInt( bits, first, last ) );
    	double bottom = ( 1 << ( last - first ) ) - 1; // eg. max for 5 bits is 31
    	if( !fullRange )
    	{
    		top += 1;
    		bottom += 2;
    	}
    	return top / bottom;
    }
    
    /** 
     * From jaga.BitSet - sets bitset values to represent integer, LSB to the right
     * @param bits
     * @param num
     * @param pos
     * @param length
     */
    public static void intToBits( BitSet bits, int num, int pos, int length )
    {
        String binStr = int2BinStr( num, length );
        for( int l = 0; l < length; l++ )
        {
            boolean value = binStr.charAt( l ) == '1';
            if( value ) bits.set( pos + l ); else bits.clear( pos + l );
        }
    }
    
    public static String int2BinStr( int num, int width )
    {
        String rv = "";
        for( int bl = 0; bl < width; bl++ )
        {
            if( ( num & ( 1 << bl ) ) > 0 )
            {
                rv = "1" + rv;
            }else
            {
                rv = "0" + rv;
            }
        }
        return rv;
    }
    
    public static String bitsToString( BitSet bits, int length)
    {
    	int codeBase = 1;
    	char[] codes = { '0','1' };
        StringBuffer rv = new StringBuffer();
        
        // trailing bits are taken as if they were 0s.
        for( int bl = 0; bl <= ( ( length - 1 ) / codeBase ) * codeBase; bl += codeBase )
        {
            int code = 0;
            for( int cbl = 0; cbl < codeBase; cbl++ )
            {
                code += ( 1 << cbl ) * ( bits.get( bl + codeBase - 1 - cbl ) ? 1 : 0 );
            }
            rv.append( codes[ code ] );
        }
        
        return rv.toString();   	
    }
    
    /** Takes two integers start,seclen and returns the BitSet of length seclen starting
     * at position start of this bitset.  NOT ROBUST, assumes start + seclen <= length
    */
    public static BitSet getChunk( BitSet bits, int start, int seclen )
    {
        BitSet rv = new BitSet( seclen );
        
        for( int bl = 0; bl < seclen; bl++ )
        {
            if( bits.get( start + bl ) ) rv.set( bl );
        }
        
        return rv;
    }
    
    public static String arr2String( Object[] table )
    {
        String rv = "[ ";
        for( int tl = 0; tl < table.length; tl++ )
        {
            rv += table[ tl ] + ", ";
        }
        rv = rv.substring( 0, rv.length() - 2 );
        rv += " ]";
        return rv;
    }
    
    public static double limitTo( double value, double min, double max )
    {
    	return Math.min( max, Math.max( min, value ) );
    }

}
