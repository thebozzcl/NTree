package cl.bozz.ntree.utils;

import cl.bozz.ntree.model.NVector;
import lombok.experimental.UtilityClass;

import java.util.stream.IntStream;

@UtilityClass
public class NVectorUtils {
    public double getSquaredDistanceBetween(final NVector v1, final NVector v2) {
        if (v1.getSize() != v2.getSize()) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }

        return IntStream.range(0, v1.getSize())
                .mapToDouble(i -> {
                    final double delta = v2.get(i) - v1.get(i);
                    return delta * delta;
                })
                .sum();
    }
}
