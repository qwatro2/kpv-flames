package backend.academy.fractals.params;

import lombok.Getter;
import lombok.Setter;
import java.nio.file.Path;

@Getter
@Setter
public class SaveParams {
    private Path path = Path.of("./result.png");
    private ImageFormat format = ImageFormat.PNG;
}
