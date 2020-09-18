package com.mojang.datafixers.types.templates;

import java.util.function.Function;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import java.util.Optional;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Codec;
import java.util.Objects;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;
import com.mojang.datafixers.OpticParts;
import java.util.function.IntFunction;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;

public final class Check implements TypeTemplate {
    private final String name;
    private final int index;
    private final TypeTemplate element;
    
    public Check(final String name, final int index, final TypeTemplate element) {
        this.name = name;
        this.index = index;
        this.element = element;
    }
    
    public int size() {
        return Math.max(this.index + 1, this.element.size());
    }
    
    public TypeFamily apply(final TypeFamily family) {
        return new TypeFamily() {
            public Type<?> apply(final int index) {
                if (index < 0) {
                    throw new IndexOutOfBoundsException();
                }
                return new CheckType<>(Check.this.name, index, Check.this.index, Check.this.element.apply(family).apply(index));
            }
        };
    }
    
    public <A, B> FamilyOptic<A, B> applyO(final FamilyOptic<A, B> input, final Type<A> aType, final Type<B> bType) {
        return TypeFamily.<A, B>familyOptic((java.util.function.IntFunction<OpticParts<A, B>>)(i -> this.element.applyO(input, (Type<Object>)aType, (Type<Object>)bType).apply(i)));
    }
    
    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(final int index, @Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
        if (index == this.index) {
            return this.element.<FT, FR>findFieldOrType(index, name, type, resultType);
        }
        return Either.<TypeTemplate, Type.FieldNotFoundException>right(new Type.FieldNotFoundException("Not a matching index"));
    }
    
    public IntFunction<RewriteResult<?, ?>> hmap(final TypeFamily family, final IntFunction<RewriteResult<?, ?>> function) {
        return (IntFunction<RewriteResult<?, ?>>)(index -> {
            final RewriteResult<?, ?> elementResult = this.element.hmap(family, function).apply(index);
            return this.cap(family, index, elementResult);
        });
    }
    
    private <A> RewriteResult<?, ?> cap(final TypeFamily family, final int index, final RewriteResult<A, ?> elementResult) {
        return CheckType.fix((CheckType)this.apply(family).apply(index), elementResult);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Check)) {
            return false;
        }
        final Check that = (Check)obj;
        return Objects.equals(this.name, that.name) && this.index == that.index && Objects.equals(this.element, that.element);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.index, this.element });
    }
    
    public String toString() {
        return "Tag[" + this.name + ", " + this.index + ": " + this.element + "]";
    }
    
    public static final class CheckType<A> extends Type<A> {
        private final String name;
        private final int index;
        private final int expectedIndex;
        private final Type<A> delegate;
        
        public CheckType(final String name, final int index, final int expectedIndex, final Type<A> delegate) {
            this.name = name;
            this.index = index;
            this.expectedIndex = expectedIndex;
            this.delegate = delegate;
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return Codec.<A>of(this.delegate.codec(), this::read);
        }
        
        private <T> DataResult<Pair<A, T>> read(final DynamicOps<T> ops, final T input) {
            if (this.index != this.expectedIndex) {
                return DataResult.<Pair<A, T>>error(new StringBuilder().append("Index mismatch: ").append(this.index).append(" != ").append(this.expectedIndex).toString());
            }
            return this.delegate.codec().<T>decode(ops, input);
        }
        
        public static <A, B> RewriteResult<A, ?> fix(final CheckType<A> type, final RewriteResult<A, B> instance) {
            if (Objects.equals(instance.view().function(), Functions.id())) {
                return RewriteResult.nop(type);
            }
            return Type.opticView(type, instance, CheckType.<S, T, A, B>wrapOptic((CheckType<S>)type, TypedOptic.adapter(instance.view().type(), instance.view().newType())));
        }
        
        @Override
        public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return RewriteResult.nop(this);
            }
            return CheckType.fix(this, this.delegate.rewriteOrNop(rule));
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return (Optional<RewriteResult<A, ?>>)Optional.empty();
            }
            return super.everywhere(rule, optimizationRule, recurse, checkIndex);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
            return (Optional<RewriteResult<A, ?>>)rule.<A>rewrite(this.delegate).map(view -> CheckType.fix((CheckType<Object>)this, (RewriteResult<Object, Object>)view));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return new CheckType<>(this.name, this.index, this.expectedIndex, this.delegate.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.check(this.name, this.expectedIndex, this.delegate.template());
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            if (index == this.expectedIndex) {
                return this.delegate.findChoiceType(name, index);
            }
            return (Optional<TaggedChoice.TaggedChoiceType<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            if (index == this.expectedIndex) {
                return (Optional<Type<?>>)Optional.of(this.delegate);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            if (this.index == this.expectedIndex) {
                return this.delegate.findFieldTypeOpt(name);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<A> point(final DynamicOps<?> ops) {
            if (this.index == this.expectedIndex) {
                return this.delegate.point(ops);
            }
            return (Optional<A>)Optional.empty();
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            if (this.index != this.expectedIndex) {
                return Either.<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>right(new FieldNotFoundException("Incorrect index in CheckType"));
            }
            return this.delegate.<FT, FR>findType(type, resultType, matcher, recurse).<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, FT, FR>, ? extends TypedOptic<A, ?, FT, FR>>)(optic -> CheckType.wrapOptic((CheckType<Object>)this, (TypedOptic<Object, Object, Object, Object>)optic)));
        }
        
        protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(final CheckType<A> type, final TypedOptic<A, B, FT, FR> optic) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: aload_1         /* optic */
            //     5: invokevirtual   com/mojang/datafixers/TypedOptic.bounds:()Ljava/util/Set;
            //     8: aload_0         /* type */
            //     9: new             Lcom/mojang/datafixers/types/templates/Check$CheckType;
            //    12: dup            
            //    13: aload_0         /* type */
            //    14: getfield        com/mojang/datafixers/types/templates/Check$CheckType.name:Ljava/lang/String;
            //    17: aload_0         /* type */
            //    18: getfield        com/mojang/datafixers/types/templates/Check$CheckType.index:I
            //    21: aload_0         /* type */
            //    22: getfield        com/mojang/datafixers/types/templates/Check$CheckType.expectedIndex:I
            //    25: aload_1         /* optic */
            //    26: invokevirtual   com/mojang/datafixers/TypedOptic.tType:()Lcom/mojang/datafixers/types/Type;
            //    29: invokespecial   com/mojang/datafixers/types/templates/Check$CheckType.<init>:(Ljava/lang/String;IILcom/mojang/datafixers/types/Type;)V
            //    32: aload_1         /* optic */
            //    33: invokevirtual   com/mojang/datafixers/TypedOptic.aType:()Lcom/mojang/datafixers/types/Type;
            //    36: aload_1         /* optic */
            //    37: invokevirtual   com/mojang/datafixers/TypedOptic.bType:()Lcom/mojang/datafixers/types/Type;
            //    40: aload_1         /* optic */
            //    41: invokevirtual   com/mojang/datafixers/TypedOptic.optic:()Lcom/mojang/datafixers/optics/Optic;
            //    44: invokespecial   com/mojang/datafixers/TypedOptic.<init>:(Ljava/util/Set;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/optics/Optic;)V
            //    47: areturn        
            //    Signature:
            //  <A:Ljava/lang/Object;B:Ljava/lang/Object;FT:Ljava/lang/Object;FR:Ljava/lang/Object;>(Lcom/mojang/datafixers/types/templates/Check$CheckType<TA;>;Lcom/mojang/datafixers/TypedOptic<TA;TB;TFT;TFR;>;)Lcom/mojang/datafixers/TypedOptic<TA;TB;TFT;TFR;>;
            //    MethodParameters:
            //  Name   Flags  
            //  -----  -----
            //  type   
            //  optic  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 14
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
            //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
            //     at com.strobel.assembler.metadata.WildcardType.hasExtendsBound(WildcardType.java:106)
            //     at com.strobel.assembler.metadata.MetadataHelper$StrictSameTypeVisitor.visitWildcard(MetadataHelper.java:2595)
            //     at com.strobel.assembler.metadata.MetadataHelper$StrictSameTypeVisitor.visitWildcard(MetadataHelper.java:2529)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
            //     at com.strobel.assembler.metadata.MetadataHelper.areSameTypes(MetadataHelper.java:1429)
            //     at com.strobel.assembler.metadata.MetadataHelper$StrictSameTypeVisitor.containsTypes(MetadataHelper.java:2584)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2386)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
            //     at com.strobel.assembler.metadata.MetadataHelper.areSameTypes(MetadataHelper.java:1429)
            //     at com.strobel.assembler.metadata.MetadataHelper$StrictSameTypeVisitor.containsTypes(MetadataHelper.java:2584)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2386)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.isCastRequired(AstMethodBodyBuilder.java:1357)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1318)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1178)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
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
        
        public String toString() {
            return new StringBuilder().append("TypeTag[").append(this.index).append("~").append(this.expectedIndex).append("][").append(this.name).append(": ").append(this.delegate).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CheckType)) {
                return false;
            }
            final CheckType<?> type = obj;
            if (this.index == type.index && this.expectedIndex == type.expectedIndex) {
                if (!checkIndex) {
                    return true;
                }
                if (this.delegate.equals(type.delegate, ignoreRecursionPoints, checkIndex)) {
                    return true;
                }
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.index, this.expectedIndex, this.delegate });
        }
    }
    
    public static final class CheckType<A> extends Type<A> {
        private final String name;
        private final int index;
        private final int expectedIndex;
        private final Type<A> delegate;
        
        public CheckType(final String name, final int index, final int expectedIndex, final Type<A> delegate) {
            this.name = name;
            this.index = index;
            this.expectedIndex = expectedIndex;
            this.delegate = delegate;
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return Codec.<A>of(this.delegate.codec(), this::read);
        }
        
        private <T> DataResult<Pair<A, T>> read(final DynamicOps<T> ops, final T input) {
            if (this.index != this.expectedIndex) {
                return DataResult.<Pair<A, T>>error(new StringBuilder().append("Index mismatch: ").append(this.index).append(" != ").append(this.expectedIndex).toString());
            }
            return this.delegate.codec().<T>decode(ops, input);
        }
        
        public static <A, B> RewriteResult<A, ?> fix(final CheckType<A> type, final RewriteResult<A, B> instance) {
            if (Objects.equals(instance.view().function(), Functions.id())) {
                return RewriteResult.nop(type);
            }
            return Type.opticView(type, instance, CheckType.<S, T, A, B>wrapOptic((CheckType<S>)type, TypedOptic.adapter(instance.view().type(), instance.view().newType())));
        }
        
        @Override
        public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return RewriteResult.nop(this);
            }
            return CheckType.fix(this, this.delegate.rewriteOrNop(rule));
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return (Optional<RewriteResult<A, ?>>)Optional.empty();
            }
            return super.everywhere(rule, optimizationRule, recurse, checkIndex);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
            return (Optional<RewriteResult<A, ?>>)rule.<A>rewrite(this.delegate).map(view -> CheckType.fix((CheckType<Object>)this, (RewriteResult<Object, Object>)view));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return new CheckType<>(this.name, this.index, this.expectedIndex, this.delegate.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.check(this.name, this.expectedIndex, this.delegate.template());
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            if (index == this.expectedIndex) {
                return this.delegate.findChoiceType(name, index);
            }
            return (Optional<TaggedChoice.TaggedChoiceType<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            if (index == this.expectedIndex) {
                return (Optional<Type<?>>)Optional.of(this.delegate);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            if (this.index == this.expectedIndex) {
                return this.delegate.findFieldTypeOpt(name);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<A> point(final DynamicOps<?> ops) {
            if (this.index == this.expectedIndex) {
                return this.delegate.point(ops);
            }
            return (Optional<A>)Optional.empty();
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            if (this.index != this.expectedIndex) {
                return Either.<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>right(new FieldNotFoundException("Incorrect index in CheckType"));
            }
            return this.delegate.<FT, FR>findType(type, resultType, matcher, recurse).<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, FT, FR>, ? extends TypedOptic<A, ?, FT, FR>>)(optic -> CheckType.wrapOptic((CheckType<Object>)this, (TypedOptic<Object, Object, Object, Object>)optic)));
        }
        
        protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(final CheckType<A> type, final TypedOptic<A, B, FT, FR> optic) {
            return new TypedOptic<A, B, FT, FR>(optic.bounds(), type, new CheckType<B>(type.name, type.index, type.expectedIndex, optic.tType()), optic.aType(), optic.bType(), optic.optic());
        }
        
        public String toString() {
            return new StringBuilder().append("TypeTag[").append(this.index).append("~").append(this.expectedIndex).append("][").append(this.name).append(": ").append(this.delegate).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CheckType)) {
                return false;
            }
            final CheckType<?> type = obj;
            if (this.index == type.index && this.expectedIndex == type.expectedIndex) {
                if (!checkIndex) {
                    return true;
                }
                if (this.delegate.equals(type.delegate, ignoreRecursionPoints, checkIndex)) {
                    return true;
                }
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.index, this.expectedIndex, this.delegate });
        }
    }
    
    public static final class CheckType<A> extends Type<A> {
        private final String name;
        private final int index;
        private final int expectedIndex;
        private final Type<A> delegate;
        
        public CheckType(final String name, final int index, final int expectedIndex, final Type<A> delegate) {
            this.name = name;
            this.index = index;
            this.expectedIndex = expectedIndex;
            this.delegate = delegate;
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return Codec.<A>of(this.delegate.codec(), this::read);
        }
        
        private <T> DataResult<Pair<A, T>> read(final DynamicOps<T> ops, final T input) {
            if (this.index != this.expectedIndex) {
                return DataResult.<Pair<A, T>>error(new StringBuilder().append("Index mismatch: ").append(this.index).append(" != ").append(this.expectedIndex).toString());
            }
            return this.delegate.codec().<T>decode(ops, input);
        }
        
        public static <A, B> RewriteResult<A, ?> fix(final CheckType<A> type, final RewriteResult<A, B> instance) {
            if (Objects.equals(instance.view().function(), Functions.id())) {
                return RewriteResult.nop(type);
            }
            return Type.opticView(type, instance, CheckType.<S, T, A, B>wrapOptic((CheckType<S>)type, TypedOptic.adapter(instance.view().type(), instance.view().newType())));
        }
        
        @Override
        public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return RewriteResult.nop(this);
            }
            return CheckType.fix(this, this.delegate.rewriteOrNop(rule));
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return (Optional<RewriteResult<A, ?>>)Optional.empty();
            }
            return super.everywhere(rule, optimizationRule, recurse, checkIndex);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
            return (Optional<RewriteResult<A, ?>>)rule.<A>rewrite(this.delegate).map(view -> CheckType.fix((CheckType<Object>)this, (RewriteResult<Object, Object>)view));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return new CheckType<>(this.name, this.index, this.expectedIndex, this.delegate.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.check(this.name, this.expectedIndex, this.delegate.template());
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            if (index == this.expectedIndex) {
                return this.delegate.findChoiceType(name, index);
            }
            return (Optional<TaggedChoice.TaggedChoiceType<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            if (index == this.expectedIndex) {
                return (Optional<Type<?>>)Optional.of(this.delegate);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            if (this.index == this.expectedIndex) {
                return this.delegate.findFieldTypeOpt(name);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<A> point(final DynamicOps<?> ops) {
            if (this.index == this.expectedIndex) {
                return this.delegate.point(ops);
            }
            return (Optional<A>)Optional.empty();
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            if (this.index != this.expectedIndex) {
                return Either.<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>right(new FieldNotFoundException("Incorrect index in CheckType"));
            }
            return this.delegate.<FT, FR>findType(type, resultType, matcher, recurse).<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, FT, FR>, ? extends TypedOptic<A, ?, FT, FR>>)(optic -> CheckType.wrapOptic((CheckType<Object>)this, (TypedOptic<Object, Object, Object, Object>)optic)));
        }
        
        protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(final CheckType<A> type, final TypedOptic<A, B, FT, FR> optic) {
            return new TypedOptic<A, B, FT, FR>(optic.bounds(), type, new CheckType<B>(type.name, type.index, type.expectedIndex, optic.tType()), optic.aType(), optic.bType(), optic.optic());
        }
        
        public String toString() {
            return new StringBuilder().append("TypeTag[").append(this.index).append("~").append(this.expectedIndex).append("][").append(this.name).append(": ").append(this.delegate).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CheckType)) {
                return false;
            }
            final CheckType<?> type = obj;
            if (this.index == type.index && this.expectedIndex == type.expectedIndex) {
                if (!checkIndex) {
                    return true;
                }
                if (this.delegate.equals(type.delegate, ignoreRecursionPoints, checkIndex)) {
                    return true;
                }
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.index, this.expectedIndex, this.delegate });
        }
    }
    
    public static final class CheckType<A> extends Type<A> {
        private final String name;
        private final int index;
        private final int expectedIndex;
        private final Type<A> delegate;
        
        public CheckType(final String name, final int index, final int expectedIndex, final Type<A> delegate) {
            this.name = name;
            this.index = index;
            this.expectedIndex = expectedIndex;
            this.delegate = delegate;
        }
        
        @Override
        protected Codec<A> buildCodec() {
            return Codec.<A>of(this.delegate.codec(), this::read);
        }
        
        private <T> DataResult<Pair<A, T>> read(final DynamicOps<T> ops, final T input) {
            if (this.index != this.expectedIndex) {
                return DataResult.<Pair<A, T>>error(new StringBuilder().append("Index mismatch: ").append(this.index).append(" != ").append(this.expectedIndex).toString());
            }
            return this.delegate.codec().<T>decode(ops, input);
        }
        
        public static <A, B> RewriteResult<A, ?> fix(final CheckType<A> type, final RewriteResult<A, B> instance) {
            if (Objects.equals(instance.view().function(), Functions.id())) {
                return RewriteResult.nop(type);
            }
            return Type.opticView(type, instance, CheckType.<S, T, A, B>wrapOptic((CheckType<S>)type, TypedOptic.adapter(instance.view().type(), instance.view().newType())));
        }
        
        @Override
        public RewriteResult<A, ?> all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return RewriteResult.nop(this);
            }
            return CheckType.fix(this, this.delegate.rewriteOrNop(rule));
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
            if (checkIndex && this.index != this.expectedIndex) {
                return (Optional<RewriteResult<A, ?>>)Optional.empty();
            }
            return super.everywhere(rule, optimizationRule, recurse, checkIndex);
        }
        
        @Override
        public Optional<RewriteResult<A, ?>> one(final TypeRewriteRule rule) {
            return (Optional<RewriteResult<A, ?>>)rule.<A>rewrite(this.delegate).map(view -> CheckType.fix((CheckType<Object>)this, (RewriteResult<Object, Object>)view));
        }
        
        @Override
        public Type<?> updateMu(final RecursiveTypeFamily newFamily) {
            return new CheckType<>(this.name, this.index, this.expectedIndex, this.delegate.updateMu(newFamily));
        }
        
        @Override
        public TypeTemplate buildTemplate() {
            return DSL.check(this.name, this.expectedIndex, this.delegate.template());
        }
        
        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(final String name, final int index) {
            if (index == this.expectedIndex) {
                return this.delegate.findChoiceType(name, index);
            }
            return (Optional<TaggedChoice.TaggedChoiceType<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findCheckedType(final int index) {
            if (index == this.expectedIndex) {
                return (Optional<Type<?>>)Optional.of(this.delegate);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<Type<?>> findFieldTypeOpt(final String name) {
            if (this.index == this.expectedIndex) {
                return this.delegate.findFieldTypeOpt(name);
            }
            return (Optional<Type<?>>)Optional.empty();
        }
        
        @Override
        public Optional<A> point(final DynamicOps<?> ops) {
            if (this.index == this.expectedIndex) {
                return this.delegate.point(ops);
            }
            return (Optional<A>)Optional.empty();
        }
        
        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(final Type<FT> type, final Type<FR> resultType, final TypeMatcher<FT, FR> matcher, final boolean recurse) {
            if (this.index != this.expectedIndex) {
                return Either.<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>right(new FieldNotFoundException("Incorrect index in CheckType"));
            }
            return this.delegate.<FT, FR>findType(type, resultType, matcher, recurse).<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, FT, FR>, ? extends TypedOptic<A, ?, FT, FR>>)(optic -> CheckType.wrapOptic((CheckType<Object>)this, (TypedOptic<Object, Object, Object, Object>)optic)));
        }
        
        protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(final CheckType<A> type, final TypedOptic<A, B, FT, FR> optic) {
            return new TypedOptic<A, B, FT, FR>(optic.bounds(), type, new CheckType<B>(type.name, type.index, type.expectedIndex, optic.tType()), optic.aType(), optic.bType(), optic.optic());
        }
        
        public String toString() {
            return new StringBuilder().append("TypeTag[").append(this.index).append("~").append(this.expectedIndex).append("][").append(this.name).append(": ").append(this.delegate).append("]").toString();
        }
        
        @Override
        public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
            if (!(obj instanceof CheckType)) {
                return false;
            }
            final CheckType<?> type = obj;
            if (this.index == type.index && this.expectedIndex == type.expectedIndex) {
                if (!checkIndex) {
                    return true;
                }
                if (this.delegate.equals(type.delegate, ignoreRecursionPoints, checkIndex)) {
                    return true;
                }
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.index, this.expectedIndex, this.delegate });
        }
    }
}
