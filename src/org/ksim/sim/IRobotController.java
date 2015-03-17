package org.ksim.sim;


/**
 * The IRobotController interface abstractly represents a differential drive
 * controller for the robot; the controller is updated every time step by
 * calling step(), with either real or calculated simulation input values. The
 * output of the controller is gained by calling getVelocity() and
 * getAngularVelocity().
 *
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface IRobotController {

    /**
     * Integrates the controller one time step by providing a RobotInput
     * containing either real world or calculated simulation values containing
     * range finder and sonar data etc.
     *
     * @param input RobotInput
     */
    public void step(double[] input);

    /**
     * Gets the current velocity output of the controller in metres/second.
     *
     * @return Current forward velocity in metres/second.
     */
    public double getVelocity();

    /**
     * Gets the current angular velocity output of the controller in
     * radians/second.
     *
     * @return Current angular velocity in radians/second.
     */
    public double getAngularVelocity();
}
