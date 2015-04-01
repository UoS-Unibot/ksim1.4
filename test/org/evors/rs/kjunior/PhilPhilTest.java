package org.evors.rs.kjunior;

import org.evors.core.geometry.Vec2;
import org.evors.phil.PhilGeometry;
import org.evors.phil.PhilSim;

public class PhilPhilTest {

	static boolean debugOutput = false;
	
	public PhilPhilTest() {
		// TODO Auto-generated constructor stub
	}
	
	static void printf( String s ){ if( debugOutput ) System.out.println( s ); }

	public static void main( String[] args )
	{
		PhilSim ps = new PhilSim();
		PhilSim.PhilRobot robot = ps.getRobot();
		
		Vec2 p1 = new Vec2(1, 1), q1 = new Vec2( 10, 1 );
	    Vec2 p2 = new Vec2(1, 2), q2 = new Vec2( 10, 2 ),p3,p4,ip;
		double d;
		int j,i;

	   String s = PhilGeometry.intersect(p1, q1, p2, q2)  ? "yes" : "no"; printf( s );
	 
	   p1 = new Vec2( 10, 0 ); q1 = new Vec2( 0, 10 );
	   p2 = new Vec2( 0, 0 ); q2 = new Vec2( 10, 10 );
	    s =PhilGeometry.intersect(p1, q1, p2, q2)?   "yes" : "no"; printf( s );
	 
		   p1 = new Vec2( -5, -5 ); q1 = new Vec2( 0, 0 );
		   p2 = new Vec2( 1, 1 ); q2 = new Vec2( 10, 10 );
	    s =PhilGeometry.intersect(p1, q1, p2, q2)  ? "yes" : "no"; printf( s );

	    p3 = new Vec2( -3, -3 );
		p1 = new Vec2( 1,1);
		p2 = new Vec2( 1, 0.5);
		d=PhilGeometry.shortest_dist(p1,q2,p3);
		printf("sh dist= "+d);
		
		p1 = new Vec2( 0,0 ); p2 = new Vec2( 10, 10 );
		p3 = new Vec2( 10, 0); p4 = new Vec2( 9, 100 );

		if(PhilGeometry.intersect(p1,p2,p3,p4))
			{ ip = PhilGeometry.intersection_point(p1,p2,p3,p4);
			printf("intersection "+ip);
		}
		else printf("no intersect \n");

		robot.position = new Vec2( 7, 140 );
		if(ps.collision()) printf(" collided \n");
		else printf(" no collision \n");

		// typical main loop
		robot.position = new Vec2( 80, 40 );
		robot.orientation = 0;
		
		PhilController pc = new PhilController( debugOutput );
		for(j=0;j<2000 && !ps.collision();j++)
			{ 
				pc.step( robot.IRvals );
				ps.move_robot( pc.getControlOutputs() );
				ps.readIRs();
				printf("rob: "+robot.position+" "+robot.orientation );
				if( !debugOutput ) System.out.println( robot.position.x + " " + robot.position.y + " " + robot.orientation );
				printf( robot.IRvals[0]+" "+robot.IRvals[1]+" "+robot.IRvals[2]+" "+robot.IRvals[3]+" "+robot.IRvals[4]+" "+robot.IRvals[5]+" ");
				if(ps.collision()) printf(" collided \n"); // checks to see if robot colided with a wall
			}
		
	}
}
