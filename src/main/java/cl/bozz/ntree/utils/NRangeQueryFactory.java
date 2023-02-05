package cl.bozz.ntree.utils;

import cl.bozz.ntree.model.NBoundary;
import cl.bozz.ntree.model.NRangeQuery;
import cl.bozz.ntree.model.NVector;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NRangeQueryFactory {
    public NRangeQuery generateNRangeQuery(final NVector center, final double radius) {
        final double[] lowerBoundary = new double[center.getSize()];
        final double[] upperBoundary = new double[center.getSize()];
        for (int i = 0; i < center.getSize(); i++) {
            lowerBoundary[i] = center.get(i) - radius;
            upperBoundary[i] = center.get(i) + radius;
        }

        final NBoundary boundary = new NBoundary(
                new NVector(lowerBoundary),
                new NVector(upperBoundary)
        );

        return new NRangeQuery(center, radius * radius, boundary);
    }
}
