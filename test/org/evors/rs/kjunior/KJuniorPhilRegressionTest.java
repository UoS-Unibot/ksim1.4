package org.evors.rs.kjunior;

import java.util.Random;

import org.evors.TestUtils;
import org.evors.core.EvoRSLib;
import org.evors.core.geometry.IntersectionPhilRegression;
import org.evors.core.geometry.Vec2;
import org.evors.phil.PhilGeometry;
import org.evors.phil.PhilSim;
import org.evors.rs.sim.core.SimulationWorld;

import junit.framework.TestCase;

public class KJuniorPhilRegressionTest extends TestCase  {

	public static final double ROB_TOPRAD = 5.75;
	public static final double[] IRrayAngs={-0.437,-0.218,0,0.218,0.437}; 
	public static final int WORLDSIZE = 10;
	public static final double TWOPI = Math.PI * 2;
	public static final double RAYLEN = 25;
	public static final double IRCOEFF = 1.0;
	public static final double WHEEL_SEP = 10;
	public static Wall[] world = { new Wall( new Vec2( -750, -750 ), new Vec2( -750, 750 ) ),
							 new Wall( new Vec2( -750, 750 ), new Vec2( 750, 750 ) ),
							 new Wall( new Vec2( 750, 750 ), new Vec2( 750, -750 ) ),
							 new Wall( new Vec2( 750, -750), new Vec2( -750, -750 )) };
	
	SimulatedKJunior robotKS, robotPhil;
	Random rnd = new Random();
	
	public KJuniorPhilRegressionTest(String testName) {
        super(testName);
        SimulationWorld simWorld = new SimulationWorld( new Vec2( 1500, 1500 ) );
        robotKS = new SimulatedKJunior( simWorld , 0.1 );
        robotPhil = new SimulatedKJunior( simWorld, 0.1 );
        SimulatedKJunior.MOTOR_NOISE = PhilSim.MNOISE = 0; // Noise tested separatelly
        IRBeam.IR_NOISE = PhilSim.IRNOISE = 0;
         
        robotKS.setPosition( new Vec2( 5, -35 ) );
        robotKS.setPolarOrientation( Math.PI / 2 ); // N
        
        robotPhil.setPosition( new Vec2( 5, -35 ) );
        robotPhil.setPolarOrientation( 0 );
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
		dx = v*Math.sin( robotPhil.getPolarOrientation() )*DT;  // change in posn from decomposed linear motion
		dy= v*Math.cos( robotPhil.getPolarOrientation() )*DT; 
		
		Vec2 rv = new Vec2( robotPhil.getPosition().x + dx , robotPhil.getPosition().y + dy );
		dtheta = (ls - rs)*DT/WHEEL_SEP;    // change in orientation from decomposed rotational movement  v=wr etc

		robotPhil.setPolarOrientation( ForceAngleInCircle( robotPhil.getPolarOrientation() + dtheta ) );
		robotPhil.setPosition( rv );
		return rv;
	}
	
	public void testConvertSpeed()
	{
		double msig = 15;
		double lsPhil = Philrob_speed( msig );
		double vKSim = robotKS.convertSpeed( msig );
		assertEquals( new Double( lsPhil ), new Double( vKSim ) );
	}
	
	public void testMoveRobot()
	{
		double[] msig = { 15, 15 };
		Vec2 PhilRobPos = PhilMove_Robot( msig );
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
			Vec2 PhilRobPos = PhilMove_Robot( msig );
			robotKS.step( msig );
			Vec2 KSimRobPos = robotKS.getPosition();
			TestUtils.assertVEq( KSimRobPos, PhilRobPos );
		}
	}
	
	public void testIRReading()
	{
        robotKS.setPosition( new Vec2( 740, 740 ) );
 		robotKS.setPolarOrientation( 3 * Math.PI / 4 ); // NW
 		
 		robotPhil.setPosition( new Vec2( 740, 740 ) );
 		robotPhil.setPolarOrientation( 7 * Math.PI / 4 ); // NW
 		
 		double ksIR = robotKS.getIRReading( 0 );
 		double pIR = ir_reading( robotPhil.getPolarOrientation() );
 		
 		assertEquals(ksIR, pIR, 1.5 );
	}
	
	public void testIRReadingMultipleAnglesTop()
	{
		System.out.println("====Top====");
        robotKS.setPosition( new Vec2( 740, 740 ) );
 		robotKS.setPolarOrientation( 3 * Math.PI / 4 ); // NW
 		robotPhil.setPosition( new Vec2( 740, 740 ) );
 		robotPhil.setPolarOrientation( 7 * Math.PI / 4 ); // NW
 		
 		int n = 100;
 		double dA = 0.1;
 		for( int i = 0; i < n; i++ )
 		{
 			double ksIR = robotKS.getIRReading( 0 );
 	 		double pIR = ir_reading( robotPhil.getPolarOrientation() );
 	 		if( Math.abs( ksIR - pIR ) > 1.5 )
 	 		{
 	 			System.out.println( ksIR + " " + pIR + " " + i + " " + robotKS.getPolarOrientation());
 	 		}
 	 		assertEquals( ksIR, pIR, 1.5 );
 	 		
 	 		
 	 		robotKS.setPolarOrientation( ( robotKS.getPolarOrientation() + dA ) % ( Math.PI * 2 ) );
 	 		robotPhil.setPolarOrientation( this.ForceAngleInCircle( robotPhil.getPolarOrientation() - dA ) );
 		}
 		
	}
	
	public void testIRReadingMultipleAnglesBottom()
	{
		System.out.println("===Bottom===");
        robotKS.setPosition( new Vec2( -300, -740 ) );
 		robotKS.setPolarOrientation( 3 * Math.PI / 2 ); // S
 		robotPhil.setPosition( new Vec2( -300, -740 ) );
 		robotPhil.setPolarOrientation( Math.PI  ); // S
 		
 		int n = 100;
 		double dA = 0.1;
 		for( int i = 0; i < n; i++ )
 		{
 			double ksIR = robotKS.getIRReading( 0 );
 	 		double pIR = ir_reading( robotPhil.getPolarOrientation() );
 	 		if( Math.abs( ksIR - pIR ) > 1.5 )
 	 		{
 	 			System.out.println( ksIR + " " + pIR + " " + i + " " + robotKS.getPolarOrientation());
 	 		}
 	 		assertEquals( ksIR, pIR, 1.5 );
 	 		
 	 		
 	 		robotKS.setPolarOrientation( ( robotKS.getPolarOrientation() + dA ) % ( Math.PI * 2 ) );
 	 		robotPhil.setPolarOrientation( this.ForceAngleInCircle( robotPhil.getPolarOrientation() - dA ) );
 		}
 		
	}
	
	/*******************************************************************************/
	// find the reading for a singel IR sensor
	// 0- 3500 int val
	// a is angle of ray thro sensor from centre robot
	int ir_reading(double a)
	{
		double sens_x,sens_y,spleft,spright,val=0,dav,ang;
		int i,count;
		boolean cflag=false;
		Vec2 p1,p2,p3;

		sens_x = robotPhil.getPosition().x + ROB_TOPRAD*Math.sin(a); // posn of sensor
		sens_y = robotPhil.getPosition().y + ROB_TOPRAD*Math.cos(a);

		spleft =  IRrayAngs[0]; // IRSPREAD*(-1.0/2);  // relative angles of bounding rays of IR beam, relative to central ray
		spright =  IRrayAngs[4]; //IRSPREAD/2;
		p1 = new Vec2( sens_x, sens_y );
		if(!ray_hit(p1,a,spleft) && !ray_hit(p1,a,spright)) // beam misses everything
			val =0;
		
		else{ 
			p2 = ray_end(sens_x,sens_y,a,spleft);
			p3 = ray_end(sens_x,sens_y,a,spright );
			Ray_HitNearestResult r1 = ray_hitNearest(p1,p2);
			Ray_HitNearestResult r2 = ray_hitNearest(p1,p3);
			
			if( r1.hit() && r2.hit() && r1.getWall() == r2.getWall() ) // bounding rays (of IR beam) both hit same wall as nearest obj
				{val = FullIRval(p1,a,r1.getWall());cflag=true;}
			if(!cflag) // previous cond not true
				{// complicated .. find which rays do hit .. proportion that do * fullval
				dav=0;  // to calc av dist
				count=0;
				for(i=0;i<5;i++) //LOOP through 5 rays per beam
					{ang= IRrayAngs[i]; // angles of rays relative to centre of beam
					p2 = ray_end(sens_x,sens_y,a,ang);   //p2 = end point of next ray, left to right
					r1 = ray_hitNearest(p1,p2);
					if(r1.hit())  // intersects, finds nearest intersection point (in case more than one wall on ray path) 
						{ count++; dav += r1.getDistance();
						}
				}
		dav/= count;  // average value of d1, dist along ray to wall
		val= (count/5.0)*IRval(dav);  // proportional to num rays that hit (i.e proportion of beam)		
		}
		}
		
		// val += uniform_noise(IRNOISE); // NO noise for testing
		if(val <0) val=0;  // can only be positive
		return ((int) val);
	}
	
	double FullIRval(Vec2 p1,double a, Wall wall)
	{
		Vec2 p2,p;
		double d;

		p2 = ray_end(p1.x,p1.y,a,0); // ray end along angle a;
		p = PhilGeometry.intersection_point(p1, p2, wall.start, wall.end);  // find intersection point mid ray to wall along ray
		d=dist(p1,p);
	//printf("d; %f   ",d); debug
		return(IRval(d));
	}
	
	double IRval(double d)
	{double k;

	//printf("d:   %f   ",d);  debug
		k= -1*(d/8.5)*(d/8.5);
		return(IRCOEFF*3371*Math.exp(k));
	}
	
	/***********************************************************/
	// simple hit test, does ray hit any walls?
	// p1 is posn of sensor, a1 is rob orientation, a2 is rel. angle of ray
	boolean ray_hit(Vec2 p1, double a1, double a2)
	{
		boolean iflag=false;
		int i=0;
		Vec2 p2 = ray_end(p1.x,p1.y,a1,a2 ); // find end of ray at angle a2

		while(!iflag && i< WORLDSIZE && i < world.length)
		{
			if(PhilGeometry.intersect(p1,p2,world[i].start,world[i].end)) // line segmentp1,p2 is ray, does intersect with a wall?
				iflag=true;
			i++;
		}

		return iflag;
	}
	
	/*******************************************************************************/
	// find end of ray of a standard length from sensor end at a partic angle
	// sensor posn is x,y a1 id angle of sensor, a2 id angle of ray relative to sensor mid (so anti-clockwise is neg)
	// p is  posn of ray end 
	Vec2 ray_end(double x, double y, double a1, double a2)
	{
		double a;

		a = ForceAngleInCircle(a1+a2);
		return new Vec2( x + RAYLEN*Math.sin(a), y + RAYLEN*Math.cos(a) );

	}
	
	double ForceAngleInCircle(double angle){
	    if (angle >= TWOPI) return ForceAngleInCircle(angle-TWOPI);
	    else if (angle < 0.0) return ForceAngleInCircle(angle+TWOPI);
	    else return angle;
	}
	
	/*******************************************************************************/
	//does a ray hit any walls
	// ray is line segment [p1,p2], w1 is index of world for wall hit by ray with shortest  distance d
	Ray_HitNearestResult ray_hitNearest(Vec2 p1, Vec2 p2)
	{
		Vec2 p;
		boolean iflag = false;
		int i=0,mini=-1;
		double dmin=200;
		double dl;
		Wall rv = null;
		

		while(i< WORLDSIZE && i < world.length )
		{
			if(PhilGeometry.intersect(p1,p2,world[i].start,world[i].end)) // line segmentp1,p2 is ray, does intersect with the wall?
				{iflag=true;
				p = PhilGeometry.intersection_point(p1, p2, world[i].start, world[i].end); // find intersection point
				 dl = dist( p, p1 );
				 if(dl<dmin)  //if min dist so far, store as min
					 {dmin=dl;
					  mini=i;
						}
				 }
			i++;
		}
		if( mini >=0 && mini < world.length ) rv=world[ mini ];   //to return min distance and index of with wall with min dist. by setting through pointers in arguments
		return new Ray_HitNearestResult( rv, dmin );
	}
	
	class Ray_HitNearestResult
	{
		protected Wall wall;
		protected double distance;
		protected boolean hit;
		public Ray_HitNearestResult( Wall wall, double dist ){ this.wall = wall; distance = dist; }
		public Wall getWall( ){ return wall; }
		public double getDistance() { return distance; }
		public boolean hit(){ return wall != null; }
	}
	
	// distance a to b, pythag
	double dist( Vec2 a,  Vec2 b)
	{
		double r, DX, DY;
		DX= b.x - a.x;
		DY = b.y - a.y; 
		//return sqrt(((b.x - a.x)*(b.x - a.x)) + ((b.y - a.y)(b.y - a.y)));
		r= Math.sqrt(DX*DX + DY*DY);
		return(r);

	}
	
	public static class Wall
	{
		public Vec2 start;
		public Vec2 end;
		
		public Wall( Vec2 s, Vec2 e){ start = s; end = e; }
	}
}
