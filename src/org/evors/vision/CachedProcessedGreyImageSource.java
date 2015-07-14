package org.evors.vision;

import java.util.Hashtable;

public class CachedProcessedGreyImageSource implements ProcessedGreyImageSource {

	protected KeyGenerator cacheKeyGen;
	protected ProcessedGreyImageSource imgSrc;
	protected Hashtable imgCache = new Hashtable();
	
	public CachedProcessedGreyImageSource( ProcessedGreyImageSource imgSrc, KeyGenerator cacheKeyGen ) {
		this.cacheKeyGen = cacheKeyGen;
		this.imgSrc = imgSrc;
	}

	public int[][] getImage() {
		Object key = cacheKeyGen.getKey();
		if( imgCache.get(key) == null )
		{
			imgCache.put(key, imgSrc.getImage() );
		}
		return ( int[][] ) imgCache.get(key);
	}

	public double getRotation() {
		return imgSrc.getRotation();
	}

}
