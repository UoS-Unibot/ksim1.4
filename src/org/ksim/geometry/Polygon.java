package org.ksim.geometry;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * The Polygon class represents a group of lines. Shape-Shape intersections for
 * collision detection can be calculated, and Shape-Line intersections (for e.g.
 * ray tracing) are provided.
 *
 * A Polygon can be built up by using the default constructors and adding lines
 * - much easier is the static factory method which will create a rectangle for
 * you.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Polygon implements Shape2D {

    private final LinkedList lines;
    private Vec2 center;
    private double rotation;

    /**
     * Creates an empty Shape2D() centred at the origin.
     */
    public Polygon() {
        this(Vec2.ZERO);
    }

    /**
     * Creates an empty Shape2D at the specified center point.
     *
     * @param center Center point.
     */
    public Polygon(Vec2 center) {
        lines = new LinkedList();
        this.center = center;
    }

    /**
     * Creates a new rectangular Polygon with four boundary lines from the
     * specified center point.
     *
     * @param center Center point of rectangle.
     * @param size Size of rectangle.
     * @param rotation amount to rotate this rectangle by.
     * @return a rectangular Polygon
     */
    public static Polygon createRectangleFromCenter(Vec2 center,
            Vec2 size, double rotation) {
        Polygon shape = new Polygon(center);
        double halfW = size.x / 2,
                halfH = size.y / 2,
                x1 = center.x - halfW,
                y1 = center.y + halfH,
                x2 = center.x + halfW,
                y2 = center.y - halfH;
        shape.addLine(Line.fromCoords(x1, y1, x1, y2));
        shape.addLine(Line.fromCoords(x1, y2, x2, y2));
        shape.addLine(Line.fromCoords(x2, y2, x2, y1));
        shape.addLine(Line.fromCoords(x2, y1, x1, y1));
        shape.rotate(rotation);
        return shape;
    }


    /**
     * Adds a line to this Polygon.
     *
     * @param line
     */
    public void addLine(Line line) {
        lines.add(line);
    }

    /**
     * Gets whether this Polygon contains any lines.
     *
     * @return
     */
    public boolean isEmpty() {
        return lines.isEmpty();
    }

    /**
     * Rotates this Polygon around the center point by the given delta angle.
     *
     * @param deltaAngle Angle to rotate by in radians.
     */
    public void rotate(double deltaAngle) {
        rotation = (rotation + deltaAngle) % (2 * Math.PI);
        for (Iterator it = lines.iterator(); it.hasNext();) {
            Line line = (Line)it.next();
            line.rotate(center, deltaAngle);
        }
    }

    /**
     * Converts local coordinates specified relative to the center point to
     * world coordinates.
     *
     * @param localCoords coordinates relative to the center point; the center
     * point is at 0,0.
     * @return the world point of the local coordinates.
     */
    public Vec2 getLocalToWorldCoords(Vec2 localCoords) {
        return getCenter().add(
                new Vec2(
                        localCoords.x * Math.cos(rotation),
                        localCoords.y * Math.sin(rotation)
                )
        );
    }

    /**
     * Translates and rotates this Polygon by the specified amount.
     *
     * @param distance distance to move this Polygon in the current orientation
     * @param deltaAngle Angle in radians to change the orientation by.
     */
    public void move(double distance, double deltaAngle) {
        translate(new Vec2(distance * Math.cos(getRotation()), distance
                * Math.sin(getRotation())));
        rotate(deltaAngle);
    }

    /**
     * Translates this Polygon by the specified change vector. Does not take
     * rotation into account.
     *
     * @param deltaMovement Change vector for movement.
     */
    public void translate(Vec2 deltaMovement) {
        center = center.add(deltaMovement);
        for (Iterator it = getLines().iterator(); it.hasNext();) {
            Line l = (Line)it.next();
            l.translate(deltaMovement);
        }
    }

    /**
     * Gets the collection of lines in this Polygon.
     *
     * @return
     */
    public Collection getLines() {
        return lines;
    }

    /**
     * Gets any intersections between the lines in this shape and the lines in
     * the shape provided.
     *
     * @param shape Other shape to check for intersections.
     * @return a list of intersecting LineIntersections; if there are no
     * intersections, an empty list.
     */
    public Collection getIntersectionPoints(Polygon shape) {
        LinkedList ips = new LinkedList();
        for (Iterator it = getLines().iterator(); it.hasNext();) {
            Line l1 = (Line)it.next();
            for (Iterator it2 = shape.getLines().iterator(); it2.hasNext();) {
                Line l2 = (Line)it2.next();
                Intersection.LineLine result = l1.getIntersection(l2);
                if (result.isIntersection) {
                    ips.add(result.intersectionPoint);
                }
            }
        }
        return ips;
    }

    /**
     * Gets the smallest intersection with the line specified, e.g. for ray
     * tracing.
     *
     * @param line Line to check for intersections.
     * @return the Intersection with the lowest segment distance.
     */
    public Intersection getSmallestIntersection(Line line) {
        Intersection lowestLine = Intersection.noIntersection();
        for (Iterator it = getLines().iterator(); it.hasNext();) {
            Line shapeLine = (Line)it.next();
            Intersection.LineLine li = line.getIntersection(shapeLine);
            if (li.isIntersection) {
                if (!lowestLine.isIntersection) {
                    lowestLine = li;
                } else if (li.line1DistToIntersect
                        < lowestLine.getSmallestLineDist()) {
                    lowestLine = li;
                }
            }
        }
        return lowestLine;
    }

    /**
     * Gets the actual distance of the smallest line segment intersecting with
     * this shape, or NaN if there is no intersection.
     *
     * @param line Line to get intersections with.
     * @return the smallest intersection distance with the given line, or NaN if
     * no intersection.
     */
    public double getSmallestLineIntersectionDist(Line line) {
        double lowestDist = Double.NaN;
        for (Iterator it = getLines().iterator(); it.hasNext();) {
            Line shapeLine = (Line)it.next();
            Intersection.LineLine li = line.getIntersection(shapeLine);
            if (li.isIntersection) {
                if (Double.isNaN(lowestDist)) {
                    lowestDist = li.line1DistToIntersect;
                } else if (li.line1DistToIntersect < lowestDist) {
                    lowestDist = li.line1DistToIntersect;
                }
            }
        }
        return lowestDist;
    }

    /**
     * Returns whether this shape intersects with another shape.
     *
     * @param shape Other shape to check for intersections.
     * @return whether the two shapes intersect.
     */
    public boolean intersectsWith(Shape2D shape) {
        if (shape instanceof Polygon) {
            Polygon poly = (Polygon)shape;
            for (Iterator it = getLines().iterator(); it.hasNext();) {
                Line l1 = (Line)it.next();
                for (Iterator it2 = poly.getLines().iterator(); it2.hasNext();) {
                    Line l2 = (Line)it2.next();
                    if (l1.getIntersection(l2).isIntersection) {
                        return true;
                    }
                }
            }
        } else if (shape instanceof Circle) {
            Circle circle = (Circle)shape;
            if(circle.intersectsWith(this))
                return true;
        } else if(shape instanceof Line) {
            Line line = (Line)shape;
            for (Iterator it = lines.iterator(); it.hasNext();) {
                Line shapeLine = (Line)it.next();
                if(shapeLine.getIntersection(line).isIntersection)
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the center point of this Polygon.
     *
     * @return
     */
    public Vec2 getCenter() {
        return center;
    }

    /**
     * Sets the center point of this Polygon.
     *
     * @param center
     */
    
    public void setCenter(Vec2 center) {
        this.center = center;
    }

    /**
     * Gets the current rotation of the shape.
     *
     * @return rotation in radians.
     */
    public double getRotation() {
        return rotation;
    }

}
