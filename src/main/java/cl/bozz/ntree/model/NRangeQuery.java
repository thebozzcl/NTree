package cl.bozz.ntree.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NRangeQuery {
    private final NVector center;
    private final double squaredRadius;
    private final NBoundary boundary;

    @Override
    public String toString() {
        return String.format(
                "%s in squared radius %f",
                center,
                squaredRadius);
    }
}
