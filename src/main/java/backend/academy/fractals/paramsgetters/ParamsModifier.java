package backend.academy.fractals.paramsgetters;

import backend.academy.fractals.params.Params;
import java.util.function.BiConsumer;

public interface ParamsModifier extends BiConsumer<Params, Integer> {
}
