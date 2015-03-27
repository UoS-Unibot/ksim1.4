package org.evors.rs.kjunior;

import java.util.Hashtable;
import java.util.Random;

import org.evors.core.geometry.Circle;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulatedRobotBody;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.core.EvoRSLib;

/**
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulatedKJunior extends SimulatedRobotBody {

    private final double AXLE_WIDTH = 10; //equivalent to WHEEL_SEP in Phil's code
    private final double MOTOR_NOISE = 0.4; //noise to add to motor signals
    private final int NUM_IRs = 6;
    private final double[][] ROB_CTRL_INPUT_RANGES = { { -20, 20 } };
    private final double[][] ROB_CONTROLLER_INPUT_RANGES = { { 0, 3500 } };
    private final double MAX_IR_LENGTH = 25;

    public static final Random rand = EvoRSLib.random;

    private final double maxIRLength;
    private final double topRadius;
    private final double[] irAngles;

    protected double V,D,maxIR;
    
    public SimulatedKJunior(SimulationWorld world,
            double timeStepLength) {
        super(world, Circle.getFromCenter(Vec2.ZERO, 6.5), timeStepLength); //radius of 6.5cm
        maxIRLength = MAX_IR_LENGTH;
        topRadius = 5.75;
        irAngles = new double[] {0.785,0.349,0,5.934,5.498,3.142};
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
        double mL = controlInputs[0], mR = controlInputs[1],
                vL = convertSpeed(mL), vR = convertSpeed(mR),
                forwardV = (vL + vR) / 2,
                angularV = (vR - vL) * getTimeStep() / AXLE_WIDTH;
        V += Math.abs( vL ) + Math.abs( vR );
        D += Math.abs( vR - vL );
        //we've converted to cm/s forward velocity and r/s angular, let the superclass deal with odometry
        super.step(forwardV, angularV);
    }

    public Vec2 getIRBase(double angle) {
        return getPosition().translatePolar(getHeading() + angle, topRadius);
    }

    public double getIRReading(double angle) {
        return new KJuniorIRBeam( getIRBase(angle), angle, getWorld() ).getReading();
    }

    public double[] getInput() {
        double[] input = new double[NUM_IRs];
        for (int i = 0; i < NUM_IRs; i++) {
            input[i] = getIRReading(irAngles[i]);
            maxIR = Math.max( maxIR, input[i]);
        }
        return input;
    }

    private double convertSpeed(double x) {
        double y = 0;

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
    	rv.put("D", new Double(D));
    	rv.put("V", new Double(V));
    	double i = ( maxIR / ROB_CONTROLLER_INPUT_RANGES[ 0 ][ 1 ] );
    	rv.put("i", new Double( i ));
    	D = V = maxIR = 0;
    	return rv;
    }
}
