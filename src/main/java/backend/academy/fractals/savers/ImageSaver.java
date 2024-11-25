package backend.academy.fractals.savers;

import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.params.ImageFormat;
import java.nio.file.Path;

public interface ImageSaver {
    void save(PixelImage image, Path path, ImageFormat format);
}
