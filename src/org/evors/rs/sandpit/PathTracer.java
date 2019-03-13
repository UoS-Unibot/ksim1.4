package org.evors.rs.sandpit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import org.evors.core.geometry.Vec2;

/**
 * Traces the path of a robot throughout a simulation, rendering it.
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class PathTracer {
    GeneralPath curPath = new GeneralPath();

    public PathTracer(Vec2 initPosition) {
        //initialise with robot initial position
        curPath.moveTo((float)initPosition.x, (float)initPosition.y);
        
    }
    
    public void step(Vec2 newPosition) {
        //Adds a point to the path
        curPath.lineTo((float)newPosition.x, (float)newPosition.y);
    }
    
    public void draw(Graphics2D g2) {
        //draw the path so far
        //g2.setColor(Color.DARK_GRAY);
        //g2.setStroke(new BasicStroke(0.1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        
    	g2.setColor( Color.RED );
    	g2.setStroke( new BasicStroke( 1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1f, new float[]{ 1f }, 0f ) );
    	g2.draw(curPath);        
    }
}
