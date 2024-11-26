package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class SwirlTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        double rSqr = x * x + y * y;
        double sinRSqr = Math.sin(rSqr);
        double cosRSqr = Math.cos(rSqr);
        double newX = x * sinRSqr - y * cosRSqr;
        double newY = x * cosRSqr + y * sinRSqr;
        return new Point(newX, newY);
    }
}
