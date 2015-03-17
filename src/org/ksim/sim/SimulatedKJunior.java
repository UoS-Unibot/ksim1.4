/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksim.sim;

import java.awt.BasicStroke;
import java.awt.Stroke;
import org.ksim.geometry.Circle;
import org.ksim.geometry.Line;
import org.ksim.geometry.Vec2;

/**
 *
 * @author miles
 */
public class SimulatedKJunior extends SimulatedRobotBody {

    private static final Stroke bstroke = new BasicStroke(0.01f);

    private final double maxIRLength = 0.5f;
    private final int NUM_IRs = 6;
    private final double radius;
    private final double[] irAngles;

    public SimulatedKJunior(SimulationWorld world,
            double timeStepLength) {
        super(world, Circle.getFromCenter(Vec2.ZERO, 0.065), timeStepLength);
        //maxIRLength =  world.getBounds().getNorm();
        radius = 0.065f;
        irAngles = new double[NUM_IRs];
        for (int i = 0; i < NUM_IRs; i++) {
            irAngles[i] =  (i * (2 * Math.PI / NUM_IRs));
        }
    }

    
    public void step(double velocity, double angularVelocity) {
        super.step(velocity, angularVelocity);
    }

    public Vec2 getIRBase(double angle) {
        return getShape().getCenter().add(new Vec2(Math.
                cos(angle), Math.sin(angle)).scalar(radius));
    }

    private Line getIRLine(double angle) {
        return Line.fromPolarVec(getIRBase(angle), angle, maxIRLength);
    }

    public double getIRReading(double angle) {
        return getWorld().traceRay(getIRLine(angle));
    }

    
    public double[] getInput() {
        double[] input = new double[NUM_IRs];
        for (int i = 0; i < NUM_IRs; i++) {
            input[i] = getIRReading( (irAngles[i] + getHeading()));
        }
        return input;
    }

    
    

}
