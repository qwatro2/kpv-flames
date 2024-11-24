package backend.academy.fractals.generators;

import backend.academy.fractals.entities.Color;
import java.util.Random;

public class ColorGenerator implements Generator<Color> {
    private static final int MAX_VALUE = 256;

    private Random random;

    @Override
    public Generator<Color> setRandom(Random random) {
        this.random = random;
        return this;
    }

    @Override
    public Color next() {
        return new Color(nextComponent(), nextComponent(), nextComponent());
    }

    private int nextComponent() {
        return random.nextInt(0, MAX_VALUE);
    }
}
