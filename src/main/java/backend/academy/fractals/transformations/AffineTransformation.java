package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;

public class AffineTransformation implements Transformation {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;

    private AffineTransformation(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public static AffineTransformation createOrNull(double a, double b, double c, double d, double e, double f) {
        if (!checkCoefficients(a, b, d, e)) {
            return null;
        }

        return new AffineTransformation(a, b, c, d, e, f);
    }

    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        double newX = a * x + b * y + c;
        double newY = d * x + e * y + f;
        return new Point(newX, newY);
    }

    private static boolean checkCoefficients(double a, double b, double d, double e) {
        double aSqr = a * a;
        double dSqr = d * d;
        if (aSqr + dSqr >= 1) {
            return false;
        }

        double bSqr = b * b;
        double eSqr = e * e;
        if (bSqr + eSqr >= 1) {
            return false;
        }

        double determinant = a * e  - b * d;
        return aSqr + bSqr + dSqr + eSqr < 1 + determinant * determinant;
    }
}
