package org.ksim.sim;

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
     * @param velocity Forward velocity of the robot.
     * @param angularVelocity Angular velocity of the robot.
     */
    void step(double velocity, double angularVelocity);

    double getTimeStep();
}
