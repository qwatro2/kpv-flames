package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class EyefishTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        double r = Math.sqrt(x * x + y * y);
        double multiplier = 2.0 / (r + 1);
        double newX = x * multiplier;
        double newY = y * multiplier;
        return new Point(newX, newY);
    }
}
