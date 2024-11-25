package backend.academy.fractals.params;

import backend.academy.fractals.transformations.DiskTransformation;
import backend.academy.fractals.transformations.HearthTransformation;
import backend.academy.fractals.transformations.PolarTransformation;
import backend.academy.fractals.transformations.SinusTransformation;
import backend.academy.fractals.transformations.SphereTransformation;
import backend.academy.fractals.transformations.Transformation;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
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
                case "--n-symmetries" -> this::processNumberOfSymmetries;
                case "--path" -> this::processPath;
                case "--format" -> this::processFormat;
                case "--n-threads" -> this::processNumberOfThreads;
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
        if (params.transformationsParams().nonlinearTransformations().isEmpty()) {
            params.isSuccess(false);
            params.message("At least one nonlinear transformation must be added");
        }
    }

    private void processUnknownArgument(Params params, int index) {
        params.isSuccess(false);
        params.message(MessageFormat.format("Unknown argument {0}", args[index]));
    }

    private void processNumberOfSamples(Params params, int index) {
        processIntegerParam(params, n -> params.numbersParams().numberOfSamples(n), index);
    }

    private void processNumberOfTransformations(Params params, int index) {
        processIntegerParam(params, n -> params.numbersParams().numberOfTransformations(n), index);
    }

    private void processNumberOfIterations(Params params, int index) {
        processIntegerParam(params, n -> params.numbersParams().numberOfIterationsPerSample(n), index);
    }

    private void processWidth(Params params, int index) {
        processIntegerParam(params, n -> params.sizeParams().width(n), index);
    }

    private void processHeight(Params params, int index) {
        processIntegerParam(params, n -> params.sizeParams().height(n), index);
    }

    private void processSeed(Params params, int index) {
        Long value = parseLong(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be long integer, \"{1}\" was passed",
                args[index], args[index + 1]));
            return;
        }

        params.seed(value);
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
        params.transformationsParams().nonlinearTransformations().add(value);
    }

    private void processGenerationOrder(Params params, int index) {
        NonlinearTransformationsGenerationOrder value = parseGenerationOrder(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be \"ordered\"|\"random\", "
                + "{1} was passed", args[index], args[index + 1]));
            return;
        }
        params.transformationsParams().generationOrder(value);
    }

    private void processNumberOfSymmetries(Params params, int index) {
        processIntegerParam(params, n -> params.numbersParams().numberOfSymmetries(n), index);
    }

    private void processPath(Params params, int index) {
        Path value = parsePath(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Invalid path \"{0}\"", args[index + 1]));
            return;
        }
        params.saveParams().path(value);
    }

    private void processFormat(Params params, int index) {
        ImageFormat value = parseImageFormat(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be \"png\"|\"jpeg\"|\"bmp\", "
                + "{1} was passed", args[index], args[index + 1]));
            return;
        }
        params.saveParams().format(value);
    }

    private void processNumberOfThreads(Params params, int index) {
        processIntegerParam(params, params::numberOfThreads, index);
    }

    private <T> void processIntegerParam(Params params, Function<Integer, T> field, int index) {
        Integer value = parseInteger(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be integer, \"{1}\" was passed",
                args[index], args[index + 1]));
            return;
        } else if (value < 1) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be integer greater than " +
                "or equal to 1, {1} was passed", args[index], args[index + 1]));
            return;
        }

        field.apply(value);
    }

    private Long parseLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
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

    private ImageFormat parseImageFormat(String s) {
        return switch (s.toLowerCase()) {
            case "png" -> ImageFormat.PNG;
            case "jpeg" -> ImageFormat.JPEG;
            case "bmp" -> ImageFormat.BMP;
            default -> null;
        };
    }

    private Path parsePath(String s) {
        try {
            return Path.of(s);
        } catch (InvalidPathException e) {
            return null;
        }
    }
}
