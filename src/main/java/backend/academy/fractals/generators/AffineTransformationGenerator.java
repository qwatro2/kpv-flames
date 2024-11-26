package backend.academy.fractals.generators;

import backend.academy.fractals.transformations.AffineTransformation;
import java.util.Random;

public class AffineTransformationGenerator implements Generator<AffineTransformation> {
    private Random random;
    private final double minValue;
    private final double maxValue;

    public AffineTransformationGenerator(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Generator<AffineTransformation> setRandom(Random random) {
        this.random = random;
        return this;
    }

    @Override
    public AffineTransformation next() {
        AffineTransformation transformation = null;
        while (transformation == null) {
            transformation = AffineTransformation.createOrNull(
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
