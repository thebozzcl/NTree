package cl.bozz.ntree;

import cl.bozz.ntree.nodes.impl.NTree;
import cl.bozz.ntree.model.NBoundary;
import cl.bozz.ntree.model.NItem;
import cl.bozz.ntree.model.NVector;
import cl.bozz.ntree.nodes.NNode;
import cl.bozz.ntree.utils.NRangeQueryFactory;

import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(final String[] args) {
        final NNode<String> tree = new NTree<>(
                new NBoundary(
                        NVector.of(-100.0, -100.0, -100.0, -100.0),
                        NVector.of(100.0, 100.0, 100.0, 100.0)
                ),
                100,
                10
        );

        final int create = 20000000;
        final int findSingle = 10;
        final int findRange = 5;
        final double findRadius = 10.0;
        final int remove = 20000000;

        final long startTime = System.nanoTime();
        final List<NItem<String>> items = IntStream.range(0, create)
                .mapToObj(i -> new NItem<>(
                        NVector.of(Math.random() * 200 - 100, Math.random() * 200 - 100, Math.random() * 200 - 100, Math.random() * 200 - 100),
                        "blabla " + i
                ))
                .peek(item -> {
                    try {
                        //System.out.println("Adding " + item);
                        tree.insertItem(item);
                        //System.out.println(tree.prettyPrint());
                        //System.out.println();
                    } catch (final IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                }).toList();
        final long insertTime = System.nanoTime();

        System.out.println("Added " + items.size() + " items in " + (insertTime - startTime) + " nanoseconds");

        final long allItemSearchStartTime = System.nanoTime();
        final List<NItem<String>> allItemSearchResult = tree.findItemsInRange(NRangeQueryFactory.generateNRangeQuery(NVector.of(0.0, 0.0, 0.0, 0.0), 100000.0));
        final long allItemSearchEndTime = System.nanoTime();
        System.out.println("All " + allItemSearchResult.size() + " item(s) found in " + (allItemSearchEndTime - allItemSearchStartTime) + " nanoseconds");

        IntStream.range(0, findSingle)
                .forEach(i -> {
                    final long singleSearchStartTime = System.nanoTime();
                    final List<NItem<String>> foundItems = tree.findItemsInCoordinate(items.get(i).getCoordinate());
                    final long singleSerachEndTime = System.nanoTime();
                    System.out.println("Found " + foundItems.size() + " " + (i + 1) + "th item(s) in " + (singleSerachEndTime - singleSearchStartTime) + " nanoseconds");
                });

        IntStream.range(0, findRange)
                .forEach(i -> {
                    final long rangeSearchStartTime = System.nanoTime();
                    final List<NItem<String>> foundItems = tree.findItemsInRange(NRangeQueryFactory.generateNRangeQuery(items.get(i).getCoordinate(), findRadius));
                    final long rangeSerachEndTime = System.nanoTime();
                    System.out.println("Found " + foundItems.size() + " item(s) in " + (rangeSerachEndTime - rangeSearchStartTime) + " nanoseconds");
                });

        final long deleteStartTime = System.nanoTime();
        allItemSearchResult.subList(0, Math.min(allItemSearchResult.size(), remove)).forEach(toRemove -> {
            //System.out.println("Removing " + toRemove);
            //items.remove(toRemove);
            tree.removeItem(toRemove);
            //System.out.println(tree.prettyPrint());
            //System.out.println();
        });
        final long endTime = System.nanoTime();
        System.out.println("Removed " + remove + " items in " + (endTime - deleteStartTime) + " nanoseconds");
        System.out.println("Total elapsed time (nanos): " + (endTime - startTime));
    }
}
