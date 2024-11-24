package backend.academy.fractals.entities;

import lombok.Getter;
import java.util.Arrays;
import java.util.stream.Stream;

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
        Pixel[] pixels = new Pixel[width * height];

        for (int i = 0; i < width * height; ++i) {
            pixels[i] = new Pixel(new Color(0, 0, 0), 0);
        }

        return pixels;
    }
}
