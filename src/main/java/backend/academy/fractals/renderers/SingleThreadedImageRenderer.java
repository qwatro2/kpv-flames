package backend.academy.fractals.renderers;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.Pixel;
import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.entities.Point;
import backend.academy.fractals.factories.GeneratorWithBounds;
import backend.academy.fractals.factories.NonlinearTransformationGeneratorFactory;
import backend.academy.fractals.factories.PointGeneratorFactory;
import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.transformations.LinearTransformation;
import backend.academy.fractals.transformations.Transformation;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SingleThreadedImageRenderer implements ImageRenderer {
    private static final int NUMBER_OF_SKIPPED_ITERATIONS = 20;

    private final int numberOfSamples;
    private final int numberOfTransformations;
    private final int numberOfIterationsPerSample;
    private final int width;
    private final int height;

    private final Random random;
    private final Generator<LinearTransformation> linearTransformationGenerator;
    private final Generator<Transformation> nonlinearTransformationGenerator;
    private final Generator<Color> colorGenerator;
    private final Generator<Point> pointGenerator;

    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;

    public SingleThreadedImageRenderer(
        Params params,
        Generator<LinearTransformation> linearTransformationGenerator,
        Generator<Color> colorGenerator,
        PointGeneratorFactory pointGeneratorFactory,
        NonlinearTransformationGeneratorFactory nonlinearTransformationGeneratorFactory
    ) {
        this.numberOfSamples = params.numberOfSamples();
        this.numberOfTransformations = params.numberOfTransformations();
        this.numberOfIterationsPerSample = params.numberOfIterationsPerSample();
        this.width = params.width();
        this.height = params.height();

        this.random = new Random();
        if (params.seed() != null) {
            this.random.setSeed(params.seed());
        }

        this.linearTransformationGenerator = linearTransformationGenerator.setRandom(this.random);
        this.colorGenerator = colorGenerator.setRandom(this.random);

        this.nonlinearTransformationGenerator =
            nonlinearTransformationGeneratorFactory.produce(params.nonlinearTransformations(),
                params.generationOrder()).setRandom(this.random);

        GeneratorWithBounds productionResult = pointGeneratorFactory.produce(this.width, this.height);
        this.pointGenerator = productionResult.generator().setRandom(this.random);
        this.xMin = productionResult.xMin();
        this.xMax = productionResult.xMax();
        this.yMin = productionResult.yMin();
        this.yMax = productionResult.yMax();
    }

    @Override
    public PixelImage render() {
        PixelImage canvas = new PixelImage(width, height);

        List<LinearTransformation> linearTransformations =
            generateList(linearTransformationGenerator, numberOfTransformations);
        List<Color> colors = generateList(colorGenerator, numberOfTransformations);

        for (int num = 0; num < numberOfSamples; ++num) {
            processSampleIterations(linearTransformations, colors, canvas);
        }

        return canvas;
    }

    private void processSampleIterations(
        List<LinearTransformation> linearTransformations,
        List<Color> colors,
        PixelImage canvas
    ) {
        Point point = pointGenerator.next();

        for (int iter = -NUMBER_OF_SKIPPED_ITERATIONS; iter < numberOfIterationsPerSample; ++iter) {
            int randomIndex = random.nextInt(numberOfTransformations);
            Transformation linearTransformation =
                linearTransformations.get(randomIndex);
            Transformation nonlinearTransformation =
                nonlinearTransformationGenerator.next();
            Color color = colors.get(randomIndex);
            point =
                processOneSampleIterations(iter, linearTransformation, nonlinearTransformation, point, canvas, color);
        }
    }

    private Point processOneSampleIterations(
        int iter, Transformation linearTransformation,
        Transformation nonlinearTransformation, Point point,
        PixelImage canvas, Color color
    ) {
        point = linearTransformation.andThen(nonlinearTransformation).apply(point);

        if (iter >= 0 && isCorrectPoint(point)) {
            int col = width - (int) (((xMax - point.x()) / (xMax - xMin)) * width);
            int row = height - (int) (((yMax - point.y()) / (yMax - yMin)) * height);

            if (0 <= col && col < width && 0 <= row && row < height) {
                processCorrectRowCol(canvas, row, col, color);
            }
        }

        return point;
    }

    private void processCorrectRowCol(PixelImage canvas, int row, int col, Color color) {
        Pixel current = canvas.get(row, col);

        if (current.hitCount() == 0) {
            current.color(color);
        } else {
            Color mixedColor = new Color(
                (current.color().r() + color.r()) / 2,
                (current.color().g() + color.g()) / 2,
                (current.color().b() + color.b()) / 2
            );
            current.color(mixedColor);
        }

        current.increment();
    }

    private boolean isCorrectPoint(Point point) {
        return xMin <= point.x() && point.x() <= xMax
            && yMin <= point.y() && point.y() <= yMax;
    }

    private <T> List<T> generateList(Generator<T> generator, int size) {
        return IntStream.range(0, size).mapToObj(_ -> generator.next()).toList();
    }
}
