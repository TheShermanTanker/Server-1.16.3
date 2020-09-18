package com.google.common.collect;

import java.util.function.Predicate;
import java.util.function.Function;
import java.util.stream.IntStream;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CollectSpliterators {
    private CollectSpliterators() {
    }
    
    static <T> Spliterator<T> indexed(final int size, final int extraCharacteristics, final IntFunction<T> function) {
        return CollectSpliterators.<T>indexed(size, extraCharacteristics, function, null);
    }
    
    static <T> Spliterator<T> indexed(final int size, final int extraCharacteristics, final IntFunction<T> function, final Comparator<? super T> comparator) {
        if (comparator != null) {
            Preconditions.checkArgument((extraCharacteristics & 0x4) != 0x0);
        }
        class 1WithCharacteristics implements Spliterator<T> {
            private final Spliterator<T> delegate = IntStream.range(0, size).mapToObj((IntFunction)function).spliterator();
            final /* synthetic */ Comparator val$comparator;
            
            1WithCharacteristics(final Spliterator delegate, final Spliterator<T> comparator) {
                this.val$comparator = (Comparator)comparator;
            }
            
            public boolean tryAdvance(final Consumer<? super T> action) {
                return this.delegate.tryAdvance((Consumer)action);
            }
            
            public void forEachRemaining(final Consumer<? super T> action) {
                this.delegate.forEachRemaining((Consumer)action);
            }
            
            @Nullable
            public Spliterator<T> trySplit() {
                final Spliterator<T> split = (Spliterator<T>)this.delegate.trySplit();
                return (Spliterator<T>)((split == null) ? null : new 1WithCharacteristics(split, extraCharacteristics, this.val$comparator));
            }
            
            public long estimateSize() {
                return this.delegate.estimateSize();
            }
            
            public int characteristics() {
                return this.delegate.characteristics() | extraCharacteristics;
            }
            
            public Comparator<? super T> getComparator() {
                if (this.hasCharacteristics(4)) {
                    return this.val$comparator;
                }
                throw new IllegalStateException();
            }
        }
        return (Spliterator<T>)new 1WithCharacteristics(comparator);
    }
    
    static <F, T> Spliterator<T> map(final Spliterator<F> fromSpliterator, final Function<? super F, ? extends T> function) {
        Preconditions.<Spliterator<F>>checkNotNull(fromSpliterator);
        Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
        return (Spliterator<T>)new Spliterator<T>() {
            public boolean tryAdvance(final Consumer<? super T> action) {
                return fromSpliterator.tryAdvance(fromElement -> action.accept(function.apply(fromElement)));
            }
            
            public void forEachRemaining(final Consumer<? super T> action) {
                fromSpliterator.forEachRemaining(fromElement -> action.accept(function.apply(fromElement)));
            }
            
            public Spliterator<T> trySplit() {
                final Spliterator<F> fromSplit = (Spliterator<F>)fromSpliterator.trySplit();
                return (Spliterator<T>)((fromSplit != null) ? CollectSpliterators.map((java.util.Spliterator<Object>)fromSplit, (java.util.function.Function<? super Object, ?>)function) : null);
            }
            
            public long estimateSize() {
                return fromSpliterator.estimateSize();
            }
            
            public int characteristics() {
                return fromSpliterator.characteristics() & 0xFFFFFEFA;
            }
        };
    }
    
    static <T> Spliterator<T> filter(final Spliterator<T> fromSpliterator, final Predicate<? super T> predicate) {
        Preconditions.<Spliterator<T>>checkNotNull(fromSpliterator);
        Preconditions.<Predicate<? super T>>checkNotNull(predicate);
        class 1Splitr implements Spliterator<T>, Consumer<T> {
            T holder;
            
            1Splitr() {
                this.holder = null;
            }
            
            public void accept(final T t) {
                this.holder = t;
            }
            
            public boolean tryAdvance(final Consumer<? super T> action) {
                while (fromSpliterator.tryAdvance((Consumer)this)) {
                    try {
                        if (predicate.test(this.holder)) {
                            action.accept(this.holder);
                            return true;
                        }
                        continue;
                    }
                    finally {
                        this.holder = null;
                    }
                }
                return false;
            }
            
            public Spliterator<T> trySplit() {
                final Spliterator<T> fromSplit = (Spliterator<T>)fromSpliterator.trySplit();
                return (Spliterator<T>)((fromSplit == null) ? null : CollectSpliterators.filter((java.util.Spliterator<Object>)fromSplit, (java.util.function.Predicate<? super Object>)predicate));
            }
            
            public long estimateSize() {
                return fromSpliterator.estimateSize() / 2L;
            }
            
            public Comparator<? super T> getComparator() {
                return fromSpliterator.getComparator();
            }
            
            public int characteristics() {
                return fromSpliterator.characteristics() & 0x115;
            }
        }
        return (Spliterator<T>)new 1Splitr();
    }
    
    static <F, T> Spliterator<T> flatMap(final Spliterator<F> fromSpliterator, final Function<? super F, Spliterator<T>> function, final int topCharacteristics, final long topSize) {
        Preconditions.checkArgument((topCharacteristics & 0x4000) == 0x0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 0x4) == 0x0, "flatMap does not support SORTED characteristic");
        Preconditions.<Spliterator<F>>checkNotNull(fromSpliterator);
        Preconditions.<Function<? super F, Spliterator<T>>>checkNotNull(function);
        class 1FlatMapSpliterator implements Spliterator<T> {
            @Nullable
            Spliterator<T> prefix;
            final Spliterator<F> from;
            final int characteristics;
            long estimatedSize;
            final /* synthetic */ Function val$function;
            
            1FlatMapSpliterator(final Spliterator prefix, final Spliterator<T> from, final Spliterator<F> characteristics, final int estimatedSize, final long function) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/CollectSpliterators$1FlatMapSpliterator.val$function:Ljava/util/function/Function;
                //     6: aload_0         /* this */
                //     7: invokespecial   java/lang/Object.<init>:()V
                //    10: aload_0         /* this */
                //    11: aload_1         /* prefix */
                //    12: putfield        com/google/common/collect/CollectSpliterators$1FlatMapSpliterator.prefix:Ljava/util/Spliterator;
                //    15: aload_0         /* this */
                //    16: aload_2         /* from */
                //    17: putfield        com/google/common/collect/CollectSpliterators$1FlatMapSpliterator.from:Ljava/util/Spliterator;
                //    20: aload_0         /* this */
                //    21: iload_3         /* characteristics */
                //    22: putfield        com/google/common/collect/CollectSpliterators$1FlatMapSpliterator.characteristics:I
                //    25: aload_0         /* this */
                //    26: lload           estimatedSize
                //    28: putfield        com/google/common/collect/CollectSpliterators$1FlatMapSpliterator.estimatedSize:J
                //    31: return         
                //    Signature:
                //  (Ljava/util/Spliterator;Ljava/util/Spliterator<TT;>;Ljava/util/Spliterator<TF;>;IJ)V [from metadata: (Ljava/util/Spliterator<TT;>;Ljava/util/Spliterator<TF;>;IJ)V]
                //  
                //    MethodParameters:
                //  Name             Flags  
                //  ---------------  -----
                //  prefix           
                //  from             
                //  characteristics  
                //  estimatedSize    
                //  function         
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
                //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
                //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:670)
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
            
            public boolean tryAdvance(final Consumer<? super T> action) {
                while (this.prefix == null || !this.prefix.tryAdvance((Consumer)action)) {
                    this.prefix = null;
                    if (!this.from.tryAdvance(fromElement -> this.prefix = (Spliterator<T>)function.apply(fromElement))) {
                        return false;
                    }
                }
                if (this.estimatedSize != Long.MAX_VALUE) {
                    --this.estimatedSize;
                }
                return true;
            }
            
            public void forEachRemaining(final Consumer<? super T> action) {
                if (this.prefix != null) {
                    this.prefix.forEachRemaining((Consumer)action);
                    this.prefix = null;
                }
                this.from.forEachRemaining(fromElement -> ((Spliterator)function.apply(fromElement)).forEachRemaining(action));
                this.estimatedSize = 0L;
            }
            
            public Spliterator<T> trySplit() {
                final Spliterator<F> fromSplit = (Spliterator<F>)this.from.trySplit();
                if (fromSplit != null) {
                    final int splitCharacteristics = this.characteristics & 0xFFFFFFBF;
                    long estSplitSize = this.estimateSize();
                    if (estSplitSize < Long.MAX_VALUE) {
                        estSplitSize /= 2L;
                        this.estimatedSize -= estSplitSize;
                    }
                    final Spliterator<T> result = (Spliterator<T>)new 1FlatMapSpliterator(this.prefix, fromSplit, splitCharacteristics, estSplitSize, this.val$function);
                    this.prefix = null;
                    return result;
                }
                if (this.prefix != null) {
                    final Spliterator<T> result2 = this.prefix;
                    this.prefix = null;
                    return result2;
                }
                return null;
            }
            
            public long estimateSize() {
                if (this.prefix != null) {
                    this.estimatedSize = Math.max(this.estimatedSize, this.prefix.estimateSize());
                }
                return Math.max(this.estimatedSize, 0L);
            }
            
            public int characteristics() {
                return this.characteristics;
            }
        }
        return (Spliterator<T>)new 1FlatMapSpliterator(null, fromSpliterator, topCharacteristics, topSize, function);
    }
}
