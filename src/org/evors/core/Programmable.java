package org.evors.core;

import java.io.Serializable;
import java.util.BitSet;

/**
 * Something that can be configured using a sequence of bits
 * @author michaelgarvie
 *
 */
public interface Programmable extends Serializable {
	
	/**
	 * Configure this using these bits
	 * @param bits Bits to use for configuration.  THe required bits will be read from the start.
	 */
	public void program( BitSet bits );
}
