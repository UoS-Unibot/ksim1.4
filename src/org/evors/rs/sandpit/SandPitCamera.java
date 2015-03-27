package org.evors.rs.sandpit;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.evors.core.geometry.Vec2;

/**
 * An orthogonal camera viewing the simulation.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SandPitCamera {

    public SandPitCamera(Vec2 windowSize, Vec2 initCenterPos, double initScale) {
        scale = initScale;
        currentPosWorldCoord = initCenterPos;
        setWindowSize(windowSize);
    }

    private double scale = 3; //amount of scale/zoom
    private Vec2 currentPosWorldCoord; //current world position of centre of camera
    private Vec2 windowSize; //size of the viewing screen

    /**
     * Get the current transform between world and screen, taking into account
     * camera scaling and transform
     *
     * @return an AffineTransform that can be applied to graphics context
     */
    public AffineTransform getTransform() {
        AffineTransform returnTransform = new AffineTransform();
        Vec2 currentPosScreenCoord = getCurrentPosWorldCoord().scalar(scale);
        Vec2 halfWindow = getHalfWindowSize();
        returnTransform.translate(currentPosScreenCoord.x + halfWindow.x,
                currentPosScreenCoord.y + halfWindow.y);
        returnTransform.scale(getScale(), -getScale());

        return returnTransform;
    }

    /**
     * Moves the camera by the specified amount in world units
     *
     * @param changeVector Vector describing relative camera movement in world
     * units
     */
    public void move(Vec2 changeVector) {
        currentPosWorldCoord = currentPosWorldCoord.add(changeVector);
    }

    /**
     * Changes current scaling of the camera. The scale is limited to between
     * 0.2 and 4.
     *
     * @param scaleDiff change in scale
     */
    public void changeScale(double scaleDiff) {
        scale = Math.min(scale + scaleDiff, 4);
        scale = Math.max(scale + scaleDiff, 0.2);
    }

    /**
     * Sets size of the viewing window in screen units.
     *
     * @param windowSize Vector of window size.
     */
    public final void setWindowSize(Vec2 windowSize) {
        this.windowSize = windowSize;
    }

    /**
     * Converts world coordinates to screen coordinates, applying the current
     * camera transform.
     *
     * @param worldCoords World coordinates.
     * @return Matching screen coordinates with current transform.
     */
    public Vec2 convertWorldToScreenCoords(Vec2 worldCoords) {
        Vec2 halfWindow = getHalfWindowSize();
        return worldCoords.scalar(getScale()).add(halfWindow);
    }

    /**
     * Converts screen coordinates to world coordinates, applying the inverse of
     * the current camera transform.
     *
     * @param screen Screen coordinates
     * @return Matching world coordinates with current transform.
     */
    public Vec2 convertScreenToWorldCoords(Vec2 screen) {
        Vec2 halfWindow = getHalfWindowSize();
        return screen.subtract(halfWindow).scalar(1 / getScale()).subtract(
                currentPosWorldCoord);
    }

    /**
     * @return Half the window size.
     */
    public Vec2 getHalfWindowSize() {
        return getWindowSize().scalar(0.5);
    }

    /**
     * Gets the current viewing rectangle transformed from screen coordinates to
     * world coordinates.
     *
     * @return
     */
    public Rectangle2D getViewPortInWorldCoords() {
        Vec2 screenZeroZeroInWorldCoords = convertScreenToWorldCoords(Vec2.ZERO);
        Vec2 screenWidthHeightInWorldCoords = convertScreenToWorldCoords(
                getWindowSize().scalar(2));

        Rectangle2D rect = new Rectangle2D.Double();
        rect.setFrame(screenZeroZeroInWorldCoords.x,
                screenZeroZeroInWorldCoords.y, screenWidthHeightInWorldCoords.x,
                screenWidthHeightInWorldCoords.y);

        return rect;
    }

    /**
     * @return Current scaling of the camera, or zoom level.
     */
    public double getScale() {
        return scale;
    }

    /**
     * @return Current position of the centre of the viewport in world
     * coordinates.
     */
    public Vec2 getCurrentPosWorldCoord() {
        return currentPosWorldCoord;
    }

    /**
     * @return the size of the viewing window.
     */
    public Vec2 getWindowSize() {
        return windowSize;
    }

}
