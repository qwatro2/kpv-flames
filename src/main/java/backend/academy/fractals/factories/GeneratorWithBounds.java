package backend.academy.fractals.factories;

import backend.academy.fractals.entities.Point;
import backend.academy.fractals.generators.Generator;

public record GeneratorWithBounds(Generator<Point> generator,
                                  double xMin, double xMax,
                                  double yMin, double yMax) {
}
