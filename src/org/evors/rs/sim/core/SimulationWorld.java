package org.evors.rs.sim.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.evors.core.geometry.Intersection;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Polygon;
import org.evors.core.geometry.Shape2D;
import org.evors.core.geometry.Vec2;

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
     * Creates a new SimulationWorld with specified bounds. Lines are
     * automatically generated to form the bounding box.
     *
     * @param bounds Vec2 size of bounding box.
     * @param positiveCoords centre is (0,0) if false, bottom left is (0,0) if true
     */
    public SimulationWorld(Vec2 bounds, boolean positiveCoords) {
    	  this.bounds = bounds;
          double pi2 =  Math.PI / 2;
          double w =  bounds.x,
                  h =  bounds.y;
          
          double xOff = positiveCoords ? 0 : ( -w / 2);
          double yOff = positiveCoords ? 0 : ( -h / 2 );
          
          objects.add( Line.fromCoords( 0 + xOff, 0 + yOff, w + xOff, 0 + yOff ) ); // bottom
          objects.add( Line.fromCoords( w + xOff, 0 + yOff, w + xOff, h + yOff ) ); // right
          objects.add( Line.fromCoords( w + xOff, h + yOff, 0 + xOff, h + yOff ) ); // top
          objects.add( Line.fromCoords( 0 + xOff, h + yOff, 0 + xOff, 0 + yOff ) ); // left
    }

    /**
     * Creates a new SimulationWorld with specified bounds. Lines are
     * automatically generated to form the bounding box.
     *
     * @param bounds Vec2 size of bounding box.
     */
    public SimulationWorld(Vec2 bounds) {
      this( bounds, false );
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
    public TraceRayResult traceRayFull(Line rangeFinderLine) {
        double lowestDist = Double.NaN; //will return this if no intersection found
        Shape2D nearestObj = null;
        for (Iterator it = objects.iterator(); it.hasNext();) {
            Shape2D obj = (Shape2D) it.next();
            Intersection li = obj.getSmallestIntersection(rangeFinderLine);
            if (li.isIntersection) {
                if (Double.isNaN(lowestDist) || li.getSmallestLineDist() < lowestDist) {
                    lowestDist = li.getSmallestLineDist();
                    nearestObj = obj;
                }
            }
        }
        return new TraceRayResult( lowestDist, nearestObj );
    }
    
    public double traceRay(Line rangeFinderLine) {
    	return traceRayFull( rangeFinderLine ).getDistance();
    }
    
    public class TraceRayResult
    {
    	double dist;
    	Shape2D obj;
    	
    	TraceRayResult( double dist, Shape2D obj )
    	{
    		this.dist = dist;
    		this.obj = obj;
    	}
    	
    	public double getDistance() { return dist; }
    	
    	public Shape2D getObject() { return obj; }
    }

}
