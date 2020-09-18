package com.google.common.collect;

import java.util.function.BiConsumer;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public class TreeMultimap<K, V> extends AbstractSortedKeySortedSetMultimap<K, V> {
    private transient Comparator<? super K> keyComparator;
    private transient Comparator<? super V> valueComparator;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
        return new TreeMultimap<K, V>((java.util.Comparator<? super K>)Ordering.<Comparable>natural(), (java.util.Comparator<? super V>)Ordering.<Comparable>natural());
    }
    
    public static <K, V> TreeMultimap<K, V> create(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
        return new TreeMultimap<K, V>(Preconditions.<Comparator<? super K>>checkNotNull(keyComparator), Preconditions.<Comparator<? super V>>checkNotNull(valueComparator));
    }
    
    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        return new TreeMultimap<K, V>((java.util.Comparator<? super K>)Ordering.<Comparable>natural(), (java.util.Comparator<? super V>)Ordering.<Comparable>natural(), multimap);
    }
    
    TreeMultimap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
        super((SortedMap)new TreeMap((Comparator)keyComparator));
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
    }
    
    private TreeMultimap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator, final Multimap<? extends K, ? extends V> multimap) {
        this(keyComparator, valueComparator);
        this.putAll((Multimap)multimap);
    }
    
    @Override
    SortedSet<V> createCollection() {
        return (SortedSet<V>)new TreeSet((Comparator)this.valueComparator);
    }
    
    @Override
    Collection<V> createCollection(@Nullable final K key) {
        if (key == null) {
            this.keyComparator().compare(key, key);
        }
        return super.createCollection(key);
    }
    
    @Deprecated
    public Comparator<? super K> keyComparator() {
        return this.keyComparator;
    }
    
    @Override
    public Comparator<? super V> valueComparator() {
        return this.valueComparator;
    }
    
    @GwtIncompatible
    public NavigableSet<V> get(@Nullable final K key) {
        return (NavigableSet<V>)super.get(key);
    }
    
    public NavigableSet<K> keySet() {
        return (NavigableSet<K>)super.keySet();
    }
    
    public NavigableMap<K, Collection<V>> asMap() {
        return (NavigableMap<K, Collection<V>>)super.asMap();
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.keyComparator());
        stream.writeObject(this.valueComparator());
        Serialization.writeMultimap((Multimap<Object, Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyComparator = Preconditions.<Comparator>checkNotNull(stream.readObject());
        this.valueComparator = Preconditions.<Comparator>checkNotNull(stream.readObject());
        this.setMap((java.util.Map<K, java.util.Collection<V>>)new TreeMap((Comparator)this.keyComparator));
        Serialization.populateMultimap((Multimap<Object, Object>)this, stream);
    }
}
