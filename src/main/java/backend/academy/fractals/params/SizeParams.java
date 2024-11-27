package backend.academy.fractals.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeParams {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 900;

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
}
