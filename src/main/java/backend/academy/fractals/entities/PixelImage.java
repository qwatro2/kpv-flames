package backend.academy.fractals.entities;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.Getter;

public class PixelImage {
    private final Pixel[] pixels;
    @Getter
    private final int width;

    @Getter
    private final int height;

    public PixelImage(int width, int height) {
        this.pixels = constructEmpty(width, height);
        this.width = width;
        this.height = height;
    }

    public Pixel get(int row, int col) {
        return pixels[row * width + col];
    }

    public Stream<Pixel> stream() {
        return Arrays.stream(pixels);
    }

    private Pixel[] constructEmpty(int width, int height) {
        Pixel[] constructedPixels = new Pixel[width * height];

        for (int i = 0; i < width * height; ++i) {
            constructedPixels[i] = new Pixel(new Color(0, 0, 0), 0);
        }

        return constructedPixels;
    }
}
