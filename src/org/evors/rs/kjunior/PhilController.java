package org.evors.rs.kjunior;

import org.evors.core.RobotController;

public class PhilController implements RobotController{

    public static final int NUM_IRS = 6;
    double[] IRVals = new double[]{0,0,0,0,0,0};

    public void step(double[] input) {
        IRVals = input;

    }

    public double[] getControlOutputs() {
        boolean highf = false;
        boolean[] high = new boolean[NUM_IRS];
        double[] mr = new double[2];
        String print = "";

        for (int i = 0; i < NUM_IRS; i++) {
            if (IRVals[i] > 2000) {
                high[i] = true;
                highf = true;
            }
        }

        if (!highf) {
            mr[0] = mr[1] = 15;
            print = "straight";
        } else if (high[3] || high[4]) {
            mr[0] = -20;
            mr[1] = 20;
            print = "right";
        } else if (high[0] || high[1]) {
            mr[0] = 20;
            mr[1] = -20;
            print = "left";
        } else if (high[2]) {
            mr[0] = mr[1] = 15;
            print = "just mid";
        } else if (high[5]) {
            mr[0] = mr[1] = 15;
            print = "hight at back";
        }
        //System.out.println( "... " + print + " ...");
        //System.out.println( "ls " + mr[ 0 ] + " rs " + mr[ 1 ] );
        return mr;
    }
}
