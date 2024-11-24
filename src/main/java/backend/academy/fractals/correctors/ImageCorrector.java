package backend.academy.fractals.correctors;

import backend.academy.fractals.entities.PixelImage;

public interface ImageCorrector {
    void correct(PixelImage image);
}
