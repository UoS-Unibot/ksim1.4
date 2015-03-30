package org.evors.core;

import java.util.Random;

import org.evors.core.geometry.Line;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;

/**
 * Library file with shared objects and functions
 * 
 * @author michaelgarvie
 *
 */
public abstract class EvoRSLib {
	
	/**
	 * Shared random number generator to allow control of seed.
	 */
	public static final Random random = new Random();
	
	public static double uniformNoise( double noiseCoeff )
	{
		return ( random.nextDouble() * 2 * noiseCoeff - noiseCoeff );
	}
	
	public static SimulationWorld getStandardKSimPhilWorld()
	{
		SimulationWorld world = new SimulationWorld( new Vec2( 150, 150 ));
		world.createWorldObject( Line.fromCoords( -25, -50, -5, -45 ) );
		world.createWorldObject( Line.fromCoords( -5, -45, -5, 45));
		world.createWorldObject( Line.fromCoords( -5, 45, 15, 47));
		return world;
	}
}
