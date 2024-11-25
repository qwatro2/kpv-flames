package backend.academy.fractals.renderers;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.factories.NonlinearTransformationGeneratorFactory;
import backend.academy.fractals.factories.PointGeneratorFactory;
import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.transformations.LinearTransformation;
import java.util.List;

public class MultiThreadedImageRenderer extends AbstractImageRenderer {
    private final int numberOfThreads;
    private final Thread[] threads;

    public MultiThreadedImageRenderer(
        Params params,
        Generator<LinearTransformation> linearTransformationGenerator,
        Generator<Color> colorGenerator,
        PointGeneratorFactory pointGeneratorFactory,
        NonlinearTransformationGeneratorFactory nonlinearTransformationGeneratorFactory
    ) {
        super(params, linearTransformationGenerator, colorGenerator, pointGeneratorFactory,
            nonlinearTransformationGeneratorFactory);
        this.numberOfThreads = params.numberOfThreads();
        this.threads = new Thread[this.numberOfThreads];
        this.numberOfSamples /= this.numberOfThreads;
    }

    @Override
    protected void processRendering(
        PixelImage canvas,
        List<LinearTransformation> linearTransformations,
        List<Color> colors
    ) {
        for (int i = 0; i < numberOfThreads; ++i) {
            threads[i] = new Thread(new OneThreadRunnable(canvas, linearTransformations, colors));
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    protected void processCorrectRowColProxy(PixelImage canvas, int row, int col, Color color) {
        synchronized (canvas.get(row, col)) {
            processCorrectRowCol(canvas, row, col, color);
        }
    }

    private class OneThreadRunnable implements Runnable {
        private final PixelImage canvas;
        private final List<LinearTransformation> linearTransformations;
        private final List<Color> colors;

        private OneThreadRunnable(
            PixelImage canvas,
            List<LinearTransformation> linearTransformations,
            List<Color> colors
        ) {
            this.canvas = canvas;
            this.linearTransformations = linearTransformations;
            this.colors = colors;
        }

        @Override
        public void run() {
            for (int num = 0; num < numberOfSamples; ++num) {
                processSampleIterations(linearTransformations, colors, canvas);
            }
        }
    }
}
