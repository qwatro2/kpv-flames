package backend.academy.fractals.factories;

import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.generators.OrderedNonlinearTransformationGenerator;
import backend.academy.fractals.generators.RandomNonlinearTransformationGenerator;
import backend.academy.fractals.params.NonlinearTransformationsGenerationOrder;
import backend.academy.fractals.transformations.Transformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ConcreteNonlinearTransformationGeneratorFactoryTest {

    private ConcreteNonlinearTransformationGeneratorFactory factory;
    private Transformation transformation;

    @BeforeEach
    void setUp() {
        factory = new ConcreteNonlinearTransformationGeneratorFactory();
        transformation = mock();
    }

    @Test
    void testProduceOrderedGenerationOrder() {
        List<Transformation> transformations = Arrays.asList(transformation, transformation);

        Generator<Transformation> generator = factory.produce(transformations, NonlinearTransformationsGenerationOrder.ORDERED);

        assertInstanceOf(OrderedNonlinearTransformationGenerator.class, generator);
    }

    @Test
    void testProduceRandomGenerationOrder() {
        List<Transformation> transformations = Arrays.asList(transformation, transformation);

        Generator<Transformation> generator = factory.produce(transformations, NonlinearTransformationsGenerationOrder.RANDOM);

        assertInstanceOf(RandomNonlinearTransformationGenerator.class, generator);
    }

    @Test
    void testProduceEmptyTransformationList() {
        List<Transformation> transformations = List.of();

        Generator<Transformation> generatorOrdered = factory.produce(transformations, NonlinearTransformationsGenerationOrder.ORDERED);
        Generator<Transformation> generatorRandom = factory.produce(transformations, NonlinearTransformationsGenerationOrder.RANDOM);

        assertNotNull(generatorOrdered);
        assertInstanceOf(OrderedNonlinearTransformationGenerator.class, generatorOrdered);
        assertNotNull(generatorRandom);
        assertInstanceOf(RandomNonlinearTransformationGenerator.class, generatorRandom);
    }
}
