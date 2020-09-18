package it.unimi.dsi.fastutil.chars;

import java.util.SortedMap;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Objects;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import java.util.Comparator;

public final class Char2BooleanSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Char2BooleanSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Character, ?>> entryComparator(final CharComparator comparator) {
        return ((x, y) -> comparator.compare((char)x.getKey(), (char)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Char2BooleanMap.Entry> fastIterator(final Char2BooleanSortedMap map) {
        final ObjectSortedSet<Char2BooleanMap.Entry> entries = map.char2BooleanEntrySet();
        return (entries instanceof Char2BooleanSortedMap.FastSortedEntrySet) ? ((Char2BooleanSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Char2BooleanMap.Entry> fastIterable(final Char2BooleanSortedMap map) {
        final ObjectSortedSet<Char2BooleanMap.Entry> entries = map.char2BooleanEntrySet();
        ObjectBidirectionalIterable<Char2BooleanMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Char2BooleanSortedMap.FastSortedEntrySet) {
            final Char2BooleanSortedMap.FastSortedEntrySet set = (Char2BooleanSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Char2BooleanSortedMap singleton(final Character key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Char2BooleanSortedMap singleton(final Character key, final Boolean value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2BooleanSortedMap singleton(final char key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Char2BooleanSortedMap singleton(final char key, final boolean value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2BooleanSortedMap synchronize(final Char2BooleanSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Char2BooleanSortedMap synchronize(final Char2BooleanSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Char2BooleanSortedMap unmodifiable(final Char2BooleanSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Char2BooleanMaps.EmptyMap implements Char2BooleanSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
            return (ObjectSortedSet<Char2BooleanMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Boolean>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet keySet() {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public Char2BooleanSortedMap subMap(final char from, final char to) {
            return Char2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2BooleanSortedMap headMap(final char to) {
            return Char2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2BooleanSortedMap tailMap(final char from) {
            return Char2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public char firstCharKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public char lastCharKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap subMap(final Character ofrom, final Character oto) {
            return this.subMap((char)ofrom, (char)oto);
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.firstCharKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.lastCharKey();
        }
    }
    
    public static class Singleton extends Char2BooleanMaps.Singleton implements Char2BooleanSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharComparator comparator;
        
        protected Singleton(final char key, final boolean value, final CharComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final char key, final boolean value) {
            this(key, value, null);
        }
        
        final int compare(final char k1, final char k2) {
            return (this.comparator == null) ? Character.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public CharComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractChar2BooleanMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractChar2BooleanMap.BasicEntry>)Char2BooleanSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Char2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Boolean>>)this.char2BooleanEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.singleton(this.key, this.comparator);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2BooleanSortedMap subMap(final char from, final char to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2BooleanSortedMap headMap(final char to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2BooleanSortedMap tailMap(final char from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Char2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public char firstCharKey() {
            return this.key;
        }
        
        @Override
        public char lastCharKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap subMap(final Character ofrom, final Character oto) {
            return this.subMap((char)ofrom, (char)oto);
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.firstCharKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.lastCharKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Char2BooleanMaps.SynchronizedMap implements Char2BooleanSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2BooleanSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Char2BooleanSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Char2BooleanSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2BooleanMap.Entry>synchronize(this.sortedMap.char2BooleanEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Char2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Boolean>>)this.char2BooleanEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2BooleanSortedMap subMap(final char from, final char to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Char2BooleanSortedMap headMap(final char to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Char2BooleanSortedMap tailMap(final char from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public char firstCharKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstCharKey();
            }
        }
        
        @Override
        public char lastCharKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastCharKey();
            }
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap subMap(final Character from, final Character to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap headMap(final Character to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap tailMap(final Character from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Char2BooleanMaps.UnmodifiableMap implements Char2BooleanSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2BooleanSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Char2BooleanSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Char2BooleanMap.Entry> char2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2BooleanMap.Entry>unmodifiable(this.sortedMap.char2BooleanEntrySet());
            }
            return (ObjectSortedSet<Char2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Boolean>>)this.char2BooleanEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2BooleanSortedMap subMap(final char from, final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Char2BooleanSortedMap headMap(final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Char2BooleanSortedMap tailMap(final char from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public char firstCharKey() {
            return this.sortedMap.firstCharKey();
        }
        
        @Override
        public char lastCharKey() {
            return this.sortedMap.lastCharKey();
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap subMap(final Character from, final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap headMap(final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Char2BooleanSortedMap tailMap(final Character from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
