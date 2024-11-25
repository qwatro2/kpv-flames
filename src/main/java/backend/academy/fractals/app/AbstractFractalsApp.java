package backend.academy.fractals.app;

import backend.academy.fractals.correctors.ImageCorrector;
import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.params.ParamsGetter;
import backend.academy.fractals.renderers.ImageRenderer;
import backend.academy.fractals.savers.ImageSaver;

public abstract class AbstractFractalsApp implements App {

    @Override
    public void run(String[] args) {
        ParamsGetter paramsGetter = getParamsGetter(args);
        Params params = paramsGetter.get();

        if (!isParamsCorrect(params)) {
            return;
        }

        ImageRenderer imageRenderer = getImageRenderer(params);
        PixelImage pixelImage = imageRenderer.render();

        ImageCorrector imageCorrector = getImageCorrector(params);
        imageCorrector.correct(pixelImage);

        ImageSaver imageSaver = getImageSaver(params);
        boolean saveSuccess = imageSaver.save(pixelImage);
        processSaveSuccess(params, saveSuccess);
    }

    protected abstract ParamsGetter getParamsGetter(String[] args);

    protected abstract boolean isParamsCorrect(Params params);

    protected abstract ImageRenderer getImageRenderer(Params params);

    protected abstract ImageCorrector getImageCorrector(Params params);

    protected abstract ImageSaver getImageSaver(Params params);

    protected abstract void processSaveSuccess(Params params, boolean saveSuccess);
}
