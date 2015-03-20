/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.geometry;

import org.evors.core.geometry.Vec2;
import org.evors.core.geometry.Circle;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Intersection;
import junit.framework.TestCase;
import org.evors.TestUtils;

/**
 *
 * @author miles
 */
public class IntersectionTest extends TestCase {
    
    public IntersectionTest(String testName) {
        super(testName);
    }

    static Line collinearDisjointA, collinearDisjointB,
            collinearOverlappingA, collinearOverlappingB,
            parallelA, parallelB,
            interAt1_1A, interAt1_1B,
            interAt4_4A, interAt4_4B,
            notIntersectingA, notIntersectingB,rotationLine,lineDist4A,lineDist4B;
    static Circle circle;

    protected void setUp() {
        collinearDisjointA = Line.fromCoords(0, 0, 1, 1);
        collinearDisjointB = Line.fromCoords(3, 3, 4, 4);
        
        collinearOverlappingA = Line.fromCoords(0, 0, 3, 3);
        collinearOverlappingB = Line.fromCoords(1, 1, 4, 4);
        
        parallelA = Line.fromCoords(0, 0, 2, 2);
        parallelB = Line.fromCoords(0, 2, 2, 4);
        
        interAt1_1A = Line.fromCoords(0, 0, 2, 2);
        interAt1_1B = Line.fromCoords(2, 0, 0, 2);
        
        interAt4_4A = Line.fromCoords(0, 0, 5, 5);
        interAt4_4B = Line.fromCoords(4, 0, 4, 5);
        
        notIntersectingA = Line.fromCoords(0, 0, 5, 5);
        notIntersectingB = Line.fromCoords(0, 3, 2, 9);
        
        rotationLine = Line.fromCoords(0,0,4,4);
        
        lineDist4A = Line.fromCoords(0, 0, 0, 5);
        lineDist4B = Line.fromCoords(-2, 4, 2, 4);
        circle = Circle.getFromCenter(Vec2.ZERO, 2);
    }



    
    
    public void testintersectCollinearDisjointLinesReturnNaN() {
        Intersection i =  collinearDisjointA.getIntersection(collinearDisjointB);
        assertEquals(Vec2.NaN,i.intersectionPoint);
    }
    
    
    public void testintersectCollinearOverlappingLinesReturnNaN() {
        Intersection i =  collinearOverlappingA.getIntersection(collinearOverlappingB);
        assertEquals(Vec2.NaN, i.intersectionPoint);
    }
    
    
    public void testintersectParallelLinesReturnNaN() {
        assertEquals(Vec2.NaN, parallelA.getIntersection(parallelB).intersectionPoint);
    }
    
    
    public void testlinesIntersectAt1_1() {
        TestUtils.assertVEq(new Vec2(1,1), interAt1_1A.getIntersection(interAt1_1B).intersectionPoint);
    }
    
    
    public void testline1IntersectionDistanceAt1_1isRoot2() {
        assertEquals(Math.sqrt(2), interAt1_1A.getIntersection(interAt1_1B).line1DistToIntersect,0.0001);
    }
    
    
    
    public void testline2IntersectionDistanceAt1_1isRoot2() {
        assertEquals(Math.sqrt(2), interAt1_1A.getIntersection(interAt1_1B).line2DistToIntersect,0.0001);
    }
    
    
    public void testrotatedLine1DistIs4() {
        TestUtils.assertVEq(lineDist4A.getIntersection(lineDist4B).intersectionPoint, new Vec2(0,4));
    }


    
    public void testlinesIntersectAt4_4() {
        TestUtils.assertVEq(new Vec2(4,4), interAt4_4A.getIntersection(interAt4_4B).intersectionPoint);
    }

    
    public void testlinesDontIntersect() {
        assertEquals(Vec2.NaN, notIntersectingA.getIntersection(notIntersectingB).intersectionPoint);
    }
    
    
    public void testcircleAndLine3DontIntersect() {
        Line circLine3 = Line.fromCoords(3,-3, 3, 3);
        assertFalse(Intersection.CircleLine.calculate(circle, circLine3).isIntersection);
    }
    
    
    public void testline2IsTangentToCircle() {
        Line circLine2 = Line.fromCoords(2, -4, 2, 4);
        assertTrue(Intersection.CircleLine.calculate(circle, circLine2).isTangent);
    }
    
    
    public void testline2TangentPointIs2_0() {
        Line circLine2 = Line.fromCoords(2, -4, 2, 4);
        TestUtils.assertVEq(new Vec2(2,0), Intersection.CircleLine.calculate(circle, circLine2).intersectp1);
    }
    
    
    public void testline1IntersectsButIsNotTangent() {
        Line circLine1 = Line.fromCoords(0, -4, 0, 4);
        Intersection.CircleLine icl = Intersection.CircleLine.calculate(circle,
                circLine1);
        assertTrue(icl.isIntersection);
        assertFalse(icl.isTangent);
    }
    
    
    public void testline1IntersectionPointsAre0_n2__0_2() {
        Line circLine1 = Line.fromCoords(0, -4, 0, 4);
        Intersection.CircleLine icl = Intersection.CircleLine.calculate(circle,
                circLine1);
        TestUtils.assertVEq(icl.intersectp1,new Vec2(0,2));
        TestUtils.assertVEq(icl.intersectp2,new Vec2(0,-2));
    }
    
    
    public void testcircleAndLineDoNotIntersect() {
        //Reproducing a bug
        circle = Circle.getFromCenter(new Vec2(0.3467132624,0.0035458654), 0.065);
        Line line = Line.fromCoords(-1.2, -1.2, -0.8, -1.2);
        assertFalse(circle.intersectsWith(line));
    }
    
    
    public void testcircleAndLineDoNotIntersect2() {
        //Reproducing a bug
        circle = Circle.getFromCenter(new Vec2(0.3396085131,-0.0168142506), 0.065);
        Line line = Line.fromCoords(0.8, 0.8, 1.2, 0.8);
        assertFalse(circle.intersectsWith(line));
    }
    
}
