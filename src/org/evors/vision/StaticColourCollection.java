package org.evors.vision;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import org.evors.core.EvoRSLib;

public class StaticColourCollection implements ColourCollectionSource {

	protected Vector colours = new Vector();
	
	public StaticColourCollection( Color[] colours ) {
		for( int i = 0; i < colours.length; i++ ) this.colours.add( colours[ i ] );
	}

	public Collection getColours() {
		return colours;
	}
	
	public String toString()
	{
		return EvoRSLib.arr2String( colours.toArray( ) );
	}

}
