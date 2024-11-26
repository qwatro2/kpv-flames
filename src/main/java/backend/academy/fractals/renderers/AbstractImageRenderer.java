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
import backend.academy.fractals.transformations.AffineTransformation;
import backend.academy.fractals.transformations.Transformation;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public abstract class AbstractImageRenderer implements ImageRenderer {
    private static final int NUMBER_OF_SKIPPED_ITERATIONS = 20;

    protected int numberOfSamples;

    private final int numberOfTransformations;
    private final int numberOfIterationsPerSample;
    private final int width;
    private final int height;

    private final double numberOfSymmetries;

    private final Random random;
    private final Generator<AffineTransformation> affineTransformationGenerator;
    private final Generator<Transformation> nonlinearTransformationGenerator;
    private final Generator<Color> colorGenerator;
    private final Generator<Point> pointGenerator;

    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;

    public AbstractImageRenderer(
        Params params,
        Generator<AffineTransformation> affineTransformationGenerator,
        Generator<Color> colorGenerator,
        PointGeneratorFactory pointGeneratorFactory,
        NonlinearTransformationGeneratorFactory nonlinearTransformationGeneratorFactory
    ) {
        this.numberOfSamples = params.numbersParams().numberOfSamples();
        this.numberOfTransformations = params.numbersParams().numberOfTransformations();
        this.numberOfIterationsPerSample = params.numbersParams().numberOfIterationsPerSample();
        this.width = params.sizeParams().width();
        this.height = params.sizeParams().height();

        this.numberOfSymmetries = params.numbersParams().numberOfSymmetries();

        this.random = new Random();
        if (params.seed() != null) {
            this.random.setSeed(params.seed());
        }

        this.affineTransformationGenerator = affineTransformationGenerator.setRandom(this.random);
        this.colorGenerator = colorGenerator.setRandom(this.random);

        this.nonlinearTransformationGenerator =
            nonlinearTransformationGeneratorFactory.produce(
                params.transformationsParams().nonlinearTransformations(),
                params.transformationsParams().generationOrder()
            ).setRandom(this.random);

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

        List<AffineTransformation> affineTransformations =
            generateList(affineTransformationGenerator, numberOfTransformations);
        List<Color> colors = generateList(colorGenerator, numberOfTransformations);

        processRendering(canvas, affineTransformations, colors);

        return canvas;
    }

    protected abstract void processRendering(
        PixelImage canvas,
        List<AffineTransformation> affineTransformations,
        List<Color> colors
    );

    protected abstract void processCorrectRowColProxy(PixelImage canvas, int row, int col, Color color);

    protected final void processSampleIterations(
        List<AffineTransformation> affineTransformations,
        List<Color> colors,
        PixelImage canvas
    ) {
        Point point = pointGenerator.next();

        for (int iter = -NUMBER_OF_SKIPPED_ITERATIONS; iter < numberOfIterationsPerSample; ++iter) {
            int randomIndex = random.nextInt(numberOfTransformations);
            Transformation affineTransformation =
                affineTransformations.get(randomIndex);
            Transformation nonlinearTransformation =
                nonlinearTransformationGenerator.next();
            Color color = colors.get(randomIndex);

            point =
                processOneSampleIterations(iter, affineTransformation, nonlinearTransformation, point, canvas, color);
        }
    }

    private Point processOneSampleIterations(
        int iter, Transformation affineTransformation,
        Transformation nonlinearTransformation, Point point,
        PixelImage canvas, Color color
    ) {
        point = affineTransformation.andThen(nonlinearTransformation).apply(point);

        if (iter >= 0) {
            double theta2 = 0.0;
            for (int s = 0; s < numberOfSymmetries; ++s) {
                theta2 += 2 * Math.PI / numberOfSymmetries;
                double xRot = point.x() * Math.cos(theta2) - point.y() * Math.sin(theta2);
                double yRot = point.x() * Math.sin(theta2) + point.y() * Math.cos(theta2);
                Point pointRot = new Point(xRot, yRot);
                if (isCorrectPoint(pointRot)) {
                    int col = width - (int) (((xMax - pointRot.x()) / (xMax - xMin)) * width);
                    int row = height - (int) (((yMax - pointRot.y()) / (yMax - yMin)) * height);

                    if (0 <= col && col < width && 0 <= row && row < height) {
                        processCorrectRowColProxy(canvas, row, col, color);
                    }
                }
            }
        }

        return point;
    }

    protected final void processCorrectRowCol(PixelImage canvas, int row, int col, Color color) {
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
