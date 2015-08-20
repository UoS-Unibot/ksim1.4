package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.evors.TestUtils;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class StoredImageSourceTest extends TestCase {

	protected static String imgPath = "/Users/michaelgarvie/git/trainingData/vision/arena19Aug/";
	
	FakePositionOrientationSource posSrc = new FakePositionOrientationSource();
	StoredImageSource imgSrc = new StoredImageSource(imgPath, posSrc);
	StoredImageSource imgSSrc = new StoredImageSource( imgPath, posSrc, Math.PI, "S" );
	
	public StoredImageSourceTest(String name) {
		super(name);
		posSrc.setOrientation(0);
	}
	
	public void testAllNorth()
	{
		posSrc.setOrientation(0);
		
		Vec2[] locations = {
			new Vec2( 6.8, 9 ), // 0,0
			new Vec2( 3, 3 ), // 0,0 (min)
			new Vec2( 6.8 + 5*3, 9 + 5*4 ), // 3, 4
			new Vec2( 6.7 + 5*3, 8.8 + 5*4 ), // 3,4 (rounding, not flooring)
			new Vec2( 6.8 + 5*3, 9 - 3.7 + 5 * 4 + 2.5 ), // 3,4 (closest to 3,4 N)
			new Vec2( 85 - 6, 114 - 5 - 3.7 ), // 13, 19
			new Vec2( 85, 114 ) // 13,19 (max)
		};
		
		Vec2[] imgCoord = {
			new Vec2( 0,0 ),
			new Vec2( 0,0 ),
			new Vec2( 3,4 ),
			new Vec2( 3,4 ),
			new Vec2( 3,4 ),
			new Vec2( 13, 19),
			new Vec2( 13, 19)
		};
		
		for( int ll = 0; ll < locations.length; ll++ )
		{
			posSrc.setPosition( locations[ ll ] );
			TestUtils.assertVEq( imgCoord[ ll ], imgSrc.getImageFileCoordinates());
		}
	}
	
	public void testSouthSampled()
	{
		posSrc.setOrientation(0);
		
		Vec2[] locations = {
			new Vec2( 6.8, 5.3 - 3.7 ), // 0,0
			new Vec2( 3, 0 ), // 0,0 (min)
			new Vec2( 6.8 + 5*3, 5.3 - 3.7 + 5*4 ), // 3, 4
			new Vec2( 6.7 + 5*3, 5 - 3.7 + 5*4 ), // 3,4 (rounding, not flooring)
			new Vec2( 6.8 + 5 *3, 9 - 3.7 + 5 * 4 + 2.5 ), // 3,5 (closest to 3,5 S)
			new Vec2( 85 - 6, 114 - 8.3 - 3.7 ), // 13, 19
			new Vec2( 85, 114 ) // 13,19 (max)
		};
		
		Vec2[] imgCoord = {
			new Vec2( 0,0 ),
			new Vec2( 0,0 ),
			new Vec2( 3,4 ),
			new Vec2( 3,4 ),
			new Vec2( 3,5 ),
			new Vec2( 13, 19),
			new Vec2( 13, 19)
		};
		
		for( int ll = 0; ll < locations.length; ll++ )
		{
			posSrc.setPosition( locations[ ll ] );

			TestUtils.assertVEq( imgCoord[ ll ], imgSSrc.getImageFileCoordinates());
		}
	}
	
	public void testNorthSampledMultipleAngles()
	{
		Vec2[] locations = {
			new Vec2( 6.8 + 5*3, 9 + 5*4 ), 
			new Vec2( 6.8 + 5*3, 9 + 5*4 ),  
			new Vec2( 6.8 + 5*3, 9 + 5*4 ),
			new Vec2( 6.8 + 5*3, 9 + 5*4 ),
			new Vec2( 6.8 + 5*3, 9 + 5*4 )
		};

		double[] orientation = {
			0,				// 3,4
			Math.PI / 4,  	// 4,4
			Math.PI * 3 / 4,//4,3
			Math.PI,		// 3,3
			Math.PI * 5/4,	// 2,3
			Math.PI * 7/4	// 2,4
		};

		Vec2[] imgCoord = {
			new Vec2( 3,4 ),
			new Vec2( 4,4 ),
			new Vec2( 4,3 ),
			new Vec2( 3,3 ),
			new Vec2( 2,3 ),
			new Vec2( 2,4 )
		};
		
		for( int ll = 0; ll < locations.length; ll++ )
		{
			posSrc.setPosition( locations[ ll ] );
			posSrc.setOrientation( orientation[ ll ]);
			
			TestUtils.assertVEq( imgCoord[ ll ], imgSrc.getImageFileCoordinates());
		}
	}
	
	public void testSouthSampledMultipleAngles()
	{
		Vec2[] locations = {
			new Vec2( 6.8 + 5*3, 9 + 5*4 ), 
			new Vec2( 6.8 + 5*3, 9 + 5*4 ),  
			new Vec2( 6.8 + 5*3, 9 + 5*4 ),
			new Vec2( 6.8 + 5*3, 9 + 5*4 ),
			new Vec2( 6.8 + 5*3, 9 + 5*4 )
		};

		double[] orientation = {
			0,				// 3,5
			Math.PI / 4,  	// 4,5
			Math.PI * 3 / 4,// 4,5
			Math.PI,		// 3,4
			Math.PI * 5/4,	// 2,4
			Math.PI * 7/4	// 2,5
		};

		Vec2[] imgCoord = {
			new Vec2( 3,5 ),
			new Vec2( 4,5 ),
			new Vec2( 4,4 ),
			new Vec2( 3,4 ),
			new Vec2( 2,4 ),
			new Vec2( 2,5 )
		};
		
		for( int ll = 0; ll < locations.length; ll++ )
		{
			posSrc.setPosition( locations[ ll ] );
			posSrc.setOrientation( orientation[ ll ]);
			
			TestUtils.assertVEq( imgCoord[ ll ], imgSSrc.getImageFileCoordinates());
		}
	}

}
