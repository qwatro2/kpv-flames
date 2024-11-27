package backend.academy.fractals.generators;

import backend.academy.fractals.entities.Point;
import backend.academy.fractals.transformations.AffineTransformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Random;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class AffineTransformationGeneratorTest {
    private AffineTransformationGenerator generator;
    private final long seed = 142857;

    @BeforeEach
    void setUp() {
        Random random = new Random(seed);
        generator = new AffineTransformationGenerator(-1.0, 1.0);
        generator.setRandom(random);
    }

    @Test
    void testNextGeneratesValidTransformation() {
        AffineTransformation transformation = generator.next();
        assertNotNull(transformation);
    }

    static Stream<Point> providePointsForTestRandomInitialization() {
        return Stream.of(
            new Point(0, 0),
            new Point(1, 0),
            new Point(-1, 0),
            new Point(0, 1),
            new Point(1, -1),
            new Point(-1, 1),
            new Point(0.5, 0.5),
            new Point(0.5, -0.5),
            new Point(-0.5, 0.5),
            new Point(-0.5, -0.5)
        );
    }

    @ParameterizedTest
    @MethodSource("providePointsForTestRandomInitialization")
    void testRandomInitialization(Point point) {
        AffineTransformationGenerator anotherGenerator = new AffineTransformationGenerator(-1.0, 1.0);
        anotherGenerator.setRandom(new Random(seed));

        AffineTransformation transformation1 = generator.next();
        AffineTransformation transformation2 = anotherGenerator.next();

        Point transformedPoint1 = transformation1.apply(point);
        Point transformedPoint2 = transformation2.apply(point);

        assertTrue(equals(transformedPoint1, transformedPoint2));
    }

    static boolean equals(Point lhs, Point rhs) {
        return lhs.x() == rhs.x() && lhs.y() == rhs.y();
    }
}
