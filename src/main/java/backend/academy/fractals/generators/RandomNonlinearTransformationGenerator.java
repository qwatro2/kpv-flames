package backend.academy.fractals.generators;

import backend.academy.fractals.transformations.Transformation;
import java.util.List;
import java.util.Random;

public class RandomNonlinearTransformationGenerator implements Generator<Transformation> {
    private final List<Transformation> transformations;
    private Random random;

    public RandomNonlinearTransformationGenerator(List<Transformation> transformations) {
        this.transformations = transformations;
    }

    @Override
    public Transformation next() {
        return transformations.get(random.nextInt(0, transformations.size()));
    }

    @Override
    public Generator<Transformation> setRandom(Random random) {
        this.random = random;
        return this;
    }
}
