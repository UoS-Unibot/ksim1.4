package org.evors.rs.kjunior;

import java.util.Random;

import org.evors.core.EvoRSLib;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;

import junit.framework.TestCase;

public class KJuniorPhilRegressionTest extends TestCase  {

	SimulatedKJunior robot;
	Random rnd = new Random();
	
	public KJuniorPhilRegressionTest(String testName) {
        super(testName);
        robot = new SimulatedKJunior( new SimulationWorld( new Vec2( 1500, 1500 ) ) , 0.1 );
        SimulatedKJunior.MOTOR_NOISE = 0; // Noise tested separatelly
         
        robot.setPosition( new Vec2( 5, -35 ) );
 		robot.setHeading( Math.PI / 2 ); // N
    }
	
	double Philrob_speed(double x)
	{
		double y = 0;

		//empirically determined non linear translation function, 2 linear sections
		if(x <= 15 && x>= -15)
			y= 0.6*x;
		else if(x > 15 || x < -15)
			y=1.2*x -9;

		// y += EvoRSLib.uniformNoise( 0.4 );
		return y;
	}
	
	Vec2 PhilMove_Robot( double[] msig)
	{
		double DT = 0.1;
		double ml = msig[ 0 ], mr = msig[ 1 ];
		
		double ls,rs, dx, dy,dtheta,v,oldx,oldy,oldtheta,k;

		ls = Philrob_speed(ml);  // convert to actuall cm/s speeds of rob
		rs = Philrob_speed(mr);
		v = (ls + rs)/2;
		dx = v*Math.cos( robot.getHeading() )*DT;  // change in posn from decomposed linear motion
		dy= v*Math.sin( robot.getHeading() )*DT; // swapped for test
		
		Vec2 rv = new Vec2( robot.getPosition().x + dx , robot.getPosition().y + dy );
		return rv;
	}
	
	public void testConvertSpeed()
	{
		double msig = 15;
		double lsPhil = Philrob_speed( msig );
		double vKSim = robot.convertSpeed( msig );
		assertEquals( new Double( lsPhil ), new Double( vKSim ) );
	}
	
	public void testMoveRobot()
	{
		double[] msig = { 15, 15 };
		Vec2 PhilRobPos = PhilMove_Robot( msig );
		robot.step( msig );
		Vec2 KSimRobPos = robot.getPosition();
		assertEquals( KSimRobPos.toString(), PhilRobPos.toString() );
	}
	
	public void testMoveRobotMultiple()
	{
		int n = 500;
		for( int i = 0; i < n; i++ )
		{
			double[] msig = { rnd.nextDouble() * 40 - 20, rnd.nextDouble() * 40 - 20 };
			Vec2 PhilRobPos = PhilMove_Robot( msig );
			robot.step( msig );
			Vec2 KSimRobPos = robot.getPosition();
			Vec2 pP = new Vec2( sixDecPlaces( PhilRobPos.x ), sixDecPlaces( PhilRobPos.y) );
			Vec2 kP = new Vec2( sixDecPlaces( KSimRobPos.x ), sixDecPlaces( KSimRobPos.y ) );
			assertEquals( pP.toString(), kP.toString() );
		}
	}
	
	public double sixDecPlaces( double x )
	{
		double precision = 1000000;
		double rv = x * precision;
		rv = Math.round( rv );
		rv /= precision;
		return rv;
	}

}
