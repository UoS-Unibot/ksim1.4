package org.evors.vision;

import java.util.BitSet;

import org.evors.core.Programmable;
import org.evors.core.geometry.Vec2;

public class VisualSensor implements Programmable {

	protected int bitsFilterType, bitsCentreX, bitsCentreY, bitsHeight;
	
	protected static VisualFilter[] filterMapping = new VisualFilter[]
			{ 
				new HaarFilter("x"), //0
				new HaarFilter("x.x"),
				new HaarFilter(".x."),
				new HaarFilter("x\n.\nx"),
				new HaarFilter(".\nx\n."), // 4
				new HaarFilter("x.\n.x"),
				new HaarFilter(".x\nx."),
				new HaarFilter("x")
			};
	
	protected Vec2 centrePerc;
	protected double heightPerc;
	protected VisualFilter filter;
	protected VisualSensorGroup group;
	
	public VisualSensor( VisualSensorGroup group,int bitsFilterType,int bitsCentreX,int bitsCentreY,int bitsHeight ) {
		this.group = group;
		this.bitsFilterType = bitsFilterType;
		this.bitsCentreX = bitsCentreX;
		this.bitsCentreY = bitsCentreY;
		this.bitsHeight = bitsHeight;
	}

	public void program(BitSet bits) {
		// TODO Auto-generated method stub
		
	}

}
