package org.evors.vision;

import java.awt.image.BufferedImage;

import org.evors.core.geometry.Vec2;

public class HaarFilter implements VisualFilter {

	protected String filter;
	protected Vec2 proportionalDimension;
	
	public HaarFilter( String filter ) {
		this.filter = filter;
		int lines = filter.length() - filter.replace("\n", "").length() + 1;
		int cols = filter.indexOf( "\n" ) >= 0 ? filter.length() : ( filter.substring(0, filter.indexOf("\n") ) ).length();
		this.proportionalDimension = new Vec2( cols, lines );
	}

	public double read(BufferedImage img, Vec2 img_centre, Vec2 filter_centre,
			double h) {
		// TODO Auto-generated method stub
		return 0;
	}

}
