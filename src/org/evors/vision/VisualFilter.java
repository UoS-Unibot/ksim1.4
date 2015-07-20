package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.evors.core.geometry.Vec2;

public interface VisualFilter extends Serializable
{
	public double getValue( int[][] img, double rotation, Vec2 imgCentre, Vec2 filter_centre, double h );

	public double getValue( int[][] img, double rotation, Vec2 imgCentre, Vec2 filter_centre, double h, BufferedImage debugImage );

}
