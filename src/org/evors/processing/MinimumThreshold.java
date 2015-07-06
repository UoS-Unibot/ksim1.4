package org.evors.processing;

public class MinimumThreshold implements Threshold {

	protected double threshold = 0;
	
	public MinimumThreshold(){}
	
	public MinimumThreshold( double threshold) {
		this.threshold = threshold;
	}

	public double threshold(double value) {
		return ( value < threshold ) ? 0 : value;
	}
	
	public void setThreshold( double threshold )
	{
		this.threshold = threshold;
	}

}
