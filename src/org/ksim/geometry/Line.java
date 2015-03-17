/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksim.geometry;

import java.awt.Shape;

/**
 *
 * @author miles
 */
public class Line extends Polygon {

    public Line(Vec2 p1, Vec2 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Line() {
        this(Vec2.ZERO, Vec2.ZERO);
    }
    
    public static Line fromPolarVec(Vec2 p1, double angle, double length) {
        return new Line(
                p1,
                p1.translatePolar(angle, length)
        );
    }

    public static Line fromCoords(double x1, double y1, double x2, double y2) {
        return new Line(new Vec2(x1, y1), new Vec2(x2, y2));
    }

    public static Line fromCenterPoint(Vec2 center, double length,
            double angle) {
        return new Line(
                center.translatePolar((angle + Math.PI) % (2 * Math.PI), length
                        / 2),
                center.translatePolar(angle, length / 2)
        );
    }

    public static Line fromCenterPoint(double c1, double c2, double length,
            double angle) {
        return fromCenterPoint(new Vec2(c1, c2), length, angle);
    }

    Vec2 p1;
    Vec2 p2;

    public void rotate(Vec2 pivot, double deltaAngle) {
        p1 = p1.getRotated(pivot, deltaAngle);
        p2 = p2.getRotated(pivot, deltaAngle);
    }
    public void rotate(double deltaAngle) {
        p2 = p2.getRotated(p1, deltaAngle);
    }

    Intersection.LineLine getIntersection(Line l2) {
        return Intersection.LineLine.calculate(this, l2);
    }

    double getShortestDistToPoint(Vec2 point) {
        /**
         * Adapted from C code by Prof Phil Husbands and
         * http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
         */
        double l2, t;
        Vec2 A, B, proj;
        Vec2 line = p2.subtract(p1);
        l2 = line.getNormSq();
        if (l2 == 0) {
            return point.subtract(p1).getNorm();
        }
        A = point.subtract(p1);
        B = line;
        t = A.dotProduct(B) / l2;
        if (t < 0) {
            return point.subtract(p1).getNorm(); //actual point lies off p1
        } else if (t > 1) {
            return point.subtract(p2).getNorm(); //lies off p2
        } else {
            proj = p1.add(t, line);
            return point.subtract(proj).getNorm();
        }
    }

    public double getLength() {
        return p2.subtract(p1).getNorm();
    }

    public String toString() {
        return "Line{" + p1 + "," + p2 + '}';
    }

    
    

}
