package org.ksim;

import org.ksim.geometry.Rectangle;
import org.ksim.geometry.Vec2;
import org.ksim.sim.CTRNN;
import org.ksim.sim.CTRNNController;
import org.ksim.sim.RunController;
import org.ksim.sim.SimulatedKJunior;
import org.ksim.sim.SimulationWorld;

/**
 * An example of how the simulation is set up and run.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class example {

    public static void main(String[] args) {
        System.out.println("Starting simulation...\n");

        double timestep = 1.0 / 60.0; //timestep used for integration of velocity etc.

        /**
         * set up the world - each world has a bounding box with specified width
         * and height of 4 lines. instantiation creates these 4 lines centred
         * around the origin
         */
        SimulationWorld world = new SimulationWorld(new Vec2(1.5, 1.5));

        /**
         * we'll add a few objects to the world. Here rectangles are used,
         * although any of the other geometry classes are okay
         */
        world.createWorldObject(Rectangle.createFromCenter(new Vec2(1, 1),
                new Vec2(0.5, 0.5), 0));
        world.createWorldObject(Rectangle.createFromCenter(new Vec2(-1, -1),
                new Vec2(0.5, 0.5), 0));
        world.createWorldObject(Rectangle.createFromCenter(new Vec2(-1, 1),
                new Vec2(0.5, 0.5), 0));

        //create robot and controller
        SimulatedKJunior robot = new SimulatedKJunior(world, timestep);
        CTRNNController nn = getController();

        //this is the class that controls both and passes velocities/sensory input between them
        RunController sim = new RunController(nn, robot);

        //Now we begin our main loop. Loop terminates after 30 seconds or if the robot crashes into something
        for (int i = 0; i < 30f * 60f & sim.isLive(); i++) {
            //this method updates the controller and the robot
            sim.step();

            //print a few bits of data - forgive the verbosity, I'm really missing String.format()!!
            System.out.print("Robot pos: {");
            System.out.print(robot.getPosition().x);
            System.out.print(",");
            System.out.print(robot.getPosition().y);
            System.out.print("}, vel: ");
            System.out.print(nn.getVelocity());
            System.out.print(", angvel: ");
            System.out.print(nn.getAngularVelocity());
            System.out.println("");

            System.out.print("\t inputs: {");
            for (int j = 0; j < robot.getInput().length; j++) {
                System.out.print(robot.getInput()[j]);
                if (j < robot.getInput().length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println("}");
        }
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

        CTRNN.CTRNNNeuron[] neurons = new CTRNN.CTRNNNeuron[]{
            //sensory
            new CTRNN.CTRNNNeuron(genes[0], genes[1], genes[2], 
                    new double[]{
                        0,0, 0, 0, 0, 0, genes[3], genes[4]
                    }),
            new CTRNN.CTRNNNeuron(genes[0], genes[1], genes[2], 
                    new double[]{
                        0,0, 0, 0, 0, 0, genes[5], genes[6]
                    }),
            new CTRNN.CTRNNNeuron(genes[0], genes[1], genes[2], 
                    new double[]{
                        0, 0, 0, 0, 0, 0, genes[7], genes[8]
                    }),
            new CTRNN.CTRNNNeuron(genes[0], genes[1], genes[2], 
                    new double[]{
                        0, 0, 0, 0, 0, 0, genes[8], genes[7]
                    }),
            new CTRNN.CTRNNNeuron(genes[0], genes[1], genes[2], 
                    new double[]{
                        0, 0, 0, 0, 0, 0, genes[6], genes[5]
                    }),
            new CTRNN.CTRNNNeuron(genes[0], genes[1], genes[2], 
                    new double[]{
                        0, 0, 0, 0, 0, 0, genes[4], genes[3]
                    }),
            //motors with recurrent connections
            new CTRNN.CTRNNNeuron(genes[9], genes[10], genes[11],
                    new double[]{
                        0, 0, 0, 0, 0, 0, genes[12], genes[13]
                    }),
            new CTRNN.CTRNNNeuron(genes[9], genes[10], genes[11],
                    new double[]{
                        0, 0, 0, 0, 0, 0, genes[13], genes[12]
                    })
        };

        int[] sensorIndices = new int[]{0, 1, 2, 3, 4, 5}; //first six neurons are sensors

        return new CTRNNController(neurons, sensorIndices,
                0.1, 0.6, 6, 7);
    }
}
