package org.evors.processing;

import java.io.Serializable;

/**
 * A Treshold object defines a way in which a threshold type filtering can be applied
 * @author michaelgarvie
 *
 */
public interface Threshold extends Serializable {
	
	/**
	 * Applies the thresholding
	 * @param input value to be processed
	 * @return the value after thresholding
	 */
	double threshold( double value );
}
