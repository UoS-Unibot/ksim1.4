/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.kjunior;

import org.evors.rs.kjunior.IRBeam;
import junit.framework.TestCase;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;

/**
 *
 * @author miles
 */
public class IRBeamTest extends TestCase {
    
    public IRBeamTest(String testName) {
        super(testName);
    }

    public void testCreates5BeamsSpreadAroundCenter() {
        SimulationWorld world = new SimulationWorld(Vec2.ZERO);
        IRBeam ir = new IRBeam(Vec2.ZERO, 5, 0,world, 10,
                0.437);
        for (int i = 0; i < ir.getBeams().length; i++) {
            System.out.println(ir.getBeams()[i]);
        }
    }
    
}
