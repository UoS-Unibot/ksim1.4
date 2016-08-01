package org.evors.rs.kjunior;

import org.evors.rs.sim.core.SimulationWorld;

public class BuggyIRSimulatedKJunior extends SimulatedKJunior
{
	int bugTypeIx = 0;
	
	double d_th = 3583;
	double d_0 = 3450;
	double delta = 2935;
	
	static final int FRONT_IR_IX = 2;
	static final String[] bugTypeStr = { "Off", "Weak" };
	
	public BuggyIRSimulatedKJunior(SimulationWorld world, double timeStepLength )
	{
		super( world, timeStepLength );
	}
	
	public double[] getInput()
	{
		double[] rv = super.getInput();
		
		double front = rv[ FRONT_IR_IX ];
		
		if( bugTypeIx == 0 )
		{
			front = 0;
		}else
		{
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
		}
		
		rv[ FRONT_IR_IX ] = front;
		
		return rv;
	}

	public String toString()
	{
		String rv = "BuggyIRSimulatedKJunior with bug type = " + bugTypeStr[ bugTypeIx ];
		rv += "\r\n\tinner robot: " + super.toString();
		return rv;
	}
	
}
