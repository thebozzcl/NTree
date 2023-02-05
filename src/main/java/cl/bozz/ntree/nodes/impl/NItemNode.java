package cl.bozz.ntree.nodes.impl;

import cl.bozz.ntree.nodes.NNode;
import cl.bozz.ntree.model.NItem;
import cl.bozz.ntree.model.NRangeQuery;
import cl.bozz.ntree.model.NVector;
import cl.bozz.ntree.utils.NVectorUtils;
import cl.bozz.ntree.utils.PrettyPrintUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static cl.bozz.ntree.utils.PrettyPrintUtils.INDENTATION;
import static cl.bozz.ntree.utils.PrettyPrintUtils.NEWLINE;

/**
 * Inner node of the N-Tree. Represents a list of items.
 * @param <T> type of item being stored by the tree
 */
@RequiredArgsConstructor
class NItemNode<T> implements NNode<T> {

    @Getter
    private final List<NItem<T>> items = new ArrayList<>();
    private final int maxItems;

    @Override
    public boolean insertItem(NItem<T> newItem) {
        if (items.size() == maxItems) {
            return false;
        }

        return items.add(newItem);
    }

    @Override
    public boolean removeItem(NItem<T> item) {
        if (items.size() == 0) {
            return false;
        }

        return items.remove(item);
    }

    @Override
    public String prettyPrint(int depth) {
        final String indentation = PrettyPrintUtils.getIndentationForDepth(depth);
        final StringBuilder sb = new StringBuilder();

        final AtomicBoolean first = new AtomicBoolean(true);
        items.forEach(item -> {
            if (!first.get()) {
                sb.append(NEWLINE);
            }
            sb.append(indentation).append(INDENTATION).append(item);
            first.set(false);
        });

        return sb.toString();
    }

    @Override
    public List<NItem<T>> findItemsInCoordinate(NVector pos) {
        return getItems().stream()
                .filter(item -> item.getCoordinate().equals(pos))
                .collect(Collectors.toList());
    }

    @Override
    public List<NItem<T>> findItemsInRange(NRangeQuery query) {
        return getItems().stream()
                .filter(item -> {
                    final double squaredDistance = NVectorUtils.getSquaredDistanceBetween(item.getCoordinate(), query.getCenter());
                    return squaredDistance <= query.getSquaredRadius();
                })
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format(
                "NItemNode (%d items)",
                items.size()
        );
    }
}
