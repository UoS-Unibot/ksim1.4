package org.evors.core;

import java.util.Random;

/**
 * Library file with shared objects and functions
 * 
 * @author michaelgarvie
 *
 */
public abstract class EvoRSLib {
	
	/**
	 * Shared random number generator to allow control of seed.
	 */
	public static final Random random = new Random();
}
