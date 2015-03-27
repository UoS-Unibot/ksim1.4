package org.evors.rs.sandpit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import org.evors.core.geometry.Circle;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Polygon;
import org.evors.core.geometry.Shape2D;
import org.evors.rs.kjunior.SimulatedKJunior;
import org.evors.rs.sim.core.SimulationWorld;

/**
 * A collection of static methods to render the simulation on a given graphics
 * context. This draws in world coordinates; transforms will need to be applied
 * to the graphics context for different scaling. Warning: this is very hacky
 * code but much more important to keep the visualisation code away from the
 * simulation logic!
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SandpitRenderer {

    //Stroke that lines will be rendered with.
    private static final Stroke bstroke = new BasicStroke(2f);

    /**
     * Draws all objects in the world on the specified graphics context. Draws
     * in world coordinates.
     *
     * @param g2 Graphics context.
     * @param world Simulation world to draw.
     */
    public static void drawWorld(Graphics2D g2, SimulationWorld world) {
        for (Iterator iterator = world.getObjects().iterator(); iterator.
                hasNext();) {
            //loop through all objects in the world
            Shape2D obj = (Shape2D) iterator.next();

            //render according to what the object is
            if (obj instanceof Line) {
                Line line = (Line) obj;
                g2.setColor(Color.BLACK);
                g2.setStroke(bstroke);
                g2.draw(new Line2D.Double(line.p1.x, line.p1.y, line.p2.x,
                        line.p2.y));
            } else if (obj instanceof Polygon) {
                Polygon poly = (Polygon) obj;
                g2.setColor(Color.blue);
                g2.setStroke(bstroke);
                Path2D path = new Path2D.Double();
                boolean firstPointSet = false;
                for (Iterator iterator1 = poly.getLines().iterator(); iterator1.
                        hasNext();) {
                    Line line = (Line) iterator1.next();
                    if (!firstPointSet) {
                        path.moveTo(line.p1.x, line.p1.y);
                        firstPointSet = true;
                    }
                    path.lineTo(line.p2.x, line.p2.y);
                }
                path.closePath();
                g2.fill(path);
            } else if (obj instanceof Circle) {
                Circle circle = (Circle) obj;
                g2.setColor(Color.blue);
                g2.setStroke(bstroke);
                Ellipse2D circle2D = new Ellipse2D.Double();
                double cx = circle.getCenter().x,
                        cy = circle.getCenter().y,
                        r = circle.getRadius();
                circle2D.setFrameFromCenter(cx, cy, cx - r, cy - r);
                g2.fill(circle2D);
            }
        }
    }

    /**
     * Draws the robot on the given graphics context, using world coordinates.
     * @param g2 Graphics context.
     * @param robot SimulatedKJunior to draw.
     */
    public static void drawRobot(Graphics2D g2, SimulatedKJunior robot) {
        g2.setColor(new Color(45, 45, 45));
        g2.setStroke(bstroke);
        Ellipse2D circle2D = new Ellipse2D.Double();
        double cx = robot.getPosition().x,
                cy = robot.getPosition().y,
                r = robot.getTopRadius();
        circle2D.setFrameFromCenter(cx, cy, cx - r, cy - r);
        g2.fill(circle2D);

    }

}
