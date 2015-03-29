package org.evors.core;


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
public interface RobotController {

    /**
     * Integrates the controller one time step by providing a RobotInput
     * containing either real world or calculated simulation values containing
     * range finder and sonar data etc.
     *
     * @param input RobotInput
     */
    public void step(double[] input);
    
    /**
     * Gets the array of control outputs to send to the real robot body.
     * @return An array of control outputs.
     */
    public double[] getControlOutputs();
}
