package backend.academy.fractals.viewers;

import backend.academy.fractals.params.Params;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;

public class ParamsViewer implements Viewer<Params> {
    private static final int ROWS = 4;
    private static final int COLS = 6;
    private static final int FIRST_COL = 0;
    private static final int SECOND_COL = 2;
    private static final int THIRD_COL = 4;
    private static final String PARAMETER = "Parameter";
    private static final String VALUE = "Value";

    private static final List<String> FIRST_COLUMN = List.of(
        "Number of samples", "Number of transformations",
        "Number of iterations per sample", "Number of symmetries"
    );

    private static final List<String> SECOND_COLUMN = List.of(
        "Width", "Height", "Nonlinear transformations", "Nonlinear transformations order"
    );

    private static final List<String> THIRD_COLUMN = List.of(
        "Path", "Image format", "Number of threads", "Seed"
    );

    @Override
    public String view(Params value) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] grid = getGrid(value);
        List<Integer> maxLengthsByCol = IntStream.range(0, COLS)
            .mapToObj(col -> calculateMaxLength(grid, col))
            .toList();

        addLine(stringBuilder, maxLengthsByCol);
        stringBuilder.append('|');
        for (int col = 0; col < COLS; ++col) {
            String header = col % 2 == 0 ? PARAMETER : VALUE;
            stringBuilder.append(StringUtils.leftPad(header, maxLengthsByCol.get(col))).append('|');
        }
        stringBuilder.append('\n');
        addLine(stringBuilder, maxLengthsByCol);

        for (int row = 0; row < ROWS; ++row) {
            stringBuilder.append('|');
            for (int col = 0; col < COLS; ++col) {
                stringBuilder.append(StringUtils.leftPad(grid[row * COLS + col],
                    maxLengthsByCol.get(col))).append('|');
            }
            stringBuilder.append('\n');
            addLine(stringBuilder, maxLengthsByCol);
        }

        return stringBuilder.toString();
    }

    private void addLine(StringBuilder stringBuilder, List<Integer> maxLengthsByCol) {
        stringBuilder.append('+');
        for (int col = 0; col < COLS; ++col) {
            stringBuilder.append("-".repeat(maxLengthsByCol.get(col))).append('+');
        }
        stringBuilder.append('\n');
    }

    private String[] getGrid(Params params) {
        String[] result = new String[ROWS * COLS];

        List<String> firstValues = List.of(
            String.valueOf(params.numbersParams().numberOfSamples()),
            String.valueOf(params.numbersParams().numberOfTransformations()),
            String.valueOf(params.numbersParams().numberOfIterationsPerSample()),
            String.valueOf(params.numbersParams().numberOfSymmetries())
        );

        List<String> secondValues = List.of(
            String.valueOf(params.sizeParams().width()),
            String.valueOf(params.sizeParams().height()),
            viewListOfString(params
                .transformationsParams()
                .nonlinearTransformations()
                .stream()
                .map(this::viewNonlinearTransformation)
                .toList()),
            String.valueOf(params.transformationsParams().generationOrder())
        );

        List<String> thirdValues = List.of(
            String.valueOf(params.saveParams().path()),
            String.valueOf(params.saveParams().format()),
            String.valueOf(params.numberOfThreads()),
            String.valueOf(params.seed())
        );

        fillNColumn(result, FIRST_COLUMN, firstValues, FIRST_COL);
        fillNColumn(result, SECOND_COLUMN, secondValues, SECOND_COL);
        fillNColumn(result, THIRD_COLUMN, thirdValues, THIRD_COL);
        return result;
    }

    private void fillNColumn(String[] grid, List<String> names, List<String> values, int col) {
        for (int r = 0; r < ROWS; ++r) {
            grid[col + r * COLS] = names.get(r);
            grid[1 + col + r * COLS] = values.get(r);
        }
    }

    private int calculateMaxLength(String[] grid, int col) {
        int result = IntStream.range(0, ROWS)
            .mapToObj(row -> grid[row * COLS + col].length())
            .max(Comparator.comparing(i -> i))
            .orElse(0);

        if (col % 2 == 0) {
            if (result < PARAMETER.length()) {
                result = PARAMETER.length();
            }
        } else {
            if (result < VALUE.length()) {
                result = VALUE.length();
            }
        }
        return result;
    }

    private String viewListOfString(List<String> list) {
        return '[' + String.join(", ",
            list.stream().map(s -> "\"" + s + "\"").toList())
            + ']';
    }

    private String viewNonlinearTransformation(Transformation transformation) {
        return switch (transformation) {
            case DiskTransformation t -> "disk";
            case HearthTransformation t -> "hearth";
            case PolarTransformation t -> "polar";
            case SinusTransformation t -> "sinus";
            case SphereTransformation t -> "sphere";
            case SwirlTransformation t -> "swirl";
            case HorseshoeTransformation t -> "horseshoe";
            case HandkerchiefTransformation t -> "handkerchief";
            case EyefishTransformation t -> "eyefish";
            default -> "unknown";
        };
    }
}
