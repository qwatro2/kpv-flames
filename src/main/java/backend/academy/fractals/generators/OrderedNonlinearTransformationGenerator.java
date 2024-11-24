package backend.academy.fractals.generators;

import backend.academy.fractals.transformations.Transformation;
import java.util.List;
import java.util.Random;

public class OrderedNonlinearTransformationGenerator implements Generator<Transformation> {
    private final List<Transformation> transformations;
    private final int size;
    private int current;

    public OrderedNonlinearTransformationGenerator(List<Transformation> transformations) {
        this.transformations = transformations;
        this.size = transformations.size();
        this.current = 0;
    }

    @Override
    public Transformation next() {
        Transformation result = transformations.get(current);
        current = (current + 1) % size;
        return result;
    }

    @Override
    public Generator<Transformation> setRandom(Random random) {
        return this;
    }
}
