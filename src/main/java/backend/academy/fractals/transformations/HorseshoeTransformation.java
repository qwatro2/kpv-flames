package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class HorseshoeTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();

        if (x == 0 && y == 0) {
            return new Point(0, 0);
        }

        double rRev = 1.0 / Math.sqrt(x * x + y * y);
        double newX = rRev * (x - y) * (x + y);
        double newY = rRev * 2 * x * y;
        return new Point(newX, newY);
    }
}
