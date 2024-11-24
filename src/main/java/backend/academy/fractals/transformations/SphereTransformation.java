package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class SphereTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        double norm = x * x + y * y;
        double newX = x / norm;
        double newY = y / norm;
        return new Point(newX, newY);
    }
}
