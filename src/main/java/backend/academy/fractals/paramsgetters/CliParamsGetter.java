package backend.academy.fractals.paramsgetters;

import backend.academy.fractals.commons.ParsingUtils;
import backend.academy.fractals.params.Params;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class CliParamsGetter extends AbstractCliParamsGetter {
    private static final List<String> VALID_TRANSFORMATIONS = List.of(
        "disk", "hearth", "polar", "sinus", "sphere", "swirl", "horseshoe", "handkerchief", "eyefish"
    );

    private static final Map<String, Function<Params, Integer>> POSITIVE_INTEGER_FIELDS = Map.of(
        "--n-samples", params -> params.numbersParams().numberOfSamples(),
        "--n-iterations", params -> params.numbersParams().numberOfIterationsPerSample(),
        "--n-symmetries", params -> params.numbersParams().numberOfSymmetries(),
        "--n-transformations", params -> params.numbersParams().numberOfTransformations(),
        "--n-threads", Params::numberOfThreads,
        "--width", params -> params.sizeParams().width(),
        "--height", params -> params.sizeParams().height()
    );

    private final List<Predicate<Params>> checkPipeline = List.of(
        this::checkIntegerParams,
        this::checkTransformations
    );

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
        for (Predicate<Params> predicate : checkPipeline) {
            if (!predicate.test(params)) {
                return;
            }
        }
    }

    private boolean checkIntegerParams(Params params) {
        for (Map.Entry<String, Function<Params, Integer>> entry: POSITIVE_INTEGER_FIELDS.entrySet()) {
            String argument = entry.getKey();
            Function<Params, Integer> field = entry.getValue();
            Integer fieldValue = field.apply(params);
            if (fieldValue != null && fieldValue < 1) {
                params.isSuccess(false);
                params.message(MessageFormat.format("Argument \"{0}\" should be positive integer, "
                    + "\"{1}\" was passed", argument, field.apply(params)));
                return false;
            }
        }
        return true;
    }

    private boolean checkTransformations(Params params) {
        if (params.transformationsParams().nonlinearTransformations().isEmpty()) {
            params.isSuccess(false);
            params.message("At least one nonlinear transformation must be added");
        }
        return params.isSuccess();
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

    private void processNumberOfSymmetries(Params params, int index) {
        processIntegerParam(params, n -> params.numbersParams().numberOfSymmetries(n), index);
    }

    private void processNumberOfThreads(Params params, int index) {
        processIntegerParam(params, params::numberOfThreads, index);
    }

    private void processAddTransformation(Params params, int index) {
        process(params, value -> params.transformationsParams().nonlinearTransformations().add(value),
            index, ParsingUtils::parseTransformation, String.join("|",
                VALID_TRANSFORMATIONS.stream().map(s -> "\"" + s + "\"").toList()));
    }

    private void processGenerationOrder(Params params, int index) {
        process(params, value -> params.transformationsParams().generationOrder(value), index,
            ParsingUtils::parseGenerationOrder, "\"ordered\"|\"random\"");
    }

    private void processPath(Params params, int index) {
        process(params, value -> params.saveParams().path(value), index, ParsingUtils::parsePath, "valid path");
    }

    private void processFormat(Params params, int index) {
        process(params, value -> params.saveParams().format(value), index, ParsingUtils::parseImageFormat,
            "\"png\"|\"jpeg\"|\"bmp\"");
    }

    private void processSeed(Params params, int index) {
        process(params, params::seed, index, ParsingUtils::parseLong, "long integer");
    }

    private <T> void processIntegerParam(Params params, Function<Integer, T> field, int index) {
        process(params, field, index, ParsingUtils::parseInteger, "integer");
    }

    private <T, R> void process(
        Params params,
        Function<T, R> field,
        int index,
        Function<String, T> parser,
        String valid
    ) {
        T value = parser.apply(args[index + 1]);
        if (value == null) {
            params.isSuccess(false);
            params.message(MessageFormat.format("Argument \"{0}\" should be {2}, \"{1}\" was passed",
                args[index], args[index + 1], valid));
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
