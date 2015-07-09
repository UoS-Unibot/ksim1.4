package org.evors.vision;

import java.awt.image.BufferedImage;
import java.util.BitSet;

import org.evors.core.EvoRSLib;
import org.evors.core.Programmable;
import org.evors.core.geometry.Vec2;

public class VisualSensor implements Programmable {

	protected int bitsFilterType, bitsCentreX, bitsCentreY, bitsHeight;
	
	public static final VisualFilter[] FILTER_MAPPING = new VisualFilter[]
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
		int currentBit = 0;
		filter = FILTER_MAPPING[ EvoRSLib.bitsToInt(bits, currentBit, bitsFilterType)];  currentBit += bitsFilterType;
		centrePerc = new Vec2( EvoRSLib.getProportionGreyValue(bits, currentBit, currentBit + bitsCentreX ),
							   EvoRSLib.getProportionGreyValue(bits, currentBit + bitsCentreX, currentBit + bitsCentreX + bitsCentreY ) );
		currentBit += bitsCentreX + bitsCentreY;
		heightPerc = EvoRSLib.getProportionGreyValue(bits, currentBit, currentBit+bitsHeight);
	}
	
	public double getValue( BufferedImage img, double rotation, Vec2 imgCentre )
	{
		return filter.getValue( img, rotation, imgCentre, centrePerc, heightPerc );
	}
	
	public Vec2 getCentrePerc(){ return centrePerc; }
	public double getHeightPerc(){ return heightPerc; }
	public VisualFilter getFilter(){ return filter; }

}
