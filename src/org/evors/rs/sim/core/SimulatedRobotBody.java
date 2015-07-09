package org.evors.rs.sim.core;

import org.evors.core.EvoRSLib;
import org.evors.core.PositionOrientationSource;
import org.evors.core.RobotBody;
import org.evors.core.geometry.Shape2D;
import org.evors.core.geometry.Vec2;

public abstract class SimulatedRobotBody implements RobotBody, PositionOrientationSource {

    private final double timeStepLength;
    private Vec2 position;
    private double heading;
    private SimulationWorld world;
    private final Shape2D shape;
    private boolean live = true;
    protected boolean graphingEnabled = false;
    protected StringBuffer graphData = new StringBuffer();

    public SimulatedRobotBody(SimulationWorld world, Shape2D shape,
            double timeStepLength) {
        this.timeStepLength = timeStepLength;
        position = Vec2.ZERO;
        heading = 0;
        this.world = world;
        this.shape = shape;
    }

    
    public boolean isLive() {
        return live;
    }

    public Vec2 getPosition() {
        return position;
    }
    
    public void setPosition( Vec2 position )
    {
    	this.position = position;
    	shape.setCenter( position );
    	live = true;
    }

    /**
     * This is actually in polar coordinates (confusing)
     * @return
     */
    public double getHeading() {
        return heading;
    }
    
    /**
     * Orientation in standard "heading" nomenclature
     */
    public double getOrientation()
    {
    	return EvoRSLib.polarToHeading( getHeading() );
    }
    
    public void setHeading( double heading )
    {
    	this.heading = heading;
    }

    public Shape2D getShape() {
        return shape;
    }

    public abstract void step(double[] controlInputs);

    
    
    public void step(double velocity, double angularVelocity) {
        if (!live) {
            return;
        }
        //Calculate movement vector
        double dist = velocity * timeStepLength;
        Vec2 changeV = new Vec2(dist * Math.cos(heading), dist * Math.
                sin(heading));
        shape.translate(changeV);
        position = position.add(changeV);
        
        if( this.graphingEnabled ) this.graphData.append( position.x + " " + position.y + "\n" );

        //Calculate actual rotation
        double changeA = (angularVelocity * timeStepLength) % (2 * Math.PI);
        shape.rotate(changeA);
        heading += changeA;
        
        world.checkCollisions(this);
    }

    public void doCollision(Shape2D obj) {
        live = false;
    }

    public SimulationWorld getWorld() {
        return world;
    }

    public double getTimeStep() {
        return timeStepLength;
    }

    public void setWorld( SimulationWorld world )
    {
    	this.world = world;
    }
    
    public void setGraphing( boolean graphingEnabled ){ this.graphingEnabled = graphingEnabled; }
    
    public String getGraphData()
    {
    	return graphData.toString();
    }
    
    public void newRun()
    {
    	graphData = new StringBuffer();
    }

}
