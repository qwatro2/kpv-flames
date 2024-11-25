package backend.academy.fractals.savers;

import backend.academy.fractals.entities.PixelImage;

public interface ImageSaver {
    boolean save(PixelImage image);
}
