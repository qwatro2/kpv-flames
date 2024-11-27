package backend.academy.fractals.params;

import static org.junit.jupiter.api.Assertions.*;
import backend.academy.fractals.transformations.DiskTransformation;
import backend.academy.fractals.transformations.HearthTransformation;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;

class CliParamsGetterTest {

    @Test
    void testGet_ValidArguments() {
        Params params = getParams();

        assertTrue(params.isSuccess());
        assertEquals(1000, params.numbersParams().numberOfSamples());
        assertEquals(10, params.numbersParams().numberOfTransformations());
        assertEquals(50000, params.numbersParams().numberOfIterationsPerSample());
        assertEquals(900, params.sizeParams().width());
        assertEquals(900, params.sizeParams().height());
        assertEquals(12345, params.seed());
        assertEquals(2, params.transformationsParams().nonlinearTransformations().size());
        assertInstanceOf(DiskTransformation.class, params.transformationsParams().nonlinearTransformations().get(0));
        assertInstanceOf(HearthTransformation.class, params.transformationsParams().nonlinearTransformations().get(1));
        assertEquals(NonlinearTransformationsGenerationOrder.ORDERED, params.transformationsParams().generationOrder());
        assertEquals(3, params.numbersParams().numberOfSymmetries());
        assertEquals(Path.of("./output.png"), params.saveParams().path());
        assertEquals(ImageFormat.PNG, params.saveParams().format());
        assertEquals(4, params.numberOfThreads());
    }

    private static Params getParams() {
        String[] args = {
            "--n-samples", "1000",
            "--n-transformations", "10",
            "--n-iterations", "50000",
            "--width", "900",
            "--height", "900",
            "--seed", "12345",
            "--add-transformation", "disk",
            "--add-transformation", "hearth",
            "--generation-order", "ordered",
            "--n-symmetries", "3",
            "--path", "./output.png",
            "--format", "png",
            "--n-threads", "4"
        };

        CliParamsGetter getter = new CliParamsGetter(args);
        return getter.get();
    }

    @Test
    void testGet_InvalidArguments() {
        String[] args = {
            "--n-samples", "-1000",
            "--n-transformations", "ten",
            "--unknown-option", "value",
            "--add-transformation", "disk"
        };

        CliParamsGetter getter = new CliParamsGetter(args);
        Params params = getter.get();

        assertFalse(params.isSuccess());
        assertEquals("Argument \"--n-samples\" should be integer greater than or equal to 1, -1000 was passed", params.message());
    }

    @Test
    void testGet_MissingRequiredTransformation() {
        String[] args = {
            "--n-samples", "1000",
            "--n-transformations", "5",
            "--n-iterations", "10000",
            "--width", "800",
            "--height", "600",
            "--seed", "12345"
        };

        CliParamsGetter getter = new CliParamsGetter(args);
        Params params = getter.get();

        assertFalse(params.isSuccess());
        assertEquals("At least one nonlinear transformation must be added", params.message());
    }

    @Test
    void testGet_HelpOption() {
        String[] args = {"--help"};

        CliParamsGetter getter = new CliParamsGetter(args);
        Params params = getter.get();

        assertFalse(params.isSuccess());
        assertTrue(params.message().contains("Options:"));
    }

    @Test
    void testGet_InvalidTransformation() {
        String[] args = {
            "--add-transformation", "invalidTransformation"
        };

        CliParamsGetter getter = new CliParamsGetter(args);
        Params params = getter.get();

        assertFalse(params.isSuccess());
        assertEquals("Argument \"--add-transformation\" should be \"disk\"|\"hearth\"|\"polar\"|"
            + "\"sinus\"|\"sphere\"|\"swirl\"|\"horseshoe\"|\"handkerchief\"|\"eyefish\", "
            + "\"invalidTransformation\" was passed", params.message());
    }

    @Test
    void testGet_ValidGenerationOrder() {
        String[] args = {
            "--add-transformation", "disk",
            "--generation-order", "random"
        };

        CliParamsGetter getter = new CliParamsGetter(args);
        Params params = getter.get();

        assertTrue(params.isSuccess());
        assertEquals(NonlinearTransformationsGenerationOrder.RANDOM, params.transformationsParams().generationOrder());
    }

    @Test
    void testGet_InvalidGenerationOrder() {
        String[] args = {
            "--generation-order", "unknownOrder"
        };

        CliParamsGetter getter = new CliParamsGetter(args);
        Params params = getter.get();

        assertFalse(params.isSuccess());
        assertEquals("Argument \"--generation-order\" should be \"ordered\"|\"random\", unknownOrder was passed", params.message());
    }

    @Test
    void testGet_ValidPath() {
        String[] args = {
            "--add-transformation", "disk",
            "--path", "./valid/path/to/image.png"
        };

        CliParamsGetter getter = new CliParamsGetter(args);
        Params params = getter.get();

        assertTrue(params.isSuccess());
        assertEquals(Path.of("./valid/path/to/image.png"), params.saveParams().path());
    }
}
