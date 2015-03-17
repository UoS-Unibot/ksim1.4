/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksim.geometry;

import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author miles
 */
public class PolygonTest extends TestCase {
    
    public PolygonTest(String testName) {
        super(testName);
    }

    
    public void testcreateRectangleShouldCreatePoints() {
        Polygon shape = Polygon.createRectangleFromCenter(Vec2.ZERO, new Vec2(2, 2), 0);
        assertFalse(shape.isEmpty());
    }


    public void testrectangleDoesntIntersectAnotherRectangle() {
        Polygon rect1 = Polygon.createRectangleFromCenter(Vec2.ZERO, new Vec2(2, 2), 0);
        Polygon rect2 = Polygon.createRectangleFromCenter(new Vec2(0, 5), new Vec2(2, 2), 0);
        assertFalse("Rectangles intersect", rect1.intersectsWith(rect2));
    }

    public void testrectangleIntersectsAnotherRectangle() {
        Polygon rect1 = Polygon.createRectangleFromCenter(Vec2.ZERO, new Vec2(2, 2), 0);
        Polygon rect2 = Polygon.createRectangleFromCenter(new Vec2(0, 1), new Vec2(2, 2), Math.PI / 4);
        assertTrue("Rectangles don't intersect", rect1.intersectsWith(rect2));
    }
    
}
