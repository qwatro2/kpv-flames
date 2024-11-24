package backend.academy.fractals.generators;

import java.util.Random;

public interface Generator<T> {
    T next();

    Generator<T> setRandom(Random random);
}
