package org.apache.logging.log4j.util;

public interface BiConsumer<K, V> {
    void accept(final K object1, final V object2);
}
