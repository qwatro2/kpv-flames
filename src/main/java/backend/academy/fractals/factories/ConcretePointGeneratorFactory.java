package backend.academy.fractals.factories;

import backend.academy.fractals.entities.Point;
import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.generators.PointGenerator;

public class ConcretePointGeneratorFactory implements PointGeneratorFactory {
    @Override
    public GeneratorWithBounds produce(int width, int height) {
        double xMin = -1.0;
        double xMax = 1.0;
        double yMin = -1.0;
        double yMax = 1.0;

        if (width > height) {
            yMax = ((double) height) / width;
            yMin = -yMax;
        } else if (width < height) {
            xMax = ((double) width) / height;
        }

        Generator<Point> generator = new PointGenerator(xMin, xMax, yMin, yMax);
        return new GeneratorWithBounds(generator, xMin, xMax, yMin, yMax);
    }
}

