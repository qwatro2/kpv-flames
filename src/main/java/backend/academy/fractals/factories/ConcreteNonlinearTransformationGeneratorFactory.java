package backend.academy.fractals.factories;

import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.generators.OrderedNonlinearTransformationGenerator;
import backend.academy.fractals.generators.RandomNonlinearTransformationGenerator;
import backend.academy.fractals.params.NonlinearTransformationsGenerationOrder;
import backend.academy.fractals.transformations.Transformation;
import java.util.List;

public class ConcreteNonlinearTransformationGeneratorFactory implements NonlinearTransformationGeneratorFactory {
    @Override
    public Generator<Transformation> produce(
        List<Transformation> transformations,
        NonlinearTransformationsGenerationOrder generationOrder
    ) {
        return switch (generationOrder) {
            case ORDERED -> new OrderedNonlinearTransformationGenerator(transformations);
            case RANDOM -> new RandomNonlinearTransformationGenerator(transformations);
        };
    }
}
