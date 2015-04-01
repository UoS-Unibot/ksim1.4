package org.evors.core.geometry;

import java.util.Random;

import org.evors.TestUtils;
import org.evors.phil.PhilGeometry;

import junit.framework.TestCase;

/**
 * Difference so far is edge case when intersection touches without crossing.
 * 
 * @author michaelgarvie
 *
 */
public class IntersectionPhilRegression extends TestCase {
	
	static final Random rnd = new Random(); static final int RND_RANGE = 10;
	
	static Line l1 = Line.fromCoords( 0, 0, 10, 10),
			l2 = Line.fromCoords( 10, 0, 0, 10),
			l3 = Line.fromCoords( -10, 0, 10, 0),
			l4 = Line.fromCoords( 0, -10, 0, 10 ),
			l5 = Line.fromCoords( rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ) ),
			l6 = Line.fromCoords( rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ) ),
			l7 = Line.fromCoords(9, 6, 0, 6),
			l8 = Line.fromCoords(1, 6, 1, 0 );
	
	public IntersectionPhilRegression(String testName) {
        super(testName);
    }
	
	protected void setUp() {
		
	}
	
	public void testL1L2() {
        Vec2 iKSim =  l1.getSmallestIntersection(l2).intersectionPoint;
        Vec2 iPhil = PhilGeometry.intersect( l1, l2 ) ? PhilGeometry.intersection_point( l1, l2 ) : Vec2.NaN;
        //assertEquals( iKSim.toString(), iPhil.toString() ); // JQuery not finding .equals method
        TestUtils.assertVEq( iKSim, iPhil ); 
    }
	
	public void testL3L4() {
        Vec2 iKSim =  l3.getSmallestIntersection(l4).intersectionPoint;
        Vec2 iPhil = PhilGeometry.intersection_point( l3, l4 );
        //assertEquals( iKSim.toString(), iPhil.toString() );
        TestUtils.assertVEq( iKSim, iPhil ); 
    }

	public void testL5L6() {
        Vec2 iKSim =  l5.getSmallestIntersection(l6).intersectionPoint;
        Vec2 iPhil = PhilGeometry.intersect( l5, l6 ) ? PhilGeometry.intersection_point( l5, l6 ) : Vec2.NaN;
        if(! iKSim.toString().equals(  iPhil.toString() ) ) System.out.println( l5 + " " + l6 + " '" + iKSim + "' '" + iPhil + "' " );
        //assertEquals( iKSim.toString(), iPhil.toString() );
        TestUtils.assertVEq( iKSim, iPhil ); 
    }
	
	public void testL7L8() {
        Vec2 iKSim =  l7.getSmallestIntersection(l8).intersectionPoint;
        Vec2 iPhil = PhilGeometry.intersection_point( l7, l8 );
        //assertEquals( iKSim.toString(), iPhil.toString() );
        TestUtils.assertVEq( iKSim, iPhil ); 
    }
	
	public void testMultiple() {
		int n = 50;
		
		for( int j = 0; j < n; j++ )
		{
			l5 = Line.fromCoords( rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ) );
			l6 = Line.fromCoords( rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ), rnd.nextInt( RND_RANGE ) );
			
	        Vec2 iKSim =  l5.getSmallestIntersection(l6).intersectionPoint;
	        Vec2 iPhil = PhilGeometry.intersect( l5, l6 ) ? PhilGeometry.intersection_point( l5, l6 ) : Vec2.NaN;
	        
	        //assertEquals( kP.toString(), pP.toString() );
	        TestUtils.assertVEq( iKSim, iPhil ); 
		}
    }
	
}
