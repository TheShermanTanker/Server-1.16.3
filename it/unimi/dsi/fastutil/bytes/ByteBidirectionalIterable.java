package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;

public interface ByteBidirectionalIterable extends ByteIterable {
    ByteBidirectionalIterator iterator();
}
