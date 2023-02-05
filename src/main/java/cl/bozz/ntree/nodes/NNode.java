package cl.bozz.ntree.nodes;

import cl.bozz.ntree.model.NItem;
import cl.bozz.ntree.model.NRangeQuery;
import cl.bozz.ntree.model.NVector;

import java.util.List;

/**
 * Common interface for all N-Tree node implementations.
 * @param <T>
 */
public interface NNode<T> {
    /**
     * Tries to insert an item into the tree. Returns true if the item was inserted successfully, false if it failed.
     * @param newItem item to insert
     * @return result of insertion
     */
    boolean insertItem(final NItem<T> newItem);

    /**
     * Tries to remove an item from the tree. Returns true if the item was found and removed, false if it wasn't found.
     * @param item item to remove
     * @return result of removal
     */
    boolean removeItem(final NItem<T> item);

    /**
     * Gets all items from the tree.
     * @return list of all items in the tree
     */
    List<NItem<T>> getItems();

    /**
     * Used to recursively print the tree and its contents.
     * @return the tree, as an indented string
     */
    public default String prettyPrint() {
        return prettyPrint(0);
    }

    /**
     * Used to recursively print the tree and its contents.
     * @param depth depth of indentation
     * @return the tree, as an indented string
     */
    String prettyPrint(final int depth);

    /**
     * Recursively finds items that have a given position in the tree
     * @param pos target position
     * @return a list of items with the same position
     */
    List<NItem<T>> findItemsInCoordinate(final NVector pos);

    /**
     * Recursively finds all items within a radius of the given point
     * @param query recursive range query
     * @return a list of items within the given hyper-sphere
     */
    List<NItem<T>> findItemsInRange(final NRangeQuery query);
}
