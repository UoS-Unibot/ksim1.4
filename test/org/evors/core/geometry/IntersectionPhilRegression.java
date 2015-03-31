package org.evors.core.geometry;

import java.util.Random;

import org.evors.TestUtils;

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
	
	public static int PhilOrientation(Vec2 p, Vec2 q, Vec2 r)
	{
	    //orientation depends on whether slope of line segment p,q is less than, equal to
		//or greater then slope of line q,r 
	    double val = (q.y - p.y) * (r.x - q.x) -
	              (q.x - p.x) * (r.y - q.y);
	 
	    if (val == 0) return 0;  // colinear
	 
	    return (val > 0)? 1: 2; // clock or counterclock wise
	}
	
	/*****************************************************************/
	// Given three colinear points p, q, r, the function checks if
	// point r lies on line segment 'pq'
	public static boolean PhilOnSegment(Vec2 p, Vec2 q, Vec2 r)
	{
	  // check x and y projections intersect
		if (r.x <= Math.max(p.x, q.x) && r.x >= Math.min(p.x, q.x) &&
	        r.y <= Math.max(p.y, q.y) && r.y >= Math.min(p.y, q.y))
	       return true;
	 
	    return false;
	}
	
	public static boolean PhilIntersect( Line p1p2, Line p3p4 )
	{
		return PhilIntersect( p1p2.p1, p1p2.p2, p3p4.p1, p3p4.p2 );
	}
	
	public static boolean PhilIntersect( Vec2 p1, Vec2 p2, Vec2 p3, Vec2 p4 )
	{
		/******************************************************************/
		// Function that returns true if line segments p1,p2
		// and p3,p4 intersect.
	    // Find the four orientations needed for general and
	    // special cases
	    int o1 = PhilOrientation(p1, p2, p3);
	    int o2 = PhilOrientation(p1, p2, p4);
	    int o3 = PhilOrientation(p3, p4, p1);
	    int o4 = PhilOrientation(p3, p4, p2);
	 
	    // General case
		// this needs to be true for intersection in gen case
	    if (o1 != o2 && o3 != o4)
	        return true;
	 
	    // Special Cases
	    // p1, p2 and p3 are colinear and p3 lies on segment p1p2
	    if (o1 == 0 && PhilOnSegment(p1, p2, p3)) return true;
	 
	    // p1, p2 and p4 are colinear and p4 lies on segment p1p2
	    if (o2 == 0 && PhilOnSegment(p1, p2, p4)) return true;
	 
	    // p3, p4 and p1 are colinear and p1 lies on segment p3p4
	    if (o3 == 0 && PhilOnSegment(p3, p4, p1)) return true;
	 
	     // p3, p4 and p2 are colinear and p2 lies on segment p3p4
	    if (o4 == 0 && PhilOnSegment(p3, p4, p2)) return true;
	 
	    return false; // Doesn't fall in any of the above cases
	}
	
	public static Vec2 PhilIntersection_Point( Line p1p2, Line p3p4 )
	{
		return PhilIntersection_Point( p1p2.p1, p1p2.p2, p3p4.p1, p3p4.p2 );
	}
	
	public static Vec2 PhilIntersection_Point( Vec2 p1, Vec2 p2, Vec2 p3, Vec2 p4 )
	{
		// vect equns of lines are Pa = P1 + t( P2 - P1 )
		// Pb = P3 + s( P4 - P3 ) 
		// Solving for the point where Pa = Pb gives the following two equations in two unknowns (t and s)
		//x1 + t(x2 - x1) = x3 + s(x4 - x3)  and
		// y1 + t(y2 - y1) = y3 + s(y4 - y3) 
		
		double t,A;
		A= ((p4.y - p3.y)*(p2.x - p1.x) - (p4.x - p3.x)*(p2.y - p1.y));
		t = ((p4.x - p3.x)*(p1.y - p3.y) - (p4.y - p3.y)*(p1.x - p3.x))/A;

		Vec2 rv = new Vec2( p1.x + t*(p2.x - p1.x), p1.y + t*(p2.y - p1.y) );
		return rv;
	}

	protected void setUp() {
		
	}
	
	public void testL1L2() {
        Vec2 iKSim =  l1.getSmallestIntersection(l2).intersectionPoint;
        Vec2 iPhil = PhilIntersect( l1, l2 ) ? PhilIntersection_Point( l1, l2 ) : Vec2.NaN;
        //assertEquals( iKSim.toString(), iPhil.toString() ); // JQuery not finding .equals method
        TestUtils.assertVEq( iKSim, iPhil ); 
    }
	
	public void testL3L4() {
        Vec2 iKSim =  l3.getSmallestIntersection(l4).intersectionPoint;
        Vec2 iPhil = PhilIntersection_Point( l3, l4 );
        //assertEquals( iKSim.toString(), iPhil.toString() );
        TestUtils.assertVEq( iKSim, iPhil ); 
    }

	public void testL5L6() {
        Vec2 iKSim =  l5.getSmallestIntersection(l6).intersectionPoint;
        Vec2 iPhil = PhilIntersect( l5, l6 ) ? PhilIntersection_Point( l5, l6 ) : Vec2.NaN;
        if(! iKSim.toString().equals(  iPhil.toString() ) ) System.out.println( l5 + " " + l6 + " '" + iKSim + "' '" + iPhil + "' " );
        //assertEquals( iKSim.toString(), iPhil.toString() );
        TestUtils.assertVEq( iKSim, iPhil ); 
    }
	
	public void testL7L8() {
        Vec2 iKSim =  l7.getSmallestIntersection(l8).intersectionPoint;
        Vec2 iPhil = PhilIntersection_Point( l7, l8 );
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
	        Vec2 iPhil = PhilIntersect( l5, l6 ) ? PhilIntersection_Point( l5, l6 ) : Vec2.NaN;
	        
	        //assertEquals( kP.toString(), pP.toString() );
	        TestUtils.assertVEq( iKSim, iPhil ); 
		}
    }
	
}
