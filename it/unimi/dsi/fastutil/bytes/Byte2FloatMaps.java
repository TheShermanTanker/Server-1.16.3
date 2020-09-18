package it.unimi.dsi.fastutil.bytes;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.floats.FloatCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatSets;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Byte2FloatMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Byte2FloatMaps() {
    }
    
    public static ObjectIterator<Byte2FloatMap.Entry> fastIterator(final Byte2FloatMap map) {
        final ObjectSet<Byte2FloatMap.Entry> entries = map.byte2FloatEntrySet();
        return (entries instanceof Byte2FloatMap.FastEntrySet) ? ((Byte2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Byte2FloatMap map, final Consumer<? super Byte2FloatMap.Entry> consumer) {
        final ObjectSet<Byte2FloatMap.Entry> entries = map.byte2FloatEntrySet();
        if (entries instanceof Byte2FloatMap.FastEntrySet) {
            ((Byte2FloatMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Byte2FloatMap.Entry> fastIterable(final Byte2FloatMap map) {
        final ObjectSet<Byte2FloatMap.Entry> entries = map.byte2FloatEntrySet();
        return (entries instanceof Byte2FloatMap.FastEntrySet) ? new ObjectIterable<Byte2FloatMap.Entry>() {
            public ObjectIterator<Byte2FloatMap.Entry> iterator() {
                return ((Byte2FloatMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Byte2FloatMap.Entry> consumer) {
                ((Byte2FloatMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Byte2FloatMap singleton(final byte key, final float value) {
        return new Singleton(key, value);
    }
    
    public static Byte2FloatMap singleton(final Byte key, final Float value) {
        return new Singleton(key, value);
    }
    
    public static Byte2FloatMap synchronize(final Byte2FloatMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Byte2FloatMap synchronize(final Byte2FloatMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Byte2FloatMap unmodifiable(final Byte2FloatMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Byte2FloatFunctions.EmptyFunction implements Byte2FloatMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final float v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends Byte, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2FloatEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ByteSet keySet() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public FloatCollection values() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Byte2FloatMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Byte2FloatFunctions.Singleton implements Byte2FloatMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient FloatCollection values;
        
        protected Singleton(final byte key, final float value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final float v) {
            return Float.floatToIntBits(this.value) == Float.floatToIntBits(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return Float.floatToIntBits((float)ov) == Float.floatToIntBits(this.value);
        }
        
        public void putAll(final Map<? extends Byte, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractByte2FloatMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Float>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Float>>)this.byte2FloatEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public FloatCollection values() {
            if (this.values == null) {
                this.values = FloatSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return this.key ^ HashCommon.float2int(this.value);
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
    
    public static class SynchronizedMap extends Byte2FloatFunctions.SynchronizedFunction implements Byte2FloatMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2FloatMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient FloatCollection values;
        
        protected SynchronizedMap(final Byte2FloatMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Byte2FloatMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final float v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            synchronized (this.sync) {
                return this.map.containsValue(ov);
            }
        }
        
        public void putAll(final Map<? extends Byte, ? extends Float> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> byte2FloatEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.byte2FloatEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Float>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Float>>)this.byte2FloatEntrySet();
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
        public FloatCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return FloatCollections.synchronize(this.map.values(), this.sync);
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
        public float getOrDefault(final byte key, final float defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Float> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Float, ? extends Float> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public float putIfAbsent(final byte key, final float value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final byte key, final float value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public float replace(final byte key, final float value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final byte key, final float oldValue, final float newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public float computeIfAbsent(final byte key, final IntToDoubleFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public float computeIfAbsentNullable(final byte key, final IntFunction<? extends Float> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public float computeIfAbsentPartial(final byte key, final Byte2FloatFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public float computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public float compute(final byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public float merge(final byte key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float getOrDefault(final Object key, final Float defaultValue) {
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
        public Float replace(final Byte key, final Float value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Float oldValue, final Float newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Float putIfAbsent(final Byte key, final Float value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Float computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Float> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/bytes/Byte2FloatMaps$SynchronizedMap.sync:Ljava/lang/Object;
            //     4: dup            
            //     5: astore_3       
            //     6: monitorenter   
            //     7: aload_0         /* this */
            //     8: getfield        it/unimi/dsi/fastutil/bytes/Byte2FloatMaps$SynchronizedMap.map:Lit/unimi/dsi/fastutil/bytes/Byte2FloatMap;
            //    11: aload_1         /* key */
            //    12: aload_2         /* remappingFunction */
            //    13: invokeinterface it/unimi/dsi/fastutil/bytes/Byte2FloatMap.computeIfPresent:(Ljava/lang/Byte;Ljava/util/function/BiFunction;)Ljava/lang/Float;
            //    18: aload_3        
            //    19: monitorexit    
            //    20: areturn        
            //    21: astore          4
            //    23: aload_3        
            //    24: monitorexit    
            //    25: aload           4
            //    27: athrow         
            //    Signature:
            //  (Ljava/lang/Byte;Ljava/util/function/BiFunction<-Ljava/lang/Byte;-Ljava/lang/Float;+Ljava/lang/Float;>;)Ljava/lang/Float;
            //    MethodParameters:
            //  Name               Flags  
            //  -----------------  -----
            //  key                
            //  remappingFunction  
            //    StackMapTable: 00 01 FF 00 15 00 04 07 00 02 07 00 E8 07 00 A1 07 00 3C 00 01 07 00 3E
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  7      20     21     28     Any
            //  21     25     21     28     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:547)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
            //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:248)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:273)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
        
        @Deprecated
        @Override
        public Float compute(final Byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float merge(final Byte key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Byte2FloatFunctions.UnmodifiableFunction implements Byte2FloatMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2FloatMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient FloatCollection values;
        
        protected UnmodifiableMap(final Byte2FloatMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final float v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends Byte, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.byte2FloatEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Float>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Float>>)this.byte2FloatEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public FloatCollection values() {
            if (this.values == null) {
                return FloatCollections.unmodifiable(this.map.values());
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
        public float getOrDefault(final byte key, final float defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Float> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Float, ? extends Float> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float putIfAbsent(final byte key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final byte key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float replace(final byte key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final byte key, final float oldValue, final float newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfAbsent(final byte key, final IntToDoubleFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfAbsentNullable(final byte key, final IntFunction<? extends Float> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfAbsentPartial(final byte key, final Byte2FloatFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float compute(final byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float merge(final byte key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float getOrDefault(final Object key, final Float defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float replace(final Byte key, final Float value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Float oldValue, final Float newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float putIfAbsent(final Byte key, final Float value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Float> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float compute(final Byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float merge(final Byte key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
