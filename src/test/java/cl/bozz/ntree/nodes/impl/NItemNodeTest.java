package cl.bozz.ntree.nodes.impl;

import org.junit.jupiter.api.BeforeEach;

public class NItemNodeTest {
    private static final int MAX_ITEMS = 3;

    private NItemNode<String> node;

    @BeforeEach
    public void setup() {
        node = new NItemNode<>(MAX_ITEMS);
    }
}
