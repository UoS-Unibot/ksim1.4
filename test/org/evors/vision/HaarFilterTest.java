package org.evors.vision;

import java.awt.image.BufferedImage;
import java.util.BitSet;

import org.evors.TestUtils;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class HaarFilterTest extends TestCase {

	protected static String imgPath = "/Users/michaelgarvie/git/trainingData/vision/arena19Aug/";
	FakePositionOrientationSource posSrc = new FakePositionOrientationSource();
	StoredImageSource imgSource = new StoredImageSource(imgPath, posSrc);
	StoredImageSource imgSSource = new StoredImageSource( imgPath, posSrc, Math.PI, "S" );
	StoredImageSource imgWSource = new StoredImageSource( imgPath, posSrc, Math.PI / 2, "W" );
	
	ProcessedMultiChannelImageSource pgimgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( imgSource );
	ProcessedMultiChannelImageSource pgSimgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( imgSSource );
	ProcessedMultiChannelImageSource pgWimgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( imgWSource );
	VisualSensorGroup vsg = new VisualSensorGroup( pgimgSrc );
	VisualSensor vs = new VisualSensor( vsg, VisualSensorGroup.BITS_FILTER_TYPE, VisualSensorGroup.BITS_CENTRE_X, VisualSensorGroup.BITS_CENTRE_Y, VisualSensorGroup.BITS_HEIGHT, VisualSensorGroup.BITS_CHANNEL );
	Vec2 imgCentre = new Vec2( VisualSensorGroup.IMG_GUESS_CENTRE_X, VisualSensorGroup.IMG_GUESS_CENTRE_Y );
	
	HaarFilter[] filterMapping = new HaarFilter[]
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
	
	String[] encoded = {
			"01101000001000100000",
			"01001100101100011011",
			"00000010000100001000",
			"11010110001000010001",
			"10011010010001010000",
			"10101000001000010001"
	};
	
	public HaarFilterTest( String name) {
		super(name);
	}
	
	public void testDimensionCalculation()
	{

		Vec2[] dimensions = {
				new Vec2(1,1),
				new Vec2(3,1),
				new Vec2(3,1),
				new Vec2(1,3),
				new Vec2(1,3),
				new Vec2(2,2),
				new Vec2(2,2),
				new Vec2(1,1)
		};
		
		for( int fl = 0; fl < filterMapping.length; fl++ )
		{
			TestUtils.assertVEq( dimensions[ fl ], filterMapping[ fl ].getProportionalDimension() );
		}
	}
	
	public void testMapGeneration()
	{
		boolean[][][] inverseMap = { // White (.) is true
				{ { true } },
				{ { true},{ false}, {true } },
				{ { false}, {true}, {false } },
				{ { true, false , true } },
				{ { false, true,false } },
				{ { true, false }, { false, true } },
				{ { false, true }, { true, false } },
				{ { false } }
		};
		
		for( int fl = 0; fl < filterMapping.length; fl++ )
		{
			boolean[][] genMap = filterMapping[ fl ].getMap();
			// System.out.println("Got genMap is " + genMap.length + " by " + genMap[0].length );
			for( int cl = 0; cl < inverseMap[ fl ].length; cl++ )
			{
				for( int rl = 0; rl < inverseMap[ fl ][ cl ].length; rl++ )
				{
					// System.out.println(fl+" "+cl+" "+rl+" "+inverseMap[ fl ][ cl ][ rl ]+" "+genMap[ cl ][ rl ]);
					
					assertEquals( !inverseMap[ fl ][ cl ][ rl ], genMap[ cl ][ rl ] );
				}
			}
		}
	}

	public void testFilterPosition()
	{
		double orientation = Math.PI * 3 / 2;
		posSrc.setOrientation(orientation);
		for( int fl = 0; fl < encoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < encoded[fl ].length(); bl++ ) if( encoded[fl].charAt(bl)=='1') bits.set(bl);
			
			vs.program( bits );
			int[][][] img = pgimgSrc.getProcessedMultiChannelImage();
			BufferedImage debugImg = this.imgSource.getImage();
			double value = vs.getValue(img, pgimgSrc.getRotation(), imgCentre, debugImg);
			
			//ShowImg si = new ShowImg( debugImg ); si.show(); si.setLocation(400, 400 );
		}
		//try { Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
	}

	public void testFilterPositionSouthFacing()
	{
		double orientation = Math.PI * 3 / 2;
		posSrc.setOrientation(orientation);
		for( int fl = 0; fl < encoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < encoded[fl ].length(); bl++ ) if( encoded[fl].charAt(bl)=='1') bits.set(bl);
			
			vs.program( bits );
			int[][][] img = pgSimgSrc.getProcessedMultiChannelImage();
			BufferedImage debugImg = this.imgSSource.getImage();
			double value = vs.getValue(img, pgSimgSrc.getRotation(), imgCentre, debugImg);
			
			//ShowImg si = new ShowImg( debugImg ); si.show();
		}
		//try { Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
	}
	
	public void testFilterPositionWestFacing()
	{
		double orientation = Math.PI * 3 / 2;
		posSrc.setOrientation(orientation);
		for( int fl = 0; fl < encoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < encoded[fl ].length(); bl++ ) if( encoded[fl].charAt(bl)=='1') bits.set(bl);
			
			vs.program( bits );
			int[][][] img = pgSimgSrc.getProcessedMultiChannelImage();
			BufferedImage debugImg = this.imgWSource.getImage();
			double value = vs.getValue(img, pgWimgSrc.getRotation(), imgCentre, debugImg);
			
			ShowImg si = new ShowImg( debugImg ); si.show();
		}
		try { Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
	}
	
	public void testValuePositive()
	{
		double orientation = Math.PI / 2;
		FakeImageSource fakeImgSrc = new FakeImageSource();
		ImageWhiteBalanceBaseGreyRedProcessor fakepgImgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( fakeImgSrc );
		VisualSensorGroup vsg2 = new VisualSensorGroup( fakepgImgSrc );
		VisualSensor vs2 = new VisualSensor( vsg, VisualSensorGroup.BITS_FILTER_TYPE, VisualSensorGroup.BITS_CENTRE_X, VisualSensorGroup.BITS_CENTRE_Y, VisualSensorGroup.BITS_HEIGHT, VisualSensorGroup.BITS_CHANNEL );
		Vec2 fakeImgCentre =  new Vec2( 404, 238 );
		
		for( int fl = 0; fl < encoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < encoded[fl ].length(); bl++ ) if( encoded[fl].charAt(bl)=='1') bits.set(bl);
			vs2.program( bits );
			
			fakeImgSrc.setID( fl + 10 );
			int[][][] img = fakepgImgSrc.getProcessedMultiChannelImage();
			BufferedImage debugImg = fakeImgSrc.getImage();
			
			double value = vs2.getValue( img, orientation, fakeImgCentre, debugImg);
			//try { ShowImg si = new ShowImg( debugImg ); si.show();Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
			assertEquals( 1, value, 0.15 );
		}
	}
	
	public void testValueNegative()
	{
		String[] negEncoded = {
				"10001000001000100000",
				"00101100101100011011",
				"11100010000100001000",
				"10110110001000010001",
				"01111010010001010000",
				"11001000001000010001"
		};
		
		double orientation = Math.PI / 2;
		FakeImageSource fakeImgSrc = new FakeImageSource();
		ImageWhiteBalanceBaseGreyRedProcessor fakepgImgSrc = new ImageWhiteBalanceBaseGreyRedProcessor( fakeImgSrc );
		VisualSensorGroup vsg2 = new VisualSensorGroup( fakepgImgSrc );
		VisualSensor vs2 = new VisualSensor( vsg, VisualSensorGroup.BITS_FILTER_TYPE, VisualSensorGroup.BITS_CENTRE_X, VisualSensorGroup.BITS_CENTRE_Y, VisualSensorGroup.BITS_HEIGHT, VisualSensorGroup.BITS_CHANNEL );
		Vec2 fakeImgCentre = new Vec2( 404, 238 );
		
		for( int fl = 0; fl < negEncoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < negEncoded[fl ].length(); bl++ ) if( negEncoded[fl].charAt(bl)=='1') bits.set(bl);
			vs2.program( bits );
			
			fakeImgSrc.setID( fl + 10);
			int[][][] img = fakepgImgSrc.getProcessedMultiChannelImage();
			BufferedImage debugImg = fakeImgSrc.getImage();
			
			double value = vs2.getValue( img, orientation, fakeImgCentre, debugImg);
			//if( fl==0) try { ShowImg si = new ShowImg( debugImg ); si.show();Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
			
			assertEquals( 0, value, 0.15 );
		}
	}
}
