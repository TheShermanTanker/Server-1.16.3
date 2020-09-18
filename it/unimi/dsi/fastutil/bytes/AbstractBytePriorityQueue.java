package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

@Deprecated
public abstract class AbstractBytePriorityQueue extends AbstractPriorityQueue<Byte> implements Serializable, BytePriorityQueue {
    private static final long serialVersionUID = 1L;
}
