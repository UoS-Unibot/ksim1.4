package org.evors.core.geometry;

import java.util.Iterator;

/**
 * Represents a geometric circle.
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Circle implements Shape2D {
    /**
     * Creates a new Circle with center point specified as a Vec2 and a
     * radius.
     *
     * @param centerPoint Vec2 of center point.
     * @param radius Radius. Must be nonnegative.
     * @return A new Circle with specified parameters.
     */
    public static Circle getFromCenter(Vec2 centerPoint, double radius) {
        return new Circle(centerPoint, radius);
    }

    /**
     * Creates a new Circle with center point specified as coordinates and a
     * radius.
     *
     * @param centerX X coordinate of center point.
     * @param centerY Y coordinate of center point.
     * @param radius Radius. Must be nonnegative.
     * @return A new Circle with specified parameters.
     */
    public static Circle getFromCenter(double centerX, double centerY,
            double radius) {
        return getFromCenter(new Vec2(centerX, centerY), radius);
    }

    private Circle(Vec2 center, double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be nonnegative");
        }
        this.center = center;
        this.radius = radius;
    }

    private Vec2 center;
    private final double radius;
    private double rotation;

    
    public Vec2 getCenter() {
        return center;
    }

    /**
     * Gets the radius of this circle.
     *
     * @return distance from centre to edge of circle.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Returns a world coordinate specified relative to the centre of this circle.
     * @param localCoords Coordinates with 0,0 being the centre of this circle
     * @return World coordinates relative to the centre
     */
    public Vec2 getLocalToWorldCoords(Vec2 localCoords) {
        return getCenter().add(
                new Vec2(
                        localCoords.x * Math.cos(rotation),
                        localCoords.y * Math.sin(rotation)
                )
        );
    }

    public double getRotation() {
        return rotation;
    }

    
    public boolean intersectsWith(Shape2D shape) {
        if (shape instanceof Circle) {
            Circle circle2 = (Circle) shape;
            double centerDists = circle2.getCenter().subtract(getCenter()).
                    getNorm();
            double radii = circle2.getRadius() + getRadius();
            if (Math.abs(centerDists) < radii) {
                return true;
            }
        } else if (shape instanceof Line) {
            Line line = (Line) shape;
            if (intersectsWithLine(line)) {
                System.out.println("intersection, circle at " + getCenter().
                        toString() + ",line at " + line.toString());
            }
            return intersectsWithLine(line);
        } else if (shape instanceof Polygon) {
            Polygon poly = (Polygon) shape;
            for (Iterator it = poly.getLines().iterator(); it.hasNext();) {
                Line l = (Line)it.next();
                if (intersectsWithLine(l)) {
                    System.out.println("intersection, circle at " + getCenter().
                            toString() + ",line at " + l.toString());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean intersectsWithLine(Line line) {
        return line.getShortestDistToPoint(center) < radius;
    }

    
    public void move(double distance, double deltaAngle) {
        translate(new Vec2(distance * Math.cos(getRotation()),
                distance
                * Math.sin(getRotation())));
        rotate(deltaAngle);
    }

    
    public void rotate(double deltaAngle) {
        rotation += deltaAngle;
    }

    
    public void setCenter(Vec2 center) {
        this.center = center;
    }
    
    public void translate(Vec2 deltaMovement) {
        center = center.add(deltaMovement);
    }

    
    public Intersection getSmallestIntersection(
            Line line) {
        return Intersection.CircleLine.calculate(this, line);
    }
}
