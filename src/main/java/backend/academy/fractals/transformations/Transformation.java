package backend.academy.fractals.transformations;

import backend.academy.fractals.entities.Point;
import java.util.function.Function;

public interface Transformation extends Function<Point, Point> {
}
