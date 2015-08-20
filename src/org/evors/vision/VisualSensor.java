package org.evors.vision;

import java.awt.image.BufferedImage;
import java.util.BitSet;

import org.evors.core.EvoRSLib;
import org.evors.core.Programmable;
import org.evors.core.geometry.Vec2;

public class VisualSensor implements Programmable {

	protected int bitsFilterType, bitsCentreX, bitsCentreY, bitsHeight, bitsChannel;
	
	public static final VisualFilter[] FILTER_MAPPING = new VisualFilter[]
			{ 
				new HaarFilter("x"), //0
				new HaarFilter("x.x"),
				new HaarFilter(".x."),
				new HaarFilter("x\n.\nx"),
				new HaarFilter(".\nx\n."), // 4
				new HaarFilter("x.\n.x"),
				new HaarFilter(".x\nx."),
				new HaarFilter(".")
			};
	
	protected Vec2 centrePerc;
	protected double heightPerc;
	protected VisualFilter filter;
	protected VisualSensorGroup group;
	protected int channelIx = 0;
		
	public VisualSensor( VisualSensorGroup group,int bitsFilterType,int bitsCentreX,int bitsCentreY,int bitsHeight,int bitsChannel ) {
		this.group = group;
		this.bitsFilterType = bitsFilterType;
		this.bitsCentreX = bitsCentreX;
		this.bitsCentreY = bitsCentreY;
		this.bitsHeight = bitsHeight;
		this.bitsChannel = bitsChannel;
	}

	public void program(BitSet bits) {
		int currentBit = 0;
		filter = FILTER_MAPPING[ EvoRSLib.bitsToInt(bits, currentBit, currentBit + bitsFilterType)];  currentBit += bitsFilterType;
		centrePerc = new Vec2( EvoRSLib.getProportionGreyValue(bits, currentBit, currentBit + bitsCentreX ),
							   EvoRSLib.getProportionGreyValue(bits, currentBit + bitsCentreX, currentBit + bitsCentreX + bitsCentreY ) );
		currentBit += bitsCentreX + bitsCentreY;
		heightPerc = EvoRSLib.getProportionGreyValue(bits, currentBit, currentBit+bitsHeight);
		currentBit += bitsHeight;
		channelIx = EvoRSLib.bitsToInt( bits, currentBit, currentBit + bitsChannel );
	}
	
	public double getValue( int[][][] img, double rotation, Vec2 imgCentre, BufferedImage debugImage )
	{
		return filter.getValue( img, rotation, imgCentre, centrePerc, heightPerc, channelIx, debugImage );
	}
	
	public double getValue( int[][][] img, double rotation, Vec2 imgCentre )
	{
		return getValue( img, rotation, imgCentre, null );
	}
	
	public Vec2 getCentrePerc(){ return centrePerc; }
	public double getHeightPerc(){ return heightPerc; }
	public VisualFilter getFilter(){ return filter; }
	
	public String toString()
	{
		StringBuffer rv = new StringBuffer();
		rv.append("VisualSensor with:");
		rv.append("\n\tFilter = " + filter);
		rv.append("\n\tCentre% = " + centrePerc );
		rv.append("\n\tHeight% = " + heightPerc );
		rv.append("\n\tChannel = " + channelIx );
		return rv.toString();
	}

}
