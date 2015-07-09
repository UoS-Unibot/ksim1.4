package org.evors.vision;

import org.evors.core.PositionOrientationSource;
import org.evors.core.geometry.Vec2;

public class FakePositionOrientationSource implements PositionOrientationSource {

	public FakePositionOrientationSource() {
	}
	
	protected Vec2 position;
	protected double orientation;
	
	public void setPosition( Vec2 position )
	{
		this.position = position;
	}

	public Vec2 getPosition() {
		return position;
	}

	public void setOrientation( double orientation )
	{
		this.orientation = orientation;
	}
	
	public double getOrientation() {
		return orientation;
	}

}
