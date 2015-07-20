package org.evors.processing;

import java.util.BitSet;

import org.evors.core.EvoRSLib;

public class MinimumThreshold implements ProgrammableThreshold {

	protected double threshold = 0;
	protected int definitionBits = 4;
	
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
	
	public double getThreshold()
	{
		return threshold;
	}

	public void program(BitSet bits) {
		threshold = EvoRSLib.getProportionGreyValue( bits, 0, definitionBits );
	}
	
	public String toString()
	{
		StringBuffer rv = new StringBuffer();
		rv.append("Minimum Threshold with threshold = " + threshold );
		return rv.toString();
	}

}
