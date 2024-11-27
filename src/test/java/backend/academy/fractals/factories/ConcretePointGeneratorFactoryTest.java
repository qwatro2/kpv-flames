package backend.academy.fractals.factories;

import backend.academy.fractals.entities.Point;
import backend.academy.fractals.generators.Generator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcretePointGeneratorFactoryTest {

    private ConcretePointGeneratorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ConcretePointGeneratorFactory();
    }

    @Test
    void testProduceWidthGreaterThanHeight() {
        int width = 800;
        int height = 400;

        GeneratorWithBounds generatorWithBounds = factory.produce(width, height);

        assertEquals(-1.0, generatorWithBounds.xMin());
        assertEquals(1.0, generatorWithBounds.xMax());
        assertEquals(-0.5, generatorWithBounds.yMin(), 0.001);  // Расчет по формуле
        assertEquals(0.5, generatorWithBounds.yMax(), 0.001);  // Расчет по формуле

        Generator<Point> generator = generatorWithBounds.generator();
        assertNotNull(generator);
    }

    @Test
    void testProduceHeightGreaterThanWidth() {
        int width = 400;
        int height = 800;

        GeneratorWithBounds generatorWithBounds = factory.produce(width, height);

        assertEquals(-0.5, generatorWithBounds.xMin());
        assertEquals(0.5, generatorWithBounds.xMax(), 0.001);  // xMax не изменился
        assertEquals(-1.0, generatorWithBounds.yMin(), 0.001);  // Расчет по формуле
        assertEquals(1.0, generatorWithBounds.yMax(), 0.001);  // Расчет по формуле

        Generator<Point> generator = generatorWithBounds.generator();
        assertNotNull(generator);
    }

    @Test
    void testProduceWidthEqualsHeight() {
        int width = 400;
        int height = 400;

        GeneratorWithBounds generatorWithBounds = factory.produce(width, height);

        assertEquals(-1.0, generatorWithBounds.xMin());
        assertEquals(1.0, generatorWithBounds.xMax());
        assertEquals(-1.0, generatorWithBounds.yMin());
        assertEquals(1.0, generatorWithBounds.yMax());

        Generator<Point> generator = generatorWithBounds.generator();
        assertNotNull(generator);
    }

    @Test
    void testGeneratorBoundsWithInvalidDimensions() {
        int width = 0;
        int height = 0;

        GeneratorWithBounds generatorWithBounds = factory.produce(width, height);

        assertEquals(-1.0, generatorWithBounds.xMin());
        assertEquals(1.0, generatorWithBounds.xMax());
        assertEquals(-1.0, generatorWithBounds.yMin());
        assertEquals(1.0, generatorWithBounds.yMax());

        Generator<Point> generator = generatorWithBounds.generator();
        assertNotNull(generator);
    }
}
