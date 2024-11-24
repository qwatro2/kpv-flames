package backend.academy.fractals.factories;

public interface PointGeneratorFactory {
    GeneratorWithBounds produce(int width, int height);
}
