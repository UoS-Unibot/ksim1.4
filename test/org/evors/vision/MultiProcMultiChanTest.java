package org.evors.vision;

import junit.framework.TestCase;

public class MultiProcMultiChanTest extends TestCase {

	FakeImageSource fakeImgSrcN = new FakeImageSource();
	FakeImageSource fakeImgSrcS = new FakeImageSource();
	ProcessedMultiChannelImageSource fakeProcImgSrcN = new ImageWhiteBalanceBaseGreyRedProcessor( fakeImgSrcN );
	ProcessedMultiChannelImageSource fakeProcImgSrcS = new ImageWhiteBalanceBaseGreyRedProcessor( fakeImgSrcS );
	ProcessedMultiChannelImageSource[] sources = { fakeProcImgSrcN, fakeProcImgSrcS };
	
	public MultiProcMultiChanTest( String name ) {
		super(name);
		fakeImgSrcN.setID( 6 );
		fakeImgSrcS.setID( 7 );
		fakeImgSrcS.setRotation( Math.PI );
	}
	
	public void testRandom()
	{
		MultipleProcessedMultiChannelImageSource mpmci = new MultipleProcessedMultiChannelImageSource( sources, MultipleProcessedMultiChannelImageSource.RESOLVE_RANDOM  );
		
		for( int i = 0; i < 10; i++ )
		{
			int[][][] pImg = mpmci.getProcessedMultiChannelImage();
			double orientation = mpmci.getRotation();
			
			double desOrientation = pImg[0][0][0] > 123 ? 0 : Math.PI;
			assertEquals( orientation, desOrientation, 0.00001 );
		}
	}
	
	public void testIterate()
	{
		MultipleProcessedMultiChannelImageSource mpmci = new MultipleProcessedMultiChannelImageSource( sources, MultipleProcessedMultiChannelImageSource.RESOLVE_ITERATE  );
		
		for( int i = 0; i < 10; i++ )
		{
			int[][][] pImg = mpmci.getProcessedMultiChannelImage();
			double orientation = mpmci.getRotation();
			
			double desOrientation = i % 2 == 0 ? 0 : Math.PI;
			
			assertEquals( orientation, desOrientation, 0.00001 );
		}
	}

}
