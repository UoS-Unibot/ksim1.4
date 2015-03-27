package org.evors.rs.sandpit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import org.evors.core.geometry.Vec2;

/**
 * Traces the path of a robot throughout a simulation, rendering it.
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class PathTracer {
    Path2D curPath = new Path2D.Double();

    public PathTracer(Vec2 initPosition) {
        //initialise with robot initial position
        curPath.moveTo(initPosition.x, initPosition.y);
    }
    
    public void step(Vec2 newPosition) {
        //Adds a point to the path
        curPath.lineTo(newPosition.x, newPosition.y);
    }
    
    public void draw(Graphics2D g2) {
        //draw the path so far
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(0.01f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g2.draw(curPath);
    }
}
