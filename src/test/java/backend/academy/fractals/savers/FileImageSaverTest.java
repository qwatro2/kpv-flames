package backend.academy.fractals.savers;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.params.ImageFormat;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileImageSaverTest {
    @Test
    void testSaveImageToFile() throws IOException {
        PixelImage image = new PixelImage(2, 2);
        image.get(0, 0).color(new Color(255, 0, 0));
        image.get(0, 1).color(new Color(0, 255, 0));
        image.get(1, 0).color(new Color(0, 0, 255));
        image.get(1, 1).color(new Color(255, 255, 0));

        Path testPath = Paths.get("test_output.png");

        FileImageSaver saver = new FileImageSaver(testPath, ImageFormat.PNG);

        boolean result = saver.save(image);

        assertTrue(result);
        File savedFile = testPath.toFile();
        assertTrue(savedFile.exists());

        assertTrue(savedFile.getName().endsWith(".png"));

        Files.deleteIfExists(testPath);
    }
}
