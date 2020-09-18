package com.mojang.datafixers.types.templates;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.functions.Functions;
import java.util.Collection;
import java.util.Set;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.DataResult;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import java.util.Optional;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.DSL;
import java.util.stream.Collector;
import java.util.Iterator;
import com.google.common.base.Joiner;
import java.util.Objects;
import com.mojang.datafixers.RewriteResult;
import java.util.function.IntFunction;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import com.mojang.datafixers.FamilyOptic;
import com.google.common.collect.Maps;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import com.mojang.datafixers.types.Type;

public final class TaggedChoice<K> implements TypeTemplate {
    private final String name;
    private final Type<K> keyType;
    private final Map<K, TypeTemplate> templates;
    private final Map<Pair<TypeFamily, Integer>, Type<?>> types;
    private final int size;
    
    public TaggedChoice(final String name, final Type<K> keyType, final Map<K, TypeTemplate> templates) {
        this.types = Maps.newConcurrentMap();
        this.name = name;
        this.keyType = keyType;
        this.templates = templates;
        this.size = templates.values().stream().mapToInt(TypeTemplate::size).max().orElse(0);
    }
    
    public int size() {
        return this.size;
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return index -> (Type)this.types.computeIfAbsent(Pair.<TypeFamily, Integer>of(family, index), key -> DSL.<K>taggedChoiceType(this.name, this.keyType, (java.util.Map<K, ? extends Type<?>>)this.templates.entrySet().stream().map(e -> Pair.<Object, Type<?>>of(e.getKey(), ((TypeTemplate)e.getValue()).apply(key.getFirst()).apply((int)key.getSecond()))).collect((Collector)Pair.toMap())));
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        throw new UnsupportedOperationException();
    }
    
    public <A, B> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<A> type, final Type<B> resultType) {
        return Either.<TypeTemplate, Type.FieldNotFoundException>right(new Type.FieldNotFoundException("Not implemented"));
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(index -> {
            RewriteResult<Pair<K, ?>, Pair<K, ?>> result = RewriteResult.<Pair<K, ?>>nop((Type<Pair<K, ?>>)(TaggedChoiceType)this.apply(family).apply(index));
            for (final Map.Entry<K, TypeTemplate> entry : this.templates.entrySet()) {
                final RewriteResult<?, ?> elementResult = ((TypeTemplate)entry.getValue()).hmap(family, function).apply(index);
                result = TaggedChoiceType.elementResult(entry.getKey(), (TaggedChoiceType)result.view().newType(), elementResult).compose((RewriteResult<Pair<K, ?>, Pair<Object, ?>>)result);
            }
            return result;
        });
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TaggedChoice)) {
            return false;
        }
        final TaggedChoice<?> other = obj;
        return Objects.equals(this.name, other.name) && Objects.equals(this.keyType, other.keyType) && Objects.equals(this.templates, other.templates);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.keyType, this.templates });
    }
    
    public String toString() {
        return "TaggedChoice[" + this.name + ", " + Joiner.on(", ").withKeyValueSeparator(" -> ").join(this.templates) + "]";
    }
    
    public static final class TaggedChoiceType<K> extends Type<Pair<K, ?>> {
        private final String name;
        private final Type<K> keyType;
        protected final Map<K, Type<?>> types;
        private final int hashCode;
        
        public TaggedChoiceType(final String name, final Type<K> keyType, final Map<K, Type<?>> types) {
            this.name = name;
            this.keyType = keyType;
            this.types = types;
            this.hashCode = Objects.hash(new Object[] { name, keyType, types });
        }
        
        @Override
        public RewriteResult<Pair<K, ?>, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.types:Ljava/util/Map;
            //     4: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
            //     9: invokeinterface java/util/Set.stream:()Ljava/util/stream/Stream;
            //    14: aload_1         /* rule */
            //    15: invokedynamic   BootstrapMethod #0, apply:(Lcom/mojang/datafixers/TypeRewriteRule;)Ljava/util/function/Function;
            //    20: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
            //    25: invokedynamic   BootstrapMethod #1, test:()Ljava/util/function/Predicate;
            //    30: invokeinterface java/util/stream/Stream.filter:(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;
            //    35: invokedynamic   BootstrapMethod #2, apply:()Ljava/util/function/Function;
            //    40: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
            //    45: invokestatic    com/mojang/datafixers/util/Pair.toMap:()Ljava/util/stream/Collector;
            //    48: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
            //    53: checkcast       Ljava/util/Map;
            //    56: astore          results
            //    58: aload           results
            //    60: invokeinterface java/util/Map.isEmpty:()Z
            //    65: ifeq            73
            //    68: aload_0         /* this */
            //    69: invokestatic    com/mojang/datafixers/RewriteResult.nop:(Lcom/mojang/datafixers/types/Type;)Lcom/mojang/datafixers/RewriteResult;
            //    72: areturn        
            //    73: aload           results
            //    75: invokeinterface java/util/Map.size:()I
            //    80: iconst_1       
            //    81: if_icmpne       128
            //    84: aload           results
            //    86: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
            //    91: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
            //    96: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   101: checkcast       Ljava/util/Map$Entry;
            //   104: astore          entry
            //   106: aload           entry
            //   108: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //   113: aload_0         /* this */
            //   114: aload           entry
            //   116: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //   121: checkcast       Lcom/mojang/datafixers/RewriteResult;
            //   124: invokestatic    com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.elementResult:(Ljava/lang/Object;Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType;Lcom/mojang/datafixers/RewriteResult;)Lcom/mojang/datafixers/RewriteResult;
            //   127: areturn        
            //   128: aload_0         /* this */
            //   129: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.types:Ljava/util/Map;
            //   132: invokestatic    com/google/common/collect/Maps.newHashMap:(Ljava/util/Map;)Ljava/util/HashMap;
            //   135: astore          newTypes
            //   137: new             Ljava/util/BitSet;
            //   140: dup            
            //   141: invokespecial   java/util/BitSet.<init>:()V
            //   144: astore          recData
            //   146: aload           results
            //   148: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
            //   153: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
            //   158: astore          7
            //   160: aload           7
            //   162: invokeinterface java/util/Iterator.hasNext:()Z
            //   167: ifeq            234
            //   170: aload           7
            //   172: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   177: checkcast       Ljava/util/Map$Entry;
            //   180: astore          entry
            //   182: aload           newTypes
            //   184: aload           entry
            //   186: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //   191: aload           entry
            //   193: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //   198: checkcast       Lcom/mojang/datafixers/RewriteResult;
            //   201: invokevirtual   com/mojang/datafixers/RewriteResult.view:()Lcom/mojang/datafixers/View;
            //   204: invokevirtual   com/mojang/datafixers/View.newType:()Lcom/mojang/datafixers/types/Type;
            //   207: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   212: pop            
            //   213: aload           recData
            //   215: aload           entry
            //   217: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //   222: checkcast       Lcom/mojang/datafixers/RewriteResult;
            //   225: invokevirtual   com/mojang/datafixers/RewriteResult.recData:()Ljava/util/BitSet;
            //   228: invokevirtual   java/util/BitSet.or:(Ljava/util/BitSet;)V
            //   231: goto            160
            //   234: aload_0         /* this */
            //   235: aload_0         /* this */
            //   236: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.name:Ljava/lang/String;
            //   239: aload_0         /* this */
            //   240: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.keyType:Lcom/mojang/datafixers/types/Type;
            //   243: aload           newTypes
            //   245: invokestatic    com/mojang/datafixers/DSL.taggedChoiceType:(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/Map;)Lcom/mojang/datafixers/types/Type;
            //   248: new             Ljava/lang/StringBuilder;
            //   251: dup            
            //   252: invokespecial   java/lang/StringBuilder.<init>:()V
            //   255: ldc             "TaggedChoiceTypeRewriteResult "
            //   257: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   260: aload           results
            //   262: invokeinterface java/util/Map.size:()I
            //   267: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
            //   270: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   273: new             Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$RewriteFunc;
            //   276: dup            
            //   277: aload           results
            //   279: invokespecial   com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$RewriteFunc.<init>:(Ljava/util/Map;)V
            //   282: invokestatic    com/mojang/datafixers/functions/Functions.fun:(Ljava/lang/String;Ljava/util/function/Function;)Lcom/mojang/datafixers/functions/PointFree;
            //   285: invokestatic    com/mojang/datafixers/View.create:(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/functions/PointFree;)Lcom/mojang/datafixers/View;
            //   288: aload           recData
            //   290: invokestatic    com/mojang/datafixers/RewriteResult.create:(Lcom/mojang/datafixers/View;Ljava/util/BitSet;)Lcom/mojang/datafixers/RewriteResult;
            //   293: areturn        
            //    Signature:
            //  (Lcom/mojang/datafixers/TypeRewriteRule;ZZ)Lcom/mojang/datafixers/RewriteResult<Lcom/mojang/datafixers/util/Pair<TK;*>;*>;
            //    MethodParameters:
            //  Name        Flags  
            //  ----------  -----
            //  rule        
            //  recurse     
            //  checkIndex  
            //    StackMapTable: 00 04 FC 00 49 07 00 16 36 FE 00 1F 07 00 16 07 00 D0 07 00 BB FA 00 49
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ParameterizedType.resolve(ParameterizedType.java:108)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
            //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
            //     at java.base/java.util.HashMap.putVal(HashMap.java:636)
            //     at java.base/java.util.HashMap.put(HashMap.java:613)
            //     at java.base/java.util.HashSet.add(HashSet.java:221)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1008)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
        
        public static <K, FT, FR> RewriteResult<Pair<K, ?>, Pair<K, ?>> elementResult(final K key, final TaggedChoiceType<K> type, final RewriteResult<FT, FR> result) {
            return Type.<Pair<K, ?>, Pair<K, ?>, FT, FR>opticView(type, result, TypedOptic.tagged(type, key, (Type<A>)result.view().type(), (Type<B>)result.view().newType()));
        }
        
        @Override
        public Optional<RewriteResult<Pair<K, ?>, ?>> one(final TypeRewriteRule rule) {
            for (final Map.Entry<K, Type<?>> entry : this.types.entrySet()) {
                final Optional<? extends RewriteResult<?, ?>> elementResult = rule.rewrite((Type<Object>)entry.getValue());
                if (elementResult.isPresent()) {
                    return (Optional<RewriteResult<Pair<K, ?>, ?>>)Optional.of(TaggedChoiceType.elementResult(entry.getKey(), (TaggedChoiceType<Object>)this, (RewriteResult<Object, Object>)elementResult.get()));
                }
            }
            return (Optional<RewriteResult<Pair<K, ?>, ?>>)Optional.empty();
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return DSL.<K>taggedChoiceType(this.name, this.keyType, (java.util.Map<K, ? extends Type<?>>)this.types.entrySet().stream().map(e -> Pair.<Object, Type>of(e.getKey(), ((Type)e.getValue()).updateMu(newFamily))).collect((Collector)Pair.toMap()));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.<K>taggedChoice(this.name, this.keyType, (java.util.Map<K, TypeTemplate>)this.types.entrySet().stream().map(e -> Pair.<Object, TypeTemplate>of(e.getKey(), ((Type)e.getValue()).template())).collect((Collector)Pair.toMap()));
        }
        
        private <V> DataResult<? extends Encoder<Pair<K, ?>>> encoder(final Pair<K, V> pair) {
            return this.getCodec(pair.getFirst()).map((java.util.function.Function<? super Codec<?>, ? extends Encoder<Pair<K, ?>>>)(c -> c.comap(p -> p.getSecond())));
        }
        
        @Override
        protected Codec<Pair<K, ?>> buildCodec() {
            return KeyDispatchCodec.<K, Object>unsafe(this.name, this.keyType.codec(), (java.util.function.Function<? super Object, ? extends DataResult<? extends K>>)(p -> DataResult.success(p.getFirst())), (java.util.function.Function<? super K, ? extends DataResult<? extends Decoder<?>>>)(k -> this.getCodec(k).map((java.util.function.Function<? super Codec<?>, ?>)(c -> c.map(v -> Pair.of(k, v))))), (java.util.function.Function<? super Object, ? extends DataResult<? extends Encoder<Object>>>)this::encoder).codec();
        }
        
        private DataResult<? extends Codec<?>> getCodec(final K k) {
            return Optional.ofNullable(this.types.get(k)).map(t -> DataResult.<Codec>success(t.codec())).orElseGet(() -> DataResult.error(new StringBuilder().append("Unsupported key: ").append(k).toString()));
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            return (Optional<Type<?>>)this.types.values().stream().map(t -> t.findFieldTypeOpt(name)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        }
        
        @Override
        public Optional<Pair<K, ?>> point(final DynamicOps<?> ops) {
            return (Optional<Pair<K, ?>>)this.types.entrySet().stream().map(e -> ((Type)e.getValue()).point(ops).map(value -> Pair.of(e.getKey(), value))).filter(Optional::isPresent).findFirst().flatMap(Function.identity()).map(p -> p);
        }
        
        public Optional<Typed<Pair<K, ?>>> point(final DynamicOps<?> ops, final K key, final Object value) {
            if (!this.types.containsKey(key)) {
                return (Optional<Typed<Pair<K, ?>>>)Optional.empty();
            }
            return (Optional<Typed<Pair<K, ?>>>)Optional.of(new Typed((Type<Object>)this, ops, Pair.<K, Object>of(key, value)));
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<Pair<K, ?>, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.types:Ljava/util/Map;
            //     4: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
            //     9: invokeinterface java/util/Set.stream:()Ljava/util/stream/Stream;
            //    14: aload_1         /* type */
            //    15: aload_2         /* resultType */
            //    16: aload_3         /* matcher */
            //    17: iload           recurse
            //    19: invokedynamic   BootstrapMethod #15, apply:(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type$TypeMatcher;Z)Ljava/util/function/Function;
            //    24: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
            //    29: invokedynamic   BootstrapMethod #16, test:()Ljava/util/function/Predicate;
            //    34: invokeinterface java/util/stream/Stream.filter:(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;
            //    39: invokedynamic   BootstrapMethod #17, apply:()Ljava/util/function/Function;
            //    44: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
            //    49: invokestatic    com/mojang/datafixers/util/Pair.toMap:()Ljava/util/stream/Collector;
            //    52: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
            //    57: checkcast       Ljava/util/Map;
            //    60: astore          optics
            //    62: aload           optics
            //    64: invokeinterface java/util/Map.isEmpty:()Z
            //    69: ifeq            86
            //    72: new             Lcom/mojang/datafixers/types/Type$FieldNotFoundException;
            //    75: dup            
            //    76: ldc_w           "Not found in any choices"
            //    79: invokespecial   com/mojang/datafixers/types/Type$FieldNotFoundException.<init>:(Ljava/lang/String;)V
            //    82: invokestatic    com/mojang/datafixers/util/Either.right:(Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
            //    85: areturn        
            //    86: aload           optics
            //    88: invokeinterface java/util/Map.size:()I
            //    93: iconst_1       
            //    94: if_icmpne       145
            //    97: aload           optics
            //    99: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
            //   104: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
            //   109: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //   114: checkcast       Ljava/util/Map$Entry;
            //   117: astore          entry
            //   119: aload_0         /* this */
            //   120: aload_0         /* this */
            //   121: aload           entry
            //   123: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
            //   128: aload           entry
            //   130: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
            //   135: checkcast       Lcom/mojang/datafixers/TypedOptic;
            //   138: invokespecial   com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.cap:(Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType;Ljava/lang/Object;Lcom/mojang/datafixers/TypedOptic;)Lcom/mojang/datafixers/TypedOptic;
            //   141: invokestatic    com/mojang/datafixers/util/Either.left:(Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
            //   144: areturn        
            //   145: invokestatic    com/google/common/collect/Sets.newHashSet:()Ljava/util/HashSet;
            //   148: astore          bounds
            //   150: aload           optics
            //   152: invokeinterface java/util/Map.values:()Ljava/util/Collection;
            //   157: aload           bounds
            //   159: invokedynamic   BootstrapMethod #18, accept:(Ljava/util/Set;)Ljava/util/function/Consumer;
            //   164: invokeinterface java/util/Collection.forEach:(Ljava/util/function/Consumer;)V
            //   169: aload           bounds
            //   171: getstatic       com/mojang/datafixers/optics/profunctors/Cartesian$Mu.TYPE_TOKEN:Lcom/google/common/reflect/TypeToken;
            //   174: invokestatic    com/mojang/datafixers/TypedOptic.instanceOf:(Ljava/util/Collection;Lcom/google/common/reflect/TypeToken;)Z
            //   177: ifeq            219
            //   180: aload           optics
            //   182: invokeinterface java/util/Map.size:()I
            //   187: aload_0         /* this */
            //   188: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.types:Ljava/util/Map;
            //   191: invokeinterface java/util/Map.size:()I
            //   196: if_icmpne       219
            //   199: getstatic       com/mojang/datafixers/optics/profunctors/Cartesian$Mu.TYPE_TOKEN:Lcom/google/common/reflect/TypeToken;
            //   202: astore          bound
            //   204: new             Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$1;
            //   207: dup            
            //   208: aload_0         /* this */
            //   209: aload           optics
            //   211: invokespecial   com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$1.<init>:(Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType;Ljava/util/Map;)V
            //   214: astore          optic
            //   216: goto            318
            //   219: aload           bounds
            //   221: getstatic       com/mojang/datafixers/optics/profunctors/AffineP$Mu.TYPE_TOKEN:Lcom/google/common/reflect/TypeToken;
            //   224: invokestatic    com/mojang/datafixers/TypedOptic.instanceOf:(Ljava/util/Collection;Lcom/google/common/reflect/TypeToken;)Z
            //   227: ifeq            250
            //   230: getstatic       com/mojang/datafixers/optics/profunctors/AffineP$Mu.TYPE_TOKEN:Lcom/google/common/reflect/TypeToken;
            //   233: astore          bound
            //   235: new             Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$2;
            //   238: dup            
            //   239: aload_0         /* this */
            //   240: aload           optics
            //   242: invokespecial   com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$2.<init>:(Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType;Ljava/util/Map;)V
            //   245: astore          optic
            //   247: goto            318
            //   250: aload           bounds
            //   252: getstatic       com/mojang/datafixers/optics/profunctors/TraversalP$Mu.TYPE_TOKEN:Lcom/google/common/reflect/TypeToken;
            //   255: invokestatic    com/mojang/datafixers/TypedOptic.instanceOf:(Ljava/util/Collection;Lcom/google/common/reflect/TypeToken;)Z
            //   258: ifeq            281
            //   261: getstatic       com/mojang/datafixers/optics/profunctors/TraversalP$Mu.TYPE_TOKEN:Lcom/google/common/reflect/TypeToken;
            //   264: astore          bound
            //   266: new             Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$3;
            //   269: dup            
            //   270: aload_0         /* this */
            //   271: aload           optics
            //   273: invokespecial   com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType$3.<init>:(Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType;Ljava/util/Map;)V
            //   276: astore          optic
            //   278: goto            318
            //   281: new             Ljava/lang/IllegalStateException;
            //   284: dup            
            //   285: new             Ljava/lang/StringBuilder;
            //   288: dup            
            //   289: invokespecial   java/lang/StringBuilder.<init>:()V
            //   292: ldc_w           "Could not merge TaggedChoiceType optics, unknown bound: "
            //   295: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   298: aload           bounds
            //   300: invokeinterface java/util/Set.toArray:()[Ljava/lang/Object;
            //   305: invokestatic    java/util/Arrays.toString:([Ljava/lang/Object;)Ljava/lang/String;
            //   308: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   311: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   314: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
            //   317: athrow         
            //   318: aload_0         /* this */
            //   319: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.types:Ljava/util/Map;
            //   322: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
            //   327: invokeinterface java/util/Set.stream:()Ljava/util/stream/Stream;
            //   332: aload           optics
            //   334: invokedynamic   BootstrapMethod #19, apply:(Ljava/util/Map;)Ljava/util/function/Function;
            //   339: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
            //   344: invokestatic    com/mojang/datafixers/util/Pair.toMap:()Ljava/util/stream/Collector;
            //   347: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
            //   352: checkcast       Ljava/util/Map;
            //   355: astore          newTypes
            //   357: new             Lcom/mojang/datafixers/TypedOptic;
            //   360: dup            
            //   361: aload           bound
            //   363: aload_0         /* this */
            //   364: aload_0         /* this */
            //   365: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.name:Ljava/lang/String;
            //   368: aload_0         /* this */
            //   369: getfield        com/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType.keyType:Lcom/mojang/datafixers/types/Type;
            //   372: aload           newTypes
            //   374: invokestatic    com/mojang/datafixers/DSL.taggedChoiceType:(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/Map;)Lcom/mojang/datafixers/types/Type;
            //   377: aload_1         /* type */
            //   378: aload_2         /* resultType */
            //   379: aload           optic
            //   381: invokespecial   com/mojang/datafixers/TypedOptic.<init>:(Lcom/google/common/reflect/TypeToken;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/optics/Optic;)V
            //   384: invokestatic    com/mojang/datafixers/util/Either.left:(Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
            //   387: areturn        
            //    Signature:
            //  <FT:Ljava/lang/Object;FR:Ljava/lang/Object;>(Lcom/mojang/datafixers/types/Type<TFT;>;Lcom/mojang/datafixers/types/Type<TFR;>;Lcom/mojang/datafixers/types/Type$TypeMatcher<TFT;TFR;>;Z)Lcom/mojang/datafixers/util/Either<Lcom/mojang/datafixers/TypedOptic<Lcom/mojang/datafixers/util/Pair<TK;*>;*TFT;TFR;>;Lcom/mojang/datafixers/types/Type$FieldNotFoundException;>;
            //    MethodParameters:
            //  Name        Flags  
            //  ----------  -----
            //  type        
            //  resultType  
            //  matcher     
            //  recurse     
            //    StackMapTable: 00 06 FC 00 56 07 00 16 3A FC 00 49 07 00 63 1E 1E FD 00 24 07 02 59 07 02 5B
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:715)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferInitObject(TypeAnalysis.java:1923)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1306)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferInitObject(TypeAnalysis.java:1952)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1306)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
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
        
        private <S, T, FT, FR> TypedOptic<Pair<K, ?>, Pair<K, ?>, FT, FR> cap(final TaggedChoiceType<K> choiceType, final K key, final TypedOptic<S, T, FT, FR> optic) {
            return TypedOptic.<K, S, T>tagged(choiceType, key, optic.sType(), optic.tType()).<FT, FR>compose(optic);
        }
        
        @Override
        public Optional<TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            if (Objects.equals(name, this.name)) {
                return (Optional<TaggedChoiceType<?>>)Optional.of(this);
            }
            return (Optional<TaggedChoiceType<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            return (Optional<Type<?>>)this.types.values().stream().map(type -> type.findCheckedType(index)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TaggedChoiceType)) {
                return false;
            }
            final TaggedChoiceType<?> other = obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!this.keyType.equals(other.keyType, ignoreRecursionPoints, checkIndex)) {
                return false;
            }
            if (this.types.size() != other.types.size()) {
                return false;
            }
            for (final Map.Entry<K, Type<?>> entry : this.types.entrySet()) {
                if (!((Type)entry.getValue()).equals(other.types.get(entry.getKey()), ignoreRecursionPoints, checkIndex)) {
                    return false;
                }
            }
            return true;
        }
        
        public int hashCode() {
            return this.hashCode;
        }
        
        public String toString() {
            return "TaggedChoiceType[" + this.name + ", " + Joiner.on(", \n").withKeyValueSeparator(" -> ").join(this.types) + "]\n";
        }
        
        public String getName() {
            return this.name;
        }
        
        public Type<K> getKeyType() {
            return this.keyType;
        }
        
        public boolean hasType(final K key) {
            return this.types.containsKey(key);
        }
        
        public Map<K, Type<?>> types() {
            return this.types;
        }
        
        private static final class RewriteFunc<K> implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> {
            private final Map<K, ? extends RewriteResult<?, ?>> results;
            
            public RewriteFunc(final Map<K, ? extends RewriteResult<?, ?>> results) {
                this.results = results;
            }
            
            public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(final DynamicOps<?> ops) {
                final RewriteResult<?, ?> result;
                return (FunctionType<Pair<K, ?>, Pair<K, ?>>)(input -> {
                    result = this.results.get(input.getFirst());
                    if (result == null) {
                        return input;
                    }
                    else {
                        return this.capRuleApply(ops, input, result);
                    }
                });
            }
            
            private <A, B> Pair<K, B> capRuleApply(final DynamicOps<?> ops, final Pair<K, ?> input, final RewriteResult<A, B> result) {
                return input.<B>mapSecond((java.util.function.Function<?, ? extends B>)(v -> ((Function)result.view().function().evalCached().apply(ops)).apply(v)));
            }
            
            public boolean equals(final Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final RewriteFunc<?> that = o;
                return Objects.equals(this.results, that.results);
            }
            
            public int hashCode() {
                return Objects.hash(new Object[] { this.results });
            }
        }
        
        private static final class RewriteFunc<K> implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> {
            private final Map<K, ? extends RewriteResult<?, ?>> results;
            
            public RewriteFunc(final Map<K, ? extends RewriteResult<?, ?>> results) {
                this.results = results;
            }
            
            public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(final DynamicOps<?> ops) {
                final RewriteResult<?, ?> result;
                return (FunctionType<Pair<K, ?>, Pair<K, ?>>)(input -> {
                    result = this.results.get(input.getFirst());
                    if (result == null) {
                        return input;
                    }
                    else {
                        return this.capRuleApply(ops, input, result);
                    }
                });
            }
            
            private <A, B> Pair<K, B> capRuleApply(final DynamicOps<?> ops, final Pair<K, ?> input, final RewriteResult<A, B> result) {
                return input.<B>mapSecond((java.util.function.Function<?, ? extends B>)(v -> ((Function)result.view().function().evalCached().apply(ops)).apply(v)));
            }
            
            public boolean equals(final Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final RewriteFunc<?> that = o;
                return Objects.equals(this.results, that.results);
            }
            
            public int hashCode() {
                return Objects.hash(new Object[] { this.results });
            }
        }
        
        private static final class RewriteFunc<K> implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> {
            private final Map<K, ? extends RewriteResult<?, ?>> results;
            
            public RewriteFunc(final Map<K, ? extends RewriteResult<?, ?>> results) {
                this.results = results;
            }
            
            public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(final DynamicOps<?> ops) {
                final RewriteResult<?, ?> result;
                return (FunctionType<Pair<K, ?>, Pair<K, ?>>)(input -> {
                    result = this.results.get(input.getFirst());
                    if (result == null) {
                        return input;
                    }
                    else {
                        return this.capRuleApply(ops, input, result);
                    }
                });
            }
            
            private <A, B> Pair<K, B> capRuleApply(final DynamicOps<?> ops, final Pair<K, ?> input, final RewriteResult<A, B> result) {
                return input.<B>mapSecond((java.util.function.Function<?, ? extends B>)(v -> ((Function)result.view().function().evalCached().apply(ops)).apply(v)));
            }
            
            public boolean equals(final Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final RewriteFunc<?> that = o;
                return Objects.equals(this.results, that.results);
            }
            
            public int hashCode() {
                return Objects.hash(new Object[] { this.results });
            }
        }
    }
}
