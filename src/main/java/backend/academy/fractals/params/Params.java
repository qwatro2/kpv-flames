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

    private int numberOfSamples = 10;
    private int numberOfTransformations = 10;
    private int numberOfIterationsPerSample = 100000;

    private int width = 900;
    private int height = 900;

    private Integer seed = null;

    private List<Transformation> nonlinearTransformations = new ArrayList<>();
    private NonlinearTransformationsGenerationOrder generationOrder = NonlinearTransformationsGenerationOrder.ORDERED;
}
