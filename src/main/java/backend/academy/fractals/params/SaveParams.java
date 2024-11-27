package backend.academy.fractals.params;

import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveParams {
    private Path path = Path.of("./result.png");
    private ImageFormat format = ImageFormat.PNG;
}
