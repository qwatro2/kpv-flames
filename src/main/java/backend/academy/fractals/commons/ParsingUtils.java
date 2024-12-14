package backend.academy.fractals.commons;

import backend.academy.fractals.params.ImageFormat;
import backend.academy.fractals.params.NonlinearTransformationsGenerationOrder;
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
import lombok.experimental.UtilityClass;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@UtilityClass
public class ParsingUtils {
    public static Long parseLong(String source) {
        try {
            return Long.parseLong(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseInteger(String source) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Transformation parseTransformation(String source) {
        return switch (source) {
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

    public static NonlinearTransformationsGenerationOrder parseGenerationOrder(String source) {
        return switch (source) {
            case "ordered" -> NonlinearTransformationsGenerationOrder.ORDERED;
            case "random" -> NonlinearTransformationsGenerationOrder.RANDOM;
            default -> null;
        };
    }

    public static ImageFormat parseImageFormat(String source) {
        return switch (source.toLowerCase()) {
            case "png" -> ImageFormat.PNG;
            case "jpeg" -> ImageFormat.JPEG;
            case "bmp" -> ImageFormat.BMP;
            default -> null;
        };
    }

    public static Path parsePath(String source) {
        try {
            return Path.of(source);
        } catch (InvalidPathException e) {
            return null;
        }
    }

}
