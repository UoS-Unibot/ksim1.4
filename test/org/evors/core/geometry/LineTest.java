/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.geometry;

import org.evors.core.geometry.Vec2;
import org.evors.core.geometry.Line;
import junit.framework.TestCase;
import org.evors.TestUtils;

/**
 *
 * @author miles
 */
public class LineTest extends TestCase {

    public LineTest(String testName) {
        super(testName);
    }

    

    public void testLineFromCentreWith0RotationHasP1_n1_0_AndP2_1_0() {
        Line l = Line.fromCenterPoint(0, 0, 2, 0);
        TestUtils.assertVEq(l.p1, new Vec2(-1, 0));
        TestUtils.assertVEq(l.p2, new Vec2(1, 0));
    }

    public void testLineFromCentreWithPI2RotationHasP1_0_n1_AndP2_0_1() {
        Line l = Line.fromCenterPoint(0.0, 0.0, 2.0, Math.PI / 2);
        TestUtils.assertVEq(l.p1, new Vec2(0, -1));
        TestUtils.assertVEq(l.p2, new Vec2(0, 1));
    }

    public void testRotatingLineByMinusPiOver2Around2_2MovesP1to0_4AndP2to4_0() {
        final Line rotationLine = Line.fromCoords(0, 0, 4, 4);
        rotationLine.rotate(new Vec2(2, 2), -Math.PI / 2);
        TestUtils.assertVEq(rotationLine.p1, new Vec2(0, 4));
        TestUtils.assertVEq(rotationLine.p2, new Vec2(4, 0));
    }

}
