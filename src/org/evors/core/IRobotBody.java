package org.evors.core;

/**
 * Represents a simulated or real robot that can be controlled by setting
 * velocity and angular velocity at each timestep and provides sensor input.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface IRobotBody {

    /**
     * Gets sensory input data from this robot.
     *
     * @return
     */
    public double[] getInput();

    /**
     * Whether this robot is live and able to move; this can be used to
     * terminate a trial in the simulation e.g. if a collision occurs, or an
     * emergency stop in serial communication.
     *
     * @return
     */
    public boolean isLive();

    /**
     * Updates this robot by one time step. If simulated, odometry will be
     * integrated from the given commands, whilst a real robot will be sent the
     * velocity commands.
     *
     * @param controlInputs an array of doubles with the output of the
     * controller. For a differential drive model, this would normally be {motor
     * left, motor right}.
     */
    public void step(double[] controlInputs);

    /**
     * Ranges that each robot control input expects.
     * Eg. for K-Junior motor actuators: -20 to 20 for both.
     * If only one range specified, all assumed the same.
     * 
     * @return Two dimensional double array.  First index for which control input.
     * Second index 0 for lower and 1 for upper.
     */
    public double[][] getRobotControlInputRanges();
    
    /**
     * Ranges that each robot output (or controller input) generates.
     * Eg. for K-Junior IR sensors: 0 to 3500 for each.
     * If ony one range specified, all assumed equal.
     * 
     * @return Two dimensional double array.  First index for which robot output (controller input).
     * Second index 0 for lower and 1 for upper limit.
     */
    public double[][] getControllerInputRanges();
    
    double getTimeStep();
}
