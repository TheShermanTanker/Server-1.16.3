package com.google.common.collect;

import java.util.ListIterator;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.AbstractCollection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.Spliterator;
import java.util.SortedMap;
import java.util.NavigableMap;
import java.util.RandomAccess;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.SortedSet;
import java.util.NavigableSet;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
    private transient Map<K, Collection<V>> map;
    private transient int totalSize;
    private static final long serialVersionUID = 2447537837011683357L;
    
    protected AbstractMapBasedMultimap(final Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }
    
    final void setMap(final Map<K, Collection<V>> map) {
        this.map = map;
        this.totalSize = 0;
        for (final Collection<V> values : map.values()) {
            Preconditions.checkArgument(!values.isEmpty());
            this.totalSize += values.size();
        }
    }
    
    Collection<V> createUnmodifiableEmptyCollection() {
        return AbstractMapBasedMultimap.<V>unmodifiableCollectionSubclass(this.createCollection());
    }
    
    abstract Collection<V> createCollection();
    
    Collection<V> createCollection(@Nullable final K key) {
        return this.createCollection();
    }
    
    Map<K, Collection<V>> backingMap() {
        return this.map;
    }
    
    public int size() {
        return this.totalSize;
    }
    
    public boolean containsKey(@Nullable final Object key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public boolean put(@Nullable final K key, @Nullable final V value) {
        Collection<V> collection = (Collection<V>)this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
            if (collection.add(value)) {
                ++this.totalSize;
                this.map.put(key, collection);
                return true;
            }
            throw new AssertionError("New Collection violated the Collection spec");
        }
        else {
            if (collection.add(value)) {
                ++this.totalSize;
                return true;
            }
            return false;
        }
    }
    
    private Collection<V> getOrCreateCollection(@Nullable final K key) {
        Collection<V> collection = (Collection<V>)this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
            this.map.put(key, collection);
        }
        return collection;
    }
    
    @Override
    public Collection<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        final Iterator<? extends V> iterator = values.iterator();
        if (!iterator.hasNext()) {
            return this.removeAll(key);
        }
        final Collection<V> collection = this.getOrCreateCollection(key);
        final Collection<V> oldValues = this.createCollection();
        oldValues.addAll((Collection)collection);
        this.totalSize -= collection.size();
        collection.clear();
        while (iterator.hasNext()) {
            if (collection.add(iterator.next())) {
                ++this.totalSize;
            }
        }
        return AbstractMapBasedMultimap.<V>unmodifiableCollectionSubclass(oldValues);
    }
    
    public Collection<V> removeAll(@Nullable final Object key) {
        final Collection<V> collection = (Collection<V>)this.map.remove(key);
        if (collection == null) {
            return this.createUnmodifiableEmptyCollection();
        }
        final Collection<V> output = this.createCollection();
        output.addAll((Collection)collection);
        this.totalSize -= collection.size();
        collection.clear();
        return AbstractMapBasedMultimap.<V>unmodifiableCollectionSubclass(output);
    }
    
    static <E> Collection<E> unmodifiableCollectionSubclass(final Collection<E> collection) {
        if (collection instanceof NavigableSet) {
            return Sets.unmodifiableNavigableSet((java.util.NavigableSet<Object>)collection);
        }
        if (collection instanceof SortedSet) {
            return (Collection<E>)Collections.unmodifiableSortedSet((SortedSet)collection);
        }
        if (collection instanceof Set) {
            return (Collection<E>)Collections.unmodifiableSet((Set)collection);
        }
        if (collection instanceof List) {
            return (Collection<E>)Collections.unmodifiableList((List)collection);
        }
        return (Collection<E>)Collections.unmodifiableCollection((Collection)collection);
    }
    
    public void clear() {
        for (final Collection<V> collection : this.map.values()) {
            collection.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }
    
    public Collection<V> get(@Nullable final K key) {
        Collection<V> collection = (Collection<V>)this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
        }
        return this.wrapCollection(key, collection);
    }
    
    Collection<V> wrapCollection(@Nullable final K key, final Collection<V> collection) {
        if (collection instanceof NavigableSet) {
            return (Collection<V>)new WrappedNavigableSet(key, (NavigableSet<V>)collection, null);
        }
        if (collection instanceof SortedSet) {
            return (Collection<V>)new WrappedSortedSet(key, (SortedSet<V>)collection, null);
        }
        if (collection instanceof Set) {
            return (Collection<V>)new WrappedSet(key, (Set<V>)collection);
        }
        if (collection instanceof List) {
            return (Collection<V>)this.wrapList(key, (List<V>)collection, null);
        }
        return (Collection<V>)new WrappedCollection(key, collection, null);
    }
    
    private List<V> wrapList(@Nullable final K key, final List<V> list, @Nullable final WrappedCollection ancestor) {
        return (List<V>)((list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor));
    }
    
    private static <E> Iterator<E> iteratorOrListIterator(final Collection<E> collection) {
        return (Iterator<E>)((collection instanceof List) ? ((List)collection).listIterator() : collection.iterator());
    }
    
    @Override
    Set<K> createKeySet() {
        if (this.map instanceof NavigableMap) {
            return (Set<K>)new NavigableKeySet((NavigableMap<K, Collection<V>>)this.map);
        }
        if (this.map instanceof SortedMap) {
            return (Set<K>)new SortedKeySet((SortedMap<K, Collection<V>>)this.map);
        }
        return (Set<K>)new KeySet(this.map);
    }
    
    private void removeValuesForKey(final Object key) {
        final Collection<V> collection = Maps.<Collection<V>>safeRemove(this.map, key);
        if (collection != null) {
            final int count = collection.size();
            collection.clear();
            this.totalSize -= count;
        }
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
    
    @Override
    Iterator<V> valueIterator() {
        return (Iterator<V>)new Itr<V>() {
            @Override
            V output(final K key, final V value) {
                return value;
            }
        };
    }
    
    @Override
    Spliterator<V> valueSpliterator() {
        return CollectSpliterators.<Object, V>flatMap((java.util.Spliterator<Object>)this.map.values().spliterator(), (java.util.function.Function<? super Object, java.util.Spliterator<V>>)Collection::spliterator, 64, this.size());
    }
    
    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return super.entries();
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return (Iterator<Map.Entry<K, V>>)new Itr<Map.Entry<K, V>>() {
            @Override
            Map.Entry<K, V> output(final K key, final V value) {
                return Maps.<K, V>immutableEntry(key, value);
            }
        };
    }
    
    @Override
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        return CollectSpliterators.<Object, Map.Entry<K, V>>flatMap((java.util.Spliterator<Object>)this.map.entrySet().spliterator(), (java.util.function.Function<? super Object, java.util.Spliterator<Map.Entry<K, V>>>)(keyToValueCollectionEntry -> {
            final K key = (K)keyToValueCollectionEntry.getKey();
            final Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue();
            return CollectSpliterators.map((java.util.Spliterator<Object>)valueCollection.spliterator(), (java.util.function.Function<? super Object, ?>)(value -> Maps.immutableEntry(key, value)));
        }), 64, this.size());
    }
    
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.<BiConsumer<? super K, ? super V>>checkNotNull(action);
        this.map.forEach((key, valueCollection) -> valueCollection.forEach(value -> action.accept(key, value)));
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        if (this.map instanceof NavigableMap) {
            return (Map<K, Collection<V>>)new NavigableAsMap((NavigableMap<K, Collection<V>>)this.map);
        }
        if (this.map instanceof SortedMap) {
            return (Map<K, Collection<V>>)new SortedAsMap((SortedMap<K, Collection<V>>)this.map);
        }
        return (Map<K, Collection<V>>)new AsMap(this.map);
    }
    
    private class WrappedCollection extends AbstractCollection<V> {
        final K key;
        Collection<V> delegate;
        final WrappedCollection ancestor;
        final Collection<V> ancestorDelegate;
        
        WrappedCollection(final K key, @Nullable final Collection<V> delegate, final WrappedCollection ancestor) {
            this.key = key;
            this.delegate = delegate;
            this.ancestor = ancestor;
            this.ancestorDelegate = ((ancestor == null) ? null : ancestor.getDelegate());
        }
        
        void refreshIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            }
            else if (this.delegate.isEmpty()) {
                final Collection<V> newDelegate = (Collection<V>)AbstractMapBasedMultimap.this.map.get(this.key);
                if (newDelegate != null) {
                    this.delegate = newDelegate;
                }
            }
        }
        
        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            }
            else if (this.delegate.isEmpty()) {
                AbstractMapBasedMultimap.this.map.remove(this.key);
            }
        }
        
        K getKey() {
            return this.key;
        }
        
        void addToMap() {
            if (this.ancestor != null) {
                this.ancestor.addToMap();
            }
            else {
                AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
            }
        }
        
        public int size() {
            this.refreshIfEmpty();
            return this.delegate.size();
        }
        
        public boolean equals(@Nullable final Object object) {
            if (object == this) {
                return true;
            }
            this.refreshIfEmpty();
            return this.delegate.equals(object);
        }
        
        public int hashCode() {
            this.refreshIfEmpty();
            return this.delegate.hashCode();
        }
        
        public String toString() {
            this.refreshIfEmpty();
            return this.delegate.toString();
        }
        
        Collection<V> getDelegate() {
            return this.delegate;
        }
        
        public Iterator<V> iterator() {
            this.refreshIfEmpty();
            return (Iterator<V>)new WrappedIterator();
        }
        
        public Spliterator<V> spliterator() {
            this.refreshIfEmpty();
            return (Spliterator<V>)this.delegate.spliterator();
        }
        
        public boolean add(final V value) {
            this.refreshIfEmpty();
            final boolean wasEmpty = this.delegate.isEmpty();
            final boolean changed = this.delegate.add(value);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        WrappedCollection getAncestor() {
            return this.ancestor;
        }
        
        public boolean addAll(final Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.delegate.addAll((Collection)collection);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        public boolean contains(final Object o) {
            this.refreshIfEmpty();
            return this.delegate.contains(o);
        }
        
        public boolean containsAll(final Collection<?> c) {
            this.refreshIfEmpty();
            return this.delegate.containsAll((Collection)c);
        }
        
        public void clear() {
            final int oldSize = this.size();
            if (oldSize == 0) {
                return;
            }
            this.delegate.clear();
            AbstractMapBasedMultimap.this.totalSize -= oldSize;
            this.removeIfEmpty();
        }
        
        public boolean remove(final Object o) {
            this.refreshIfEmpty();
            final boolean changed = this.delegate.remove(o);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize--;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        public boolean removeAll(final Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.delegate.removeAll((Collection)c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        public boolean retainAll(final Collection<?> c) {
            Preconditions.<Collection<?>>checkNotNull(c);
            final int oldSize = this.size();
            final boolean changed = this.delegate.retainAll((Collection)c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        class WrappedIterator implements Iterator<V> {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate;
            
            WrappedIterator() {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = AbstractMapBasedMultimap.iteratorOrListIterator((java.util.Collection<Object>)WrappedCollection.this.delegate);
            }
            
            WrappedIterator(final Iterator<V> delegateIterator) {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = delegateIterator;
            }
            
            void validateIterator() {
                WrappedCollection.this.refreshIfEmpty();
                if (WrappedCollection.this.delegate != this.originalDelegate) {
                    throw new ConcurrentModificationException();
                }
            }
            
            public boolean hasNext() {
                this.validateIterator();
                return this.delegateIterator.hasNext();
            }
            
            public V next() {
                this.validateIterator();
                return (V)this.delegateIterator.next();
            }
            
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize--;
                WrappedCollection.this.removeIfEmpty();
            }
            
            Iterator<V> getDelegateIterator() {
                this.validateIterator();
                return this.delegateIterator;
            }
        }
    }
    
    private class WrappedSet extends WrappedCollection implements Set<V> {
        WrappedSet(final K key, final Set<V> delegate) {
            super(key, (Collection<V>)delegate, null);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = Sets.removeAllImpl(this.delegate, c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
    }
    
    private class WrappedSortedSet extends WrappedCollection implements SortedSet<V> {
        WrappedSortedSet(final K key, @Nullable final SortedSet<V> delegate, final WrappedCollection ancestor) {
            super(key, (Collection<V>)delegate, ancestor);
        }
        
        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet<V>)this.getDelegate();
        }
        
        public Comparator<? super V> comparator() {
            return this.getSortedSetDelegate().comparator();
        }
        
        public V first() {
            this.refreshIfEmpty();
            return (V)this.getSortedSetDelegate().first();
        }
        
        public V last() {
            this.refreshIfEmpty();
            return (V)this.getSortedSetDelegate().last();
        }
        
        public SortedSet<V> headSet(final V toElement) {
            this.refreshIfEmpty();
            return (SortedSet<V>)new WrappedSortedSet(this.getKey(), (SortedSet<V>)this.getSortedSetDelegate().headSet(toElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        public SortedSet<V> subSet(final V fromElement, final V toElement) {
            this.refreshIfEmpty();
            return (SortedSet<V>)new WrappedSortedSet(this.getKey(), (SortedSet<V>)this.getSortedSetDelegate().subSet(fromElement, toElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        public SortedSet<V> tailSet(final V fromElement) {
            this.refreshIfEmpty();
            return (SortedSet<V>)new WrappedSortedSet(this.getKey(), (SortedSet<V>)this.getSortedSetDelegate().tailSet(fromElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
    }
    
    class WrappedNavigableSet extends WrappedSortedSet implements NavigableSet<V> {
        WrappedNavigableSet(final K key, @Nullable final NavigableSet<V> delegate, final WrappedCollection ancestor) {
            super(key, (SortedSet<V>)delegate, ancestor);
        }
        
        NavigableSet<V> getSortedSetDelegate() {
            return (NavigableSet<V>)super.getSortedSetDelegate();
        }
        
        public V lower(final V v) {
            return (V)this.getSortedSetDelegate().lower(v);
        }
        
        public V floor(final V v) {
            return (V)this.getSortedSetDelegate().floor(v);
        }
        
        public V ceiling(final V v) {
            return (V)this.getSortedSetDelegate().ceiling(v);
        }
        
        public V higher(final V v) {
            return (V)this.getSortedSetDelegate().higher(v);
        }
        
        public V pollFirst() {
            return Iterators.<V>pollNext(this.iterator());
        }
        
        public V pollLast() {
            return Iterators.<V>pollNext(this.descendingIterator());
        }
        
        private NavigableSet<V> wrap(final NavigableSet<V> wrapped) {
            return (NavigableSet<V>)new WrappedNavigableSet(this.key, wrapped, (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        public NavigableSet<V> descendingSet() {
            return this.wrap((NavigableSet<V>)this.getSortedSetDelegate().descendingSet());
        }
        
        public Iterator<V> descendingIterator() {
            return (Iterator<V>)new WrappedIterator((Iterator<V>)this.getSortedSetDelegate().descendingIterator());
        }
        
        public NavigableSet<V> subSet(final V fromElement, final boolean fromInclusive, final V toElement, final boolean toInclusive) {
            return this.wrap((NavigableSet<V>)this.getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        public NavigableSet<V> headSet(final V toElement, final boolean inclusive) {
            return this.wrap((NavigableSet<V>)this.getSortedSetDelegate().headSet(toElement, inclusive));
        }
        
        public NavigableSet<V> tailSet(final V fromElement, final boolean inclusive) {
            return this.wrap((NavigableSet<V>)this.getSortedSetDelegate().tailSet(fromElement, inclusive));
        }
    }
    
    private class WrappedList extends WrappedCollection implements List<V> {
        WrappedList(final K key, @Nullable final List<V> delegate, final WrappedCollection ancestor) {
            super(key, (Collection<V>)delegate, ancestor);
        }
        
        List<V> getListDelegate() {
            return (List<V>)this.getDelegate();
        }
        
        public boolean addAll(final int index, final Collection<? extends V> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.getListDelegate().addAll(index, (Collection)c);
            if (changed) {
                final int newSize = this.getDelegate().size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        public V get(final int index) {
            this.refreshIfEmpty();
            return (V)this.getListDelegate().get(index);
        }
        
        public V set(final int index, final V element) {
            this.refreshIfEmpty();
            return (V)this.getListDelegate().set(index, element);
        }
        
        public void add(final int index, final V element) {
            this.refreshIfEmpty();
            final boolean wasEmpty = this.getDelegate().isEmpty();
            this.getListDelegate().add(index, element);
            AbstractMapBasedMultimap.this.totalSize++;
            if (wasEmpty) {
                this.addToMap();
            }
        }
        
        public V remove(final int index) {
            this.refreshIfEmpty();
            final V value = (V)this.getListDelegate().remove(index);
            AbstractMapBasedMultimap.this.totalSize--;
            this.removeIfEmpty();
            return value;
        }
        
        public int indexOf(final Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().indexOf(o);
        }
        
        public int lastIndexOf(final Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().lastIndexOf(o);
        }
        
        public ListIterator<V> listIterator() {
            this.refreshIfEmpty();
            return (ListIterator<V>)new WrappedListIterator();
        }
        
        public ListIterator<V> listIterator(final int index) {
            this.refreshIfEmpty();
            return (ListIterator<V>)new WrappedListIterator(index);
        }
        
        public List<V> subList(final int fromIndex, final int toIndex) {
            this.refreshIfEmpty();
            return AbstractMapBasedMultimap.this.wrapList(this.getKey(), this.getListDelegate().subList(fromIndex, toIndex), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        private class WrappedListIterator extends WrappedIterator implements ListIterator<V> {
            WrappedListIterator() {
            }
            
            public WrappedListIterator(final int index) {
                super((Iterator<V>)WrappedList.this.getListDelegate().listIterator(index));
            }
            
            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator<V>)this.getDelegateIterator();
            }
            
            public boolean hasPrevious() {
                return this.getDelegateListIterator().hasPrevious();
            }
            
            public V previous() {
                return (V)this.getDelegateListIterator().previous();
            }
            
            public int nextIndex() {
                return this.getDelegateListIterator().nextIndex();
            }
            
            public int previousIndex() {
                return this.getDelegateListIterator().previousIndex();
            }
            
            public void set(final V value) {
                this.getDelegateListIterator().set(value);
            }
            
            public void add(final V value) {
                final boolean wasEmpty = WrappedList.this.isEmpty();
                this.getDelegateListIterator().add(value);
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    WrappedList.this.addToMap();
                }
            }
        }
    }
    
    private class RandomAccessWrappedList extends WrappedList implements RandomAccess {
        RandomAccessWrappedList(final K key, @Nullable final List<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
    }
    
    private class KeySet extends Maps.KeySet<K, Collection<V>> {
        KeySet(final Map<K, Collection<V>> subMap) {
            super(subMap);
        }
        
        @Override
        public Iterator<K> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> entryIterator = (Iterator<Map.Entry<K, Collection<V>>>)this.map().entrySet().iterator();
            return (Iterator<K>)new Iterator<K>() {
                Map.Entry<K, Collection<V>> entry;
                
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }
                
                public K next() {
                    this.entry = (Map.Entry<K, Collection<V>>)entryIterator.next();
                    return (K)this.entry.getKey();
                }
                
                public void remove() {
                    CollectPreconditions.checkRemove(this.entry != null);
                    final Collection<V> collection = (Collection<V>)this.entry.getValue();
                    entryIterator.remove();
                    AbstractMapBasedMultimap.this.totalSize -= collection.size();
                    collection.clear();
                }
            };
        }
        
        public Spliterator<K> spliterator() {
            return (Spliterator<K>)this.map().keySet().spliterator();
        }
        
        @Override
        public boolean remove(final Object key) {
            int count = 0;
            final Collection<V> collection = (Collection<V>)this.map().remove(key);
            if (collection != null) {
                count = collection.size();
                collection.clear();
                AbstractMapBasedMultimap.this.totalSize -= count;
            }
            return count > 0;
        }
        
        @Override
        public void clear() {
            Iterators.clear(this.iterator());
        }
        
        public boolean containsAll(final Collection<?> c) {
            return this.map().keySet().containsAll((Collection)c);
        }
        
        public boolean equals(@Nullable final Object object) {
            return this == object || this.map().keySet().equals(object);
        }
        
        public int hashCode() {
            return this.map().keySet().hashCode();
        }
    }
    
    private class SortedKeySet extends KeySet implements SortedSet<K> {
        SortedKeySet(final SortedMap<K, Collection<V>> subMap) {
            super((Map<K, Collection<V>>)subMap);
        }
        
        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap<K, Collection<V>>)super.map();
        }
        
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        public K first() {
            return (K)this.sortedMap().firstKey();
        }
        
        public SortedSet<K> headSet(final K toElement) {
            return (SortedSet<K>)new SortedKeySet((SortedMap<K, Collection<V>>)this.sortedMap().headMap(toElement));
        }
        
        public K last() {
            return (K)this.sortedMap().lastKey();
        }
        
        public SortedSet<K> subSet(final K fromElement, final K toElement) {
            return (SortedSet<K>)new SortedKeySet((SortedMap<K, Collection<V>>)this.sortedMap().subMap(fromElement, toElement));
        }
        
        public SortedSet<K> tailSet(final K fromElement) {
            return (SortedSet<K>)new SortedKeySet((SortedMap<K, Collection<V>>)this.sortedMap().tailMap(fromElement));
        }
    }
    
    class NavigableKeySet extends SortedKeySet implements NavigableSet<K> {
        NavigableKeySet(final NavigableMap<K, Collection<V>> subMap) {
            super((SortedMap<K, Collection<V>>)subMap);
        }
        
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap<K, Collection<V>>)super.sortedMap();
        }
        
        public K lower(final K k) {
            return (K)this.sortedMap().lowerKey(k);
        }
        
        public K floor(final K k) {
            return (K)this.sortedMap().floorKey(k);
        }
        
        public K ceiling(final K k) {
            return (K)this.sortedMap().ceilingKey(k);
        }
        
        public K higher(final K k) {
            return (K)this.sortedMap().higherKey(k);
        }
        
        public K pollFirst() {
            return Iterators.<K>pollNext(this.iterator());
        }
        
        public K pollLast() {
            return Iterators.<K>pollNext(this.descendingIterator());
        }
        
        public NavigableSet<K> descendingSet() {
            return (NavigableSet<K>)new NavigableKeySet((NavigableMap<K, Collection<V>>)this.sortedMap().descendingMap());
        }
        
        public Iterator<K> descendingIterator() {
            return (Iterator<K>)this.descendingSet().iterator();
        }
        
        public NavigableSet<K> headSet(final K toElement) {
            return this.headSet(toElement, false);
        }
        
        public NavigableSet<K> headSet(final K toElement, final boolean inclusive) {
            return (NavigableSet<K>)new NavigableKeySet((NavigableMap<K, Collection<V>>)this.sortedMap().headMap(toElement, inclusive));
        }
        
        public NavigableSet<K> subSet(final K fromElement, final K toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }
        
        public NavigableSet<K> subSet(final K fromElement, final boolean fromInclusive, final K toElement, final boolean toInclusive) {
            return (NavigableSet<K>)new NavigableKeySet((NavigableMap<K, Collection<V>>)this.sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        public NavigableSet<K> tailSet(final K fromElement) {
            return this.tailSet(fromElement, true);
        }
        
        public NavigableSet<K> tailSet(final K fromElement, final boolean inclusive) {
            return (NavigableSet<K>)new NavigableKeySet((NavigableMap<K, Collection<V>>)this.sortedMap().tailMap(fromElement, inclusive));
        }
    }
    
    private abstract class Itr<T> implements Iterator<T> {
        final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
        K key;
        Collection<V> collection;
        Iterator<V> valueIterator;
        
        Itr() {
            this.keyIterator = (Iterator<Map.Entry<K, Collection<V>>>)AbstractMapBasedMultimap.this.map.entrySet().iterator();
            this.key = null;
            this.collection = null;
            this.valueIterator = Iterators.<V>emptyModifiableIterator();
        }
        
        abstract T output(final K object1, final V object2);
        
        public boolean hasNext() {
            return this.keyIterator.hasNext() || this.valueIterator.hasNext();
        }
        
        public T next() {
            if (!this.valueIterator.hasNext()) {
                final Map.Entry<K, Collection<V>> mapEntry = (Map.Entry<K, Collection<V>>)this.keyIterator.next();
                this.key = (K)mapEntry.getKey();
                this.collection = (Collection<V>)mapEntry.getValue();
                this.valueIterator = (Iterator<V>)this.collection.iterator();
            }
            return this.output(this.key, this.valueIterator.next());
        }
        
        public void remove() {
            this.valueIterator.remove();
            if (this.collection.isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMapBasedMultimap.this.totalSize--;
        }
    }
    
    private class AsMap extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        final transient Map<K, Collection<V>> submap;
        
        AsMap(final Map<K, Collection<V>> submap) {
            this.submap = submap;
        }
        
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return (Set<Map.Entry<K, Collection<V>>>)new AsMapEntries();
        }
        
        public boolean containsKey(final Object key) {
            return Maps.safeContainsKey(this.submap, key);
        }
        
        public Collection<V> get(final Object key) {
            final Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
            if (collection == null) {
                return null;
            }
            final K k = (K)key;
            return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
        }
        
        @Override
        public Set<K> keySet() {
            return AbstractMapBasedMultimap.this.keySet();
        }
        
        public int size() {
            return this.submap.size();
        }
        
        public Collection<V> remove(final Object key) {
            final Collection<V> collection = (Collection<V>)this.submap.remove(key);
            if (collection == null) {
                return null;
            }
            final Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll((Collection)collection);
            AbstractMapBasedMultimap.this.totalSize -= collection.size();
            collection.clear();
            return output;
        }
        
        public boolean equals(@Nullable final Object object) {
            return this == object || this.submap.equals(object);
        }
        
        public int hashCode() {
            return this.submap.hashCode();
        }
        
        public String toString() {
            return this.submap.toString();
        }
        
        public void clear() {
            if (this.submap == AbstractMapBasedMultimap.this.map) {
                AbstractMapBasedMultimap.this.clear();
            }
            else {
                Iterators.clear(new AsMapIterator());
            }
        }
        
        Map.Entry<K, Collection<V>> wrapEntry(final Map.Entry<K, Collection<V>> entry) {
            final K key = (K)entry.getKey();
            return Maps.<K, Collection<V>>immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, (Collection<V>)entry.getValue()));
        }
        
        class AsMapEntries extends Maps.EntrySet<K, Collection<V>> {
            @Override
            Map<K, Collection<V>> map() {
                return (Map<K, Collection<V>>)AsMap.this;
            }
            
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return (Iterator<Map.Entry<K, Collection<V>>>)new AsMapIterator();
            }
            
            public Spliterator<Map.Entry<K, Collection<V>>> spliterator() {
                return CollectSpliterators.<Object, Map.Entry<K, Collection<V>>>map((java.util.Spliterator<Object>)AsMap.this.submap.entrySet().spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<K, Collection<V>>>)AsMap.this::wrapEntry);
            }
            
            @Override
            public boolean contains(final Object o) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), o);
            }
            
            @Override
            public boolean remove(final Object o) {
                if (!this.contains(o)) {
                    return false;
                }
                final Map.Entry<?, ?> entry = o;
                AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
                return true;
            }
        }
        
        class AsMapIterator implements Iterator<Map.Entry<K, Collection<V>>> {
            final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;
            Collection<V> collection;
            
            AsMapIterator() {
                this.delegateIterator = (Iterator<Map.Entry<K, Collection<V>>>)AsMap.this.submap.entrySet().iterator();
            }
            
            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }
            
            public Map.Entry<K, Collection<V>> next() {
                final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.delegateIterator.next();
                this.collection = (Collection<V>)entry.getValue();
                return AsMap.this.wrapEntry(entry);
            }
            
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize -= this.collection.size();
                this.collection.clear();
            }
        }
    }
    
    private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
        SortedSet<K> sortedKeySet;
        
        SortedAsMap(final SortedMap<K, Collection<V>> submap) {
            super((Map<K, Collection<V>>)submap);
        }
        
        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap<K, Collection<V>>)this.submap;
        }
        
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        public K firstKey() {
            return (K)this.sortedMap().firstKey();
        }
        
        public K lastKey() {
            return (K)this.sortedMap().lastKey();
        }
        
        public SortedMap<K, Collection<V>> headMap(final K toKey) {
            return (SortedMap<K, Collection<V>>)new SortedAsMap((SortedMap<K, Collection<V>>)this.sortedMap().headMap(toKey));
        }
        
        public SortedMap<K, Collection<V>> subMap(final K fromKey, final K toKey) {
            return (SortedMap<K, Collection<V>>)new SortedAsMap((SortedMap<K, Collection<V>>)this.sortedMap().subMap(fromKey, toKey));
        }
        
        public SortedMap<K, Collection<V>> tailMap(final K fromKey) {
            return (SortedMap<K, Collection<V>>)new SortedAsMap((SortedMap<K, Collection<V>>)this.sortedMap().tailMap(fromKey));
        }
        
        public SortedSet<K> keySet() {
            final SortedSet<K> result = this.sortedKeySet;
            return (result == null) ? (this.sortedKeySet = this.createKeySet()) : result;
        }
        
        SortedSet<K> createKeySet() {
            return (SortedSet<K>)new AbstractMapBasedMultimap.SortedKeySet(this.sortedMap());
        }
    }
    
    class NavigableAsMap extends SortedAsMap implements NavigableMap<K, Collection<V>> {
        NavigableAsMap(final NavigableMap<K, Collection<V>> submap) {
            super((SortedMap<K, Collection<V>>)submap);
        }
        
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap<K, Collection<V>>)super.sortedMap();
        }
        
        public Map.Entry<K, Collection<V>> lowerEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.sortedMap().lowerEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        public K lowerKey(final K key) {
            return (K)this.sortedMap().lowerKey(key);
        }
        
        public Map.Entry<K, Collection<V>> floorEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.sortedMap().floorEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        public K floorKey(final K key) {
            return (K)this.sortedMap().floorKey(key);
        }
        
        public Map.Entry<K, Collection<V>> ceilingEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.sortedMap().ceilingEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        public K ceilingKey(final K key) {
            return (K)this.sortedMap().ceilingKey(key);
        }
        
        public Map.Entry<K, Collection<V>> higherEntry(final K key) {
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.sortedMap().higherEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        public K higherKey(final K key) {
            return (K)this.sortedMap().higherKey(key);
        }
        
        public Map.Entry<K, Collection<V>> firstEntry() {
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.sortedMap().firstEntry();
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        public Map.Entry<K, Collection<V>> lastEntry() {
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.sortedMap().lastEntry();
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        public Map.Entry<K, Collection<V>> pollFirstEntry() {
            return this.pollAsMapEntry((Iterator<Map.Entry<K, Collection<V>>>)this.entrySet().iterator());
        }
        
        public Map.Entry<K, Collection<V>> pollLastEntry() {
            return this.pollAsMapEntry((Iterator<Map.Entry<K, Collection<V>>>)this.descendingMap().entrySet().iterator());
        }
        
        Map.Entry<K, Collection<V>> pollAsMapEntry(final Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
            if (!entryIterator.hasNext()) {
                return null;
            }
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)entryIterator.next();
            final Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll((Collection)entry.getValue());
            entryIterator.remove();
            return Maps.<K, java.util.Collection<V>>immutableEntry(entry.getKey(), AbstractMapBasedMultimap.<V>unmodifiableCollectionSubclass(output));
        }
        
        public NavigableMap<K, Collection<V>> descendingMap() {
            return (NavigableMap<K, Collection<V>>)new NavigableAsMap((NavigableMap<K, Collection<V>>)this.sortedMap().descendingMap());
        }
        
        public NavigableSet<K> keySet() {
            return (NavigableSet<K>)super.keySet();
        }
        
        NavigableSet<K> createKeySet() {
            return (NavigableSet<K>)new AbstractMapBasedMultimap.NavigableKeySet(this.sortedMap());
        }
        
        public NavigableSet<K> navigableKeySet() {
            return this.keySet();
        }
        
        public NavigableSet<K> descendingKeySet() {
            return (NavigableSet<K>)this.descendingMap().navigableKeySet();
        }
        
        public NavigableMap<K, Collection<V>> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        public NavigableMap<K, Collection<V>> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            return (NavigableMap<K, Collection<V>>)new NavigableAsMap((NavigableMap<K, Collection<V>>)this.sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
        }
        
        public NavigableMap<K, Collection<V>> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        public NavigableMap<K, Collection<V>> headMap(final K toKey, final boolean inclusive) {
            return (NavigableMap<K, Collection<V>>)new NavigableAsMap((NavigableMap<K, Collection<V>>)this.sortedMap().headMap(toKey, inclusive));
        }
        
        public NavigableMap<K, Collection<V>> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        public NavigableMap<K, Collection<V>> tailMap(final K fromKey, final boolean inclusive) {
            return (NavigableMap<K, Collection<V>>)new NavigableAsMap((NavigableMap<K, Collection<V>>)this.sortedMap().tailMap(fromKey, inclusive));
        }
    }
}
