package org.evors.core.geometry;

/**
 * The Shape2D represents an abstract shape which can be moved around, rotated
 * and checked for intersections with other shapes.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface Shape2D extends java.io.Serializable {

    /**
     * Gets the center point of this Polygon.
     *
     * @return
     */
    Vec2 getCenter();

    /**
     * Converts local coordinates specified relative to the center point to
     * world coordinates.
     *
     * @param localCoords coordinates relative to the center point; the center
     * point is at 0,0.
     * @return the world point of the local coordinates.
     */
    Vec2 getLocalToWorldCoords(Vec2 localCoords);

    /**
     * Gets the current rotation of the shape.
     *
     * @return rotation in radians.
     */
    double getRotation();

    /**
     * Returns whether this shape intersects with another shape.
     *
     * @param shape Other shape to check for intersections.
     * @return whether the two shapes intersect.
     */
    boolean intersectsWith(Shape2D shape);

    /**
     * Translates and rotates this shape by the specified amount.
     *
     * @param distance distance to move this shape in the current orientation
     * @param deltaAngle Angle in radians to change the orientation by.
     */
    void move(double distance, double deltaAngle);

    /**
     * Rotates this shape around the center point by the given delta angle.
     *
     * @param deltaAngle Angle to rotate by in radians.
     */
    void rotate(double deltaAngle);

    /**
     * Sets the center point of this shape.
     *
     * @param center
     */
    void setCenter(Vec2 center);

    /**
     * Translates this shape by the specified change vector. Does not take
     * rotation into account.
     *
     * @param deltaMovement Change vector for movement.
     */
    void translate(Vec2 deltaMovement);

    /**
     * Finds the smallest intersection between this shape and a line.
     *
     * @param ray Line to check for intersections.
     * @return an Intersection object containing details of the specified
     * intersection, or Intersection.noIntersection() if applicable.
     */
    public Intersection getSmallestIntersection(Line ray);

}
