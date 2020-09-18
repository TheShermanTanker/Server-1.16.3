package com.mojang.serialization;

import com.mojang.datafixers.kinds.K1;
import java.util.Objects;
import java.util.function.UnaryOperator;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.Optional;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.kinds.App;

public class DataResult<R> implements App<Mu, R> {
    private final Either<R, PartialResult<R>> result;
    private final Lifecycle lifecycle;
    
    public static <R> DataResult<R> unbox(final App<Mu, R> box) {
        return (DataResult<R>)(DataResult)box;
    }
    
    public static <R> DataResult<R> success(final R result) {
        return DataResult.<R>success(result, Lifecycle.experimental());
    }
    
    public static <R> DataResult<R> error(final String message, final R partialResult) {
        return DataResult.<R>error(message, partialResult, Lifecycle.experimental());
    }
    
    public static <R> DataResult<R> error(final String message) {
        return DataResult.<R>error(message, Lifecycle.experimental());
    }
    
    public static <R> DataResult<R> success(final R result, final Lifecycle experimental) {
        return new DataResult<R>(Either.<R, PartialResult<R>>left(result), experimental);
    }
    
    public static <R> DataResult<R> error(final String message, final R partialResult, final Lifecycle lifecycle) {
        return new DataResult<R>(Either.right(new PartialResult<>(message, (java.util.Optional<Object>)Optional.of(partialResult))), lifecycle);
    }
    
    public static <R> DataResult<R> error(final String message, final Lifecycle lifecycle) {
        return new DataResult<R>(Either.right(new PartialResult<>(message, (java.util.Optional<Object>)Optional.empty())), lifecycle);
    }
    
    public static <K, V> Function<K, DataResult<V>> partialGet(final Function<K, V> partialGet, final Supplier<String> errorPrefix) {
        return (Function<K, DataResult<V>>)(name -> (DataResult)Optional.ofNullable(partialGet.apply(name)).map(DataResult::success).orElseGet(() -> DataResult.error((String)errorPrefix.get() + name)));
    }
    
    private static <R> DataResult<R> create(final Either<R, PartialResult<R>> result, final Lifecycle lifecycle) {
        return new DataResult<R>(result, lifecycle);
    }
    
    private DataResult(final Either<R, PartialResult<R>> result, final Lifecycle lifecycle) {
        this.result = result;
        this.lifecycle = lifecycle;
    }
    
    public Either<R, PartialResult<R>> get() {
        return this.result;
    }
    
    public Optional<R> result() {
        return this.result.left();
    }
    
    public Lifecycle lifecycle() {
        return this.lifecycle;
    }
    
    public Optional<R> resultOrPartial(final Consumer<String> onError) {
        return this.result.map((java.util.function.Function<? super R, ? extends Optional>)Optional::of, (java.util.function.Function<? super PartialResult<R>, ? extends Optional>)(r -> {
            onError.accept(r.message);
            return r.partialResult;
        }));
    }
    
    public R getOrThrow(final boolean allowPartial, final Consumer<String> onError) {
        return this.result.<R>map((java.util.function.Function<? super R, ? extends R>)(l -> l), (java.util.function.Function<? super PartialResult<R>, ? extends R>)(r -> {
            onError.accept(r.message);
            if (allowPartial && r.partialResult.isPresent()) {
                return r.partialResult.get();
            }
            throw new RuntimeException(r.message);
        }));
    }
    
    public Optional<PartialResult<R>> error() {
        return this.result.right();
    }
    
    public <T> DataResult<T> map(final Function<? super R, ? extends T> function) {
        return DataResult.<T>create(this.result.<T, PartialResult<T>>mapBoth(function, (java.util.function.Function<? super PartialResult<R>, ? extends PartialResult<T>>)(r -> new PartialResult(r.message, (Optional<R>)r.partialResult.map(function)))), this.lifecycle);
    }
    
    public DataResult<R> promotePartial(final Consumer<String> onError) {
        return this.result.<DataResult<R>>map((java.util.function.Function<? super R, ? extends DataResult<R>>)(r -> new DataResult(Either.<R, PartialResult<R>>left(r), this.lifecycle)), (java.util.function.Function<? super PartialResult<R>, ? extends DataResult<R>>)(r -> {
            onError.accept(r.message);
            return (DataResult)r.partialResult.map(pr -> new DataResult(Either.<R, PartialResult<R>>left(pr), this.lifecycle)).orElseGet(() -> DataResult.create(Either.right(r), this.lifecycle));
        }));
    }
    
    private static String appendMessages(final String first, final String second) {
        return first + "; " + second;
    }
    
    public <R2> DataResult<R2> flatMap(final Function<? super R, ? extends DataResult<R2>> function) {
        return this.result.<DataResult<R2>>map((java.util.function.Function<? super R, ? extends DataResult<R2>>)(l -> {
            final DataResult<Object> second = (DataResult<Object>)function.apply(l);
            return DataResult.create(second.get(), this.lifecycle.add(second.lifecycle));
        }), (java.util.function.Function<? super PartialResult<R>, ? extends DataResult<R2>>)(r -> (DataResult)r.partialResult.map(value -> {
            final DataResult<Object> second = (DataResult<Object>)function.apply(value);
            return DataResult.create(Either.right(second.get().<PartialResult<R>>map((java.util.function.Function<? super Object, ? extends PartialResult<R>>)(l2 -> new PartialResult(r.message, (Optional<R>)Optional.of(l2))), (java.util.function.Function<? super PartialResult<Object>, ? extends PartialResult<R>>)(r2 -> new PartialResult(appendMessages(r.message, r2.message), r2.partialResult)))), this.lifecycle.add(second.lifecycle));
        }).orElseGet(() -> DataResult.create(Either.right((PartialResult<R>)new PartialResult<>(r.message, (java.util.Optional<R>)Optional.empty())), this.lifecycle))));
    }
    
    public <R2> DataResult<R2> ap(final DataResult<Function<R, R2>> functionResult) {
        return DataResult.<R2>create(this.result.<Either<R2, PartialResult<R2>>>map((java.util.function.Function<? super R, ? extends Either<R2, PartialResult<R2>>>)(arg -> functionResult.result.mapBoth((java.util.function.Function<? super R, ?>)(func -> func.apply(arg)), (java.util.function.Function<? super PartialResult<R>, ?>)(funcError -> new PartialResult(funcError.message, (Optional<R>)funcError.partialResult.map(f -> f.apply(arg)))))), (java.util.function.Function<? super PartialResult<R>, ? extends Either<R2, PartialResult<R2>>>)(argError -> Either.right(functionResult.result.map((java.util.function.Function<? super R, ?>)(func -> new PartialResult(argError.message, (Optional<R>)argError.partialResult.map(func))), (java.util.function.Function<? super PartialResult<R>, ?>)(funcError -> new PartialResult(appendMessages(argError.message, funcError.message), (Optional<R>)argError.partialResult.flatMap(a -> funcError.partialResult.map(f -> f.apply(a))))))))), this.lifecycle.add(functionResult.lifecycle));
    }
    
    public <R2, S> DataResult<S> apply2(final BiFunction<R, R2, S> function, final DataResult<R2> second) {
        return DataResult.<S>unbox(((Applicative<Mu, Mu>)instance()).apply2((java.util.function.BiFunction<R, R2, R>)function, this, second));
    }
    
    public <R2, S> DataResult<S> apply2stable(final BiFunction<R, R2, S> function, final DataResult<R2> second) {
        final Applicative<Mu, Instance.Mu> instance = instance();
        final DataResult<BiFunction<R, R2, S>> f = (DataResult<BiFunction<R, R2, S>>)DataResult.<java.util.function.BiFunction<BiFunction<R, R2, S>, R2, S>>unbox(instance.point((R)function)).setLifecycle(Lifecycle.stable());
        return DataResult.<S>unbox(instance.ap2((App<Mu, java.util.function.BiFunction<Object, Object, R>>)f, (App<Mu, Object>)this, (App<Mu, Object>)second));
    }
    
    public <R2, R3, S> DataResult<S> apply3(final Function3<R, R2, R3, S> function, final DataResult<R2> second, final DataResult<R3> third) {
        return DataResult.<S>unbox(((Applicative<Mu, Mu>)instance()).apply3((Function3<R, R2, R3, R>)function, this, second, third));
    }
    
    public DataResult<R> setPartial(final Supplier<R> partial) {
        return DataResult.<R>create(this.result.<PartialResult<R>>mapRight((java.util.function.Function<? super PartialResult<R>, ? extends PartialResult<R>>)(r -> new PartialResult(r.message, (Optional<R>)Optional.of(partial.get())))), this.lifecycle);
    }
    
    public DataResult<R> setPartial(final R partial) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/mojang/serialization/DataResult.result:Lcom/mojang/datafixers/util/Either;
        //     4: aload_1         /* partial */
        //     5: invokedynamic   BootstrapMethod #13, apply:(Ljava/lang/Object;)Ljava/util/function/Function;
        //    10: invokevirtual   com/mojang/datafixers/util/Either.mapRight:(Ljava/util/function/Function;)Lcom/mojang/datafixers/util/Either;
        //    13: aload_0         /* this */
        //    14: getfield        com/mojang/serialization/DataResult.lifecycle:Lcom/mojang/serialization/Lifecycle;
        //    17: invokestatic    com/mojang/serialization/DataResult.create:(Lcom/mojang/datafixers/util/Either;Lcom/mojang/serialization/Lifecycle;)Lcom/mojang/serialization/DataResult;
        //    20: areturn        
        //    Signature:
        //  (TR;)Lcom/mojang/serialization/DataResult<TR;>;
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  partial  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2694)
        //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2691)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferDynamicCall(TypeAnalysis.java:2241)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1022)
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
    
    public DataResult<R> mapError(final UnaryOperator<String> function) {
        return DataResult.<R>create(this.result.<PartialResult<R>>mapRight((java.util.function.Function<? super PartialResult<R>, ? extends PartialResult<R>>)(r -> new PartialResult((String)function.apply(r.message), r.partialResult))), this.lifecycle);
    }
    
    public DataResult<R> setLifecycle(final Lifecycle lifecycle) {
        return DataResult.<R>create(this.result, lifecycle);
    }
    
    public DataResult<R> addLifecycle(final Lifecycle lifecycle) {
        return DataResult.<R>create(this.result, this.lifecycle.add(lifecycle));
    }
    
    public static Instance instance() {
        return Instance.INSTANCE;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final DataResult<?> that = o;
        return Objects.equals(this.result, that.result);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.result });
    }
    
    public String toString() {
        return new StringBuilder().append("DataResult[").append(this.result).append(']').toString();
    }
    
    public static final class Mu implements K1 {
    }
    
    public static class PartialResult<R> {
        private final String message;
        private final Optional<R> partialResult;
        
        public PartialResult(final String message, final Optional<R> partialResult) {
            this.message = message;
            this.partialResult = partialResult;
        }
        
        public <R2> PartialResult<R2> map(final Function<? super R, ? extends R2> function) {
            return new PartialResult<R2>(this.message, (java.util.Optional<R2>)this.partialResult.map((Function)function));
        }
        
        public <R2> PartialResult<R2> flatMap(final Function<R, PartialResult<R2>> function) {
            if (this.partialResult.isPresent()) {
                final PartialResult<R2> result = (PartialResult<R2>)function.apply(this.partialResult.get());
                return new PartialResult<R2>(appendMessages(this.message, result.message), result.partialResult);
            }
            return new PartialResult<R2>(this.message, (java.util.Optional<R2>)Optional.empty());
        }
        
        public String message() {
            return this.message;
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final PartialResult<?> that = o;
            return Objects.equals(this.message, that.message) && Objects.equals(this.partialResult, that.partialResult);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.message, this.partialResult });
        }
        
        public String toString() {
            return "DynamicException[" + this.message + ' ' + this.partialResult + ']';
        }
    }
    
    public enum Instance implements Applicative<DataResult.Mu, Mu> {
        INSTANCE;
        
        public <T, R> App<DataResult.Mu, R> map(final Function<? super T, ? extends R> func, final App<DataResult.Mu, T> ts) {
            return DataResult.<T>unbox(ts).map((java.util.function.Function<? super T, ?>)func);
        }
        
        public <A> App<DataResult.Mu, A> point(final A a) {
            return DataResult.<A>success(a);
        }
        
        public <A, R> Function<App<DataResult.Mu, A>, App<DataResult.Mu, R>> lift1(final App<DataResult.Mu, Function<A, R>> function) {
            return (Function<App<DataResult.Mu, A>, App<DataResult.Mu, R>>)(fa -> this.ap((App<DataResult.Mu, java.util.function.Function<Object, Object>>)function, fa));
        }
        
        public <A, R> App<DataResult.Mu, R> ap(final App<DataResult.Mu, Function<A, R>> func, final App<DataResult.Mu, A> arg) {
            return DataResult.<A>unbox(arg).ap(DataResult.unbox((App<DataResult.Mu, java.util.function.Function<A, R2>>)func));
        }
        
        public <A, B, R> App<DataResult.Mu, R> ap2(final App<DataResult.Mu, BiFunction<A, B, R>> func, final App<DataResult.Mu, A> a, final App<DataResult.Mu, B> b) {
            final DataResult<BiFunction<A, B, R>> fr = DataResult.<BiFunction<A, B, R>>unbox(func);
            final DataResult<A> ra = DataResult.<A>unbox(a);
            final DataResult<B> rb = DataResult.<B>unbox(b);
            if (((DataResult<Object>)fr).result.left().isPresent() && ((DataResult<Object>)ra).result.left().isPresent() && ((DataResult<Object>)rb).result.left().isPresent()) {
                return new DataResult<R>(Either.left(((BiFunction)((DataResult<Object>)fr).result.left().get()).apply(((DataResult<Object>)ra).result.left().get(), ((DataResult<Object>)rb).result.left().get())), ((DataResult<Object>)fr).lifecycle.add(((DataResult<Object>)ra).lifecycle).add(((DataResult<Object>)rb).lifecycle), null);
            }
            return super.<A, B, R>ap2(func, a, b);
        }
        
        public <T1, T2, T3, R> App<DataResult.Mu, R> ap3(final App<DataResult.Mu, Function3<T1, T2, T3, R>> func, final App<DataResult.Mu, T1> t1, final App<DataResult.Mu, T2> t2, final App<DataResult.Mu, T3> t3) {
            final DataResult<Function3<T1, T2, T3, R>> fr = DataResult.<Function3<T1, T2, T3, R>>unbox(func);
            final DataResult<T1> dr1 = DataResult.<T1>unbox(t1);
            final DataResult<T2> dr2 = DataResult.<T2>unbox(t2);
            final DataResult<T3> dr3 = DataResult.<T3>unbox(t3);
            if (((DataResult<Object>)fr).result.left().isPresent() && ((DataResult<Object>)dr1).result.left().isPresent() && ((DataResult<Object>)dr2).result.left().isPresent() && ((DataResult<Object>)dr3).result.left().isPresent()) {
                return new DataResult<R>(Either.left(((Function3)((DataResult<Object>)fr).result.left().get()).apply(((DataResult<Object>)dr1).result.left().get(), ((DataResult<Object>)dr2).result.left().get(), ((DataResult<Object>)dr3).result.left().get())), ((DataResult<Object>)fr).lifecycle.add(((DataResult<Object>)dr1).lifecycle).add(((DataResult<Object>)dr2).lifecycle).add(((DataResult<Object>)dr3).lifecycle), null);
            }
            return super.<T1, T2, T3, R>ap3(func, t1, t2, t3);
        }
        
        public static final class Mu implements Applicative.Mu {
        }
    }
}
