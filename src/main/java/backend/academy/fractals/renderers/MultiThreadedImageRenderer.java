package backend.academy.fractals.renderers;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.factories.NonlinearTransformationGeneratorFactory;
import backend.academy.fractals.factories.PointGeneratorFactory;
import backend.academy.fractals.generators.Generator;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.transformations.AffineTransformation;
import java.util.List;

public class MultiThreadedImageRenderer extends AbstractImageRenderer {
    private final int numberOfThreads;
    private final Thread[] threads;
    private final int[] numberOfSamplesPerThread;

    public MultiThreadedImageRenderer(
        Params params,
        Generator<AffineTransformation> affineTransformationGenerator,
        Generator<Color> colorGenerator,
        PointGeneratorFactory pointGeneratorFactory,
        NonlinearTransformationGeneratorFactory nonlinearTransformationGeneratorFactory
    ) {
        super(params, affineTransformationGenerator, colorGenerator, pointGeneratorFactory,
            nonlinearTransformationGeneratorFactory);
        this.numberOfThreads = this.numberOfSamples > params.numberOfThreads()
            ? params.numberOfThreads()
            : this.numberOfSamples;
        this.threads = new Thread[this.numberOfThreads];
        this.numberOfSamplesPerThread = new int[this.numberOfThreads];
        for (int i = 0; i < this.numberOfThreads - 1; ++i) {
            this.numberOfSamplesPerThread[i] = this.numberOfSamples / (this.numberOfThreads - 1);
        }
        this.numberOfSamplesPerThread[this.numberOfThreads - 1] = this.numberOfThreads > 1
            ? this.numberOfSamples % (this.numberOfThreads - 1)
            : this.numberOfSamples;
    }

    @Override
    protected void processRendering(
        PixelImage canvas,
        List<AffineTransformation> affineTransformations,
        List<Color> colors
    ) {
        for (int i = 0; i < numberOfThreads; ++i) {
            threads[i] = new Thread(
                new OneThreadRunnable(canvas, affineTransformations, colors, numberOfSamplesPerThread[i])
            );
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
        private final List<AffineTransformation> affineTransformations;
        private final List<Color> colors;
        private final int numberOfSamplesThisThreadProcess;

        private OneThreadRunnable(
            PixelImage canvas,
            List<AffineTransformation> affineTransformations,
            List<Color> colors,
            int numberOfSamplesThisThreadProcess
        ) {
            this.canvas = canvas;
            this.affineTransformations = affineTransformations;
            this.colors = colors;
            this.numberOfSamplesThisThreadProcess = numberOfSamplesThisThreadProcess;
        }

        @Override
        public void run() {
            for (int num = 0; num < numberOfSamplesThisThreadProcess; ++num) {
                processSampleIterations(affineTransformations, colors, canvas);
            }
        }
    }
}
