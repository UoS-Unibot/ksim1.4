package org.evors.rs.kjunior;

import org.evors.core.EvoRSLib;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Shape2D;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;

public class KJuniorIRBeam extends IRBeam {

	public static final double MAX_LENGTH = 25;
	public static final int N_BEAMS = 5;
	public static final double[] DIV_ANGLES = { -0.437, -0.218, 0, 0.218, 0.437 };
	
	public KJuniorIRBeam(Vec2 basePoint, double centralAngle, SimulationWorld world) {
		super(basePoint, centralAngle, MAX_LENGTH, world, N_BEAMS, 0 );
		
		for (int i = 0; i < N_BEAMS; i++) {
            double angle = centralAngle + DIV_ANGLES[ i ];
            beams[ i ] = Line.fromPolarVec(basePoint, angle, maxLength);
        }
	}

	public double getReading() {
		SimulationWorld.TraceRayResult trrL = world.traceRayFull(beams[0]);
		SimulationWorld.TraceRayResult trrR = world.traceRayFull(beams[4]);
		
		double reading;
		if( Double.isNaN( trrL.getDistance() ) && Double.isNaN( trrR.getDistance() ) )
		{
			reading = 0;
		}else if( trrL.getObject() == trrR.getObject() )
		{
			reading = getFullIRVal( beams[ 2 ], trrL.getObject() );
		}else
		{
			return super.getReading(); // This already adds noise
		}
        
        //add noise
        reading += EvoRSLib.uniformNoise( IR_NOISE );
        if(Double.isNaN(reading) || reading < 0)
            return 0;
        return Math.round(reading);
    }
	
	public double getFullIRVal( Line beam, Shape2D obj )
	{
		double dist = obj.getSmallestIntersection( beam ).intersectionPoint.distance( beam.p1 ); // Not sure if LineLine.getSmallestIntersectDistance would work
		return convertDistToReading( dist );
	}
}
