package backend.academy.fractals.generators;

import backend.academy.fractals.transformations.LinearTransformation;
import java.util.Random;

public class LinearTransformationGenerator implements Generator<LinearTransformation> {
    private Random random;
    private final double minValue;
    private final double maxValue;

    public LinearTransformationGenerator(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Generator<LinearTransformation> setRandom(Random random) {
        this.random = random;
        return this;
    }

    @Override
    public LinearTransformation next() {
        LinearTransformation transformation = null;
        while (transformation == null) {
            transformation = LinearTransformation.createOrNull(
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble(),
                nextDouble()
            );
        }
        return transformation;
    }

    private double nextDouble() {
        return random.nextDouble(minValue, maxValue);
    }
}
