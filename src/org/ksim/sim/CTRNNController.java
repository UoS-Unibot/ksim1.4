/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksim.sim;


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

    public double getVelocity() {
        return (getNeurons()[leftMotorID].activation + getNeurons()[rightMotorID].activation) / 2;
    }

    public double getAngularVelocity() {
        return (getNeurons()[rightMotorID].activation - getNeurons()[leftMotorID].activation) * 1000;
    }
    
   
    
}
