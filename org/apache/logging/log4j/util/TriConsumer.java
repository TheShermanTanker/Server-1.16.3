package org.apache.logging.log4j.util;

public interface TriConsumer<K, V, S> {
    void accept(final K object1, final V object2, final S object3);
}
