package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.evors.TestUtils;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class StoredImageSourceTest extends TestCase {

	protected static String imgPath = "/Users/michaelgarvie/Documents/eh/camera/arena/";
	
	FakePositionOrientationSource posSrc = new FakePositionOrientationSource();
	StoredImageSource imgSrc = new StoredImageSource(imgPath, posSrc);
	
	public StoredImageSourceTest(String name) {
		super(name);
		posSrc.setOrientation(0);
	}
	
	public void testCoordinateToImage()
	{
		Vec2[] locations = {
				new Vec2( 11.35, 55 ),
				new Vec2( 66.35, 55 ),
				new Vec2( 6,6 ),
				new Vec2( 500,500)
		};
		
		Vec2[] imgCood = {
				new Vec2( 1, 9 ),
				new Vec2( 12, 9 ),
				new Vec2( 0,0 ),
				new Vec2( 12, 20 )
		};
		
		for( int ll = 0; ll < locations.length; ll++ )
		{
			posSrc.setPosition( locations[ ll ] );
			
			TestUtils.assertVEq( imgCood[ ll ], imgSrc.getImageFileCoordinates());
		}
		
	}

}
