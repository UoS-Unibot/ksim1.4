package org.evors.rs.sandpit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.evors.core.EvoRSLib;
import org.evors.core.RunController;
import org.evors.core.geometry.Vec2;
import org.evors.rs.kjunior.SimulatedKJunior;
import org.evors.rs.sim.ctrnn.CTRNN;

/**
 * Renders a single Trial on a Canvas.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class TrialViewer extends SandPitCanvas implements Runnable {

    RunController controller;
    private boolean simulationLoaded = false; //whether a simulation has been loaded.
    private SimulatedKJunior robot; //robot to render.
    private PathTracer path; //path of robot
    public static long DELAY = Math.round(1000 / 60); //delay in milliseconds between frames.
    private volatile boolean simulationStopped = false; //whether the simulation has been stopped.
    private float time = 0; //current timestep of the simulation in seconds.
    protected BufferStrategy buffer;

    public TrialViewer() {
        setSize(new Dimension(800, 600));
    }

    /**
     * @return whether a simulation has been loaded.
     */
    public boolean isSimulationLoaded() {
        return simulationLoaded;
    }

    /**
     * Loads the specified simulation.
     *
     * @param controller RunController of simulation to load
     */
    public void loadSimulation(RunController controller) {
        time = 0;
        simulationStopped = true;
        this.controller = controller;
        robot = (SimulatedKJunior) this.controller.getRobot();
        world = robot.getWorld();
        path = new PathTracer(robot.getPosition());
        simulationLoaded = true;
    }

    private void step() {
        controller.step();
        path.step(robot.getPosition());
        time += controller.getTimeStep();
        if( !controller.isLive() ) simulationStopped = true;
    }

    public void addNotify() {
        super.addNotify();
        Thread canvas = new Thread(this);
        canvas.start();
    }

    /**
     * Sets delay between frames relative to timestep.
     *
     * @param newDelay
     */
    public void setDelay(double newDelay) {
        DELAY = Math.round(1000 / (60 * newDelay));
    }

    /**
     * Runs the simulation and renders it.
     */
    public void run() {
        postInitialise();
        long beforeTime;
        long timeDiff;
        long sleep;
        //Draw and render world as it currently is
        draw();
        render();
        if (!simulationLoaded) {
            return;
        }
        beforeTime = System.currentTimeMillis();
        while (!simulationStopped) {
            if (controller.isLive()) {
                step();
            }
            draw();
            render();
            //maintaining a consistent frame rate
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
                simulationStopped = true;
            }
            beforeTime = System.currentTimeMillis();
        }

    }

    /**
     * Called after initialisation to set the buffer strategy, as this requires
     * a valid peer (e.g. parent JPanel).
     */
    public void postInitialise() {
        createBufferStrategy(3);
        buffer = getBufferStrategy();
    }

    /**
     * Stops the simulation.
     */
    public void stop() {
        simulationStopped = true;
    }

    /**
     * Starts the simulation.
     */
    public void start() {
        simulationStopped = false;
        new Thread(this).start();
    }

    /**
     * Draws an array of strings onto the graphics context.
     *
     * @param g2 Graphics context.
     * @param strings Array of strings; each element is a single line.
     */
    public void drawText(Graphics2D g2, String[] strings) {
        FontMetrics fm = g2.getFontMetrics();
        float x1 = 40, y = 530;
        int dx = 0;
        for (int i = 0; i < strings.length; i++) {
            int width = fm.stringWidth(strings[i]);
            if (width > dx) {
                dx = width;
            }
        }
        g2.setColor(new Color(0.6f, 0.6f, 0.6f, 0.6f));
        g2.fillRect((int) x1, (int) y - fm.getHeight(), dx + 5, fm.getHeight()
                * strings.length + 5);
        g2.setColor(Color.red);
        for (int i = 0; i < strings.length; i++) {
            g2.drawString(strings[i], 40, y);
            y += fm.getHeight();
        }

    }

    /**
     * Draws the simulation.
     */
    public void draw() {
    	DecimalFormat timeF = new DecimalFormat("#0.00" );
    	DecimalFormat posF = timeF;
    	DecimalFormat headF = timeF;
    	
        if (buffer == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) buffer.getDrawGraphics();
        camera.setWindowSize(new Vec2(this.getWidth(), this.getHeight()));

        //clear background to white
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        //set the camera transform
        AffineTransform prevTrans = g2.getTransform();
        g2.setTransform(camera.getTransform());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //draw grid first, then other world elements
        grid.draw(g2);
        if (world != null) {
            SandpitRenderer.drawWorld(g2, world);
        }
        if (path != null) {
            path.draw(g2);
        }
        if (robot != null) {
            SandpitRenderer.drawRobot(g2, robot);
        }

        String controllerStr = "";
        if(controller.getController() instanceof CTRNN) {
            controllerStr = "Neurons: " + ((CTRNN) controller.
                            getController()).
                            getNeurons().toString();
        } else
            controllerStr = "Control outputs: " + EvoRSLib.arrayToString( controller.getController().getControlOutputs() );
        
        //set previous transform
        g2.setTransform(prevTrans);
        drawText(
                g2,
                new String[]{
                    "Time: " + timeF.format(time),
                    "Robot pos: " + posF.format(robot.getPosition().x) + ", " + posF.format(robot.getPosition().y),
                    "Heading: " + headF.format(robot.getHeading()),
                    "Input: " + EvoRSLib.arrayToStringInputs( robot.getInput() ),
                    controllerStr
                }
        );
        
    }
   

    /**
     * Displays the current contents of the buffer. Should be called after
     * draw()
     */
    public void render() {
        if (buffer != null) {
            buffer.show();
        }
    }

    /**
     * Called when a swing repaint event comes in.
     *
     * @param grphcs
     */
    public void paint(Graphics grphcs) {
        draw();
        render();
    }
    
	public void setX(double x) {
		robot.setPosition( new Vec2( x, robot.getPosition().y ) );
		draw();
		repaint();
	}

	public void setY(double y) {
		robot.setPosition( new Vec2( robot.getPosition().x, y ) );
		draw();
		repaint();
	}

	public void setHeading(double heading) {
		robot.setHeading( EvoRSLib.headingToPolar(heading));
		draw();
		repaint();
	}
}
