package backend.academy.fractals.paramsgetters;

import backend.academy.fractals.commons.ParsingUtils;
import backend.academy.fractals.params.ImageFormat;
import backend.academy.fractals.params.NonlinearTransformationsGenerationOrder;
import backend.academy.fractals.params.Params;
import backend.academy.fractals.transformations.Transformation;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.function.Function;

public class CliParamsGetter extends AbstractCliParamsGetter {
    private static final String ONE_WAS_PASSED = "{1} was passed";

    public CliParamsGetter(String[] args) {
        super(args);
        this.addParam("--n-samples", 1, this::processNumberOfSamples)
            .addParam("--n-transformations", 1, this::processNumberOfTransformations)
            .addParam("--n-iterations", 1, this::processNumberOfIterations)
            .addParam("--width", 1, this::processWidth)
            .addParam("--height", 1, this::processHeight)
            .addParam("--seed", 1, this::processSeed)
            .addParam("--add-transformation", 1, this::processAddTransformation)
            .addParam("--generation-order", 1, this::processGenerationOrder)
            .addParam("--n-symmetries", 1, this::processNumberOfSymmetries)
            .addParam("--path", 1, this::processPath)
            .addParam("--format", 1, this::processFormat)
            .addParam("--n-threads", 1, this::processNumberOfThreads);
    }

    @Override
    protected void processHelp(Params params, int index) {
        params.isSuccess(false);
        params.message(getHelpString());
    }

    @Override
    protected void processUnknownArgument(Params params, int index) {
        params.isSuccess(false);
        params.message(MessageFormat.format("Unknown argument {0}", args[index]));
    }

    @Override
    protected void checkProcessedParams(Params params) {
        if (params.transformationsParams().nonlinearTransformations().isEmpty()) {
            params.isSuccess(false);
            params.message("At least one nonlinear transformation must be added");
        }
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
        Long value = ParsingUtils.parseLong(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be long integer, \"{1}\" was passed",
                args[index], args[index + 1]));
            return;
        }

        params.seed(value);
    }

    private void processAddTransformation(Params params, int index) {
        Transformation value = ParsingUtils.parseTransformation(args[index + 1]);
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
        NonlinearTransformationsGenerationOrder value = ParsingUtils.parseGenerationOrder(args[index + 1]);
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
        Path value = ParsingUtils.parsePath(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Invalid path \"{0}\"", args[index + 1]));
            return;
        }
        params.saveParams().path(value);
    }

    private void processFormat(Params params, int index) {
        ImageFormat value = ParsingUtils.parseImageFormat(args[index + 1]);
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
        Integer value = ParsingUtils.parseInteger(args[index + 1]);
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

    private String getHelpString() {
        return """
            Options:
            --n-samples N             : integer > 0 : number of starting points                        \
            : default 1000
            --n-iterations N          : integer > 0 : number of iterations per sample                  \
            : default 100000
            --n-symmetries N          : integer > 0 : number of symmetries                             \
            : default 1 (no symmetries)
            --n-transformations N     : integer > 0 : number of affine transformations                 \
            : default 10
            --n-threads N             : integer > 0 : number of threads                                \
            : default null (no threading)
            --width W                 : integer > 0 : width of image in pixels                         \
            : default 900
            --height H                : integer > 0 : height of image in pixels                        \
            : default 900
            --seed N                  : long        : seed for random generator                        \
            : default null (no seed)
            --path PATH               : string      : path to save image                               \
            : default "./result.png"
            --format FORMAT           : string      : format to save image : one of "png"|"jpeg"|"bmp" \
            : default "png"
            --add-transformation NAME : string      : add nonlinear transformation
                                        one of "disk"|"hearth"|"polar"|"sinus"|"sphere"|"swirl"|\
            "horseshoe"|"handkerchief"|"eyefish"
                                        (can be used multiple times)
            --generation-order ORDER  : string      \
            : use nonlinear transformation in adding order or in random order
                                        one of "ordered"|"random"\
                                                  : default "ordered"
            """;
    }
}
