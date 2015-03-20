package org.evors;

import junit.framework.TestCase;
import org.evors.core.geometry.Vec2;


/**
 *
 * @author miles
 */
public class TestUtils extends TestCase{

    public TestUtils(String name) {
        super(name);
    }
    
    
    
    public static final double EPS = 1E-4;
    
    public static void assertVEq(Vec2 v1, Vec2 v2) {
        assertTwoVector2DsEqual(v1, v2, true);
    }
    
    public static void assertVNEq(Vec2 v1, Vec2 v2) {
        assertTwoVector2DsEqual(v1, v2, false);
    }
        
    public static void assertTwoVector2DsEqual(Vec2 v1, Vec2 v2,boolean assertion) {
        boolean result;
        if(assertion)
            result = v1.distance(v2) < EPS;
        else
            result = v1.distance(v2) > EPS;
        junit.framework.Assert.assertTrue(
                "Expected: " + v1.toString() + ", actual: " + v2.toString(),
                result
        );
    }
    
    public static void assertTwoVector2DsEqual(float x, float y, Vec2 v2,boolean assertion) {
        assertTwoVector2DsEqual(new Vec2(x,y),v2,assertion);
    }
    
    
}
