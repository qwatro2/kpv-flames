package backend.academy.fractals.correctors;

import backend.academy.fractals.entities.Color;
import backend.academy.fractals.entities.Pixel;
import backend.academy.fractals.entities.PixelImage;

public class GammaImageCorrector implements ImageCorrector {
    private static final double DEFAULT_GAMMA = 2.2;
    private final double gamma;

    public GammaImageCorrector(double gamma) {
        this.gamma = gamma;
    }

    public GammaImageCorrector() {
        this(DEFAULT_GAMMA);
    }

    @Override
    public void correct(PixelImage image) {
        NormalsAndMaxPair pair = calculateNormalsAndMax(image);
        processCorrection(image, pair);
    }

    private NormalsAndMaxPair calculateNormalsAndMax(PixelImage image) {
        int width = image.width();
        int height = image.height();

        double[] normals = new double[width * height];

        double max = 0.0;
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                if (image.get(row, col).hitCount() != 0) {
                    normals[row * width + col] = Math.log10(image.get(row, col).hitCount());
                    if (normals[row * width + col] > max) {
                        max = normals[row * width + col];
                    }
                }
            }
        }

        return new NormalsAndMaxPair(normals, max);
    }

    private void processCorrection(PixelImage image, NormalsAndMaxPair pair) {
        int width = image.width();
        int height = image.height();

        double[] normals = pair.normals();
        double max = pair.max();

        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                normals[row * width + col] /= max;
                Pixel currentPixel = image.get(row, col);
                double multiplier = Math.pow(normals[row * width + col], 1.0 / gamma);
                int newR = (int) (currentPixel.color().r() * multiplier);
                int newG = (int) (currentPixel.color().g() * multiplier);
                int newB = (int) (currentPixel.color().b() * multiplier);
                currentPixel.color(new Color(newR, newG, newB));
            }
        }
    }

    private record NormalsAndMaxPair(double[] normals, double max) {}
}
