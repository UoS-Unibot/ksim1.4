package org.evors.rs.sim.core;

import org.evors.core.IRobotBody;
import org.evors.core.geometry.Shape2D;
import org.evors.core.geometry.Vec2;

public abstract class SimulatedRobotBody implements IRobotBody {

    private final double timeStepLength;
    private Vec2 position;
    private double heading;
    private final SimulationWorld world;
    private final Shape2D shape;
    private boolean live = true;

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
    }

    public double getHeading() {
        return heading;
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

    
    

}
