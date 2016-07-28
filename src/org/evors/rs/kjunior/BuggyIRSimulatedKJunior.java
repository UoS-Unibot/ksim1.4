package org.evors.rs.kjunior;

import org.evors.rs.sim.core.SimulationWorld;

public class BuggyIRSimulatedKJunior extends SimulatedKJunior
{
	double d_th = 3583;
	double d_0 = 3450;
	double delta = 2935;
	
	int FRONT_IR_IX = 2;
	
	public BuggyIRSimulatedKJunior(SimulationWorld world, double timeStepLength )
	{
		super( world, timeStepLength );
	}
	
	public double[] getInput()
	{
		double[] rv = super.getInput();
		
		double front = rv[ FRONT_IR_IX ];
		
		if( front > d_th )
		{
			front -= delta;
		}else if( front < d_0 )
		{
			front = 0;
		}else
		{
			front = ( front - delta ) * ( d_0 - front ) / ( d_0 - d_th );
		}
		
		rv[ FRONT_IR_IX ] = 0; // *** front;
		
		return rv;
	}
	
}
