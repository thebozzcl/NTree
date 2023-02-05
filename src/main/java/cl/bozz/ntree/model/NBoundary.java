package cl.bozz.ntree.model;

import lombok.Getter;

@Getter
public class NBoundary {
    private final NVector lowerBoundary;
    private final NVector upperBoundary;

    public NBoundary(final NVector lowerBoundary, final NVector upperBoundary) {
        if (lowerBoundary.getSize() != upperBoundary.getSize()) {
            throw new IllegalArgumentException("Boundary vector sizes need to match");
        }
        this.lowerBoundary = lowerBoundary;
        this.upperBoundary = upperBoundary;
    }

    public int getBoundarySize() {
        return lowerBoundary.getSize();
    }

    @Override
    public String toString() {
        return String.format(
                "[%s, %s]",
                lowerBoundary,
                upperBoundary
        );
    }
}
