package org.evors.vision;

import java.awt.Point;

import junit.framework.TestCase;

public class CheckGRValues extends TestCase {

	Point[] cyans = { null, new Point( 78, 108 ), 
			 new Point( 155, 61 ), 
			 new Point( 69, 23 ),
			 new Point( 74, 53 ), 
			 new Point( 96, 113 ), 
			 new Point( 83, 107 ),
			 new Point( 70, 21	 ),
			 new Point( 147, 85 ), 
			 new Point( 152, 52 ), 
			 new Point( 128, 17 ), 
			 new Point( 128, 21 ), 
			 new Point( 125, 26 ), 
			 new Point( 134, 102 ), 
			 
	};
	
	Point[] whites = { null, new Point( 47, 44 ),
			 new Point( 69, 103 ),
			 new Point( 150, 83 ),
			 new Point( 143, 46 ),
			 new Point( 103, 115 ),
			 new Point( 109, 106 ),
			 new Point( 121, 18 ),
			 new Point( 54, 88 ),
			 new Point( 88, 13 ),
			 new Point( 121, 13 ), 
			 new Point( 77, 17 ),
			 new Point( 67, 27 ),
			 new Point( 83, 113 ), };
	
	protected static String imgPath = "/Users/michaelgarvie/dev/eh/fotos/cylmazecolours/";
	FakeImageSource fakeImgSrc = new FakeImageSource( imgPath );
	
	ImageWhiteBalanceBaseGreyRedProcessor greyRed = new ImageWhiteBalanceBaseGreyRedProcessor( fakeImgSrc );
	
	ShowImg showImg;
	
	public CheckGRValues() {
		greyRed.debugMode = true;
	}

	public void testCheckGRValue()
	{
		String[] cylCol = { "Cyan", "White" };
		String[] chanNam ={ "Grey", "Red" };
		Point[][] allPs = { cyans, whites };
		
		int cyl = 1, chan = 1;
		
		System.out.println( "Doing " + chanNam[ chan ] + " channel of " + cylCol[ cyl ] + " cyls");
		
		for( int i = 1; i < cyans.length; i++ )
		{
			fakeImgSrc.setID( i );
			short[][][] data = greyRed.getProcessedMultiChannelImage();
			System.out.println( data[ chan ][ allPs[ cyl ][ i ].x ][ allPs[ cyl ][ i ]. y ] );
		}
	}
	
}
