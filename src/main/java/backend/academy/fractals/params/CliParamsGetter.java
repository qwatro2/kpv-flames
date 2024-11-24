package backend.academy.fractals.params;

import backend.academy.fractals.transformations.DiskTransformation;
import backend.academy.fractals.transformations.HearthTransformation;
import backend.academy.fractals.transformations.PolarTransformation;
import backend.academy.fractals.transformations.SinusTransformation;
import backend.academy.fractals.transformations.SphereTransformation;
import backend.academy.fractals.transformations.Transformation;
import java.text.MessageFormat;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CliParamsGetter implements ParamsGetter {
    private final String[] args;

    public CliParamsGetter(String[] args) {
        this.args = args;
    }

    @Override
    public Params get() {
        Params result = new Params();

        for (int i = 0; i < args.length - 1; i += 2) {
            BiConsumer<Params, Integer> biConsumer = switch (args[i]) {
                case "--n-samples" -> this::processNumberOfSamples;
                case "--n-transformations" -> this::processNumberOfTransformations;
                case "--n-iterations" -> this::processNumberOfIterations;
                case "--width" -> this::processWidth;
                case "--height" -> this::processHeight;
                case "--seed" -> this::processSeed;
                case "--add-transformation" -> this::processAddTransformation;
                case "--generation-order" -> this::processGenerationOrder;
                default -> this::processUnknownArgument;
            };
            biConsumer.accept(result, i);
            if (!result.isSuccess()) {
                return result;
            }
        }

        checkAddedTransformations(result);
        return result;
    }

    private void checkAddedTransformations(Params params) {
        if (params.nonlinearTransformations().isEmpty()) {
            params.isSuccess(false);
            params.message("At least one nonlinear transformation must be added");
        }
    }

    private void processUnknownArgument(Params params, int index) {
        params.isSuccess(false);
        params.message(MessageFormat.format("Unknown argument {0}", args[index]));
    }

    private void processNumberOfSamples(Params params, int index) {
        processIntegerParam(params, params::numberOfSamples, index);
    }

    private void processNumberOfTransformations(Params params, int index) {
        processIntegerParam(params, params::numberOfTransformations, index);
    }

    private void processNumberOfIterations(Params params, int index) {
        processIntegerParam(params, params::numberOfIterationsPerSample, index);
    }

    private void processWidth(Params params, int index) {
        processIntegerParam(params, params::width, index);
    }

    private void processHeight(Params params, int index) {
        processIntegerParam(params, params::height, index);
    }

    private void processSeed(Params params, int index) {
        processIntegerParam(params, params::seed, index);
    }

    private void processAddTransformation(Params params, int index) {
        Transformation value = parseTransformation(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be \"disk\"|\"hearth\"|\"polar\"|"
                    + "\"sinus\"|\"sphere\", \"{1}\" was passed",
                args[index], args[index + 1]));
            return;
        }
        params.nonlinearTransformations().add(value);
    }

    private void processGenerationOrder(Params params, int index) {
        NonlinearTransformationsGenerationOrder value = parseGenerationOrder(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be \"ordered\"|\"random\", "
                + "{1} was passed", args[index], args[index + 1]));
            return;
        }
        params.generationOrder(value);
    }

    private void processIntegerParam(Params params, Function<Integer, Params> field, int index) {
        Integer value = parseInteger(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be integer, \"{1}\" was passed",
                args[index], args[index + 1]));
            return;
        }

        field.apply(value);
    }

    private Integer parseInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Transformation parseTransformation(String s) {
        return switch (s) {
            case "disk" -> new DiskTransformation();
            case "hearth" -> new HearthTransformation();
            case "polar" -> new PolarTransformation();
            case "sinus" -> new SinusTransformation();
            case "sphere" -> new SphereTransformation();
            default -> null;
        };
    }

    private NonlinearTransformationsGenerationOrder parseGenerationOrder(String s) {
        return switch (s) {
            case "ordered" -> NonlinearTransformationsGenerationOrder.ORDERED;
            case "random" -> NonlinearTransformationsGenerationOrder.RANDOM;
            default -> null;
        };
    }
}