package backend.academy.fractals.app;

import backend.academy.fractals.correctors.GammaImageCorrector;
import backend.academy.fractals.correctors.ImageCorrector;
import backend.academy.fractals.factories.ConcreteNonlinearTransformationGeneratorFactory;
import backend.academy.fractals.factories.ConcretePointGeneratorFactory;
import backend.academy.fractals.generators.AffineTransformationGenerator;
import backend.academy.fractals.generators.ColorGenerator;
import backend.academy.fractals.params.CliParamsGetter;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.params.ParamsGetter;
import backend.academy.fractals.renderers.ImageRenderer;
import backend.academy.fractals.renderers.MultiThreadedImageRenderer;
import backend.academy.fractals.renderers.SingleThreadedImageRenderer;
import backend.academy.fractals.savers.FileImageSaver;
import backend.academy.fractals.savers.ImageSaver;
import backend.academy.fractals.viewers.ParamsViewer;
import backend.academy.fractals.viewers.Viewer;
import java.io.PrintStream;
import java.text.MessageFormat;

public class FractalsApp extends AbstractFractalsApp {
    private final PrintStream okStream;
    private final PrintStream errorStream;
    private final Viewer<Params> paramsViewer;

    public FractalsApp(PrintStream okStream, PrintStream errorStream) {
        this(okStream, errorStream, new ParamsViewer());
    }

    public FractalsApp(PrintStream okStream, PrintStream errorStream, Viewer<Params> paramsViewer) {
        this.okStream = okStream;
        this.errorStream = errorStream;
        this.paramsViewer = paramsViewer;
    }

    @Override
    protected ParamsGetter getParamsGetter(String[] args) {
        return new CliParamsGetter(args);
    }

    @Override
    protected boolean isParamsCorrect(Params params) {
        if (!params.isSuccess()) {
            errorStream.println(params.message());
            return false;
        }
        okStream.println(params.message());
        okStream.println(paramsViewer.view(params));
        return true;
    }

    @Override
    protected ImageRenderer getImageRenderer(Params params) {
        if (params.numberOfThreads() == null) {
            return new SingleThreadedImageRenderer(
                params,
                new AffineTransformationGenerator(-1.0, 1.0),
                new ColorGenerator(),
                new ConcretePointGeneratorFactory(),
                new ConcreteNonlinearTransformationGeneratorFactory()
            );
        }

        return new MultiThreadedImageRenderer(
            params,
            new AffineTransformationGenerator(-1.0, 1.0),
            new ColorGenerator(),
            new ConcretePointGeneratorFactory(),
            new ConcreteNonlinearTransformationGeneratorFactory()
        );
    }

    @Override
    protected ImageCorrector getImageCorrector(Params params) {
        return new GammaImageCorrector();
    }

    @Override
    protected ImageSaver getImageSaver(Params params) {
        return new FileImageSaver(params.saveParams().path(), params.saveParams().format());
    }

    @Override
    protected void processSaveSuccess(Params params, boolean saveSuccess) {
        if (!saveSuccess) {
            errorStream.println("Error while saving image");
            return;
        }
        okStream.println(MessageFormat.format("Image successfully saved to {0}",
            params.saveParams().path()));
    }
}
