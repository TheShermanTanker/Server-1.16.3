package com.mojang.datafixers;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.IdF;
import com.mojang.datafixers.kinds.Functor;
import com.mojang.datafixers.optics.Procompose;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.optics.Wander;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Monoidal;
import com.mojang.datafixers.optics.profunctors.Mapping;
import com.mojang.datafixers.optics.profunctors.MonoidProfunctor;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.kinds.Representable;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import javax.annotation.Nonnull;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import java.util.function.Function;

public interface FunctionType<A, B> extends Function<A, B>, App2<Mu, A, B>, App<ReaderMu<A>, B> {
    default <A, B> FunctionType<A, B> create(final Function<? super A, ? extends B> function) {
        return function::apply;
    }
    
    default <A, B> Function<A, B> unbox(final App2<Mu, A, B> box) {
        return (Function<A, B>)(FunctionType)box;
    }
    
    default <A, B> Function<A, B> unbox(final App<ReaderMu<A>, B> box) {
        return (Function<A, B>)(FunctionType)box;
    }
    
    @Nonnull
    B apply(@Nonnull final A object);
    
    public static final class Mu implements K2 {
    }
    
    public static final class ReaderMu<A> implements K1 {
    }
    
    public static final class ReaderInstance<R> implements Representable<ReaderMu<R>, R, Mu<R>> {
        public <T, R2> App<ReaderMu<R>, R2> map(final Function<? super T, ? extends R2> func, final App<ReaderMu<R>, T> ts) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)func.compose((Function)FunctionType.<R, T>unbox(ts)));
        }
        
        public <B> App<ReaderMu<R>, B> to(final App<ReaderMu<R>, B> input) {
            return input;
        }
        
        public <B> App<ReaderMu<R>, B> from(final App<ReaderMu<R>, B> input) {
            return input;
        }
        
        public static final class Mu<A> implements Representable.Mu {
        }
    }
    
    public enum Instance implements TraversalP<FunctionType.Mu, Mu>, MonoidProfunctor<FunctionType.Mu, Mu>, Mapping<FunctionType.Mu, Mu>, Monoidal<FunctionType.Mu, Mu>, App<Mu, FunctionType.Mu> {
        INSTANCE;
        
        public <A, B, C, D> FunctionType<App2<FunctionType.Mu, A, B>, App2<FunctionType.Mu, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
            return (FunctionType<App2<FunctionType.Mu, A, B>, App2<FunctionType.Mu, C, D>>)(f -> FunctionType.create((java.util.function.Function<? super Object, ?>)h.compose((Function)Optics.getFunc(f)).compose((Function)g)));
        }
        
        public <A, B, C> App2<FunctionType.Mu, Pair<A, C>, Pair<B, C>> first(final App2<FunctionType.Mu, A, B> input) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)(p -> Pair.of(Optics.getFunc((App2<FunctionType.Mu, Object, Object>)input).apply(p.getFirst()), p.getSecond())));
        }
        
        public <A, B, C> App2<FunctionType.Mu, Pair<C, A>, Pair<C, B>> second(final App2<FunctionType.Mu, A, B> input) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)(p -> Pair.of(p.getFirst(), Optics.getFunc((App2<FunctionType.Mu, Object, Object>)input).apply(p.getSecond()))));
        }
        
        public <S, T, A, B> App2<FunctionType.Mu, S, T> wander(final Wander<S, T, A, B> wander, final App2<FunctionType.Mu, A, B> input) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_2         /* input */
            //     2: invokedynamic   BootstrapMethod #3, apply:(Lcom/mojang/datafixers/optics/Wander;Lcom/mojang/datafixers/kinds/App2;)Ljava/util/function/Function;
            //     7: invokestatic    com/mojang/datafixers/FunctionType.create:(Ljava/util/function/Function;)Lcom/mojang/datafixers/FunctionType;
            //    10: areturn        
            //    Signature:
            //  <S:Ljava/lang/Object;T:Ljava/lang/Object;A:Ljava/lang/Object;B:Ljava/lang/Object;>(Lcom/mojang/datafixers/optics/Wander<TS;TT;TA;TB;>;Lcom/mojang/datafixers/kinds/App2<Lcom/mojang/datafixers/FunctionType$Mu;TA;TB;>;)Lcom/mojang/datafixers/kinds/App2<Lcom/mojang/datafixers/FunctionType$Mu;TS;TT;>;
            //    MethodParameters:
            //  Name    Flags  
            //  ------  -----
            //  wander  
            //  input   
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
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.isCastRequired(AstMethodBodyBuilder.java:1357)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1318)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:715)
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
        
        public <A, B, C> App2<FunctionType.Mu, Either<A, C>, Either<B, C>> left(final App2<FunctionType.Mu, A, B> input) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)(either -> either.mapLeft(Optics.getFunc(input))));
        }
        
        public <A, B, C> App2<FunctionType.Mu, Either<C, A>, Either<C, B>> right(final App2<FunctionType.Mu, A, B> input) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)(either -> either.mapRight(Optics.getFunc(input))));
        }
        
        public <A, B, C, D> App2<FunctionType.Mu, Pair<A, C>, Pair<B, D>> par(final App2<FunctionType.Mu, A, B> first, final Supplier<App2<FunctionType.Mu, C, D>> second) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)(pair -> Pair.of(Optics.getFunc((App2<FunctionType.Mu, Object, Object>)first).apply(pair.getFirst()), Optics.getFunc((App2<FunctionType.Mu, Object, Object>)second.get()).apply(pair.getSecond()))));
        }
        
        public App2<FunctionType.Mu, Void, Void> empty() {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)Function.identity());
        }
        
        public <A, B> App2<FunctionType.Mu, A, B> zero(final App2<FunctionType.Mu, A, B> func) {
            return func;
        }
        
        public <A, B> App2<FunctionType.Mu, A, B> plus(final App2<Procompose.Mu<FunctionType.Mu, FunctionType.Mu>, A, B> input) {
            final Procompose<FunctionType.Mu, FunctionType.Mu, A, B, ?> cmp = Procompose.<FunctionType.Mu, FunctionType.Mu, A, B>unbox(input);
            return this.cap(cmp);
        }
        
        private <A, B, C> App2<FunctionType.Mu, A, B> cap(final Procompose<FunctionType.Mu, FunctionType.Mu, A, B, C> cmp) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)Optics.<C, B>getFunc(cmp.second()).compose((Function)Optics.getFunc((App2<FunctionType.Mu, Object, Object>)cmp.first().get())));
        }
        
        public <A, B, F extends K1> App2<FunctionType.Mu, App<F, A>, App<F, B>> mapping(final Functor<F, ?> functor, final App2<FunctionType.Mu, A, B> input) {
            return FunctionType.create((java.util.function.Function<? super Object, ?>)(fa -> functor.map(Optics.getFunc(input), fa)));
        }
        
        public static final class Mu implements TraversalP.Mu, MonoidProfunctor.Mu, Mapping.Mu, Monoidal.Mu {
            public static final TypeToken<Mu> TYPE_TOKEN;
            
            static {
                TYPE_TOKEN = new TypeToken<Mu>() {};
            }
        }
    }
}
