package org.evors.vision;

import java.awt.image.BufferedImage;
import java.util.BitSet;

import org.evors.TestUtils;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class HaarFilterTest extends TestCase {

	ImageSource imgSource = VisionTestLib.getImageSource();
	ProcessedGreyImageSource pgimgSrc = new ImageGreyProcessor( imgSource );
	VisualSensorGroup vsg = new VisualSensorGroup( pgimgSrc );
	VisualSensor vs = new VisualSensor( vsg, VisualSensorGroup.BITS_FILTER_TYPE, VisualSensorGroup.BITS_CENTRE_X, VisualSensorGroup.BITS_CENTRE_Y, VisualSensorGroup.BITS_HEIGHT );
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
			"0110100000100010000",
			"0100110010110001101",
			"0000001000010000100",
			"1101011000100001000",
			"1001101001000101000",
			"1010100000100001000"
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
		double orientation = Math.PI / 2;
		for( int fl = 0; fl < encoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < encoded[fl ].length(); bl++ ) if( encoded[fl].charAt(bl)=='1') bits.set(bl);
			
			vs.program( bits );
			int[][] img = pgimgSrc.getImage();
			double value = vs.getValue(img, orientation, imgCentre);
			
			// ShowImg si = new ShowImg( img ); si.show();
		}
		//try { Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
	}

	
	public void testValuePositive()
	{
		double orientation = Math.PI / 2;
		FakeImageSource fakeImgSrc = new FakeImageSource();
		ProcessedGreyImageSource fakepgImgSrc = new ImageGreyProcessor( fakeImgSrc );
		VisualSensorGroup vsg2 = new VisualSensorGroup( fakepgImgSrc );
		VisualSensor vs2 = new VisualSensor( vsg, VisualSensorGroup.BITS_FILTER_TYPE, VisualSensorGroup.BITS_CENTRE_X, VisualSensorGroup.BITS_CENTRE_Y, VisualSensorGroup.BITS_HEIGHT );
		Vec2 fakeImgCentre = new Vec2( 404, 238 );
		
		for( int fl = 0; fl < encoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < encoded[fl ].length(); bl++ ) if( encoded[fl].charAt(bl)=='1') bits.set(bl);
			vs2.program( bits );
			
			fakeImgSrc.setID( fl );
			int[][] img = fakepgImgSrc.getImage();
			
			double value = vs2.getValue( img, orientation, fakeImgCentre);
			//try { ShowImg si = new ShowImg( img ); si.show();Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
			assertEquals( 1, value, 0.15 );
		}
	}
	
	public void testValueNegative()
	{
		String[] negEncoded = {
				"1000100000100010000",
				"0010110010110001101",
				"1110001000010000100",
				"1011011000100001000",
				"0111101001000101000",
				"1100100000100001000"
		};
		
		double orientation = Math.PI / 2;
		FakeImageSource fakeImgSrc = new FakeImageSource();
		ProcessedGreyImageSource fakepgImgSrc = new ImageGreyProcessor( fakeImgSrc );
		VisualSensorGroup vsg2 = new VisualSensorGroup( fakepgImgSrc );
		VisualSensor vs2 = new VisualSensor( vsg, VisualSensorGroup.BITS_FILTER_TYPE, VisualSensorGroup.BITS_CENTRE_X, VisualSensorGroup.BITS_CENTRE_Y, VisualSensorGroup.BITS_HEIGHT );
		Vec2 fakeImgCentre = new Vec2( 404, 238 );
		
		for( int fl = 0; fl < negEncoded.length; fl++ )
		{
			BitSet bits = new BitSet();
			for( int bl = 0; bl < negEncoded[fl ].length(); bl++ ) if( negEncoded[fl].charAt(bl)=='1') bits.set(bl);
			vs2.program( bits );
			
			fakeImgSrc.setID( fl );
			int[][] img = fakepgImgSrc.getImage();
			
			double value = vs2.getValue( img, orientation, fakeImgCentre);
			// if( fl==0) try { ShowImg si = new ShowImg( img ); si.show();Thread.currentThread().sleep( 1000 * 600 );} catch (InterruptedException e) {}
			
			assertEquals( 0, value, 0.15 );
		}
	}
}
