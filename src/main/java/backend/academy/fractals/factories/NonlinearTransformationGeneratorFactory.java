package backend.academy.fractals.factories;

import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.params.NonlinearTransformationsGenerationOrder;
import backend.academy.fractals.transformations.Transformation;
import java.util.List;

public interface NonlinearTransformationGeneratorFactory {
    Generator<Transformation> produce(List<Transformation> transformations,
        NonlinearTransformationsGenerationOrder generationOrder);
}
