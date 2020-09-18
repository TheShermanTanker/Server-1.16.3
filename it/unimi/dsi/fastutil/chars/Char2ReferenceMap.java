package it.unimi.dsi.fastutil.chars;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Char2ReferenceMap<V> extends Char2ReferenceFunction<V>, Map<Character, V> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final V object);
    
    V defaultReturnValue();
    
    ObjectSet<Entry<V>> char2ReferenceEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Character, V>> entrySet() {
        return (ObjectSet<Map.Entry<Character, V>>)this.char2ReferenceEntrySet();
    }
    
    @Deprecated
    default V put(final Character key, final V value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default V get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default V remove(final Object key) {
        return super.remove(key);
    }
    
    CharSet keySet();
    
    ReferenceCollection<V> values();
    
    boolean containsKey(final char character);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    default V getOrDefault(final char key, final V defaultValue) {
        final V v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default V putIfAbsent(final char key, final V value) {
        final V v = this.get(key);
        final V drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final char key, final Object value) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iload_1         /* key */
        //     2: invokeinterface it/unimi/dsi/fastutil/chars/Char2ReferenceMap.get:(C)Ljava/lang/Object;
        //     7: astore_3        /* curValue */
        //     8: aload_3         /* curValue */
        //     9: aload_2         /* value */
        //    10: if_acmpne       33
        //    13: aload_3         /* curValue */
        //    14: aload_0         /* this */
        //    15: invokeinterface it/unimi/dsi/fastutil/chars/Char2ReferenceMap.defaultReturnValue:()Ljava/lang/Object;
        //    20: if_acmpne       35
        //    23: aload_0         /* this */
        //    24: iload_1         /* key */
        //    25: invokeinterface it/unimi/dsi/fastutil/chars/Char2ReferenceMap.containsKey:(C)Z
        //    30: ifne            35
        //    33: iconst_0       
        //    34: ireturn        
        //    35: aload_0         /* this */
        //    36: iload_1         /* key */
        //    37: invokeinterface it/unimi/dsi/fastutil/chars/Char2ReferenceMap.remove:(C)Ljava/lang/Object;
        //    42: pop            
        //    43: iconst_1       
        //    44: ireturn        
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  key    
        //  value  
        //    StackMapTable: 00 02 FC 00 21 07 00 05 01
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.resolve(ParameterizedType.java:108)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:764)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:805)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:805)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:805)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    default boolean replace(final char key, final V oldValue, final V newValue) {
        final V curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default V replace(final char key, final V value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default V computeIfAbsent(final char key, final IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final V v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final V newValue = (V)mappingFunction.apply((int)key);
        this.put(key, newValue);
        return newValue;
    }
    
    default V computeIfAbsentPartial(final char key, final Char2ReferenceFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final V v = this.get(key);
        final V drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final V newValue = (V)mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default V computeIfPresent(final char key, final BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final V newValue = (V)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V compute(final char key, final BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final V newValue = (V)remappingFunction.apply(key, (contained ? oldValue : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V merge(final char key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        V newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final V mergedValue = (V)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.remove(key);
                return drv;
            }
            newValue = mergedValue;
        }
        else {
            newValue = value;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    @Deprecated
    default V getOrDefault(final Object key, final V defaultValue) {
        return (V)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default V putIfAbsent(final Character key, final V value) {
        return (V)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Character key, final V oldValue, final V newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default V replace(final Character key, final V value) {
        return (V)super.replace(key, value);
    }
    
    @Deprecated
    default V computeIfAbsent(final Character key, final java.util.function.Function<? super Character, ? extends V> mappingFunction) {
        return (V)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default V computeIfPresent(final Character key, final BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        return (V)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default V compute(final Character key, final BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        return (V)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default V merge(final Character key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return (V)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>> {
        ObjectIterator<Entry<V>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<V>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<V> extends Map.Entry<Character, V> {
        char getCharKey();
        
        @Deprecated
        default Character getKey() {
            return this.getCharKey();
        }
    }
}
