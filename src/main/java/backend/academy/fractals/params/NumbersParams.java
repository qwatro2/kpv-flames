package backend.academy.fractals.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumbersParams {
    private static final int DEFAULT_N_SAMPLES = 1000;
    private static final int DEFAULT_N_TRANSFORMATIONS = 10;
    private static final int DEFAULT_N_ITERATIONS = 100000;
    private static final int DEFAULT_N_SYMMETRIES = 1;

    private int numberOfSamples = DEFAULT_N_SAMPLES;
    private int numberOfTransformations = DEFAULT_N_TRANSFORMATIONS;
    private int numberOfIterationsPerSample = DEFAULT_N_ITERATIONS;
    private int numberOfSymmetries = DEFAULT_N_SYMMETRIES;
}
