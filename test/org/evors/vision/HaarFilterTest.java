package org.evors.vision;

import org.evors.TestUtils;
import org.evors.core.geometry.Vec2;

import junit.framework.TestCase;

public class HaarFilterTest extends TestCase {

	HaarFilter[] filterMapping = new HaarFilter[]
			{ 
				new HaarFilter("x"), //0
				new HaarFilter("x.x"),
				new HaarFilter(".x."),
				new HaarFilter("x\n.\nx"),
				new HaarFilter(".\nx\n."), // 4
				new HaarFilter("x.\n.x"),
				new HaarFilter(".x\nx."),
				new HaarFilter("x")
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
				{ { true } }
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
		
	}
}
