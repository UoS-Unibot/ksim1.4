/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksim.geometry;

import junit.framework.TestCase;

/**
 *
 * @author miles
 */
public class CircleTest extends TestCase {
    
    public CircleTest(String testName) {
        super(testName);
    }

    public void testTwoCirclesShouldIntersect() {
        Circle circle1 = Circle.getFromCenter(Vec2.ZERO, 4),
                circle2 = Circle.getFromCenter(7, 0, 4);
        assertTrue(circle1.intersectsWith(circle2));
    }
    
    public void testCircleShouldIntersectWithLine() {
        Circle circle1 = Circle.getFromCenter(Vec2.ZERO, 4);
        Line line = Line.fromCoords(2, 0, 0, 4);
        assertTrue(circle1.intersectsWith(line));
    }
    
}
