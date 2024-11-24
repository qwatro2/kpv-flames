package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class HearthTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        double norm = Math.sqrt(x * x + y * y);
        double normAtan = norm * Math.atan(y / x);
        double newX = norm * Math.sin(normAtan);
        double newY = -norm * Math.cos(normAtan);
        return new Point(newX, newY);
    }
}
