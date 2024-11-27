package backend.academy.fractals.correctors;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.PixelImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GammaImageCorrectorTest {

    private GammaImageCorrector corrector;
    private PixelImage image;

    @BeforeEach
    void setUp() {
        image = new PixelImage(3, 3);
        corrector = new GammaImageCorrector();
    }

    @Test
    void testGammaCorrectionWithDefaultGamma() {
        image.get(0, 0).color(new Color(100, 150, 200));
        image.get(0, 0).increment();
        image.get(1, 1).color(new Color(50, 100, 150));
        image.get(1, 1).increment();

        corrector.correct(image);

        Color correctedColor0 = image.get(0, 0).color();
        Color correctedColor1 = image.get(1, 1).color();

        assertNotEquals(100, correctedColor0.r());
        assertNotEquals(150, correctedColor0.g());
        assertNotEquals(200, correctedColor0.b());

        assertNotEquals(50, correctedColor1.r());
        assertNotEquals(100, correctedColor1.g());
        assertNotEquals(150, correctedColor1.b());
    }

    @Test
    void testGammaCorrectionWithCustomGamma() {
        image.get(0, 0).color(new Color(100, 150, 200));
        image.get(0, 0).increment();
        image.get(1, 1).color(new Color(50, 100, 150));
        image.get(1, 1).increment();

        corrector = new GammaImageCorrector(1.5);

        corrector.correct(image);

        Color correctedColor0 = image.get(0, 0).color();
        Color correctedColor1 = image.get(1, 1).color();

        assertNotEquals(100, correctedColor0.r());
        assertNotEquals(150, correctedColor0.g());
        assertNotEquals(200, correctedColor0.b());

        assertNotEquals(50, correctedColor1.r());
        assertNotEquals(100, correctedColor1.g());
        assertNotEquals(150, correctedColor1.b());
    }

    @Test
    void testGammaCorrectionCornerCase() {
        image.get(0, 0).color(new Color(255, 255, 255));
        image.get(0, 0).increment();

        corrector.correct(image);

        Color correctedColor0 = image.get(0, 0).color();
        assertTrue(correctedColor0.r() <= 255);
        assertTrue(correctedColor0.g() <= 255);
        assertTrue(correctedColor0.b() <= 255);
    }
}
