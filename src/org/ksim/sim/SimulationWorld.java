package org.ksim.sim;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.ksim.geometry.Intersection;
import org.ksim.geometry.Line;
import org.ksim.geometry.Polygon;
import org.ksim.geometry.Shape2D;
import org.ksim.geometry.Vec2;

/**
 * The SimulationWorld represents a collection of objects making up the world,
 * including 4 bounding lines. Can check for collisions between the robot and
 * any objects in the world (including bounding lines)
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulationWorld {

    private final LinkedList objects = new LinkedList();
    private final LinkedList listeners = new LinkedList();
    private final Vec2 bounds;
    private String filename = "";

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    

    /**
     * Creates a new SimulationWorld with specified bounds. LineObjs are
     * automatically generated to form the bounding box.
     *
     * @param bounds Vec2 size of bounding box in metres.
     */
    public SimulationWorld(Vec2 bounds) {
        this.bounds = bounds;
        double pi2 =  Math.PI / 2;
        double w =  bounds.x,
                h =  bounds.y;
        objects.add(Line.fromCenterPoint(0, h / 2, w, 0));
        objects.add(Line.fromCenterPoint(w / 2, 0, h, pi2));
        objects.add(Line.fromCenterPoint(0, -h / 2, w, 0));
        objects.add(Line.fromCenterPoint(-w / 2, 0, h, pi2));

    }
    
    public void addListener(CollisionListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(CollisionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Adds all objects in a Collection to the world.
     *
     * @param objects Collection of WorldObjs
     */
    public void addWorldObjects(Collection objects) {
        this.objects.addAll(objects);
    }

    public void checkCollisions(SimulatedRobotBody robot) {
        for (Iterator it = objects.iterator(); it.hasNext();) {
            Shape2D obj = (Shape2D)it.next();
            if (obj.intersectsWith(robot.getShape())) {
                for (Iterator it2 = listeners.iterator(); it2.hasNext();) {
                    CollisionListener cl = (CollisionListener) it2.next();
                    if(cl.collisionOccured())
                        break;
                }
                robot.doCollision(obj);
            }
        }
    }

    /**
     * Adds a single WorldObj to the world.
     *
     * @param object WorldObj to add.
     */
    public void createWorldObject(Polygon object) {
        objects.add(object);
    }

    /**
     * @return a collection of the current objects in the world.
     */
    public Collection getObjects() {
        return objects;
    }

    
    public String toString() {
        return "SimulationWorld{" + "objects=" + objects + ", listeners=" + listeners + '}';
    }
    
    public Vec2 getBounds() {
        return bounds;
    }

    /**
     * Traces a single line throughout the world, getting the shortest distance to the nearest intersection.
     * @param rangeFinderLine
     * @return 
     */
    public double traceRay(Line rangeFinderLine) {
        double lowestDist = Double.NaN; //will return this if no intersection found
        for (Iterator it = objects.iterator(); it.hasNext();) {
            Shape2D obj = (Shape2D) it.next();
            Intersection li = obj.getSmallestIntersection(rangeFinderLine);
            if (li.isIntersection) {
                if (Double.isNaN(lowestDist) || li.getSmallestLineDist() < lowestDist) {
                    lowestDist = li.getSmallestLineDist();
                }
            }
        }
        return  lowestDist;
    }

}
