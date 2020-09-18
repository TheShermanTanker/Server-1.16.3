package com.google.common.collect;

import java.util.Set;
import java.util.function.ObjIntConsumer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class EnumMultiset<E extends Enum<E>> extends AbstractMapBasedMultiset<E> {
    private transient Class<E> type;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <E extends Enum<E>> EnumMultiset<E> create(final Class<E> type) {
        return new EnumMultiset<E>(type);
    }
    
    public static <E extends Enum<E>> EnumMultiset<E> create(final Iterable<E> elements) {
        final Iterator<E> iterator = (Iterator<E>)elements.iterator();
        Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
        final EnumMultiset<E> multiset = new EnumMultiset<E>((java.lang.Class<E>)((Enum)iterator.next()).getDeclaringClass());
        Iterables.addAll((java.util.Collection<Object>)multiset, elements);
        return multiset;
    }
    
    public static <E extends Enum<E>> EnumMultiset<E> create(final Iterable<E> elements, final Class<E> type) {
        final EnumMultiset<E> result = EnumMultiset.<E>create(type);
        Iterables.addAll((java.util.Collection<Object>)result, elements);
        return result;
    }
    
    private EnumMultiset(final Class<E> type) {
        super((Map)WellBehavedMap.wrap((java.util.Map<Object, Object>)new EnumMap((Class)type)));
        this.type = type;
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.type);
        Serialization.writeMultiset((Multiset<Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final Class<E> localType = (Class<E>)stream.readObject();
        this.type = localType;
        this.setBackingMap((java.util.Map<E, Count>)WellBehavedMap.wrap((java.util.Map<Object, Object>)new EnumMap((Class)this.type)));
        Serialization.populateMultiset((Multiset<Object>)this, stream);
    }
}
