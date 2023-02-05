package cl.bozz.ntree.nodes.impl;

import cl.bozz.ntree.nodes.NNode;
import cl.bozz.ntree.model.NBoundary;
import cl.bozz.ntree.model.NItem;
import cl.bozz.ntree.model.NRangeQuery;
import cl.bozz.ntree.model.NVector;
import cl.bozz.ntree.utils.PrettyPrintUtils;
import cl.bozz.ntree.utils.NBoundaryUtils;
import lombok.Getter;

import java.util.List;

import static cl.bozz.ntree.utils.PrettyPrintUtils.NEWLINE;

/**
 * Root of an N-Tree, an N-dimensional implementation of a quad-tree or an oct-tree.
 *
 * This node is a wrapper around an item node or a partition node. It handles the following tasks:
 * * Boundary checks for insertion, removal and search
 * * Delegation to inner node
 * * Transitioning between node types:
 *   * When the inner item node reaches the partitioning threshold, split it into 2^N partitions.
 *   * When the inner partition node loses enough items to reach the pruning threshold, replace it with an item node
 *
 * @param <T> Type of objects stored in tree.
 */
public class NTree<T> implements NNode<T> {

    private final NBoundary boundary;
    private final int partitionThreshold;
    private final int pruningThreshold;

    private NNode<T> innerNode;

    @Getter
    private int size = 0;

    public NTree(final NBoundary boundary, final int partitionThreshold, final int pruningThreshold) {
        if (partitionThreshold < pruningThreshold) {
            throw new IllegalArgumentException("Pruning threshold can't be higher than max items per node");
        }

        this.boundary = boundary;
        this.partitionThreshold = partitionThreshold;
        this.pruningThreshold = pruningThreshold;

        this.innerNode = new NItemNode<>(partitionThreshold);
    }

    @Override
    synchronized public boolean insertItem(NItem<T> newItem) {
        if (!NBoundaryUtils.isInBoundary(boundary, newItem.getCoordinate())) {
            throw new IllegalArgumentException("Item is out of bounds");
        }

        if (innerNode instanceof NItemNode && getSize() == partitionThreshold) {
            switchToPartitionNode();
        }

        final boolean result = innerNode.insertItem(newItem);
        if (result) {
            size++;
        }
        return result;
    }

    private void switchToPartitionNode() {
        //System.out.println("Partitioning node " + this);
        final NPartitionNode<T> newPartitionNode = new NPartitionNode<>(boundary, partitionThreshold, pruningThreshold);

        final boolean partitionSuccessful = innerNode.getItems().stream()
                .map(newPartitionNode::insertItem)
                .reduce(true, (a, b) -> a && b);
        if (!partitionSuccessful) {
            throw new IllegalStateException("Partitioning failed!");
        }
        innerNode = newPartitionNode;
    }

    @Override
    synchronized public boolean removeItem(NItem<T> item) {
        if (!NBoundaryUtils.isInBoundary(boundary, item.getCoordinate())) {
            throw new IllegalArgumentException("Item is out of bounds");
        }

        final boolean result = innerNode.removeItem(item);

        if (result) {
            size--;
            if (innerNode instanceof NPartitionNode && getSize() <= pruningThreshold) {
                //System.out.println("Pruning node " + this);
                final NNode<T> newItemNode = new NItemNode<>(partitionThreshold);

                innerNode.getItems().forEach(newItemNode::insertItem);
                innerNode = newItemNode;
            }
        }

        return result;
    }

    @Override
    public List<NItem<T>> getItems() {
        return innerNode.getItems();
    }

    @Override
    public String prettyPrint(int depth) {
        final String indentation = PrettyPrintUtils.getIndentationForDepth(depth);
        final StringBuilder sb = new StringBuilder();

        sb.append(indentation).append(this);

        final String innerPrettyPrint = innerNode.prettyPrint(depth);
        if (!innerPrettyPrint.isEmpty()) {
            sb.append(NEWLINE).append(innerPrettyPrint);
        }

        return sb.toString();
    }

    @Override
    public List<NItem<T>> findItemsInCoordinate(NVector pos) {
        return innerNode.findItemsInCoordinate(pos);
    }

    @Override
    public List<NItem<T>> findItemsInRange(NRangeQuery query) {
        return innerNode.findItemsInRange(query);
    }

    public boolean isInBoundary(final NVector vector) {
        return NBoundaryUtils.isInBoundary(boundary, vector);
    }

    public boolean boundaryIntersects(final NBoundary boundary) {
        return NBoundaryUtils.boundariesIntersect(this.boundary, boundary);
    }

    @Override
    public String toString() {
        return String.format(
                "%s : %s",
                boundary,
                innerNode.toString()
        );
    }
}
