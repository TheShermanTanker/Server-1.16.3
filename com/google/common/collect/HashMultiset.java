package com.google.common.collect;

import java.util.Set;
import java.util.function.ObjIntConsumer;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public final class HashMultiset<E> extends AbstractMapBasedMultiset<E> {
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <E> HashMultiset<E> create() {
        return new HashMultiset<E>();
    }
    
    public static <E> HashMultiset<E> create(final int distinctElements) {
        return new HashMultiset<E>(distinctElements);
    }
    
    public static <E> HashMultiset<E> create(final Iterable<? extends E> elements) {
        final HashMultiset<E> multiset = HashMultiset.<E>create(Multisets.inferDistinctElements(elements));
        Iterables.addAll((java.util.Collection<Object>)multiset, elements);
        return multiset;
    }
    
    private HashMultiset() {
        super((Map)new HashMap());
    }
    
    private HashMultiset(final int distinctElements) {
        super((Map)Maps.newHashMapWithExpectedSize(distinctElements));
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMultiset((Multiset<Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final int distinctElements = Serialization.readCount(stream);
        this.setBackingMap((java.util.Map<E, Count>)Maps.newHashMap());
        Serialization.populateMultiset((Multiset<Object>)this, stream, distinctElements);
    }
}
