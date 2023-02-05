package cl.bozz.ntree.model;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NVector {
    private final double[] coordinate;

    public double get(final int index) {
        return coordinate[index];
    }
    public int getSize() {
        return coordinate.length;
    }

    @Override
    public String toString() {
        return String.format(
                "[%s]",
                Arrays.stream(coordinate).mapToObj(Objects::toString).collect(Collectors.joining(", "))
        );
    }

    public static NVector of(final double... coordinate) {
        return new NVector(coordinate);
    }
}
