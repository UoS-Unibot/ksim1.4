package org.evors.rs.sandpit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import org.evors.core.geometry.Circle;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Polygon;
import org.evors.core.geometry.Shape2D;
import org.evors.core.geometry.Vec2;
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
                GeneralPath path = new GeneralPath();
                boolean firstPointSet = false;
                for (Iterator iterator1 = poly.getLines().iterator(); iterator1.
                        hasNext();) {
                    Line line = (Line) iterator1.next();
                    if (!firstPointSet) {
                        path.moveTo((float) line.p1.x, (float) line.p1.y);
                        firstPointSet = true;
                    }
                    path.lineTo((float) line.p2.x, (float) line.p2.y);
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
     *
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

        //draw heading
        Line arrowLine = Line.fromPolarVec(robot.getPosition(), robot.
                getPolarOrientation(), 5.75);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(0.5f));
        g2.draw(new Line2D.Double(arrowLine.p1.x, arrowLine.p1.y, arrowLine.p2.x,
                arrowLine.p2.y));

        double irCircR = 1;
        double[] readings = robot.getInput();
        g2.setColor(Color.red);
        //draw IRs
        for (int i = 0; i < robot.irAngles.length; i++) {
            double ang = robot.irAngles[i];
            float reading = (float) (readings[i]/3500f);
            g2.setColor(new Color(reading, 0, 0));
            Vec2 circleCentre = robot.getIRBase(ang);
            Ellipse2D circle = new Ellipse2D.Double();
            circle.setFrameFromCenter(circleCentre.x, circleCentre.y, circleCentre.x - irCircR, circleCentre.y - irCircR);
            g2.fill(circle);
        }
    }

}
