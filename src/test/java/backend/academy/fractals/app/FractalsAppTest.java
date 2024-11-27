package backend.academy.fractals.app;

import backend.academy.fractals.correctors.GammaImageCorrector;
import backend.academy.fractals.params.CliParamsGetter;
import backend.academy.fractals.params.ImageFormat;
import backend.academy.fractals.params.NonlinearTransformationsGenerationOrder;
import backend.academy.fractals.params.NumbersParams;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.params.ParamsGetter;
import backend.academy.fractals.params.SaveParams;
import backend.academy.fractals.params.SizeParams;
import backend.academy.fractals.params.TransformationsParams;
import backend.academy.fractals.renderers.ImageRenderer;
import backend.academy.fractals.renderers.SingleThreadedImageRenderer;
import backend.academy.fractals.renderers.MultiThreadedImageRenderer;
import backend.academy.fractals.correctors.ImageCorrector;
import backend.academy.fractals.savers.ImageSaver;
import backend.academy.fractals.savers.FileImageSaver;
import backend.academy.fractals.viewers.Viewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FractalsAppTest {
    private ByteArrayOutputStream okStream;
    private ByteArrayOutputStream errorStream;
    private FractalsApp fractalsApp;
    private Params params;

    @BeforeEach
    public void setup() {
        okStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();
        Viewer<Params> paramsViewer = value -> "";
        fractalsApp = new FractalsApp(new PrintStream(okStream), new PrintStream(errorStream), paramsViewer);

        params = mock(Params.class);
    }

    @Test
    public void testGetParamsGetter() {
        String[] args = {"arg1", "arg2"};
        ParamsGetter paramsGetter = fractalsApp.getParamsGetter(args);
        assertNotNull(paramsGetter);
        assertInstanceOf(CliParamsGetter.class, paramsGetter);
    }

    @Test
    public void testIsParamsCorrectSuccess() {
        when(params.isSuccess()).thenReturn(true);
        when(params.message()).thenReturn("Parameters are valid");

        boolean result = fractalsApp.isParamsCorrect(params);

        assertTrue(result);
        assertTrue(okStream.toString().contains("Parameters are valid"));
    }

    @Test
    public void testIsParamsCorrectFailure() {
        when(params.isSuccess()).thenReturn(false);
        when(params.message()).thenReturn("Parameters are invalid");

        boolean result = fractalsApp.isParamsCorrect(params);

        assertFalse(result);
        assertTrue(errorStream.toString().contains("Parameters are invalid"));
    }

    @Test
    public void testGetImageRendererSingleThreaded() {
        when(params.numbersParams()).thenReturn(mock(NumbersParams.class));
        when(params.sizeParams()).thenReturn(mock(SizeParams.class));
        when(params.transformationsParams()).thenReturn(mock(TransformationsParams.class));
        when(params.transformationsParams().generationOrder()).thenReturn(mock(NonlinearTransformationsGenerationOrder.class));
        when(params.numberOfThreads()).thenReturn(null);

        ImageRenderer renderer = fractalsApp.getImageRenderer(params);

        assertNotNull(renderer);
        assertInstanceOf(SingleThreadedImageRenderer.class, renderer);
    }

    @Test
    public void testGetImageRendererMultiThreaded() {
        when(params.numbersParams()).thenReturn(mock(NumbersParams.class));
        when(params.numbersParams().numberOfSamples()).thenReturn(100);
        when(params.sizeParams()).thenReturn(mock(SizeParams.class));
        when(params.transformationsParams()).thenReturn(mock(TransformationsParams.class));
        when(params.transformationsParams().generationOrder()).thenReturn(mock(NonlinearTransformationsGenerationOrder.class));
        when(params.numberOfThreads()).thenReturn(4);

        ImageRenderer renderer = fractalsApp.getImageRenderer(params);

        assertNotNull(renderer);
        assertInstanceOf(MultiThreadedImageRenderer.class, renderer);
    }

    @Test
    public void testGetImageCorrector() {
        ImageCorrector corrector = fractalsApp.getImageCorrector(params);
        assertNotNull(corrector);
        assertInstanceOf(GammaImageCorrector.class, corrector);
    }

    @Test
    public void testGetImageSaver() {
        when(params.saveParams()).thenReturn(mock(SaveParams.class));
        when(params.saveParams().path()).thenReturn(Path.of("./result.png"));
        when(params.saveParams().format()).thenReturn(ImageFormat.PNG);

        ImageSaver saver = fractalsApp.getImageSaver(params);

        assertNotNull(saver);
        assertInstanceOf(FileImageSaver.class, saver);
    }

    @Test
    public void testProcessSaveSuccess() {
        when(params.saveParams()).thenReturn(mock(SaveParams.class));
        Path path = Path.of("./result.png");
        when(params.saveParams().path()).thenReturn(path);

        fractalsApp.processSaveSuccess(params, true);

        String expected = MessageFormat.format("Image successfully saved to {0}", path);
        assertTrue(okStream.toString().contains(expected));
    }

    @Test
    public void testProcessSaveFailure() {
        when(params.saveParams()).thenReturn(mock(SaveParams.class));
        when(params.saveParams().path()).thenReturn(Path.of("./result.png"));

        fractalsApp.processSaveSuccess(params, false);

        assertTrue(errorStream.toString().contains("Error while saving image"));
    }
}
