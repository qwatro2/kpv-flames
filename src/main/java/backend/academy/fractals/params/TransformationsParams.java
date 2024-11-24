package backend.academy.fractals.params;

import backend.academy.fractals.transformations.Transformation;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TransformationsParams {
    private List<Transformation> nonlinearTransformations = new ArrayList<>();
    private NonlinearTransformationsGenerationOrder generationOrder = NonlinearTransformationsGenerationOrder.ORDERED;

}
