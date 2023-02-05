package cl.bozz.ntree.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NItem<T> {
    private final NVector coordinate;
    private final T item;

    @Override
    public String toString() {
        return String.format(
                "Item: %s %s",
                coordinate,
                item.toString()
        );
    }
}
