package org.evors.vision;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

public class FixedColourCollection implements ColourCollectionSource {

	protected Vector colours = new Vector();
	
	public FixedColourCollection( Color[] colours ) {
		for( int i = 0; i < colours.length; i++ ) this.colours.add( colours[ i ] );
	}

	public Collection getColours() {
		return colours;
	}

}
