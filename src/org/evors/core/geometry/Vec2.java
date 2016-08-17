package org.evors.core.geometry;

/**
 * Implementation of 2D vector based on the Commons.math3 Vector2D class.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Vec2 implements java.io.Serializable {

    public final double x;
    public final double y;

    public static final Vec2 ZERO = new Vec2(0, 0);
    public static final Vec2 NaN = new Vec2(Double.NaN, Double.NaN);

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Vec2 v) {
        return subtract(v).getNorm();
    }

    public Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    public Vec2 add(double s, Vec2 v) {
        Vec2 scaled = v.scalar(s);
        return new Vec2(x + scaled.x, y + scaled.y);
    }

    public Vec2 scalar(double s) {
        return new Vec2(x * s, y * s);
    }

    public Vec2 subtract(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    public double getNorm() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 translatePolar(double angle, double length) {
        return new Vec2(
                x + length * Math.cos(angle),
                y + length * Math.sin(angle)
        );
    }

    public Vec2 getRotated(Vec2 pivot, double deltaAngle) {
        return new Vec2(
                pivot.x + Math.cos(deltaAngle) * (x
                - pivot.x)
                - Math.sin(deltaAngle)
                * (y - pivot.y),
                pivot.y + Math.sin(deltaAngle) * (x
                - pivot.x)
                + Math.cos(deltaAngle)
                * (y - pivot.y)
        );
    }

    double dotProduct(Vec2 v) {
        return x * v.x + y * v.y;
    }

    double getNormSq() {
        return x * x + y * y;
    }

    public String toString() {
        return "Vec2{" + x + "," + y + '}';
    }
    
    public boolean equals( Vec2 obj )
    {
    	return ( x == obj.x ) && ( y == obj.y );
    }
    
    public boolean equals( Object obj )
    {
    	if( obj instanceof Vec2 )
    	{
    		return equals( ( Vec2 ) obj );
    	}
    	return false;
    }
    
    public int hashCode()
    {
    	int hash = 17;
	    hash = hash * 23 + new Double(x).hashCode();
	    hash = hash * 23 + new Double(y).hashCode();
	    return hash;
    }

}
