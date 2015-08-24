package org.evors.vision;

import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class WhiteBalanceBaseTest extends TestCase {
	
	protected static String imgPath = "/Users/michaelgarvie/git/trainingData/vision/arena19Aug/quarter/";
	FakePositionOrientationSource posSrc = new FakePositionOrientationSource();
	StoredImageSource imgSrc = new StoredImageSource(imgPath, posSrc);
	StoredImageSource imgSSrc = new StoredImageSource( imgPath, posSrc, Math.PI, "S" );
	
	ImageWhiteBalanceBaseGreyRedProcessor procImgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( imgSrc );
	ImageWhiteBalanceBaseGreyRedProcessor procSImgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( imgSSrc );
	ShowImg showImg = new ShowImg( imgSrc.getImage() );
	
	public static int IMG_WIDTH = 752, IMG_HEIGHT = 480, IMG_GUESS_CENTRE_X = 404, IMG_GUESS_CENTRE_Y = 260, IMG_DISC_RADIUS = 170, IMG_OUTER_RADIUS = 220; // 120,258 **** old img disc radius
	
	
	public WhiteBalanceBaseTest( String name) {
		super(name);
		showImg.show();
		procImgSrc.debugMode = procSImgSrc.debugMode = true;
	}
	
	public void testFindingColourPoints()
	{
		Vec2[] locations = {
				new Vec2( 6.8, 9 ), // 0,0
				new Vec2( 6.8 + 5*6, 9 + 5*12 ), // 6,12
				new Vec2( 6.8 + 5*3, 9-3.7*2+5*13 ) // 
		};
		
		ImageWhiteBalanceBaseGreyRedProcessor[] srcs = {
				procImgSrc,
				procImgSrc,
				procSImgSrc
		};
		
		Vec2 centre = new Vec2( IMG_GUESS_CENTRE_X, IMG_GUESS_CENTRE_Y );
		double r_out = IMG_OUTER_RADIUS, r_in = IMG_DISC_RADIUS;
		double scale = 4;
		centre = new Vec2( centre.x/scale, centre.y/scale );
		r_out /= scale; r_in /= scale;
		
		for( int i = 0; i < locations.length; i++ )
		{
			posSrc.setPosition( locations[ i ] );
			short[][][] pImg = srcs[ i ].getProcessedMultiChannelImage();
			int darkest = Integer.MAX_VALUE;
			Vec2 darkestP = null;
			int reddest = -1;
			Vec2 reddestP = null;
			for( int x = 0; x < pImg[0].length; x++ )
			{
				for( int y = 0; y < pImg[0][0].length; y++ )
				{
					Vec2 p = new Vec2( x,y );
					double r = p.distance( centre );
					if( r > r_in && r < r_out )
					{
						if( pImg[ImageWhiteBalanceBaseGreyRedProcessor.IX_GREY][x][y] < darkest )
						{
							darkest = pImg[ImageWhiteBalanceBaseGreyRedProcessor.IX_GREY][x][y];
							darkestP = p;
						}
						if( pImg[ImageWhiteBalanceBaseGreyRedProcessor.IX_RED][x][y] > reddest )
						{
							reddest = pImg[ImageWhiteBalanceBaseGreyRedProcessor.IX_RED][x][y];
							reddestP = p;
						}
					}
				}
			}
			
			System.out.println( "Looking at image " + i );
			System.out.println( "Darkest found at " + darkestP );
			System.out.println( "Reddest found at " + reddestP );
			
		}
	}
	
	public void testExamples() throws InterruptedException
	{
		Vec2[] locations = {
				new Vec2( 6.8, 9 ), // 0,0
				new Vec2( 6.8 + 5*6, 9 + 5*12 )
		};
		
		for( int i = 0; i < locations.length; i++ )
		{
			posSrc.setPosition( locations[ i ] );
			procImgSrc.getProcessedMultiChannelImage();
			showImg.setImage( procImgSrc.debugImage );
			//Thread.currentThread().sleep( 5000 );
		}
	}

}
