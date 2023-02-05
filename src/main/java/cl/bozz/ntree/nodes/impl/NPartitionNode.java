package cl.bozz.ntree.nodes.impl;

import cl.bozz.ntree.nodes.NNode;
import cl.bozz.ntree.model.NBoundary;
import cl.bozz.ntree.model.NItem;
import cl.bozz.ntree.model.NRangeQuery;
import cl.bozz.ntree.model.NVector;
import cl.bozz.ntree.utils.NBoundaryUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static cl.bozz.ntree.utils.PrettyPrintUtils.NEWLINE;

/**
 * Represents a partition of a node into multiple sub-spaces. Partitions are created by generating
 * @param <T>
 */
class NPartitionNode<T> implements NNode<T> {

    @Getter
    private final List<NTree<T>> partitions;

    protected NPartitionNode(
            final NBoundary boundary,
            final int partitionThreshold,
            final int pruningThreshold
    ) {
        this.partitions = NBoundaryUtils.createRegularPartition(boundary).stream()
                .map(partitionBoundary -> new NTree<T>(partitionBoundary, partitionThreshold, pruningThreshold))
                .collect(Collectors.toList());
    }

    @Override
    public boolean insertItem(NItem<T> newItem) {
        final NTree<T> targetPartition = findMatchingPartition(newItem.getCoordinate())
                .orElseThrow(() -> new IllegalArgumentException("Item doesn't fit in any bounds"));

        return targetPartition.insertItem(newItem);
    }

    private Optional<NTree<T>> findMatchingPartition(final NVector vector) {
        return partitions.stream()
                .filter(partition -> partition.isInBoundary(vector))
                .findFirst();
    }

    @Override
    public boolean removeItem(NItem<T> item) {
        final NTree<T> targetPartition = findMatchingPartition(item.getCoordinate())
                .orElseThrow(() -> new IllegalArgumentException("Item doesn't fit in any bounds"));

        return targetPartition.removeItem(item);
    }

    @Override
    public List<NItem<T>> getItems() {
        return partitions.stream()
                .map(NTree::getItems)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public String prettyPrint(int depth) {
        final StringBuilder sb = new StringBuilder();

        final AtomicBoolean first = new AtomicBoolean(true);
        partitions.forEach(partition -> {
            if (!first.get()) {
                sb.append(NEWLINE);
            }
            sb.append(partition.prettyPrint(depth + 1));
            first.set(false);
        });

        return sb.toString();
    }

    @Override
    public List<NItem<T>> findItemsInCoordinate(NVector pos) {
        final Optional<NTree<T>> maybePartition = findMatchingPartition(pos);
        if (maybePartition.isEmpty()) {
            return new ArrayList<>();
        }
        return maybePartition.get().findItemsInCoordinate(pos);
    }

    @Override
    public List<NItem<T>> findItemsInRange(NRangeQuery query) {
        return partitions.stream()
                .filter(partition -> partition.boundaryIntersects(query.getBoundary()))
                .map(partition -> partition.findItemsInRange(query))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format(
                "NPartitionNode (%d partitions)",
                partitions.size()
        );
    }
}
