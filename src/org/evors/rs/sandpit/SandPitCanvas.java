package org.evors.rs.sandpit;

import java.awt.Canvas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputAdapter;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;

/**
 * Basic functionality for rendering the simulation, including mouse/camera
 * functionality.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public abstract class SandPitCanvas extends Canvas {

    public SandPitCanvas() {

        camera = new SandPitCamera(Vec2.ZERO, Vec2.ZERO, 2);
        grid = new Grid(camera);

        MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
            private Vec2 prevCoord = Vec2.NaN;

            public void mouseDragged(MouseEvent me) {
                Vec2 newCoord = getCamera().convertScreenToWorldCoords(new Vec2(
                        me.getX(), me.getY()));
                if (prevCoord == Vec2.NaN) {
                    prevCoord = newCoord;
                } else {
                    Vec2 sub = newCoord.subtract(prevCoord);
                    getCamera().move(sub);
                    draw();
                }
            }

            public void mouseReleased(MouseEvent me) {
                prevCoord = Vec2.NaN;
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                getCamera().changeScale(-0.05 * e.getUnitsToScroll());
                draw();
            }
        });

    }

    
    public abstract void draw();

    public SandPitCamera getCamera() {
        return camera;
    }

    protected SimulationWorld world;
    protected final SandPitCamera camera;
    protected final Grid grid;

}
