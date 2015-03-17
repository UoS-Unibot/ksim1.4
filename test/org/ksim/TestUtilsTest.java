/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksim;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.ksim.geometry.Vec2;

/**
 *
 * @author miles
 */
public class TestUtilsTest extends TestCase {
    
    public TestUtilsTest(String testName) {
        super(testName);
    }
    
    public void testVectorsShouldBeEqual() {
        Vec2 v1 = new Vec2(2,2);
        Vec2 v2 = new Vec2(2,2);
        TestUtils.assertVEq(v1, v2);
    }
    
    
    public void testVectorsShouldntBeEqual() {
        Vec2 v1 = new Vec2(2,2);
        Vec2 v2 = new Vec2(3,2);
        TestUtils.assertVNEq(v1, v2);
    }
}
