package org.evors.core.geometry;

/**
 * The Rectangle class represents a special case of Polygon with only 4 lines.
 * Lines cannot be added or removed, and the rectangle cannot be resized (it is
 * presumed that rectangular objects will not need to change size mid
 * simulation!).
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Rectangle extends Polygon {

    private final Vec2 size;

    /**
     * Creates a new rectangle with the specified size at the origin with 0
     * rotation.
     *
     * @param size Size of rectangle. Must be non-negative.
     */
    public Rectangle(Vec2 size) {
        if (size.x < 0 || size.y < 0) {
            throw new IllegalArgumentException("Rectangle size must be positive");
        }
        this.size = size;
    }

    /**
     * Creates a new rectangle specifying a center coordinate, size and
     * rotation.
     *
     * @param center Point rectangle will be centred around.
     * @param size Size of rectangle.
     * @param rotation Amount to rotate rectangle by.
     * @return A new rectangle with the specified parameters.
     */
    public static Rectangle createFromCenter(Vec2 center,
            Vec2 size, double rotation) {
        Rectangle rect = new Rectangle(size);
        double halfW = size.x / 2,
                halfH = size.y / 2,
                x1 = center.x - halfW,
                y1 = center.y + halfH,
                x2 = center.x + halfW,
                y2 = center.y - halfH;
        rect.addLinePrivate(Line.fromCoords(x1, y1, x1, y2));
        rect.addLinePrivate(Line.fromCoords(x1, y2, x2, y2));
        rect.addLinePrivate(Line.fromCoords(x2, y2, x2, y1));
        rect.addLinePrivate(Line.fromCoords(x2, y1, x1, y1));
        rect.rotate(rotation);
        return rect;
    }

    /**
     * Adding lines to a rectangle is not possible - calling this on a Rectangle
     * will throw an UnsupportedOperationException.
     *
     * @param line
     */
    public void addLine(Line line) {
        throw new UnsupportedOperationException(
                "Cannot add line to a rectangle. Construct a new rectangle instead or use the Polygon class.");
    }

    
    private void addLinePrivate(Line line) {
        //For the static constructors
        super.addLine(line);
    }

    public Vec2 getSize() {
        return size;
    }
    
    
}
