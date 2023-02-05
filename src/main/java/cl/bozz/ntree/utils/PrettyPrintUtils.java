package cl.bozz.ntree.utils;

import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class PrettyPrintUtils {
    public static final String INDENTATION = "    ";
    public static final String NEWLINE = "\n";

    public String getIndentationForDepth(final int depth) {
        return IntStream.range(0, depth)
                .mapToObj(i -> INDENTATION)
                .collect(Collectors.joining());
    }
}
