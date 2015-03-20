/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.ctrnn;

import org.evors.rs.sim.ctrnn.CTRNN;
import org.evors.core.IRobotController;


/**
 * Simulates a CTRNN. Loads neurons into an array from a CTRNNLayout.
 * @author Miles
 */
public class CTRNNController extends CTRNN implements IRobotController{

    private final double axleWidth;
    private final int leftMotorID,rightMotorID;
    
    public CTRNNController(CTRNNNeuron[] neurons, int[] sensorIndices,
            double stepSize,double axleWidth,int leftMotorID,int rightMotorID) {
        super(neurons, sensorIndices, stepSize);
        this.axleWidth = axleWidth;
        this.leftMotorID = leftMotorID;
        this.rightMotorID = rightMotorID;
    }

    public void step(double[] input) {
        super.integrate(input);
    }

    public double getMotorLOutput() {
        return getNeurons()[leftMotorID].activation;
    }
    
    public double getMotorROutput() {
        return getNeurons()[rightMotorID].activation;
    }
    
    public double getVelocity() {
        return (getNeurons()[leftMotorID].activation + getNeurons()[rightMotorID].activation) / 2;
    }

    public double getAngularVelocity() {
        return (getNeurons()[rightMotorID].activation - getNeurons()[leftMotorID].activation) * axleWidth;
    }

    public double[] getControlOutputs() {
        return new double[]{getMotorLOutput(),getMotorROutput()};
    }
    
   
    
}
