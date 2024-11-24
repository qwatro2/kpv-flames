package backend.academy.fractals.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Params {
    private boolean isSuccess = true;
    private String message = "Params got successfully";

    private Integer seed = null;
    private NumbersParams numbersParams = new NumbersParams();
    private SizeParams sizeParams = new SizeParams();
    private TransformationsParams transformationsParams = new TransformationsParams();
}
