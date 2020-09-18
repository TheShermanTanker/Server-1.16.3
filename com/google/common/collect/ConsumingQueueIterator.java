package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayDeque;
import java.util.Queue;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class ConsumingQueueIterator<T> extends AbstractIterator<T> {
    private final Queue<T> queue;
    
    ConsumingQueueIterator(final T... elements) {
        Collections.addAll((Collection)(this.queue = (Queue<T>)new ArrayDeque(elements.length)), (Object[])elements);
    }
    
    ConsumingQueueIterator(final Queue<T> queue) {
        this.queue = Preconditions.<Queue<T>>checkNotNull(queue);
    }
    
    public T computeNext() {
        return (T)(this.queue.isEmpty() ? this.endOfData() : this.queue.remove());
    }
}
