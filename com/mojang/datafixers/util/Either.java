package com.mojang.datafixers.util;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.CocartesianLike;
import com.mojang.datafixers.kinds.Traversable;
import com.mojang.datafixers.kinds.Applicative;
import java.util.Objects;
import com.mojang.datafixers.kinds.K1;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;

public abstract class Either<L, R> implements App<Mu<R>, L> {
    public static <L, R> Either<L, R> unbox(final App<Mu<R>, L> box) {
        return (Either<L, R>)(Either)box;
    }
    
    private Either() {
    }
    
    public abstract <C, D> Either<C, D> mapBoth(final Function<? super L, ? extends C> function1, final Function<? super R, ? extends D> function2);
    
    public abstract <T> T map(final Function<? super L, ? extends T> function1, final Function<? super R, ? extends T> function2);
    
    public abstract Either<L, R> ifLeft(final Consumer<? super L> consumer);
    
    public abstract Either<L, R> ifRight(final Consumer<? super R> consumer);
    
    public abstract Optional<L> left();
    
    public abstract Optional<R> right();
    
    public <T> Either<T, R> mapLeft(final Function<? super L, ? extends T> l) {
        return this.<Either<T, R>>map((java.util.function.Function<? super L, ? extends Either<T, R>>)(t -> Either.left(l.apply(t))), (java.util.function.Function<? super R, ? extends Either<T, R>>)Either::right);
    }
    
    public <T> Either<L, T> mapRight(final Function<? super R, ? extends T> l) {
        return this.<Either<L, T>>map((java.util.function.Function<? super L, ? extends Either<L, T>>)Either::left, (java.util.function.Function<? super R, ? extends Either<L, T>>)(t -> Either.right(l.apply(t))));
    }
    
    public static <L, R> Either<L, R> left(final L value) {
        return new Left<L, R>(value);
    }
    
    public static <L, R> Either<L, R> right(final R value) {
        return new Right<L, R>(value);
    }
    
    public L orThrow() {
        return this.<L>map((java.util.function.Function<? super L, ? extends L>)(l -> l), (java.util.function.Function<? super R, ? extends L>)(r -> {
            if (r instanceof Throwable) {
                throw new RuntimeException((Throwable)r);
            }
            throw new RuntimeException(r.toString());
        }));
    }
    
    public Either<R, L> swap() {
        return this.map((java.util.function.Function<? super L, ? extends Either>)Either::right, (java.util.function.Function<? super R, ? extends Either>)Either::left);
    }
    
    public <L2> Either<L2, R> flatMap(final Function<L, Either<L2, R>> function) {
        return this.<Either<L2, R>>map((java.util.function.Function<? super L, ? extends Either<L2, R>>)function, (java.util.function.Function<? super R, ? extends Either<L2, R>>)Either::right);
    }
    
    public static final class Mu<R> implements K1 {
    }
    
    private static final class Left<L, R> extends Either<L, R> {
        private final L value;
        
        public Left(final L value) {
            super(null);
            this.value = value;
        }
        
        @Override
        public <C, D> Either<C, D> mapBoth(final Function<? super L, ? extends C> f1, final Function<? super R, ? extends D> f2) {
            return new Left<C, D>((C)f1.apply(this.value));
        }
        
        @Override
        public <T> T map(final Function<? super L, ? extends T> l, final Function<? super R, ? extends T> r) {
            return (T)l.apply(this.value);
        }
        
        @Override
        public Either<L, R> ifLeft(final Consumer<? super L> consumer) {
            consumer.accept(this.value);
            return this;
        }
        
        @Override
        public Either<L, R> ifRight(final Consumer<? super R> consumer) {
            return this;
        }
        
        @Override
        public Optional<L> left() {
            return (Optional<L>)Optional.of(this.value);
        }
        
        @Override
        public Optional<R> right() {
            return (Optional<R>)Optional.empty();
        }
        
        public String toString() {
            return new StringBuilder().append("Left[").append(this.value).append("]").toString();
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Left<?, ?> left = o;
            return Objects.equals(this.value, left.value);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.value });
        }
    }
    
    private static final class Right<L, R> extends Either<L, R> {
        private final R value;
        
        public Right(final R value) {
            super(null);
            this.value = value;
        }
        
        @Override
        public <C, D> Either<C, D> mapBoth(final Function<? super L, ? extends C> f1, final Function<? super R, ? extends D> f2) {
            return new Right<C, D>((D)f2.apply(this.value));
        }
        
        @Override
        public <T> T map(final Function<? super L, ? extends T> l, final Function<? super R, ? extends T> r) {
            return (T)r.apply(this.value);
        }
        
        @Override
        public Either<L, R> ifLeft(final Consumer<? super L> consumer) {
            return this;
        }
        
        @Override
        public Either<L, R> ifRight(final Consumer<? super R> consumer) {
            consumer.accept(this.value);
            return this;
        }
        
        @Override
        public Optional<L> left() {
            return (Optional<L>)Optional.empty();
        }
        
        @Override
        public Optional<R> right() {
            return (Optional<R>)Optional.of(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append("Right[").append(this.value).append("]").toString();
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Right<?, ?> right = o;
            return Objects.equals(this.value, right.value);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.value });
        }
    }
    
    public static final class Instance<R2> implements Applicative<Either.Mu<R2>, Mu<R2>>, Traversable<Either.Mu<R2>, Mu<R2>>, CocartesianLike<Either.Mu<R2>, R2, Mu<R2>> {
        public <T, R> App<Either.Mu<R2>, R> map(final Function<? super T, ? extends R> func, final App<Either.Mu<R2>, T> ts) {
            return Either.<T, R2>unbox(ts).mapLeft((java.util.function.Function<? super T, ?>)func);
        }
        
        public <A> App<Either.Mu<R2>, A> point(final A a) {
            return Either.left(a);
        }
        
        public <A, R> Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>> lift1(final App<Either.Mu<R2>, Function<A, R>> function) {
            return (Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>>)(a -> Either.unbox((App<Either.Mu<Object>, Object>)function).flatMap((java.util.function.Function<Object, Either<Object, Object>>)(f -> Either.unbox((App<Either.Mu<Object>, Object>)a).mapLeft((java.util.function.Function<? super Object, ?>)f))));
        }
        
        public <A, B, R> BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>> lift2(final App<Either.Mu<R2>, BiFunction<A, B, R>> function) {
            return (BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>>)((a, b) -> Either.unbox((App<Either.Mu<Object>, Object>)function).flatMap((java.util.function.Function<Object, Either<Object, Object>>)(f -> {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: invokestatic    com/mojang/datafixers/util/Either.unbox:(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/util/Either;
                //     4: aload_1         /* b */
                //     5: aload_2         /* f */
                //     6: invokedynamic   BootstrapMethod #6, apply:(Lcom/mojang/datafixers/kinds/App;Ljava/util/function/BiFunction;)Ljava/util/function/Function;
                //    11: invokevirtual   com/mojang/datafixers/util/Either.flatMap:(Ljava/util/function/Function;)Lcom/mojang/datafixers/util/Either;
                //    14: areturn        
                //    MethodParameters:
                //  Name  Flags  
                //  ----  -----
                //  a     
                //  b     
                //  f     
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
                //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
                //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
                //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:715)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3216)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2617)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
            })));
        }
        
        public <F extends K1, A, B> App<F, App<Either.Mu<R2>, B>> traverse(final Applicative<F, ?> applicative, final Function<A, App<F, B>> function, final App<Either.Mu<R2>, A> input) {
            return Either.<A, R2>unbox(input).<App<F, App<Either.Mu<R2>, B>>>map((java.util.function.Function<? super A, ? extends App<F, App<Either.Mu<R2>, B>>>)(l -> {
                final App<F, Object> b = (App<F, Object>)function.apply(l);
                return applicative.ap(Either::left, b);
            }), (java.util.function.Function<? super R2, ? extends App<F, App<Either.Mu<R2>, B>>>)(r -> applicative.<Either<Object, Object>>point(Either.right(r))));
        }
        
        public <A> App<Either.Mu<R2>, A> to(final App<Either.Mu<R2>, A> input) {
            return input;
        }
        
        public <A> App<Either.Mu<R2>, A> from(final App<Either.Mu<R2>, A> input) {
            return input;
        }
        
        public static final class Mu<R2> implements Applicative.Mu, Traversable.Mu, CocartesianLike.Mu {
        }
        
        public static final class Mu<R2> implements Applicative.Mu, Traversable.Mu, CocartesianLike.Mu {
        }
    }
    
    public static final class Instance<R2> implements Applicative<Either.Mu<R2>, Mu<R2>>, Traversable<Either.Mu<R2>, Mu<R2>>, CocartesianLike<Either.Mu<R2>, R2, Mu<R2>> {
        public <T, R> App<Either.Mu<R2>, R> map(final Function<? super T, ? extends R> func, final App<Either.Mu<R2>, T> ts) {
            return Either.<T, R2>unbox(ts).mapLeft((java.util.function.Function<? super T, ?>)func);
        }
        
        public <A> App<Either.Mu<R2>, A> point(final A a) {
            return Either.left(a);
        }
        
        public <A, R> Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>> lift1(final App<Either.Mu<R2>, Function<A, R>> function) {
            return (Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>>)(a -> Either.unbox((App<Either.Mu<Object>, Object>)function).flatMap((java.util.function.Function<Object, Either<Object, Object>>)(f -> Either.unbox((App<Either.Mu<Object>, Object>)a).mapLeft((java.util.function.Function<? super Object, ?>)f))));
        }
        
        public <A, B, R> BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>> lift2(final App<Either.Mu<R2>, BiFunction<A, B, R>> function) {
            return (BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>>)((a, b) -> Either.unbox((App<Either.Mu<Object>, Object>)function).flatMap((java.util.function.Function<Object, Either<Object, Object>>)(f -> {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: invokestatic    com/mojang/datafixers/util/Either.unbox:(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/util/Either;
                //     4: aload_1         /* b */
                //     5: aload_2         /* f */
                //     6: invokedynamic   BootstrapMethod #6, apply:(Lcom/mojang/datafixers/kinds/App;Ljava/util/function/BiFunction;)Ljava/util/function/Function;
                //    11: invokevirtual   com/mojang/datafixers/util/Either.flatMap:(Ljava/util/function/Function;)Lcom/mojang/datafixers/util/Either;
                //    14: areturn        
                //    MethodParameters:
                //  Name  Flags  
                //  ----  -----
                //  a     
                //  b     
                //  f     
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
                //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
                //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
                //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:715)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3216)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2617)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
            })));
        }
        
        public <F extends K1, A, B> App<F, App<Either.Mu<R2>, B>> traverse(final Applicative<F, ?> applicative, final Function<A, App<F, B>> function, final App<Either.Mu<R2>, A> input) {
            return Either.<A, R2>unbox(input).<App<F, App<Either.Mu<R2>, B>>>map((java.util.function.Function<? super A, ? extends App<F, App<Either.Mu<R2>, B>>>)(l -> {
                final App<F, Object> b = (App<F, Object>)function.apply(l);
                return applicative.ap(Either::left, b);
            }), (java.util.function.Function<? super R2, ? extends App<F, App<Either.Mu<R2>, B>>>)(r -> applicative.<Either<Object, Object>>point(Either.right(r))));
        }
        
        public <A> App<Either.Mu<R2>, A> to(final App<Either.Mu<R2>, A> input) {
            return input;
        }
        
        public <A> App<Either.Mu<R2>, A> from(final App<Either.Mu<R2>, A> input) {
            return input;
        }
        
        public static final class Mu<R2> implements Applicative.Mu, Traversable.Mu, CocartesianLike.Mu {
        }
    }
}
