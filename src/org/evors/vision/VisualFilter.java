package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.evors.core.geometry.Vec2;

public interface VisualFilter extends Serializable
{
	public double read( BufferedImage img, Vec2 img_centre, Vec2 filter_centre, double h );
}
