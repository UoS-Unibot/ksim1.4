package org.evors.rs.kjunior;

import org.evors.core.EvoRSLib;
import org.evors.core.RobotController;
import org.evors.core.RunController;
import org.evors.core.geometry.Intersection;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;


/**
 * Copy of main.cpp test case in Phil's C++ sim code
 * @author michaelgarvie
 *
 */
public class PhilTest {
	
	public static final double TIME_STEP = 0.1;
	public static final int NUM_IRS = 6;
	public static final Vec2 WORLD_OFFSET = new Vec2( 75, 75 );
	public static boolean consoleOutput = false;
	
	public PhilTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		PhilTest pt = new PhilTest();
		SimulationWorld world = EvoRSLib.getStandardKSimPhilWorld();
		SimulatedKJunior robot = new SimulatedKJunior( world, TIME_STEP );
		
		// Geometry tests
		Line l1 = Line.fromCoords( 1, 1, 10, 1);
		Line l2 = Line.fromCoords( 1, 2, 10, 2);
		Intersection ix = l1.getSmallestIntersection( l2 );
		if( consoleOutput ) System.out.println( ix.intersectionPoint ); 
		
		l1 = Line.fromCoords( 10, 0, 0, 10);
		l2 = Line.fromCoords(0, 0, 10, 10);
		ix = l1.getSmallestIntersection( l2 );
		if( consoleOutput ) System.out.println( ix.intersectionPoint );
		
		l1 = Line.fromCoords( -5,-5,0,0 );
		l2 = Line.fromCoords( 1,1,10,10 );
		ix = l1.getSmallestIntersection( l2 );
		if( consoleOutput ) System.out.println( ix.intersectionPoint );
		
		Vec2 p1 = new Vec2( 1, 1 );		
		if( consoleOutput ) System.out.println( p1.distance( new Vec2( -3, -3 ) ) );
		
		l1 = Line.fromCoords( 0,0,10,10 );
		l2 = Line.fromCoords( 10,0,9,100);
		ix = l1.getSmallestIntersection( l2 );
		if( consoleOutput ) System.out.println( ix.intersectionPoint );
		
		robot.setPosition( new Vec2( -68, 65 ) );
		world.checkCollisions( robot );
				
		robot.setPosition( new Vec2( 5, -35 ) );
		robot.setPolarOrientation( Math.PI / 2 ); // N
		
		RunController sim = new RunController( new PhilController( consoleOutput ), robot );
		
		for( int i = 0; i < 2000; i++ )
		{
			sim.step();
			
			Vec2 robpos = robot.getPosition().add( WORLD_OFFSET );
			if( consoleOutput ) System.out.println( "rob: " + robpos.x + " " + robpos.y + " " + trigToBearing( robot.getPolarOrientation()) );
			if( !consoleOutput ) System.out.println( robpos.x +  " " + robpos.y );
			double[] robirs = robot.getInput();
			String irs = "";
			for( int j = 0; j < NUM_IRS; j++ )
			{
				irs += ( (int) robirs[ j ] ) + " ";
			}
			
			if( consoleOutput ) System.out.println( irs );
			
			// Check collision
			if( !sim.isLive() )
			{
				System.out.println( " collided");
				break;
			}
		}

	}
	
	static double trigToBearing( double angle )
	{
		double rv = ( 2 * Math.PI - angle ) + Math.PI / 2;
		return rv % ( 2 * Math.PI );
	}
	

	
}
