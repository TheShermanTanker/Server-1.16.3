package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.Collection;
import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.base.Predicate;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class FilteredEntryMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
    final Multimap<K, V> unfiltered;
    final Predicate<? super Map.Entry<K, V>> predicate;
    
    FilteredEntryMultimap(final Multimap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> predicate) {
        this.unfiltered = Preconditions.<Multimap<K, V>>checkNotNull(unfiltered);
        this.predicate = Preconditions.<Predicate<? super Map.Entry<K, V>>>checkNotNull(predicate);
    }
    
    @Override
    public Multimap<K, V> unfiltered() {
        return this.unfiltered;
    }
    
    @Override
    public Predicate<? super Map.Entry<K, V>> entryPredicate() {
        return this.predicate;
    }
    
    public int size() {
        return this.entries().size();
    }
    
    private boolean satisfies(final K key, final V value) {
        return this.predicate.apply(Maps.<K, V>immutableEntry(key, value));
    }
    
    static <E> Collection<E> filterCollection(final Collection<E> collection, final Predicate<? super E> predicate) {
        if (collection instanceof Set) {
            return Sets.filter((java.util.Set<Object>)collection, predicate);
        }
        return Collections2.<E>filter(collection, predicate);
    }
    
    public boolean containsKey(@Nullable final Object key) {
        return this.asMap().get(key) != null;
    }
    
    public Collection<V> removeAll(@Nullable final Object key) {
        return MoreObjects.firstNonNull(this.asMap().remove(key), this.unmodifiableEmptyCollection());
    }
    
    Collection<V> unmodifiableEmptyCollection() {
        return (Collection<V>)((this.unfiltered instanceof SetMultimap) ? Collections.emptySet() : Collections.emptyList());
    }
    
    public void clear() {
        this.entries().clear();
    }
    
    public Collection<V> get(final K key) {
        return FilteredEntryMultimap.<V>filterCollection(this.unfiltered.get(key), new ValuePredicate(key));
    }
    
    @Override
    Collection<Map.Entry<K, V>> createEntries() {
        return FilteredEntryMultimap.<Map.Entry<K, V>>filterCollection(this.unfiltered.entries(), this.predicate);
    }
    
    @Override
    Collection<V> createValues() {
        return (Collection<V>)new FilteredMultimapValues((FilteredMultimap<Object, Object>)this);
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        throw new AssertionError("should never be called");
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return (Map<K, Collection<V>>)new AsMap();
    }
    
    @Override
    public Set<K> keySet() {
        return (Set<K>)this.asMap().keySet();
    }
    
    boolean removeEntriesIf(final Predicate<? super Map.Entry<K, Collection<V>>> predicate) {
        final Iterator<Map.Entry<K, Collection<V>>> entryIterator = (Iterator<Map.Entry<K, Collection<V>>>)this.unfiltered.asMap().entrySet().iterator();
        boolean changed = false;
        while (entryIterator.hasNext()) {
            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)entryIterator.next();
            final K key = (K)entry.getKey();
            final Collection<V> collection = FilteredEntryMultimap.<V>filterCollection((java.util.Collection<V>)entry.getValue(), new ValuePredicate(key));
            if (!collection.isEmpty() && predicate.apply(Maps.<K, Collection<V>>immutableEntry(key, collection))) {
                if (collection.size() == ((Collection)entry.getValue()).size()) {
                    entryIterator.remove();
                }
                else {
                    collection.clear();
                }
                changed = true;
            }
        }
        return changed;
    }
    
    @Override
    Multiset<K> createKeys() {
        return (Multiset<K>)new Keys();
    }
    
    final class ValuePredicate implements Predicate<V> {
        private final K key;
        
        ValuePredicate(final K key) {
            this.key = key;
        }
        
        public boolean apply(@Nullable final V value) {
            return FilteredEntryMultimap.this.satisfies(this.key, value);
        }
    }
    
    class AsMap extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        public boolean containsKey(@Nullable final Object key) {
            return this.get(key) != null;
        }
        
        public void clear() {
            FilteredEntryMultimap.this.clear();
        }
        
        public Collection<V> get(@Nullable final Object key) {
            Collection<V> result = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (result == null) {
                return null;
            }
            final K k = (K)key;
            result = FilteredEntryMultimap.<V>filterCollection(result, new ValuePredicate(k));
            return result.isEmpty() ? null : result;
        }
        
        public Collection<V> remove(@Nullable final Object key) {
            final Collection<V> collection = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (collection == null) {
                return null;
            }
            final K k = (K)key;
            final List<V> result = Lists.newArrayList();
            final Iterator<V> itr = (Iterator<V>)collection.iterator();
            while (itr.hasNext()) {
                final V v = (V)itr.next();
                if (FilteredEntryMultimap.this.satisfies(k, v)) {
                    itr.remove();
                    result.add(v);
                }
            }
            if (result.isEmpty()) {
                return null;
            }
            if (FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) {
                return (Collection<V>)Collections.unmodifiableSet((Set)Sets.newLinkedHashSet((java.lang.Iterable<?>)result));
            }
            return (Collection<V>)Collections.unmodifiableList((List)result);
        }
        
        @Override
        Set<K> createKeySet() {
            class 1KeySetImpl extends Maps.KeySet<K, Collection<V>> {
                1KeySetImpl() {
                    super((Map)AsMap.this);
                }
                
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in((java.util.Collection<? extends K>)c))));
                }
                
                @Override
                public boolean remove(@Nullable final Object o) {
                    return AsMap.this.remove(o) != null;
                }
            }
            return (Set<K>)new 1KeySetImpl();
        }
        
        @Override
        Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            class 1EntrySetImpl extends Maps.EntrySet<K, Collection<V>> {
                @Override
                Map<K, Collection<V>> map() {
                    return (Map<K, Collection<V>>)AsMap.this;
                }
                
                public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                    return (Iterator<Map.Entry<K, Collection<V>>>)new AbstractIterator<Map.Entry<K, Collection<V>>>() {
                        final Iterator<Map.Entry<K, Collection<V>>> backingIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
                        
                        @Override
                        protected Map.Entry<K, Collection<V>> computeNext() {
                            while (this.backingIterator.hasNext()) {
                                final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)this.backingIterator.next();
                                final K key = (K)entry.getKey();
                                final Collection<V> collection = FilteredEntryMultimap.<V>filterCollection((java.util.Collection<V>)entry.getValue(), new ValuePredicate(key));
                                if (!collection.isEmpty()) {
                                    return Maps.<K, Collection<V>>immutableEntry(key, collection);
                                }
                            }
                            return (Map.Entry<K, Collection<V>>)((AbstractIterator<Map.Entry>)this).endOfData();
                        }
                    };
                }
                
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Predicates.in((java.util.Collection<? extends Map.Entry<K, Collection<V>>>)c));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Predicates.not(Predicates.in((java.util.Collection<? extends Map.Entry<K, Collection<V>>>)c)));
                }
                
                @Override
                public int size() {
                    return Iterators.size(this.iterator());
                }
            }
            return (Set<Map.Entry<K, Collection<V>>>)new 1EntrySetImpl();
        }
        
        @Override
        Collection<Collection<V>> createValues() {
            class 1ValuesImpl extends Maps.Values<K, Collection<V>> {
                1ValuesImpl() {
                    super((Map)AsMap.this);
                }
                
                @Override
                public boolean remove(@Nullable final Object o) {
                    if (o instanceof Collection) {
                        final Collection<?> c = o;
                        final Iterator<Map.Entry<K, Collection<V>>> entryIterator = (Iterator<Map.Entry<K, Collection<V>>>)FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
                        while (entryIterator.hasNext()) {
                            final Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>)entryIterator.next();
                            final K key = (K)entry.getKey();
                            final Collection<V> collection = FilteredEntryMultimap.<V>filterCollection((java.util.Collection<V>)entry.getValue(), new ValuePredicate(key));
                            if (!collection.isEmpty() && c.equals(collection)) {
                                if (collection.size() == ((Collection)entry.getValue()).size()) {
                                    entryIterator.remove();
                                }
                                else {
                                    collection.clear();
                                }
                                return true;
                            }
                        }
                    }
                    return false;
                }
                
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in((java.util.Collection<? extends V>)c))));
                }
            }
            return (Collection<Collection<V>>)new 1ValuesImpl();
        }
    }
    
    class Keys extends Multimaps.Keys<K, V> {
        Keys() {
            super(FilteredEntryMultimap.this);
        }
        
        @Override
        public int remove(@Nullable final Object key, final int occurrences) {
            CollectPreconditions.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return this.count(key);
            }
            final Collection<V> collection = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
            if (collection == null) {
                return 0;
            }
            final K k = (K)key;
            int oldCount = 0;
            final Iterator<V> itr = (Iterator<V>)collection.iterator();
            while (itr.hasNext()) {
                final V v = (V)itr.next();
                if (FilteredEntryMultimap.this.satisfies(k, v) && ++oldCount <= occurrences) {
                    itr.remove();
                }
            }
            return oldCount;
        }
        
        @Override
        public Set<Multiset.Entry<K>> entrySet() {
            return (Set<Multiset.Entry<K>>)new Multisets.EntrySet<K>() {
                @Override
                Multiset<K> multiset() {
                    return (Multiset<K>)Keys.this;
                }
                
                public Iterator<Multiset.Entry<K>> iterator() {
                    return ((Multimaps.Keys<K, V>)Keys.this).entryIterator();
                }
                
                public int size() {
                    return FilteredEntryMultimap.this.keySet().size();
                }
                
                private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> predicate) {
                    return FilteredEntryMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>() {
                        public boolean apply(final Map.Entry<K, Collection<V>> entry) {
                            return predicate.apply(Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size()));
                        }
                    });
                }
                
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return this.removeEntriesIf(Predicates.in((java.util.Collection<? extends Multiset.Entry<K>>)c));
                }
                
                @Override
                public boolean retainAll(final Collection<?> c) {
                    return this.removeEntriesIf(Predicates.not(Predicates.in((java.util.Collection<? extends Multiset.Entry<K>>)c)));
                }
            };
        }
    }
}
