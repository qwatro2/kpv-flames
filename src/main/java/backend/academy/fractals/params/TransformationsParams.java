package backend.academy.fractals.params;

import backend.academy.fractals.transformations.Transformation;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransformationsParams {
    private List<Transformation> nonlinearTransformations = new ArrayList<>();
    private NonlinearTransformationsGenerationOrder generationOrder = NonlinearTransformationsGenerationOrder.ORDERED;

}
