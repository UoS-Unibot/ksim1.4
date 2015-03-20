package org.evors.core;

/**
 * The RunController controls interaction between the controller and the robot,
 * passing control and sensory data. In turn the RunController is controlled by
 * execution of the step() function so that this can be run in real time, or at
 * maximum speed.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class RunController {

    private final IRobotController controller;
    private final IRobotBody robot;
    private final double timeStep;
    private boolean live = true;

    /**
     * Creates a new RunController with specified controller and robot, using
     * the robot's timestep.
     *
     * @param controller IRobotController to get control data from.
     * @param robot IRobotBody to send commands to.
     */
    public RunController(IRobotController controller, IRobotBody robot) {
        this(controller, robot, robot.getTimeStep());
    }

    /**
     * Creates a new RunController with specified controller, robot and
     * timestep.
     *
     * @param controller IRobotController to get control data from.
     * @param robot IRobotBody to send commands to.
     * @param timeStep Timestep in seconds to use.
     */
    public RunController(IRobotController controller, IRobotBody robot,
            double timeStep) {
        this.controller = controller;
        this.robot = robot;
        this.timeStep = timeStep;
    }

    /**
     * Updates the RunController, stepping the IRobotController with the
     * IRobotBody's sensory data, then sending velocities based on the
     * IRobotController's output.
     */
    public void step() {
        controller.step(robot.getInput());
        robot.step(controller.getControlOutputs());
        live = robot.isLive();
    }

    /**
     * Whether the robot is live.
     *
     * @return
     */
    public boolean isLive() {
        return live;
    }

    /**
     * The IRobotController.
     *
     * @return
     */
    public IRobotController getController() {
        return controller;
    }

    /**
     * The IRobotBody.
     *
     * @return
     */
    public IRobotBody getRobot() {
        return robot;
    }

    /**
     * The timestep in seconds.
     *
     * @return
     */
    public double getTimeStep() {
        return timeStep;
    }

}
