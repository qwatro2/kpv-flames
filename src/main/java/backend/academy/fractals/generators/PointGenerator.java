package backend.academy.fractals.generators;

import backend.academy.fractals.entities.Point;
import java.util.Random;

public class PointGenerator implements Generator<Point> {
    private Random random;

    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;

    public PointGenerator(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    public Point next() {
        return new Point(nextX(), nextY());
    }

    @Override
    public Generator<Point> setRandom(Random random) {
        this.random = random;
        return this;
    }

    private double nextX() {
        return random.nextDouble(xMin, xMax);
    }

    private double nextY() {
        return random.nextDouble(yMin, yMax);
    }
}
