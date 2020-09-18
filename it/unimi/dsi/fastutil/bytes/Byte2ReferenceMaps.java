package it.unimi.dsi.fastutil.bytes;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.ReferenceCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Byte2ReferenceMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Byte2ReferenceMaps() {
    }
    
    public static <V> ObjectIterator<Byte2ReferenceMap.Entry<V>> fastIterator(final Byte2ReferenceMap<V> map) {
        final ObjectSet<Byte2ReferenceMap.Entry<V>> entries = map.byte2ReferenceEntrySet();
        return (entries instanceof Byte2ReferenceMap.FastEntrySet) ? ((Byte2ReferenceMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> void fastForEach(final Byte2ReferenceMap<V> map, final Consumer<? super Byte2ReferenceMap.Entry<V>> consumer) {
        final ObjectSet<Byte2ReferenceMap.Entry<V>> entries = map.byte2ReferenceEntrySet();
        if (entries instanceof Byte2ReferenceMap.FastEntrySet) {
            ((Byte2ReferenceMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <V> ObjectIterable<Byte2ReferenceMap.Entry<V>> fastIterable(final Byte2ReferenceMap<V> map) {
        final ObjectSet<Byte2ReferenceMap.Entry<V>> entries = map.byte2ReferenceEntrySet();
        return (entries instanceof Byte2ReferenceMap.FastEntrySet) ? new ObjectIterable<Byte2ReferenceMap.Entry<V>>() {
            public ObjectIterator<Byte2ReferenceMap.Entry<V>> iterator() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getfield        it/unimi/dsi/fastutil/bytes/Byte2ReferenceMaps$1.val$entries:Lit/unimi/dsi/fastutil/objects/ObjectSet;
                //     4: checkcast       Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceMap$FastEntrySet;
                //     7: invokeinterface it/unimi/dsi/fastutil/bytes/Byte2ReferenceMap$FastEntrySet.fastIterator:()Lit/unimi/dsi/fastutil/objects/ObjectIterator;
                //    12: areturn        
                //    Signature:
                //  ()Lit/unimi/dsi/fastutil/objects/ObjectIterator<Lit/unimi/dsi/fastutil/bytes/Byte2ReferenceMap$Entry<TV;>;>;
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
                //     at com.strobel.assembler.metadata.RawType.resolve(RawType.java:73)
                //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
                //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitRawType(DefaultTypeVisitor.java:75)
                //     at com.strobel.assembler.metadata.RawType.accept(RawType.java:68)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitRawType(MetadataHelper.java:2066)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitRawType(MetadataHelper.java:1994)
                //     at com.strobel.assembler.metadata.RawType.accept(RawType.java:68)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitRawType(MetadataHelper.java:2066)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitRawType(MetadataHelper.java:1994)
                //     at com.strobel.assembler.metadata.RawType.accept(RawType.java:68)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
                //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
                //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
                //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
                //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
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
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
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
            
            public void forEach(final Consumer<? super Byte2ReferenceMap.Entry<V>> consumer) {
                ((Byte2ReferenceMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <V> Byte2ReferenceMap<V> emptyMap() {
        return (Byte2ReferenceMap<V>)Byte2ReferenceMaps.EMPTY_MAP;
    }
    
    public static <V> Byte2ReferenceMap<V> singleton(final byte key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Byte2ReferenceMap<V> singleton(final Byte key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Byte2ReferenceMap<V> synchronize(final Byte2ReferenceMap<V> m) {
        return new SynchronizedMap<V>(m);
    }
    
    public static <V> Byte2ReferenceMap<V> synchronize(final Byte2ReferenceMap<V> m, final Object sync) {
        return new SynchronizedMap<V>(m, sync);
    }
    
    public static <V> Byte2ReferenceMap<V> unmodifiable(final Byte2ReferenceMap<V> m) {
        return new UnmodifiableMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<V> extends Byte2ReferenceFunctions.EmptyFunction<V> implements Byte2ReferenceMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        public boolean containsValue(final Object v) {
            return false;
        }
        
        public void putAll(final Map<? extends Byte, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> byte2ReferenceEntrySet() {
            return (ObjectSet<Entry<V>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ByteSet keySet() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            return (ReferenceCollection<V>)ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Byte2ReferenceMaps.EMPTY_MAP;
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
    
    public static class Singleton<V> extends Byte2ReferenceFunctions.Singleton<V> implements Byte2ReferenceMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient ByteSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected Singleton(final byte key, final V value) {
            super(key, value);
        }
        
        public boolean containsValue(final Object v) {
            return this.value == v;
        }
        
        public void putAll(final Map<? extends Byte, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> byte2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractByte2ReferenceMap.BasicEntry<V>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, V>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, V>>)this.byte2ReferenceEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                this.values = ReferenceSets.<V>singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
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
            return new StringBuilder().append("{").append((int)this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap<V> extends Byte2ReferenceFunctions.SynchronizedFunction<V> implements Byte2ReferenceMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ReferenceMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient ByteSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected SynchronizedMap(final Byte2ReferenceMap<V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Byte2ReferenceMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        public void putAll(final Map<? extends Byte, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<V>> byte2ReferenceEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<V>>synchronize(this.map.byte2ReferenceEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, V>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, V>>)this.byte2ReferenceEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ByteSets.synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public ReferenceCollection<V> values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ReferenceCollections.<V>synchronize(this.map.values(), this.sync);
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
        public V getOrDefault(final byte key, final V defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public V putIfAbsent(final byte key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final byte key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public V replace(final byte key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final byte key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public V computeIfAbsent(final byte key, final IntFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfAbsentPartial(final byte key, final Byte2ReferenceFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfPresent(final byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public V compute(final byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public V merge(final byte key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
        public V replace(final Byte key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Byte key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V compute(final Byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V merge(final Byte key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<V> extends Byte2ReferenceFunctions.UnmodifiableFunction<V> implements Byte2ReferenceMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ReferenceMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient ByteSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected UnmodifiableMap(final Byte2ReferenceMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            return this.map.containsValue(v);
        }
        
        public void putAll(final Map<? extends Byte, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> byte2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<V>>unmodifiable(this.map.byte2ReferenceEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, V>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, V>>)this.byte2ReferenceEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                return ReferenceCollections.<V>unmodifiable(this.map.values());
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
        public V getOrDefault(final byte key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super V> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V putIfAbsent(final byte key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final byte key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V replace(final byte key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final byte key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsent(final byte key, final IntFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsentPartial(final byte key, final Byte2ReferenceFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfPresent(final byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V compute(final byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V merge(final byte key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
        public V replace(final Byte key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Byte key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V compute(final Byte key, final BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V merge(final Byte key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
