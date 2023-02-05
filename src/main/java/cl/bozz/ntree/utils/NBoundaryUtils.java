package cl.bozz.ntree.utils;

import cl.bozz.ntree.model.NBoundary;
import cl.bozz.ntree.model.NVector;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
public class NBoundaryUtils {

    public boolean isInBoundary(final NBoundary boundary, final NVector vector) {
        if (vector.getSize() != boundary.getBoundarySize()) {
            throw new IllegalArgumentException("Item coordinate vector must have the same size as boundaries");
        }

        for (int i = 0; i < vector.getSize(); i++) {
            if (vector.get(i) < boundary.getLowerBoundary().get(i) || vector.get(i) >= boundary.getUpperBoundary().get(i)) {
                return false;
            }
        }
        return true;
    }

    public List<NBoundary> createRegularPartition(final NBoundary boundary) {
        final int boundarySize = boundary.getBoundarySize();
        final NVector lowerBoundary = boundary.getLowerBoundary();

        final long combinationCount = 1L << boundarySize;
        final NVector delta = getBoundaryDelta(boundary.getLowerBoundary(), boundary.getUpperBoundary());

        final List<NBoundary> partitions = new ArrayList<>();

        for (long i = 0; i < combinationCount; i++) {
            final double[] newLowerBoundary = new double[boundarySize];
            final double[] newUpperBoundary = new double[boundarySize];
            long number = i;
            for (int j = 0; j < boundarySize; j++) {
                final long residue = number % 2;
                final boolean result = residue != 0;
                newLowerBoundary[j] = lowerBoundary.get(j) + (result ? delta.get(j) : 0);
                newUpperBoundary[j] = newLowerBoundary[j] + delta.get(j);
                number = (number - residue) >> 1;
            }
            final NBoundary newBoundary = new NBoundary(new NVector(newLowerBoundary), new NVector(newUpperBoundary));
            partitions.add(newBoundary);
        }
        return partitions;
    }

    private NVector getBoundaryDelta(final NVector lowerVector, final NVector upperVector) {
        final double[] delta = new double[lowerVector.getSize()];
        for (int i = 0; i < delta.length; i++) {
            delta[i] = (upperVector.get(i) - lowerVector.get(i)) / 2;
        }
        return new NVector(delta);
    }

    public boolean boundariesIntersect(final NBoundary boundary1, final NBoundary boundary2) {
        return IntStream.range(0, boundary1.getBoundarySize())
                .allMatch(
                        i -> boundary1.getLowerBoundary().get(i) < boundary2.getUpperBoundary().get(i)
                                && boundary1.getUpperBoundary().get(i) > boundary2.getLowerBoundary().get(i)
                );
    }
}
