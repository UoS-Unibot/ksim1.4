package org.evors.rs.kjunior;

import org.evors.core.IRobotController;
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
	
	public PhilTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PhilTest pt = new PhilTest();
		SimulationWorld world = pt.createWorld();
		SimulatedKJunior robot = new SimulatedKJunior( world, TIME_STEP );
		
		// Geometry tests
		Line l1 = Line.fromCoords( 1, 1, 10, 1);
		Line l2 = Line.fromCoords( 1, 2, 10, 2);
		Intersection ix = l1.getSmallestIntersection( l2 );
		System.out.println( ix.intersectionPoint ); 
		
		l1 = Line.fromCoords( 10, 0, 0, 10);
		l2 = Line.fromCoords(0, 0, 10, 10);
		ix = l1.getSmallestIntersection( l2 );
		System.out.println( ix.intersectionPoint );
		
		l1 = Line.fromCoords( 0,0,10,10 );
		l2 = Line.fromCoords( 10,0,9,100);
		ix = l1.getSmallestIntersection( l2 );
		System.out.println( ix.intersectionPoint );
		
		
		robot.setPosition( new Vec2( -68, 65 ) );
		
		// Check collision here
		
		robot.setPosition( new Vec2( 5, -35 ) );
		robot.setHeading( Math.PI / 2 ); // N
		
		RunController sim = new RunController( pt.new PhilController(), robot );
		
		for( int i = 0; i < 2000; i++ )
		{
			sim.step();
			
			Vec2 robpos = robot.getPosition().add( WORLD_OFFSET );
			//System.out.println( "rob: " + robpos.x + " " + robpos.y + " " + trigToBearing( robot.getHeading()) );
			System.out.println( robpos.x +  " " + robpos.y );
			double[] robirs = robot.getInput();
			for( int j = 0; j < NUM_IRS; j++ )
			{
				//System.out.println( robirs[ j ] );
			}
			
			//System.out.println();
			
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
	
	SimulationWorld createWorld()
	{
		SimulationWorld world = new SimulationWorld( new Vec2( 150, 150 ));
		world.createWorldObject( Line.fromCoords( -25, -50, -5, -45 ) );
		world.createWorldObject( Line.fromCoords( -5, -45, -5, 45));
		world.createWorldObject( Line.fromCoords( -5, 45, 15, 47));
		return world;
	}
	
	class PhilController implements IRobotController
	{

		double[] IRVals;
		
		public void step(double[] input) {
			IRVals = input;
			
		}

		public double[] getControlOutputs() {
			boolean highf = false;
			boolean[] high = new boolean[ NUM_IRS ];
			double[] mr = new double[ 2 ];
			String print = "";
			
			for( int i = 0; i < NUM_IRS; i++ )
			{
				if( IRVals[ i ] > 2000 )
				{
					high[ i ] = true;
					highf = true;
				}
			}
			
			if( !highf )
			{
				mr[ 0 ] = mr[ 1 ] = 15; 	print = "straight";
			}else if( high[3] || high[4])
			{
				mr[ 0 ] = -20; mr[ 1 ] = 20; print = "right";
			}else if( high[0] || high[1])
			{
				mr[ 0 ] = 20; mr[ 1 ] = -20; print = "left";
			}else if( high[ 2 ] )
			{
				mr[ 0 ] = mr[ 1 ] = 15;		print = "just mid";
			}else if( high[ 5] )
			{
				mr[ 0 ] = mr[ 1 ] = 15;		print = "hight at back";
			}
			//System.out.println( "... " + print + " ...");
			//System.out.println( "ls " + mr[ 0 ] + " rs " + mr[ 1 ] );
			return mr;
		}
		
	}

}
