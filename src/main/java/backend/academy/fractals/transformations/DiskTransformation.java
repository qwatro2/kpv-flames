package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class DiskTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        double piNorm = Math.PI * Math.sqrt(x * x + y * y);
        double atan = Math.atan(y / x);
        double newX = atan * Math.sin(piNorm) / Math.PI;
        double newY = atan * Math.cos(piNorm) / Math.PI;
        return new Point(newX, newY);
    }
}
