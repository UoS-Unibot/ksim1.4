package org.evors.vision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.evors.TestUtils;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class VisualSensorGroupTest extends TestCase {

	protected static String imgPath = "/Users/michaelgarvie/Documents/eh/camera/arena/";
	protected static FakePositionOrientationSource posSrc = new FakePositionOrientationSource();
	protected static StoredImageSource imgSrc = new StoredImageSource( imgPath, posSrc );
	
	public VisualSensorGroupTest() {
		// TODO Auto-generated constructor stub
	}
	
	public void testGetRGBHorizontal()
	{
		posSrc.setPosition( new Vec2( 0, 55 ) );
		BufferedImage img = imgSrc.getImage();
		
		int scanLeftX = ( int )( VisualSensorGroup.IMG_GUESS_CENTRE_X - VisualSensorGroup.IMG_DISC_RADIUS * 1.2 );
		int scanLength = ( int ) ( VisualSensorGroup.IMG_DISC_RADIUS * 2 * 1.2 );
		
		int[] pixelValues = new int[ scanLength ];
		img.getRGB(scanLeftX, VisualSensorGroup.IMG_GUESS_CENTRE_Y, scanLength, 1, pixelValues, 0, scanLength);
		
		for( int pl = 0; pl < pixelValues.length; pl++ )
		{
			assertEquals( img.getRGB( scanLeftX+pl, VisualSensorGroup.IMG_GUESS_CENTRE_Y), pixelValues[ pl ] );
		}
		
	}
	
	public void testGetRGBVertical()
	{
		posSrc.setPosition( new Vec2( 0, 55 ) );
		BufferedImage img = imgSrc.getImage();
		
		int scanTopY = ( int ) ( VisualSensorGroup.IMG_GUESS_CENTRE_Y - VisualSensorGroup.IMG_DISC_RADIUS * 1.2 );
		int scanLength = ( int ) ( VisualSensorGroup.IMG_DISC_RADIUS * 2 * 1.2 );
		
		int[] pixelValues = new int[ scanLength ];
		img.getRGB(VisualSensorGroup.IMG_GUESS_CENTRE_X, scanTopY, 1, scanLength, pixelValues, 0, 1);
		
		for( int pl = 0; pl < pixelValues.length; pl++ )
		{
			//System.out.println(pixelValues[ pl ]& 0xff);
			assertEquals( img.getRGB( VisualSensorGroup.IMG_GUESS_CENTRE_X, scanTopY+pl), pixelValues[ pl ] );
		}
		
	}
	
	public void testFindCentre()
	{
		Vec2[] locations = {
				
				new Vec2( 11.35, 55 ),
				new Vec2( 66.35, 55 ),
				new Vec2( 0, 55 )
				
		};
		double[] orientations = {
				0,
				Math.PI / 2,
				0
		};
		Vec2[] centres = {
				new Vec2( 404, 260 ),
				 new Vec2( 404, 260 ),//new Vec2( 402, 259 ),
				 new Vec2( 404, 260 )
		};
		
		for( int ll = 0; ll < locations.length; ll++ )
		{
			posSrc.setPosition( locations[ ll ] );
			posSrc.setOrientation(orientations[ ll ] );
			BufferedImage img = imgSrc.getImage();
			
			Vec2 calcCentre = VisualSensorGroup.findCentre( img );
			Vec2 storedCentre = centres[ ll ];
			
			img.setRGB( (int)calcCentre.x, (int)calcCentre.y, Color.WHITE.getRGB() );
			ShowImg si = new ShowImg( img ); si.show();
			
			try {
				if(ll==2)Thread.currentThread().sleep( 1000 * 600 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TestUtils.assertVEq( storedCentre, calcCentre);
		}
		
	}

}
