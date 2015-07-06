package org.evors.core;

import java.io.Serializable;

import org.evors.core.geometry.Vec2;

/**
 * Provides a 2D location of an something 
 * @author michaelgarvie
 *
 */
public interface PositionOrientationSource extends Serializable {
	
	/**
	 * 
	 * @return The something's location
	 */
	public Vec2 getPosition();
	
	/**
	 * What direction it's heading in
	 * @return Orientation angle.  0 = N and increases clockwise.
	 */
	public double getOrientation();
}
