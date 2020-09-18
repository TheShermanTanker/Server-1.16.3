package net.minecraft.world.level.block.state;

import javax.annotation.Nullable;
import com.mojang.serialization.Codec;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.Collection;
import com.mojang.serialization.MapCodec;
import com.google.common.collect.Table;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Map;
import java.util.function.Function;

public abstract class StateHolder<O, S> {
    private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_ENTRY_TO_STRING_FUNCTION;
    protected final O owner;
    private final ImmutableMap<Property<?>, Comparable<?>> values;
    private Table<Property<?>, Comparable<?>, S> neighbours;
    protected final MapCodec<S> propertiesCodec;
    
    protected StateHolder(final O object, final ImmutableMap<Property<?>, Comparable<?>> immutableMap, final MapCodec<S> mapCodec) {
        this.owner = object;
        this.values = immutableMap;
        this.propertiesCodec = mapCodec;
    }
    
    public <T extends Comparable<T>> S cycle(final Property<T> cfg) {
        return this.<T, Comparable>setValue(cfg, (Comparable)StateHolder.<V>findNextInCollection((java.util.Collection<V>)cfg.getPossibleValues(), (V)this.<T>getValue((Property<T>)cfg)));
    }
    
    protected static <T> T findNextInCollection(final Collection<T> collection, final T object) {
        final Iterator<T> iterator3 = (Iterator<T>)collection.iterator();
        while (iterator3.hasNext()) {
            if (iterator3.next().equals(object)) {
                if (iterator3.hasNext()) {
                    return (T)iterator3.next();
                }
                return (T)collection.iterator().next();
            }
        }
        return (T)iterator3.next();
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.owner);
        if (!this.getValues().isEmpty()) {
            stringBuilder2.append('[');
            stringBuilder2.append((String)this.getValues().entrySet().stream().map((Function)StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
            stringBuilder2.append(']');
        }
        return stringBuilder2.toString();
    }
    
    public Collection<Property<?>> getProperties() {
        return (Collection<Property<?>>)Collections.unmodifiableCollection((Collection)this.values.keySet());
    }
    
    public <T extends Comparable<T>> boolean hasProperty(final Property<T> cfg) {
        return this.values.containsKey(cfg);
    }
    
    public <T extends Comparable<T>> T getValue(final Property<T> cfg) {
        final Comparable<?> comparable3 = this.values.get(cfg);
        if (comparable3 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Cannot get property ").append(cfg).append(" as it does not exist in ").append(this.owner).toString());
        }
        return (T)cfg.getValueClass().cast(comparable3);
    }
    
    public <T extends Comparable<T>> Optional<T> getOptionalValue(final Property<T> cfg) {
        final Comparable<?> comparable3 = this.values.get(cfg);
        if (comparable3 == null) {
            return (Optional<T>)Optional.empty();
        }
        return (Optional<T>)Optional.of(cfg.getValueClass().cast(comparable3));
    }
    
    public <T extends Comparable<T>, V extends T> S setValue(final Property<T> cfg, final V comparable) {
        final Comparable<?> comparable2 = this.values.get(cfg);
        if (comparable2 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Cannot set property ").append(cfg).append(" as it does not exist in ").append(this.owner).toString());
        }
        if (comparable2 == comparable) {
            return (S)this;
        }
        final S object5 = this.neighbours.get(cfg, comparable);
        if (object5 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("Cannot set property ").append(cfg).append(" to ").append(comparable).append(" on ").append(this.owner).append(", it is not an allowed value").toString());
        }
        return object5;
    }
    
    public void populateNeighbours(final Map<Map<Property<?>, Comparable<?>>, S> map) {
        if (this.neighbours != null) {
            throw new IllegalStateException();
        }
        final Table<Property<?>, Comparable<?>, S> table3 = HashBasedTable.create();
        for (final Map.Entry<Property<?>, Comparable<?>> entry5 : this.values.entrySet()) {
            final Property<?> cfg6 = entry5.getKey();
            for (final Comparable<?> comparable8 : cfg6.getPossibleValues()) {
                if (comparable8 != entry5.getValue()) {
                    table3.put(cfg6, comparable8, (S)map.get(this.makeNeighbourValues(cfg6, comparable8)));
                }
            }
        }
        this.neighbours = (table3.isEmpty() ? table3 : ArrayTable.<Property<?>, Comparable<?>, S>create(table3));
    }
    
    private Map<Property<?>, Comparable<?>> makeNeighbourValues(final Property<?> cfg, final Comparable<?> comparable) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/minecraft/world/level/block/state/StateHolder.values:Lcom/google/common/collect/ImmutableMap;
        //     4: invokestatic    com/google/common/collect/Maps.newHashMap:(Ljava/util/Map;)Ljava/util/HashMap;
        //     7: astore_3        /* map4 */
        //     8: aload_3         /* map4 */
        //     9: aload_1         /* cfg */
        //    10: aload_2         /* comparable */
        //    11: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    16: pop            
        //    17: aload_3         /* map4 */
        //    18: areturn        
        //    Signature:
        //  (Lnet/minecraft/world/level/block/state/properties/Property<*>;Ljava/lang/Comparable<*>;)Ljava/util/Map<Lnet/minecraft/world/level/block/state/properties/Property<*>;Ljava/lang/Comparable<*>;>;
        //    MethodParameters:
        //  Name        Flags  
        //  ----------  -----
        //  cfg         
        //  comparable  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3215)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2526)
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
    
    public ImmutableMap<Property<?>, Comparable<?>> getValues() {
        return this.values;
    }
    
    protected static <O, S extends StateHolder<O, S>> Codec<S> codec(final Codec<O> codec, final Function<O, S> function) {
        return codec.<S>dispatch("Name", (java.util.function.Function<? super S, ? extends O>)(ceg -> ceg.owner), (java.util.function.Function<? super O, ? extends Codec<? extends S>>)(object -> {
            final S ceg3 = (S)function.apply(object);
            if (((StateHolder)ceg3).getValues().isEmpty()) {
                return Codec.<S>unit(ceg3);
            }
            return ((StateHolder)ceg3).propertiesCodec.fieldOf("Properties").codec();
        }));
    }
    
    static {
        PROPERTY_ENTRY_TO_STRING_FUNCTION = (Function)new Function<Map.Entry<Property<?>, Comparable<?>>, String>() {
            public String apply(@Nullable final Map.Entry<Property<?>, Comparable<?>> entry) {
                if (entry == null) {
                    return "<NULL>";
                }
                final Property<?> cfg3 = entry.getKey();
                return cfg3.getName() + "=" + this.getName(cfg3, entry.getValue());
            }
            
            private <T extends Comparable<T>> String getName(final Property<T> cfg, final Comparable<?> comparable) {
                return cfg.getName((T)comparable);
            }
        };
    }
}
