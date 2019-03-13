package org.evors.rs.kjunior;

import java.util.BitSet;
import java.util.Hashtable;
import java.util.Random;

import org.evors.core.geometry.Circle;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulatedRobotBody;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.vision.ImageSource;
import org.evors.vision.VisualSensorGroup;
import org.evors.core.EvoRSLib;
import org.evors.core.Programmable;

/**
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulatedKJunior extends SimulatedRobotBody implements Programmable {

    private final double AXLE_WIDTH = 10; //equivalent to WHEEL_SEP in Phil's code
    public static double MOTOR_NOISE = 1; //noise to add to motor signals - public for testing (was 0.6)
    private final int NUM_IRs = 6;
    private final double[][] ROB_CTRL_INPUT_RANGES = { { -20, 20 } };
    private final double[][] ROB_CONTROLLER_INPUT_RANGES = { { 0, 3500 },{ 0, 3500 },{ 0, 3500 },{ 0, 3500 },{ 0, 3500 },{ 0, 3500 },{ 0, 1},{0,1},{0,1},{0,1},{0,1} };
    private final double MAX_IR_LENGTH = 25;
    public static final double ROBOT_RADIUS = 6.5;

    protected static final Random rand = EvoRSLib.random;

    private final double topRadius;
    public final double[] irAngles;

    protected double totV,totD,maxIR,cumf,cumvf;
    protected int step;
    
    protected VisualSensorGroup visualSensorGroup;
    
    protected boolean simulateLowMotorNoMovement = true;
    
    protected Object currentSignal = null, chargeSignal = new Integer( 0 );
    
    public SimulatedKJunior(SimulationWorld world,
            double timeStepLength ) {
        super(world, Circle.getFromCenter(Vec2.ZERO, ROBOT_RADIUS ), timeStepLength); //radius of 6.5cm
        topRadius = 5.75;
        irAngles = new double[] {0.785,0.349,0,5.934,5.498,3.142};
    }
    
    public void setVisualSensorGroup( VisualSensorGroup visualSensorGroup )
    {
    	this.visualSensorGroup = visualSensorGroup;
    }

    /**
     * The KJunior takes two motor input signals, a velocity for each of the
     * wheels in the range [-20,20]. This function converts these wheel speeds
     * to forward and angular velocities in cm/s and radians/s respectively and
     * the superclass step function performs odometry.
     *
     * @param controlInputs double array of control inputs, for the K-Junior
     * this is [LeftMotor,RightMotor] in the range [-20,20].
     */
    public void step(double[] controlInputs) {
        //convert motor signals to actual velocities in cm/s
    	if( simulateLowMotorNoMovement && Math.abs( controlInputs[ 0 ] ) < 5 ) controlInputs[ 0 ] = 0;
    	if( simulateLowMotorNoMovement && Math.abs( controlInputs[ 1 ] ) < 5 ) controlInputs[ 1 ] = 0;
    	
        double mL = controlInputs[0], mR = controlInputs[1];
        double vL = convertSpeed(mL), vR = convertSpeed(mR);
        double forwardV = (vL + vR) / 2;
        double angularV = (vR - vL) / AXLE_WIDTH; // Timestep multiplication already done in superclass as this is velocity, that is change.
        
        double instV =  ( Math.abs( vL ) + Math.abs( vR ) ) * getTimeStep();
        double instD = ( Math.abs( vR - vL ) ) * getTimeStep();
        totV += instV;
        totD += instD;
        
        double deltaCumF = instV * ( 1 - Math.sqrt( instD ) ), deltaCumVF = 0;
        if( currentSignal == null || !currentSignal.equals(chargeSignal ) ) { cumf += deltaCumF; }
        
        if( this.referencePosition != null )
        {
        	double startDist = lastSetPosition.distance( referencePosition );
        	double nowDist = getPosition().distance( referencePosition );
        	if( nowDist < topRadius * 2 )
        	{
        		deltaCumVF = 2;
        	}else
        	{
        		deltaCumVF = 1 - ( nowDist / startDist );
        	}
        }
        if( currentSignal == null || currentSignal.equals( chargeSignal ) ){ cumvf += deltaCumVF; }
        
        step++;
        //we've converted to cm/s forward velocity and r/s angular, let the superclass deal with odometry
        
        super.step(forwardV, angularV);
    }

    public Vec2 getIRBase(double angle) {
        return getPosition().translatePolar(getPolarOrientation() + angle, topRadius);
    }

    public double getIRReading(double angle) {
        return new KJuniorIRBeam( getIRBase(angle), getPolarOrientation() + angle, getWorld() ).getReading();
    }
    
    public double[] getVisualSensorReading()
    {
    	return visualSensorGroup.getReadings();
    }

    public double[] getInput() {
    	int inputChannels = NUM_IRs;
    	if( visualSensorGroup != null ) inputChannels += visualSensorGroup.getSensorCount();
        double[] input = new double[ inputChannels ];
        for (int i = 0; i < NUM_IRs; i++) {
            input[i] = getIRReading(irAngles[i]);
            if( currentSignal == null || !currentSignal.equals(chargeSignal ) ) { maxIR = Math.max( maxIR, input[i] ); } 
        }
        if( visualSensorGroup != null )
        {
	        double[] visualSensors = visualSensorGroup.getReadings();
	        for( int i = NUM_IRs; i < input.length; i++ )
	        {
	        	input[i] = visualSensors[ i - NUM_IRs ];
	        }
        }
        return input;
    }

    public double convertSpeed(double x) {
        double y = 0;
        
        if( x * x < 9 ) x = 0; // Values < 3 ignored
        
        if (x <= 15 && x >= -15) {
            y = 0.6 * x;
        } else if (x > 15 || x < -15) {
            y = 1.2 * x - 9;
        }

        y += EvoRSLib.uniformNoise( MOTOR_NOISE );
        return y;
    }

    public double getTopRadius() {
        return topRadius;
    }
    
    public double[][] getRobotControlInputRanges()
    {
    	return ROB_CTRL_INPUT_RANGES;
    }
    
    public double[][] getControllerInputRanges()
    {
    	return ROB_CONTROLLER_INPUT_RANGES;
    }

    public Hashtable getStats()
    {
    	Hashtable rv = new Hashtable();
    	rv.put("D", new Double(totD ));
    	rv.put("V", new Double(totV  ));
    	double i = ( maxIR / ROB_CONTROLLER_INPUT_RANGES[ 0 ][ 1 ] );
    	rv.put("i", new Double( i ));
    	rv.put("f", new Double( cumf ) );
    	rv.put("vf", new Double( cumvf ));
    	rv.put("step", new Integer( step ) );
    	return rv;
    }
    
    public void newRun()
    {
    	cumvf = cumf = totV = totD = maxIR = step = 0;
    	currentSignal = null;
    	super.newRun();
    }

	public void program(BitSet bits) {
		visualSensorGroup.program(bits);
	}
	
    public String toString()
    {
    	StringBuffer rv = new StringBuffer();
    	rv.append( "SimulatedKJunior with\n" );
    	rv.append("\t\tTime Step = " + this.getTimeStep() );
    	rv.append("\n\t\tVisual Sensor Group: " + this.visualSensorGroup );
    	return rv.toString();
    }

	public void signal(Object signal) {
		this.currentSignal = signal;
		// Update last set position
		this.lastSetPosition = this.getPosition();
	}
}
