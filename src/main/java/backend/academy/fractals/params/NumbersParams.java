package backend.academy.fractals.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumbersParams {
    private int numberOfSamples = 1000;
    private int numberOfTransformations = 10;
    private int numberOfIterationsPerSample = 100000;
    private int numberOfSymmetries = 1;
}
