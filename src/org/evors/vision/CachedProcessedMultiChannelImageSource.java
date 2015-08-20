package org.evors.vision;

import java.util.Hashtable;

public class CachedProcessedMultiChannelImageSource implements ProcessedMultiChannelImageSource {

	protected KeyGenerator cacheKeyGen;
	protected ProcessedMultiChannelImageSource imgSrc;
	protected Hashtable imgCache = new Hashtable();
	
	public CachedProcessedMultiChannelImageSource( ProcessedMultiChannelImageSource imgSrc, KeyGenerator cacheKeyGen ) {
		this.cacheKeyGen = cacheKeyGen;
		this.imgSrc = imgSrc;
	}

	public short[][][] getProcessedMultiChannelImage() {
		Object key = cacheKeyGen.getKey();
		if( imgCache.get(key) == null )
		{
			imgCache.put(key, imgSrc.getProcessedMultiChannelImage() );
		}
		return ( short[][][] ) imgCache.get(key);
	}

	public double getRotation() {
		return imgSrc.getRotation();
	}

	public String toString()
	{
		return "Cached Processed Multi Channel Image Source with Inner Source:" + imgSrc.toString();
	}
	
}
