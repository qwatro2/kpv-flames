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
    private final int[] numberOfSamplesPerThread;

    public MultiThreadedImageRenderer(
        Params params,
        Generator<LinearTransformation> linearTransformationGenerator,
        Generator<Color> colorGenerator,
        PointGeneratorFactory pointGeneratorFactory,
        NonlinearTransformationGeneratorFactory nonlinearTransformationGeneratorFactory
    ) {
        super(params, linearTransformationGenerator, colorGenerator, pointGeneratorFactory,
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
        List<LinearTransformation> linearTransformations,
        List<Color> colors
    ) {
        for (int i = 0; i < numberOfThreads; ++i) {
            threads[i] = new Thread(
                new OneThreadRunnable(canvas, linearTransformations, colors, numberOfSamplesPerThread[i])
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
        private final List<LinearTransformation> linearTransformations;
        private final List<Color> colors;
        private final int numberOfSamplesThisThreadProcess;

        private OneThreadRunnable(
            PixelImage canvas,
            List<LinearTransformation> linearTransformations,
            List<Color> colors,
            int numberOfSamplesThisThreadProcess
        ) {
            this.canvas = canvas;
            this.linearTransformations = linearTransformations;
            this.colors = colors;
            this.numberOfSamplesThisThreadProcess = numberOfSamplesThisThreadProcess;
        }

        @Override
        public void run() {
            for (int num = 0; num < numberOfSamplesThisThreadProcess; ++num) {
                processSampleIterations(linearTransformations, colors, canvas);
            }
        }
    }
}
