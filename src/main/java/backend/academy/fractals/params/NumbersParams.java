package backend.academy.fractals.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumbersParams {
    private int numberOfSamples = 1000;
    private int numberOfTransformations = 100;
    private int numberOfIterationsPerSample = 10000;
}
