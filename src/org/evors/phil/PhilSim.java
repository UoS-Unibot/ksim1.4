package org.evors.phil;

import java.util.Random;

import org.evors.core.geometry.Vec2;

public class PhilSim {
	
	public static final double ROB_TOPRAD = 5.75;
	public static final double[] IRANGS = {5.498,5.934,0,0.349,0.785,3.142};  
	public static final double[] IRrayAngs = {-0.437,-0.218,0,0.218,0.437}; 
	public static final int WORLDSIZE = 10;
	public static final double TWOPI = Math.PI * 2;
	public static final double RAYLEN = 25;
	public static final double IRCOEFF = 1.0;
	public static final double WHEEL_SEP = 10;
	public static final int NUM_IR = 6;
	public static final double ROBRAD = 6.5;
	public static  double IRNOISE = 50;
	public static  double MNOISE = 0.4;
	public Wall[] world = { new Wall( new Vec2( 0, 0 ), new Vec2( 150,0 ) ),
							 new Wall( new Vec2( 150, 0 ), new Vec2( 150, 150 ) ),
							 new Wall( new Vec2( 150, 150 ), new Vec2( 0, 150 ) ),
							 new Wall( new Vec2( 0, 150), new Vec2( 0, 0 )),
							 new Wall( new Vec2( 70, 30), new Vec2( 70,120 )),
							 new Wall( new Vec2( 50, 25), new Vec2( 70,30)),
							 new Wall( new Vec2( 70, 120), new Vec2( 90, 122))};

	protected PhilRobot robot = new PhilRobot();
	protected static Random rnd = new Random();
	
	public PhilSim() {
		// TODO Auto-generated constructor stub
	}
	
	public PhilRobot getRobot(){ return robot; }

	double rob_speed(double x)
	{
		double y = 0;

		//empirically determined non linear translation function, 2 linear sections
		if(x <= 15 && x>= -15)
			y= 0.6*x;
		else if(x > 15 || x < -15)
			y=1.2*x -9;

		y += uniform_noise( MNOISE );
		return y;
	}
	
	public Vec2 move_robot( double[] msig)
	{
		double DT = 0.1;
		double ml = msig[ 0 ], mr = msig[ 1 ];
		
		double ls,rs, dx, dy,dtheta,v,oldx,oldy,oldtheta,k;

		ls = rob_speed(ml);  // convert to actuall cm/s speeds of rob
		rs = rob_speed(mr);
		v = (ls + rs)/2;
		dx = v*Math.sin( robot.orientation )*DT;  // change in posn from decomposed linear motion
		dy= v*Math.cos( robot.orientation )*DT; 
		
		Vec2 rv = new Vec2( robot.position.x + dx , robot.position.y + dy );
		dtheta = (ls - rs)*DT/WHEEL_SEP;    // change in orientation from decomposed rotational movement  v=wr etc

		robot.orientation = ForceAngleInCircle( robot.orientation + dtheta );
		robot.position = rv;
		return rv;
	}
	
	public void readIRs( )
	{
		int i;
		double ir_ang,v,ang;

		for(i=0;i<NUM_IR;i++)
		{ir_ang = IRANGS[i];   // angle of IR sensor relative to mid point of robot at front, clockwise
		v = ir_ang + robot.orientation;  // angle of mid ray from sensor, clockwise from due north
		ang = ForceAngleInCircle(v);  // makes sure stays in range [0,twopi] to avoid trig calc errors
		robot.IRvals[i]=ir_reading(ang);
		}

	}
	
	/*******************************************************************************/
	// find the reading for a singel IR sensor
	// 0- 3500 int val
	// a is angle of ray thro sensor from centre robot
	public int ir_reading(double a)
	{
		double sens_x,sens_y,spleft,spright,val=0,dav,ang;
		int i,count;
		boolean cflag=false;
		Vec2 p1,p2,p3;

		sens_x = robot.position.x + ROB_TOPRAD*Math.sin(a); // posn of sensor
		sens_y = robot.position.y + ROB_TOPRAD*Math.cos(a);

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
		
		val += uniform_noise(IRNOISE); // NO noise for testing
		if(val <0) val=0;  // can only be positive
		return ((int) val);
	}
	
	double FullIRval(Vec2 p1,double a, Wall wall)
	{
		Vec2 p2,p;
		double d;

		p2 = ray_end(p1.x,p1.y,a,0); // ray end along angle a;
		p = PhilGeometry.intersection_point(p1, p2, wall.start, wall.end);  // find intersection point mid ray to wall along ray
		d=PhilGeometry.dist(p1,p);
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
	
	public static double ForceAngleInCircle(double angle){
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
				 dl = PhilGeometry.dist( p, p1 );
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
	
	// check if robot collides with any walls

	public boolean collision( )
	{
		int i=0;
		boolean cflag=false;

		while(!cflag && i< WORLDSIZE && i < world.length)
		{
			if(wall_collision(world[i].start,world[i].end))
				cflag=true;
			i++;
		}
		return cflag;

	}
	
	// check if robot has collided with an individual wall
	//if shotest dist from robot centre to wall <= robot radius

	boolean wall_collision(Vec2 p1, Vec2 p2)
	{
		if(PhilGeometry.shortest_dist(p1,p2,robot.position) <= ROBRAD)
			return true;
		return false;
	}
	
	// return value in range [-val,val] uniformly distributed
	static double uniform_noise(double val)
	{
		//return(drand48()*val*2 -val); // drand48 no longer in VS 2010
		return( rnd.nextDouble()  *val*2 -val);
	}
	
	public static class Wall
	{
		public Vec2 start;
		public Vec2 end;
		
		public Wall( Vec2 s, Vec2 e){ start = s; end = e; }
	}
	
	public static class PhilRobot
	{
		public double[] IRvals = new double[ NUM_IR ];
		
		public Vec2 position;
		public double orientation;
	}
	
}
