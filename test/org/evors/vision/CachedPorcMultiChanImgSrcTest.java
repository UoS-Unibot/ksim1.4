package org.evors.vision;

import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class CachedPorcMultiChanImgSrcTest extends TestCase {

	protected static String imgPath = "/Users/michaelgarvie/git/trainingData/vision/arena19Aug/";
	FakePositionOrientationSource posSrc = new FakePositionOrientationSource();
	StoredImageSource imgSrc = new StoredImageSource(imgPath, posSrc);
	ImageWhiteBalanceBaseGreyRedProcessor procImgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( imgSrc );
	CachedProcessedMultiChannelImageSource cachedProcImgSrc = new CachedProcessedMultiChannelImageSource( procImgSrc, imgSrc );
	
	public CachedPorcMultiChanImgSrcTest( String name ) {
		super( name );
		procImgSrc.debugMode = true;
	}
	
	public void testMultiLocation()
	{
		Vec2[] locations = {
			new Vec2( 6.8, 9 ), // 0,0
			new Vec2( 3, 3 ), // 0,0 (min)
			new Vec2( 6.8 + 5*3, 9 + 5*4 ), // 3, 4
			new Vec2( 6.7 + 5*3, 8.8 + 5*4 ), // 3,4 (rounding, not flooring)
			new Vec2( 6.8 + 5*3, 9 - 3.7 + 5 * 4 + 2.5 ), // 3,4 (closest to 3,4 N)
			new Vec2( 85 - 6, 114 - 5 - 3.7 ), // 13, 19
			new Vec2( 85, 114 ) // 13,19 (max)
		};
		
		int[] procCount = {
				1,
				1,
				2,
				2,
				2,
				3,
				3
		};
		
		int[][][] lastOne = null;
		for( int i = 0; i < locations.length; i++ )
		{
			posSrc.setPosition( locations[i]);
			int[][][] thisOne = cachedProcImgSrc.getProcessedMultiChannelImage();
			if( i > 0 && procCount[i]==procCount[i-1])
			{
				assertEquals( lastOne, thisOne );
			}
			assertEquals( procCount[i], procImgSrc.processedCount );
			lastOne = thisOne;
		}
		
	}

}
