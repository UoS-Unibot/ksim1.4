package org.evors.rs.kjunior;

import java.util.Random;

import org.evors.TestUtils;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Vec2;
import org.evors.phil.PhilSim;
import org.evors.phil.PhilSim.Wall;
import org.evors.rs.sim.core.SimulationWorld;

import junit.framework.TestCase;

public class KSimPSimRegression extends TestCase {

	public static Wall[] world = { new Wall( new Vec2( -750, -750 ), new Vec2( -750, 750 ) ),
							 new Wall( new Vec2( -750, 750 ), new Vec2( 750, 750 ) ),
							 new Wall( new Vec2( 750, 750 ), new Vec2( 750, -750 ) ),
							 new Wall( new Vec2( 750, -750), new Vec2( -750, -750 )) };
	
	SimulatedKJunior robotKS;
	PhilSim ps = new PhilSim();
	PhilSim.PhilRobot robotPhil = ps.getRobot();
	Random rnd = new Random();
	
	public KSimPSimRegression(String testName) {
        super(testName);
        SimulationWorld simWorld = new SimulationWorld( new Vec2( 1500, 1500 ) );
        ps.world = world;
        robotKS = new SimulatedKJunior( simWorld , 0.1 );
        SimulatedKJunior.MOTOR_NOISE = PhilSim.MNOISE = 0; // Noise tested separatelly
        IRBeam.IR_NOISE = PhilSim.IRNOISE = 0;
         
        robotKS.setPosition( new Vec2( 5, -35 ) );
        robotKS.setHeading( Math.PI / 2 ); // N
        
        robotPhil.position = new Vec2( 5, -35 );
        robotPhil.orientation = 0;
    }
	
	public void testMoveRobot()
	{
		double[] msig = { 15, 15 };
		Vec2 PhilRobPos = ps.move_robot( msig );
		robotKS.step( msig );
		Vec2 KSimRobPos = robotKS.getPosition();
		TestUtils.assertVEq( KSimRobPos, PhilRobPos );
	}
	
	public void testMoveRobotMultiple()
	{
		int n = 500;
		for( int i = 0; i < n; i++ )
		{
			double[] msig = { rnd.nextDouble() * 40 - 20, rnd.nextDouble() * 40 - 20 };
			Vec2 PhilRobPos = ps.move_robot( msig );
			robotKS.step( msig );
			Vec2 KSimRobPos = robotKS.getPosition();
			TestUtils.assertVEq( KSimRobPos, PhilRobPos );
		}
	}
	
	public void testIRReading()
	{
        robotKS.setPosition( new Vec2( 740, 740 ) );
 		robotKS.setHeading( 3 * Math.PI / 4 ); // NW
 		
 		robotPhil.position = new Vec2( 740, 740 );
 		robotPhil.orientation = 7 * Math.PI / 4; // NW
 		
 		double ksIR = robotKS.getIRReading( 0 );
 		//ps.readIRs();
 		double pIR = ps.ir_reading( robotPhil.orientation );
 		
 		assertEquals(ksIR, pIR, 1.5 );
	}
	
	public void testIRReadingMultipleAnglesTop()
	{
		System.out.println("===Top===");
        robotKS.setPosition( new Vec2( 740, 740 ) );
 		robotKS.setHeading( 3 * Math.PI / 4 ); // NW
		robotPhil.position = new Vec2( 740, 740 );
 		robotPhil.orientation = 7 * Math.PI / 4; // NW
  		
 		int n = 100;
 		double dA = 0.1;
 		for( int i = 0; i < n; i++ )
 		{
 			double ksIR = robotKS.getIRReading( 0 );
 			ps.readIRs();
 	 		double pIR = robotPhil.IRvals[ 2 ]; // 0 angle one
 	 		
 	 		if( Math.abs( ksIR - pIR ) > 0.5 )
 	 		{
 	 			System.out.println( ksIR + " " + pIR + " " + i + " " + robotKS.getHeading());
 	 		}
 	 		assertEquals( ksIR, pIR, 1.1 );
 	 		
 	 		robotKS.setHeading( ( robotKS.getHeading() + dA ) % ( Math.PI * 2 ) );
 	 		robotPhil.orientation = PhilSim.ForceAngleInCircle( robotPhil.orientation - dA );
 		}
 		
	}
	
	public void testIRReadingMultipleAnglesBottom()
	{
		System.out.println("===Bottom===");
        robotKS.setPosition( new Vec2( -300, -740 ) );
 		robotKS.setHeading( 3 * Math.PI / 2 ); // S
 		robotPhil.position = new Vec2( -300, -740 );
 		robotPhil.orientation= Math.PI ; // S
 		
 		int n = 100;
 		double dA = 0.1;
 		for( int i = 0; i < n; i++ )
 		{
 			double ksIR = robotKS.getIRReading( 0 );
 	 		double pIR = ps.ir_reading( robotPhil.orientation );
 	 		if( Math.abs( ksIR - pIR ) > 0.5 )
 	 		{
 	 			System.out.println( ksIR + " " + pIR + " " + i + " " + robotKS.getHeading());
 	 		}
 	 		assertEquals( ksIR, pIR, 1.1 );
 	 		
 	 		
 	 		robotKS.setHeading( ( robotKS.getHeading() + dA ) % ( Math.PI * 2 ) );
 	 		robotPhil.orientation = PhilSim.ForceAngleInCircle( robotPhil.orientation - dA );
 		}
 		
	}
	
	static double bearingToTrig( double theta )
	{
		double rv = Math.PI / 2 - theta;
		return rv % ( 2 * Math.PI );
	}
	
	public void testBugScenario()
	{
		ps = new PhilSim();
		robotPhil = ps.getRobot();
		SimulationWorld simWorld = new SimulationWorld( new Vec2( 300, 300 ) );
		simWorld.createWorldObject( Line.fromCoords( 0, 0, 150, 0));
		simWorld.createWorldObject( Line.fromCoords( 0, 0, 0, 150));
		robotKS.setWorld( simWorld );
		
        robotKS.setPosition( new Vec2( 36, 6 ) );
 		robotKS.setHeading( bearingToTrig( 2.9191 ) );
 		robotPhil.position = new Vec2( 36, 6 );
 		robotPhil.orientation= 2.9191;
 		
 		int n = 1;
 		double dA = 0.1;
 		for( int i = 0; i < n; i++ )
 		{
 			double ksIR = robotKS.getIRReading( 0 );
 	 		double pIR = ps.ir_reading( robotPhil.orientation );
 	 		assertEquals( ksIR, pIR, 1.1 );
 	 		
 	 		robotKS.setHeading( ( robotKS.getHeading() + dA ) % ( Math.PI * 2 ) );
 	 		robotPhil.orientation = PhilSim.ForceAngleInCircle( robotPhil.orientation - dA );
 		}
	}
	
}
