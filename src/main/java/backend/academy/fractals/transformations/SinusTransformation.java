package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class SinusTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        double newX = Math.sin(x);
        double newY = Math.sin(y);
        return new Point(newX, newY);
    }
}
