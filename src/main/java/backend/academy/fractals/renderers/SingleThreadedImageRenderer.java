package backend.academy.fractals.renderers;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.factories.NonlinearTransformationGeneratorFactory;
import backend.academy.fractals.factories.PointGeneratorFactory;
import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.transformations.LinearTransformation;
import java.util.List;

public class SingleThreadedImageRenderer extends AbstractImageRenderer {
    public SingleThreadedImageRenderer(
        Params params,
        Generator<LinearTransformation> linearTransformationGenerator,
        Generator<Color> colorGenerator,
        PointGeneratorFactory pointGeneratorFactory,
        NonlinearTransformationGeneratorFactory nonlinearTransformationGeneratorFactory
    ) {
        super(params, linearTransformationGenerator, colorGenerator, pointGeneratorFactory,
            nonlinearTransformationGeneratorFactory);
    }

    @Override
    protected void processRendering(
        PixelImage canvas,
        List<LinearTransformation> linearTransformations,
        List<Color> colors
    ) {
        for (int num = 0; num < numberOfSamples; ++num) {
            processSampleIterations(linearTransformations, colors, canvas);
        }
    }

    @Override
    protected void processCorrectRowColProxy(PixelImage canvas, int row, int col, Color color) {
        processCorrectRowCol(canvas, row, col, color);
    }
}
