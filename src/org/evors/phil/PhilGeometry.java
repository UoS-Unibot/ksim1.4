package org.evors.phil;

import org.evors.core.geometry.Line;
import org.evors.core.geometry.Vec2;

public abstract class PhilGeometry {

	public static int orientation(Vec2 p, Vec2 q, Vec2 r)
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
	public static boolean on_segment(Vec2 p, Vec2 q, Vec2 r)
	{
	  // check x and y projections intersect
		if (r.x <= Math.max(p.x, q.x) && r.x >= Math.min(p.x, q.x) &&
	        r.y <= Math.max(p.y, q.y) && r.y >= Math.min(p.y, q.y))
	       return true;
	 
	    return false;
	}
	
	public static boolean intersect( Line p1p2, Line p3p4 )
	{
		return intersect( p1p2.p1, p1p2.p2, p3p4.p1, p3p4.p2 );
	}
	
	public static boolean intersect( Vec2 p1, Vec2 p2, Vec2 p3, Vec2 p4 )
	{
		/******************************************************************/
		// Function that returns true if line segments p1,p2
		// and p3,p4 intersect.
	    // Find the four orientations needed for general and
	    // special cases
	    int o1 = orientation(p1, p2, p3);
	    int o2 = orientation(p1, p2, p4);
	    int o3 = orientation(p3, p4, p1);
	    int o4 = orientation(p3, p4, p2);
	 
	    // General case
		// this needs to be true for intersection in gen case
	    if (o1 != o2 && o3 != o4)
	        return true;
	 
	    // Special Cases
	    // p1, p2 and p3 are colinear and p3 lies on segment p1p2
	    if (o1 == 0 && on_segment(p1, p2, p3)) return true;
	 
	    // p1, p2 and p4 are colinear and p4 lies on segment p1p2
	    if (o2 == 0 && on_segment(p1, p2, p4)) return true;
	 
	    // p3, p4 and p1 are colinear and p1 lies on segment p3p4
	    if (o3 == 0 && on_segment(p3, p4, p1)) return true;
	 
	     // p3, p4 and p2 are colinear and p2 lies on segment p3p4
	    if (o4 == 0 && on_segment(p3, p4, p2)) return true;
	 
	    return false; // Doesn't fall in any of the above cases
	}
	
	public static Vec2 intersection_point( Line p1p2, Line p3p4 )
	{
		return intersection_point( p1p2.p1, p1p2.p2, p3p4.p1, p3p4.p2 );
	}
	
	public static Vec2 intersection_point( Vec2 p1, Vec2 p2, Vec2 p3, Vec2 p4 )
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
	
	// distance a to b, pythag
	public static double dist( Vec2 a,  Vec2 b)
	{
		double r, DX, DY;
		DX= b.x - a.x;
		DY = b.y - a.y; 
		r= Math.sqrt(DX*DX + DY*DY);
		return(r);
	}
	
	/**************************************************************/
	//dot product

	static double dot(Vec2 a, Vec2 b)
	{
		double d;

		d = (a.x*b.x) + (a.y*b.y);
			return d;
	}
	
	//shortest distance between a point p and a line segment [a,b]
	public static double shortest_dist(Vec2 a, Vec2 b, Vec2 p)
	{
		double l,t,X,Y;
		Vec2 A,B,proj;

		l = dist(a,b);
		if (l ==0) return dist(p,a);  // a= b special case

		// Consider the line extending the segment, parameterized as a + t (b-a).
	  // find projection of point p onto the line. 
	  // It falls where t = [(p-a) . (b-a)] / l^2
		A = p.subtract( a );
		B = b.subtract( a );
		t= dot(A,B)/(l*l);  // dot product

		if (t<0) return dist(p,a);  // off the a end
		if (t>1) return dist(p,b); //off the b end
		X = a.x + t*(b.x - a.x);
		Y = a.y + t*(b.y - a.y);
		proj = new Vec2( X, Y ); // projection onto line segment
		return dist(p,proj);


	}
}
