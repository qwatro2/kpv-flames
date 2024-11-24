package backend.academy.fractals.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Pixel {
    @Setter
    private Color color;

    private int hitCount;

    public Pixel(Color color, int hitCount) {
        this.color = color;
        this.hitCount = hitCount;
    }

    public void increment() {
        ++this.hitCount;
    }
}
