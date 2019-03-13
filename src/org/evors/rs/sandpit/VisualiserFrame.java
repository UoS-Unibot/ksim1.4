package org.evors.rs.sandpit;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.evors.core.RobotController;
import org.evors.core.RunController;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Vec2;
import org.evors.rs.kjunior.PhilController;
import org.evors.rs.kjunior.SimulatedKJunior;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.ctrnn.CTRNN;
import org.evors.rs.sim.ctrnn.CTRNNController;

/**
 *
 * @author miles
 */
public class VisualiserFrame extends JFrame implements VisualiserListener {

    private final JPanel container = new JPanel();
    private final TrialViewer trialv = new TrialViewer();
    private final VisualiserControls controls = new VisualiserControls();
    private final RunController rc;

    public VisualiserFrame(RunController rc) throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("KJunior Simulator");
        setSize(800, 600);
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(container);

        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        this.rc = rc;
        trialv.loadSimulation(rc);
        trialv.stop();
        container.add(trialv);
        container.add(controls);
        controls.setListener(this);
        pack();
    }

    public void setRunning(boolean isRunning) {
        if (isRunning) {
            trialv.start();
        } else {
            trialv.stop();
        }
    }
    
    public void step()
    {
    	trialv.startSingleStep();
    }

    public void restart() {
        trialv.stop();
        trialv.loadSimulation(rc);
    }

    public void speedChanged(double newSpeed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {
        VisualiserFrame frame = new VisualiserFrame(getDefaultSimulation());
        frame.setVisible(true);
    }

    public static RunController getDefaultSimulation() {
        /**
         * set up the world - each world has a bounding box with specified width
         * and height of 4 lines. instantiation creates these 4 lines centred
         * around the origin. Units are in cm as in Phil's simulation.
         */
        SimulationWorld world = new SimulationWorld(new Vec2(150, 150));

        /**
         * we'll add a few objects to the world. Here rectangles are used,
         * although any of the other geometry classes are okay
         */
//        world.createWorldObject(Rectangle.createFromCenter(new Vec2(30, 50),
//                new Vec2(20, 20), 0));
//        world.createWorldObject(Rectangle.createFromCenter(new Vec2(-55, -65),
//                new Vec2(20, 20), 0));
//        world.createWorldObject(Rectangle.createFromCenter(new Vec2(-20, 40),
//                new Vec2(20, 20), 0));
        world.createWorldObject(Line.fromCoords(-25, -50, -5, -45));
        world.createWorldObject(Line.fromCoords(-5, -45, -5, 45));
        world.createWorldObject(Line.fromCoords(-5, 45, 15, 47));

        //create robot and controller
        SimulatedKJunior robot = new SimulatedKJunior(world, 0.1);

        robot.setPosition(new Vec2(5, -35));
        robot.setPolarOrientation(Math.PI / 2); // N

        //CTRNNController nn = getController();
        RobotController nn = new PhilController();

        //this is the class that controls both and passes velocities/sensory input between them
        return new RunController(nn, robot);
    }

    static CTRNNController getController() {
        /**
         * This uses my CTRNN controller, to implement another controller use
         * the IRobotController interface.
         */

        //use a random genome
        double[] genes = new double[14];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.random();
        }

        double gain = 5;
        double gainM = 3;
        double bias = 3;
        double tau = 2;

        CTRNN.CTRNNNeuron[] neurons = new CTRNN.CTRNNNeuron[]{
            //sensory
            new CTRNN.CTRNNNeuron(genes[0] * gain, genes[1] * bias, genes[2]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[3], genes[4]
            }),
            new CTRNN.CTRNNNeuron(genes[0] * gain, genes[1] * bias, genes[2]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[5], genes[6]
            }),
            new CTRNN.CTRNNNeuron(genes[0] * gain, genes[1] * bias, genes[2]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[7], genes[8]
            }),
            new CTRNN.CTRNNNeuron(genes[0] * gain, genes[1] * bias, genes[2]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[8], genes[7]
            }),
            new CTRNN.CTRNNNeuron(genes[0] * gain, genes[1] * bias, genes[2]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[6], genes[5]
            }),
            new CTRNN.CTRNNNeuron(genes[0] * gain, genes[1] * bias, genes[2]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[4], genes[3]
            }),
            //motors with recurrent connections
            new CTRNN.CTRNNNeuron(genes[9] * gainM, genes[10] * bias, genes[11]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[12], genes[13]
            }),
            new CTRNN.CTRNNNeuron(genes[9] * gainM, genes[10] * bias, genes[11]
            * tau,
            new double[]{
                0, 0, 0, 0, 0, 0, genes[13], genes[12]
            })
        };

        int[] sensorIndices = new int[]{0, 1, 2, 3, 4, 5}; //first six neurons are sensors

        return new CTRNNController(neurons, sensorIndices,
                0.1, 0.6, 6, 7);
    }

	public void setX(double x) {
		trialv.setX(x);
		
	}

	public void setY(double y) {
		trialv.setY(y);
	}

	public void setHeading(double heading) {
		trialv.setHeading(heading);
	}
}
