package backend.academy.fractals.params;

import backend.academy.fractals.transformations.DiskTransformation;
import backend.academy.fractals.transformations.EyefishTransformation;
import backend.academy.fractals.transformations.HandkerchiefTransformation;
import backend.academy.fractals.transformations.HearthTransformation;
import backend.academy.fractals.transformations.HorseshoeTransformation;
import backend.academy.fractals.transformations.PolarTransformation;
import backend.academy.fractals.transformations.SinusTransformation;
import backend.academy.fractals.transformations.SphereTransformation;
import backend.academy.fractals.transformations.SwirlTransformation;
import backend.academy.fractals.transformations.Transformation;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CliParamsGetter implements ParamsGetter {
    private static final String ONE_WAS_PASSED = "{1} was passed";

    private final String[] args;

    public CliParamsGetter(String[] args) {
        this.args = args;
    }

    @Override
    public Params get() {
        Params result = new Params();

        if (!processHelp(args, result)) {
            return result;
        }

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
                    + "\"sinus\"|\"sphere\"|\"swirl\"|\"horseshoe\"|"
                    + "\"handkerchief\"|\"eyefish\", \"{1}\" was passed",
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
                + ONE_WAS_PASSED, args[index], args[index + 1]));
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
                + ONE_WAS_PASSED, args[index], args[index + 1]));
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
        } else if (value < 1) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be integer greater than "
                + "or equal to 1, " + ONE_WAS_PASSED, args[index], args[index + 1]));
        } else {
            field.apply(value);
        }
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
            case "swirl" -> new SwirlTransformation();
            case "horseshoe" -> new HorseshoeTransformation();
            case "handkerchief" -> new HandkerchiefTransformation();
            case "eyefish" -> new EyefishTransformation();
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

    private boolean processHelp(String[] args, Params params) {
        if (args.length > 0 && (Objects.equals(args[0], "--help") || Objects.equals(args[0], "-h"))) {
            params.isSuccess(false);
            params.message(getHelpString());
        }
        return params.isSuccess();
    }

    private String getHelpString() {
        return """
            Options:
            --n-samples N             : integer > 0 : number of starting points                        : default 1000
            --n-iterations N          : integer > 0 : number of iterations per sample                  : default 100000
            --n-symmetries N          : integer > 0 : number of symmetries                             : default 1 (no symmetries)
            --n-transformations N     : integer > 0 : number of affine transformations                 : default 10
            --n-threads N             : integer > 0 : number of threads                                : default null (no threading)
            --width W                 : integer > 0 : width of image in pixels                         : default 900
            --height H                : integer > 0 : height of image in pixels                        : default 900
            --seed N                  : long        : seed for random generator                        : default null (no seed)
            --path PATH               : string      : path to save image                               : default "./result.png"
            --format FORMAT           : string      : format to save image : one of "png"|"jpeg"|"bmp" : default "png"
            --add-transformation NAME : string      : add nonlinear transformation
                                        one of "disk"|"hearth"|"polar"|"sinus"|"sphere"|"swirl"|"horseshoe"|"handkerchief"|"eyefish"
                                        (can be used multiple times)
            --generation-order ORDER  : string      : use nonlinear transformation in adding order or in random order
                                        one of "ordered"|"random"                                      : default "ordered"
            """;
    }
}
