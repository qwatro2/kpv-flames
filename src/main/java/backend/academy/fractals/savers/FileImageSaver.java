package backend.academy.fractals.savers;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.PixelImage;
import backend.academy.fractals.params.ImageFormat;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;

public class FileImageSaver implements ImageSaver {
    private final static int BASE = 256;
    private final static int RED = 0xff0000;
    private final static int GREEN = 0xff00;
    private final static int BLUE = 0xff;
    private final static int BIT_LEN = 24;

    private final Path path;
    private final ImageFormat format;

    public FileImageSaver(Path path, ImageFormat format) {
        this.path = path;
        this.format = format;
    }

    @Override
    public boolean save(PixelImage image) {
        int[] rgbValues = pixelImageToIntArray(image);
        DataBuffer rgbData = new DataBufferInt(rgbValues, rgbValues.length);

        WritableRaster raster = Raster.createPackedRaster(rgbData, image.width(), image.height(), image.width(),
            new int[] {RED, GREEN, BLUE},
            null);

        ColorModel colorModel = new DirectColorModel(BIT_LEN, RED, GREEN, BLUE);
        BufferedImage img = new BufferedImage(colorModel, raster, false, null);
        try {
            ImageIO.write(img, format.name(), path.toFile());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private int[] pixelImageToIntArray(PixelImage image) {
        List<Integer> rgbValues = image.stream().map(pixel -> rgbToInt(pixel.color())).toList();
        int[] result = new int[rgbValues.size()];
        for (int i = 0; i < rgbValues.size(); ++i) {
            result[i] = rgbValues.get(i);
        }
        return result;
    }

    private int rgbToInt(Color color) {
        return color.b() + BASE * (color.g() + BASE * color.r());
    }
}
