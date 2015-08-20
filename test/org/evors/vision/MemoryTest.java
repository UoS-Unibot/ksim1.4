package org.evors.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

public class MemoryTest extends TestCase {

	protected static String imagePath = "/Users/michaelgarvie/git/trainingData/vision/arena19Aug/";
	int loaded = 0;
	ArrayList a = new ArrayList();
	
	public MemoryTest( String name)  {
		super(name);
	}
	
	public void testMemory()
	{
		BufferedImage rv;
		for( int x = 0; x <= 13; x++ )
		{
			for( int y = 0; y <= 19; y++ )
			{
				String ext = "N";
				String imageFile = imagePath + x + "_" + y + "-" + ext + ".jpg"; // [sic] bad day when captured!..
				System.out.println(loaded++ + "\t" + imageFile + "\t" + Runtime.getRuntime().totalMemory() + "\t" + Runtime.getRuntime().freeMemory() + "\t" );
				a.add( new byte[2][752][480] );
				
				try {
					rv = ImageIO.read(new File( imageFile ));
				} catch (IOException e) {
					e.printStackTrace();
					System.exit( 0 );
				}
				
				ext = "S";
				imageFile = imagePath + x + "_" + y + "-" + ext + ".jpg"; // [sic] bad day when captured!..
				System.out.println(loaded++ + "\t" + imageFile + "\t" + Runtime.getRuntime().totalMemory() + "\t" + Runtime.getRuntime().freeMemory() + "\t" );
				a.add( new byte[2][752][480] );
				
				try {
					rv = ImageIO.read(new File( imageFile ));
				} catch (IOException e) {
					e.printStackTrace();
					System.exit( 0 );
				}
				
				System.gc();
				
			}
		}
	}

}
