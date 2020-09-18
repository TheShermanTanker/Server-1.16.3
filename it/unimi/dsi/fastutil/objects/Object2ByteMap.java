package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;

public interface Object2ByteMap<K> extends Object2ByteFunction<K>, Map<K, Byte> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final byte byte1);
    
    byte defaultReturnValue();
    
    ObjectSet<Entry<K>> object2ByteEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Byte>> entrySet() {
        return (ObjectSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
    }
    
    @Deprecated
    default Byte put(final K key, final Byte value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Byte get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        return super.remove(key);
    }
    
    ObjectSet<K> keySet();
    
    ByteCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final byte byte1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((byte)value);
    }
    
    default byte getOrDefault(final Object key, final byte defaultValue) {
        final byte v;
        return ((v = this.getByte(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default byte putIfAbsent(final K key, final byte value) {
        final byte v = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final byte value) {
        final byte curValue = this.getByte(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeByte(key);
        return true;
    }
    
    default boolean replace(final K key, final byte oldValue, final byte newValue) {
        final byte curValue = this.getByte(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default byte replace(final K key, final byte value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default byte computeByteIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.getByte(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeByteIfAbsentPartial(final K key, final Object2ByteFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final byte newValue = mappingFunction.getByte(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeByteIfPresent(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    java/util/Objects.requireNonNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //     4: pop            
        //     5: aload_0         /* this */
        //     6: aload_1         /* key */
        //     7: invokeinterface it/unimi/dsi/fastutil/objects/Object2ByteMap.getByte:(Ljava/lang/Object;)B
        //    12: istore_3        /* oldValue */
        //    13: aload_0         /* this */
        //    14: invokeinterface it/unimi/dsi/fastutil/objects/Object2ByteMap.defaultReturnValue:()B
        //    19: istore          drv
        //    21: iload_3         /* oldValue */
        //    22: iload           drv
        //    24: if_icmpne       40
        //    27: aload_0         /* this */
        //    28: aload_1         /* key */
        //    29: invokeinterface it/unimi/dsi/fastutil/objects/Object2ByteMap.containsKey:(Ljava/lang/Object;)Z
        //    34: ifne            40
        //    37: iload           drv
        //    39: ireturn        
        //    40: aload_2         /* remappingFunction */
        //    41: aload_1         /* key */
        //    42: iload_3         /* oldValue */
        //    43: invokestatic    java/lang/Byte.valueOf:(B)Ljava/lang/Byte;
        //    46: invokeinterface java/util/function/BiFunction.apply:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    51: checkcast       Ljava/lang/Byte;
        //    54: astore          newValue
        //    56: aload           newValue
        //    58: ifnonnull       72
        //    61: aload_0         /* this */
        //    62: aload_1         /* key */
        //    63: invokeinterface it/unimi/dsi/fastutil/objects/Object2ByteMap.removeByte:(Ljava/lang/Object;)B
        //    68: pop            
        //    69: iload           drv
        //    71: ireturn        
        //    72: aload           newValue
        //    74: invokevirtual   java/lang/Byte.byteValue:()B
        //    77: istore          newVal
        //    79: aload_0         /* this */
        //    80: aload_1         /* key */
        //    81: iload           newVal
        //    83: invokeinterface it/unimi/dsi/fastutil/objects/Object2ByteMap.put:(Ljava/lang/Object;B)B
        //    88: pop            
        //    89: iload           newVal
        //    91: ireturn        
        //    Signature:
        //  (TK;Ljava/util/function/BiFunction<-TK;-Ljava/lang/Byte;+Ljava/lang/Byte;>;)B
        //    MethodParameters:
        //  Name               Flags  
        //  -----------------  -----
        //  key                
        //  remappingFunction  
        //    StackMapTable: 00 02 FD 00 28 01 01 FC 00 1F 07 00 46
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2663)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    default byte computeByte(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Byte newValue = (Byte)remappingFunction.apply(key, (contained ? Byte.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeByte(key);
            }
            return drv;
        }
        final byte newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default byte mergeByte(final K key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        byte newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Byte mergedValue = (Byte)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeByte(key);
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
    default Byte getOrDefault(final Object key, final Byte defaultValue) {
        return (Byte)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Byte putIfAbsent(final K key, final Byte value) {
        return (Byte)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Byte oldValue, final Byte newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Byte replace(final K key, final Byte value) {
        return (Byte)super.replace(key, value);
    }
    
    @Deprecated
    default Byte merge(final K key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Byte> {
        byte getByteValue();
        
        byte setValue(final byte byte1);
        
        @Deprecated
        default Byte getValue() {
            return this.getByteValue();
        }
        
        @Deprecated
        default Byte setValue(final Byte value) {
            return this.setValue((byte)value);
        }
    }
}
