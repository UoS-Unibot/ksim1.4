package org.evors.rs.sandpit;

/**
 * Listens for control signals for the visualisation, e.g. starting and
 * stopping.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface VisualiserListener {

    public void setRunning(boolean isRunning);

    public void restart();

    public void speedChanged(double newSpeed);
    
    public void setX( double x );
    public void setY( double y );
    public void setHeading( double heading );
    
    public void step();
    
}
