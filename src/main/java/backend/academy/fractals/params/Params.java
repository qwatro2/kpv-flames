package backend.academy.fractals.params;

import backend.academy.fractals.transformations.Transformation;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Params {
    private boolean isSuccess = true;
    private String message = "Params got successfully";

    private int numberOfSamples = 1000;
    private int numberOfTransformations = 100;
    private int numberOfIterationsPerSample = 10000;

    private int width = 900;
    private int height = 900;

    private Integer seed = null;

    private List<Transformation> nonlinearTransformations = new ArrayList<>();
    private NonlinearTransformationsGenerationOrder generationOrder = NonlinearTransformationsGenerationOrder.ORDERED;
}
