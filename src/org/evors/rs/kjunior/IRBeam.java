package org.evors.rs.kjunior;

import java.util.Random;
import org.evors.core.geometry.Line;
import org.evors.core.geometry.Vec2;
import org.evors.rs.sim.core.SimulationWorld;

/**
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class IRBeam {

    public static final int IR_NOISE = 50;
    private static final Random rand = new Random();

    private final double angleC, maxLength;
    private final Line[] beams;
    private final SimulationWorld world;

    public IRBeam(Vec2 basePoint, double centralAngle,
            double maxLength, SimulationWorld world) {
        //initialise the IR beam with KJunior empirical values determined by Phil
        this(basePoint, centralAngle, maxLength, world, 5, 0.437);
    }

    public IRBeam(Vec2 basePoint, double centralAngle,
            double maxLength, SimulationWorld world, int nBeams,
            double divergence) {
        this.angleC = centralAngle;
        this.world = world;
        this.maxLength = maxLength;
        beams = new Line[nBeams];
        double dDiv = divergence / nBeams;

        for (int i = 0; i < nBeams; i++) {
            double angle = centralAngle + dDiv * (i - nBeams / 2);
            beams[i] = Line.fromPolarVec(basePoint, angle, maxLength);
        }
    }

    public double getReading() {
        double sum = 0;
        double count = 0;
        for (int i = 0; i < beams.length; i++) {
            double dist = world.traceRay(beams[i]) * 1000;
            if (!Double.isNaN(dist)) {
                count++;
                sum += dist;
            }
        }
        double reading = (count / (double)beams.length) * convertDistToReading(sum
                / count);
        
        //add noise
        reading += (rand.nextDouble() * 2 * IR_NOISE - IR_NOISE);
        if(Double.isNaN(reading) || reading < 0)
            return 0;
        return Math.round(reading);
    }

    private double convertDistToReading(double d) {
        double k = -(d / 8.5) * (d / 8.5);
        return 3371.0 * Math.exp(k);
    }

    public double getCentralAngle() {
        return angleC;
    }

    public Line[] getBeams() {
        return beams;
    }

}
