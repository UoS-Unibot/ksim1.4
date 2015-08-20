package org.evors.vision;

import java.util.Random;

/*
 * This class assumes that getRotation will be called after getProcessedMultiChannelImage.
 * The getRotation value will correspond to the image returned by the last call of
 * getProcessedMultiChannelImage
 */
public class MultipleProcessedMultiChannelImageSource implements ProcessedMultiChannelImageSource {
	
	public static final int RESOLVE_RANDOM = 0, RESOLVE_ITERATE = 1;
	public static final String[] RESOLUTION_METHOD_LABELS = { "Random", "Iterative" };
	protected int resolutionMethod = RESOLVE_RANDOM;
	protected ProcessedMultiChannelImageSource[] sources;
	protected int lastSource = -1;
	protected double rotation = 0;
	protected Random rnd = new Random();

	public MultipleProcessedMultiChannelImageSource( ProcessedMultiChannelImageSource[] sources, int resolutionMethod ) {
		this.sources = sources;
		this.resolutionMethod = resolutionMethod;
	}		
	
	public MultipleProcessedMultiChannelImageSource( ProcessedMultiChannelImageSource[] sources ) {
		this( sources, RESOLVE_RANDOM );
	}

	public int[][][] getProcessedMultiChannelImage() {
		int sourceIx;
		if( resolutionMethod == RESOLVE_RANDOM )
		{
			sourceIx = rnd.nextInt( sources.length );
		}else
		{
			lastSource = ( lastSource + 1 ) % sources.length;
			sourceIx = lastSource;
		}
		int[][][] rv = sources[ sourceIx ].getProcessedMultiChannelImage();
		rotation = sources[ sourceIx ].getRotation();
		return rv;
	}

	public double getRotation() {
		return rotation;
	}
	
	public void setSeed( long seed )
	{
		rnd.setSeed(seed);
	}

	public String toString()
	{
		StringBuffer rv= new StringBuffer();
		rv.append("Multiple Processed Multi Channel Image Source with:");
		rv.append("\n\t\t\t\tResolution Method = " + this.RESOLUTION_METHOD_LABELS[ this.resolutionMethod ] );
		for( int i = 0; i < this.sources.length; i++ )
		{
			rv.append("\n\t\t\t\tSource " + i + ": " + sources[ i ].toString() );
		}
		return rv.toString();
	}
	
}
