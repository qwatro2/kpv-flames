package backend.academy.fractals.entities;

import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
class PixelImageTest {

    @Test
    void get() {
        int width = 100;
        int height = 100;
        PixelImage pixelImage = new PixelImage(width, height);
        boolean actual = true;
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                actual &= isDefaultPixel(pixelImage.get(row, col));
            }
        }
        assertTrue(actual);
    }

    @Test
    void testStreamCount() {
        int width = 100;
        int height = 100;
        PixelImage pixelImage = new PixelImage(width, height);
        Stream<Pixel> stream = pixelImage.stream();
        long numberOfPixels = stream.count();
        assertEquals(width * height, numberOfPixels);
    }

    @Test
    void testCreation() {
        int width = 100;
        int height = 100;
        PixelImage pixelImage = new PixelImage(width, height);
        boolean actual = pixelImage.stream().allMatch(PixelImageTest::isDefaultPixel);
        assertTrue(actual);
        pixelImage.get(0, 0).increment();
        actual = pixelImage.stream().allMatch(PixelImageTest::isDefaultPixel);
        assertFalse(actual);
    }

    static boolean isDefaultPixel(Pixel pixel) {
        return pixel.hitCount() == 0
            && pixel.color().r() == 0
            && pixel.color().g() == 0
            && pixel.color().b() == 0;
    }
}
