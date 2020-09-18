package it.unimi.dsi.fastutil.longs;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Long2ObjectMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Long2ObjectMaps() {
    }
    
    public static <V> ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator(final Long2ObjectMap<V> map) {
        final ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        return (entries instanceof Long2ObjectMap.FastEntrySet) ? ((Long2ObjectMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> void fastForEach(final Long2ObjectMap<V> map, final Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
        final ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        if (entries instanceof Long2ObjectMap.FastEntrySet) {
            ((Long2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <V> ObjectIterable<Long2ObjectMap.Entry<V>> fastIterable(final Long2ObjectMap<V> map) {
        final ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        return (entries instanceof Long2ObjectMap.FastEntrySet) ? new ObjectIterable<Long2ObjectMap.Entry<V>>() {
            public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
                return ((Long2ObjectMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
                ((Long2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <V> Long2ObjectMap<V> emptyMap() {
        return (Long2ObjectMap<V>)Long2ObjectMaps.EMPTY_MAP;
    }
    
    public static <V> Long2ObjectMap<V> singleton(final long key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Long2ObjectMap<V> singleton(final Long key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Long2ObjectMap<V> synchronize(final Long2ObjectMap<V> m) {
        return new SynchronizedMap<V>(m);
    }
    
    public static <V> Long2ObjectMap<V> synchronize(final Long2ObjectMap<V> m, final Object sync) {
        return new SynchronizedMap<V>(m, sync);
    }
    
    public static <V> Long2ObjectMap<V> unmodifiable(final Long2ObjectMap<V> m) {
        return new UnmodifiableMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<V> extends Long2ObjectFunctions.EmptyFunction<V> implements Long2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        public boolean containsValue(final Object v) {
            return false;
        }
        
        public void putAll(final Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> long2ObjectEntrySet() {
            return (ObjectSet<Entry<V>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public LongSet keySet() {
            return LongSets.EMPTY_SET;
        }
        
        @Override
        public ObjectCollection<V> values() {
            return (ObjectCollection<V>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Long2ObjectMaps.EMPTY_MAP;
        }
        
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Map && ((Map)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "{}";
        }
    }
    
    public static class Singleton<V> extends Long2ObjectFunctions.Singleton<V> implements Long2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient LongSet keys;
        protected transient ObjectCollection<V> values;
        
        protected Singleton(final long key, final V value) {
            super(key, value);
        }
        
        public boolean containsValue(final Object v) {
            return Objects.equals(this.value, v);
        }
        
        public void putAll(final Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractLong2ObjectMap.BasicEntry<V>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            if (this.keys == null) {
                this.keys = LongSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = ObjectSets.<V>singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map)) {
                return false;
            }
            final Map<?, ?> m = o;
            return m.size() == 1 && ((Map.Entry)m.entrySet().iterator().next()).equals(this.entrySet().iterator().next());
        }
        
        public String toString() {
            return new StringBuilder().append("{").append(this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap<V> extends Long2ObjectFunctions.SynchronizedFunction<V> implements Long2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient LongSet keys;
        protected transient ObjectCollection<V> values;
        
        protected SynchronizedMap(final Long2ObjectMap<V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Long2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        public void putAll(final Map<? extends Long, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<V>> long2ObjectEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<V>>synchronize(this.map.long2ObjectEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = LongSets.synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public ObjectCollection<V> values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ObjectCollections.<V>synchronize(this.map.values(), this.sync);
                }
                return this.values;
            }
        }
        
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.map.hashCode();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.map.equals(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
        
        @Override
        public V getOrDefault(final long key, final V defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Long, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Long, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public V putIfAbsent(final long key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final long key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public V replace(final long key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final long key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public V computeIfAbsent(final long key, final LongFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfAbsentPartial(final long key, final Long2ObjectFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfPresent(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public V compute(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public V merge(final long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Deprecated
        @Override
        public V replace(final Long key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Long key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Long key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Long key, final java.util.function.Function<? super Long, ? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V compute(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V merge(final Long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class SynchronizedMap<V> extends Long2ObjectFunctions.SynchronizedFunction<V> implements Long2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient LongSet keys;
        protected transient ObjectCollection<V> values;
        
        protected SynchronizedMap(final Long2ObjectMap<V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Long2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        public void putAll(final Map<? extends Long, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<V>> long2ObjectEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<V>>synchronize(this.map.long2ObjectEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = LongSets.synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public ObjectCollection<V> values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ObjectCollections.<V>synchronize(this.map.values(), this.sync);
                }
                return this.values;
            }
        }
        
        public boolean isEmpty() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/longs/Long2ObjectMaps$SynchronizedMap.sync:Ljava/lang/Object;
            //     4: dup            
            //     5: astore_1       
            //     6: monitorenter   
            //     7: aload_0         /* this */
            //     8: getfield        it/unimi/dsi/fastutil/longs/Long2ObjectMaps$SynchronizedMap.map:Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;
            //    11: invokeinterface it/unimi/dsi/fastutil/longs/Long2ObjectMap.isEmpty:()Z
            //    16: aload_1        
            //    17: monitorexit    
            //    18: ireturn        
            //    19: astore_2       
            //    20: aload_1        
            //    21: monitorexit    
            //    22: aload_2        
            //    23: athrow         
            //    StackMapTable: 00 01 FF 00 13 00 02 07 00 02 07 00 42 00 01 07 00 44
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  7      18     19     24     Any
            //  19     22     19     24     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
            //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
            //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
            //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
            //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
            //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
            //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
            //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
            //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.map.hashCode();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.map.equals(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
        
        @Override
        public V getOrDefault(final long key, final V defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Long, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Long, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public V putIfAbsent(final long key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final long key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public V replace(final long key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final long key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public V computeIfAbsent(final long key, final LongFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfAbsentPartial(final long key, final Long2ObjectFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfPresent(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public V compute(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public V merge(final long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Deprecated
        @Override
        public V replace(final Long key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Long key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Long key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Long key, final java.util.function.Function<? super Long, ? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V compute(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V merge(final Long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<V> extends Long2ObjectFunctions.UnmodifiableFunction<V> implements Long2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient LongSet keys;
        protected transient ObjectCollection<V> values;
        
        protected UnmodifiableMap(final Long2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            return this.map.containsValue(v);
        }
        
        public void putAll(final Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<V>>unmodifiable(this.map.long2ObjectEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            if (this.keys == null) {
                this.keys = LongSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                return ObjectCollections.<V>unmodifiable(this.map.values());
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public int hashCode() {
            return this.map.hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.map.equals(o);
        }
        
        @Override
        public V getOrDefault(final long key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Long, ? super V> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Long, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V putIfAbsent(final long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final long key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V replace(final long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final long key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsent(final long key, final LongFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsentPartial(final long key, final Long2ObjectFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfPresent(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V compute(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V merge(final long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V replace(final Long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Long key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Long key, final java.util.function.Function<? super Long, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V compute(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V merge(final Long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class UnmodifiableMap<V> extends Long2ObjectFunctions.UnmodifiableFunction<V> implements Long2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient LongSet keys;
        protected transient ObjectCollection<V> values;
        
        protected UnmodifiableMap(final Long2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            return this.map.containsValue(v);
        }
        
        public void putAll(final Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<V>>unmodifiable(this.map.long2ObjectEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            if (this.keys == null) {
                this.keys = LongSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                return ObjectCollections.<V>unmodifiable(this.map.values());
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public int hashCode() {
            return this.map.hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.map.equals(o);
        }
        
        @Override
        public V getOrDefault(final long key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Long, ? super V> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Long, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V putIfAbsent(final long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final long key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V replace(final long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final long key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsent(final long key, final LongFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsentPartial(final long key, final Long2ObjectFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfPresent(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V compute(final long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V merge(final long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V replace(final Long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Long key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Long key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Long key, final java.util.function.Function<? super Long, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V compute(final Long key, final BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V merge(final Long key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
