/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksim.sim;

import junit.framework.TestCase;
import org.ksim.geometry.Vec2;

/**
 *
 * @author miles
 */
public class IRBeamTest extends TestCase {
    
    public IRBeamTest(String testName) {
        super(testName);
    }

    public void testCreates5BeamsSpreadAroundCenter() {
        IRBeam ir = new IRBeam(Vec2.ZERO, 5, 0, 10,
                0.437);
        for (int i = 0; i < ir.getBeams().length; i++) {
            System.out.println(ir.getBeams()[i]);
        }
    }
    
}
