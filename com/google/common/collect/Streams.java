package com.google.common.collect;

import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Deque;
import java.util.ArrayDeque;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.OptionalInt;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.function.Function;
import com.google.common.math.LongMath;
import java.util.Spliterator;
import com.google.common.base.Optional;
import java.util.Spliterators;
import java.util.Iterator;
import java.util.stream.StreamSupport;
import java.util.Collection;
import java.util.stream.Stream;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class Streams {
    public static <T> Stream<T> stream(final Iterable<T> iterable) {
        return (Stream<T>)((iterable instanceof Collection) ? ((Collection)iterable).stream() : StreamSupport.stream(iterable.spliterator(), false));
    }
    
    @Deprecated
    public static <T> Stream<T> stream(final Collection<T> collection) {
        return (Stream<T>)collection.stream();
    }
    
    public static <T> Stream<T> stream(final Iterator<T> iterator) {
        return (Stream<T>)StreamSupport.stream(Spliterators.spliteratorUnknownSize((Iterator)iterator, 0), false);
    }
    
    public static <T> Stream<T> stream(final Optional<T> optional) {
        return (Stream<T>)(optional.isPresent() ? Stream.of(optional.get()) : Stream.of(new Object[0]));
    }
    
    public static <T> Stream<T> stream(final java.util.Optional<T> optional) {
        return (Stream<T>)(optional.isPresent() ? Stream.of(optional.get()) : Stream.of(new Object[0]));
    }
    
    @SafeVarargs
    public static <T> Stream<T> concat(final Stream<? extends T>... streams) {
        boolean isParallel = false;
        int characteristics = 336;
        long estimatedSize = 0L;
        final ImmutableList.Builder<Spliterator<? extends T>> splitrsBuilder = new ImmutableList.Builder<Spliterator<? extends T>>(streams.length);
        for (final Stream<? extends T> stream : streams) {
            isParallel |= stream.isParallel();
            final Spliterator<? extends T> splitr = stream.spliterator();
            splitrsBuilder.add(splitr);
            characteristics &= splitr.characteristics();
            estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
        }
        return (Stream<T>)StreamSupport.stream((Spliterator)CollectSpliterators.<Spliterator<? extends T>, Object>flatMap(splitrsBuilder.build().spliterator(), (java.util.function.Function<? super Spliterator<? extends T>, java.util.Spliterator<Object>>)(splitr -> splitr), characteristics, estimatedSize), isParallel);
    }
    
    public static IntStream concat(final IntStream... streams) {
        return Stream.of((Object[])streams).flatMapToInt(stream -> stream);
    }
    
    public static LongStream concat(final LongStream... streams) {
        return Stream.of((Object[])streams).flatMapToLong(stream -> stream);
    }
    
    public static DoubleStream concat(final DoubleStream... streams) {
        return Stream.of((Object[])streams).flatMapToDouble(stream -> stream);
    }
    
    public static IntStream stream(final OptionalInt optional) {
        return optional.isPresent() ? IntStream.of(optional.getAsInt()) : IntStream.empty();
    }
    
    public static LongStream stream(final OptionalLong optional) {
        return optional.isPresent() ? LongStream.of(optional.getAsLong()) : LongStream.empty();
    }
    
    public static DoubleStream stream(final OptionalDouble optional) {
        return optional.isPresent() ? DoubleStream.of(optional.getAsDouble()) : DoubleStream.empty();
    }
    
    public static <T> java.util.Optional<T> findLast(final Stream<T> stream) {
        class 1OptionalState<T> {
            boolean set;
            T value;
            
            1OptionalState() {
                this.set = false;
                this.value = null;
            }
            
            void set(@Nullable final T value) {
                this.set = true;
                this.value = value;
            }
            
            T get() {
                Preconditions.checkState(this.set);
                return this.value;
            }
        }
        final 1OptionalState<T> state = new 1OptionalState<T>();
        final Deque<Spliterator<T>> splits = (Deque<Spliterator<T>>)new ArrayDeque();
        splits.addLast(stream.spliterator());
        while (!splits.isEmpty()) {
            Spliterator<T> spliterator = (Spliterator<T>)splits.removeLast();
            if (spliterator.getExactSizeIfKnown() == 0L) {
                continue;
            }
            if (spliterator.hasCharacteristics(16384)) {
                while (true) {
                    final Spliterator<T> prefix = (Spliterator<T>)spliterator.trySplit();
                    if (prefix == null) {
                        break;
                    }
                    if (prefix.getExactSizeIfKnown() == 0L) {
                        break;
                    }
                    if (spliterator.getExactSizeIfKnown() == 0L) {
                        spliterator = prefix;
                        break;
                    }
                }
                spliterator.forEachRemaining(state::set);
                return (java.util.Optional<T>)java.util.Optional.of(state.get());
            }
            final Spliterator<T> prefix = (Spliterator<T>)spliterator.trySplit();
            if (prefix == null || prefix.getExactSizeIfKnown() == 0L) {
                spliterator.forEachRemaining(state::set);
                if (state.set) {
                    return (java.util.Optional<T>)java.util.Optional.of(state.get());
                }
                continue;
            }
            else {
                splits.addLast(prefix);
                splits.addLast(spliterator);
            }
        }
        return (java.util.Optional<T>)java.util.Optional.empty();
    }
    
    public static OptionalInt findLast(final IntStream stream) {
        final java.util.Optional<Integer> boxedLast = Streams.<Integer>findLast((java.util.stream.Stream<Integer>)stream.boxed());
        return boxedLast.isPresent() ? OptionalInt.of((int)boxedLast.get()) : OptionalInt.empty();
    }
    
    public static OptionalLong findLast(final LongStream stream) {
        final java.util.Optional<Long> boxedLast = Streams.<Long>findLast((java.util.stream.Stream<Long>)stream.boxed());
        return boxedLast.isPresent() ? OptionalLong.of((long)boxedLast.get()) : OptionalLong.empty();
    }
    
    public static OptionalDouble findLast(final DoubleStream stream) {
        final java.util.Optional<Double> boxedLast = Streams.<Double>findLast((java.util.stream.Stream<Double>)stream.boxed());
        return boxedLast.isPresent() ? OptionalDouble.of((double)boxedLast.get()) : OptionalDouble.empty();
    }
    
    public static <A, B, R> Stream<R> zip(final Stream<A> streamA, final Stream<B> streamB, final BiFunction<? super A, ? super B, R> function) {
        Preconditions.<Stream<A>>checkNotNull(streamA);
        Preconditions.<Stream<B>>checkNotNull(streamB);
        Preconditions.<BiFunction<? super A, ? super B, R>>checkNotNull(function);
        final boolean isParallel = streamA.isParallel() || streamB.isParallel();
        final Spliterator<A> splitrA = (Spliterator<A>)streamA.spliterator();
        final Spliterator<B> splitrB = (Spliterator<B>)streamB.spliterator();
        final int characteristics = splitrA.characteristics() & splitrB.characteristics() & 0x50;
        final Iterator<A> itrA = (Iterator<A>)Spliterators.iterator((Spliterator)splitrA);
        final Iterator<B> itrB = (Iterator<B>)Spliterators.iterator((Spliterator)splitrB);
        return (Stream<R>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<R>(Math.min(splitrA.estimateSize(), splitrB.estimateSize()), characteristics) {
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (itrA.hasNext() && itrB.hasNext()) {
                    action.accept(function.apply(itrA.next(), itrB.next()));
                    return true;
                }
                return false;
            }
        }, isParallel);
    }
    
    public static <T, R> Stream<R> mapWithIndex(final Stream<T> stream, final FunctionWithIndex<? super T, ? extends R> function) {
        Preconditions.<Stream<T>>checkNotNull(stream);
        Preconditions.<FunctionWithIndex<? super T, ? extends R>>checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator<T> fromSpliterator = (Spliterator<T>)stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final Iterator<T> fromIterator = (Iterator<T>)Spliterators.iterator((Spliterator)fromSpliterator);
            return (Stream<R>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.next(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        class 1Splitr extends MapWithIndexSpliterator<Spliterator<T>, R, 1Splitr> implements Consumer<T> {
            T holder;
            final /* synthetic */ FunctionWithIndex val$function;
            
            1Splitr(final Spliterator splitr, final Spliterator<T> index, final long functionWithIndex) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$1Splitr.val$function:Lcom/google/common/collect/Streams$FunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator;Ljava/util/Spliterator<TT;>;J)V [from metadata: (Ljava/util/Spliterator<TT;>;J)V]
                //  
                //    MethodParameters:
                //  Name               Flags  
                //  -----------------  -----
                //  splitr             
                //  index              
                //  functionWithIndex  
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            public void accept(@Nullable final T t) {
                this.holder = t;
            }
            
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (this.fromSpliterator.tryAdvance((Consumer)this)) {
                    try {
                        action.accept(this.val$function.apply(this.holder, this.index++));
                        return true;
                    }
                    finally {
                        this.holder = null;
                    }
                }
                return false;
            }
            
            @Override
            1Splitr createSplit(final Spliterator<T> from, final long i) {
                return new 1Splitr(from, i, this.val$function);
            }
        }
        return (Stream<R>)StreamSupport.stream((Spliterator)new 1Splitr(fromSpliterator, 0L, function), isParallel);
    }
    
    public static <R> Stream<R> mapWithIndex(final IntStream stream, final IntFunctionWithIndex<R> function) {
        Preconditions.<IntStream>checkNotNull(stream);
        Preconditions.<IntFunctionWithIndex<R>>checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator.OfInt fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfInt fromIterator = Spliterators.iterator(fromSpliterator);
            return (Stream<R>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.nextInt(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        class 2Splitr extends MapWithIndexSpliterator<Spliterator.OfInt, R, 2Splitr> implements IntConsumer, Spliterator<R> {
            int holder;
            final /* synthetic */ IntFunctionWithIndex val$function;
            
            2Splitr(final Spliterator.OfInt splitr, final Spliterator.OfInt index, final long intFunctionWithIndex) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$2Splitr.val$function:Lcom/google/common/collect/Streams$IntFunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator$OfInt;Ljava/util/Spliterator$OfInt;J)V [from metadata: (Ljava/util/Spliterator$OfInt;J)V]
                //  
                //    MethodParameters:
                //  Name                  Flags  
                //  --------------------  -----
                //  splitr                
                //  index                 
                //  intFunctionWithIndex  
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            public void accept(final int t) {
                this.holder = t;
            }
            
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (((Spliterator.OfInt)this.fromSpliterator).tryAdvance((IntConsumer)this)) {
                    action.accept(this.val$function.apply(this.holder, this.index++));
                    return true;
                }
                return false;
            }
            
            @Override
            2Splitr createSplit(final Spliterator.OfInt from, final long i) {
                return new 2Splitr(from, i, this.val$function);
            }
        }
        return (Stream<R>)StreamSupport.stream((Spliterator)new 2Splitr(fromSpliterator, 0L, function), isParallel);
    }
    
    public static <R> Stream<R> mapWithIndex(final LongStream stream, final LongFunctionWithIndex<R> function) {
        Preconditions.<LongStream>checkNotNull(stream);
        Preconditions.<LongFunctionWithIndex<R>>checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator.OfLong fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfLong fromIterator = Spliterators.iterator(fromSpliterator);
            return (Stream<R>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.nextLong(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        class 3Splitr extends MapWithIndexSpliterator<Spliterator.OfLong, R, 3Splitr> implements LongConsumer, Spliterator<R> {
            long holder;
            final /* synthetic */ LongFunctionWithIndex val$function;
            
            3Splitr(final Spliterator.OfLong splitr, final Spliterator.OfLong index, final long longFunctionWithIndex) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$3Splitr.val$function:Lcom/google/common/collect/Streams$LongFunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator$OfLong;Ljava/util/Spliterator$OfLong;J)V [from metadata: (Ljava/util/Spliterator$OfLong;J)V]
                //  
                //    MethodParameters:
                //  Name                   Flags  
                //  ---------------------  -----
                //  splitr                 
                //  index                  
                //  longFunctionWithIndex  
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            public void accept(final long t) {
                this.holder = t;
            }
            
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (((Spliterator.OfLong)this.fromSpliterator).tryAdvance((LongConsumer)this)) {
                    action.accept(this.val$function.apply(this.holder, this.index++));
                    return true;
                }
                return false;
            }
            
            @Override
            3Splitr createSplit(final Spliterator.OfLong from, final long i) {
                return new 3Splitr(from, i, this.val$function);
            }
        }
        return (Stream<R>)StreamSupport.stream((Spliterator)new 3Splitr(fromSpliterator, 0L, function), isParallel);
    }
    
    public static <R> Stream<R> mapWithIndex(final DoubleStream stream, final DoubleFunctionWithIndex<R> function) {
        Preconditions.<DoubleStream>checkNotNull(stream);
        Preconditions.<DoubleFunctionWithIndex<R>>checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator.OfDouble fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfDouble fromIterator = Spliterators.iterator(fromSpliterator);
            return (Stream<R>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.nextDouble(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        class 4Splitr extends MapWithIndexSpliterator<Spliterator.OfDouble, R, 4Splitr> implements DoubleConsumer, Spliterator<R> {
            double holder;
            final /* synthetic */ DoubleFunctionWithIndex val$function;
            
            4Splitr(final Spliterator.OfDouble splitr, final Spliterator.OfDouble index, final long doubleFunctionWithIndex) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$4Splitr.val$function:Lcom/google/common/collect/Streams$DoubleFunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator$OfDouble;Ljava/util/Spliterator$OfDouble;J)V [from metadata: (Ljava/util/Spliterator$OfDouble;J)V]
                //  
                //    MethodParameters:
                //  Name                     Flags  
                //  -----------------------  -----
                //  splitr                   
                //  index                    
                //  doubleFunctionWithIndex  
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            public void accept(final double t) {
                this.holder = t;
            }
            
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (((Spliterator.OfDouble)this.fromSpliterator).tryAdvance((DoubleConsumer)this)) {
                    action.accept(this.val$function.apply(this.holder, this.index++));
                    return true;
                }
                return false;
            }
            
            @Override
            4Splitr createSplit(final Spliterator.OfDouble from, final long i) {
                return new 4Splitr(from, i, this.val$function);
            }
        }
        return (Stream<R>)StreamSupport.stream((Spliterator)new 4Splitr(fromSpliterator, 0L, function), isParallel);
    }
    
    private Streams() {
    }
    
    private abstract static class MapWithIndexSpliterator<F extends Spliterator<?>, R, S extends MapWithIndexSpliterator<F, R, S>> implements Spliterator<R> {
        final F fromSpliterator;
        long index;
        
        MapWithIndexSpliterator(final F fromSpliterator, final long index) {
            this.fromSpliterator = fromSpliterator;
            this.index = index;
        }
        
        abstract S createSplit(final F spliterator, final long long2);
        
        public S trySplit() {
            final F split = (F)this.fromSpliterator.trySplit();
            if (split == null) {
                return null;
            }
            final S result = this.createSplit(split, this.index);
            this.index += split.getExactSizeIfKnown();
            return result;
        }
        
        public long estimateSize() {
            return this.fromSpliterator.estimateSize();
        }
        
        public int characteristics() {
            return this.fromSpliterator.characteristics() & 0x4050;
        }
    }
    
    @Beta
    public interface DoubleFunctionWithIndex<R> {
        R apply(final double double1, final long long2);
    }
    
    @Beta
    public interface LongFunctionWithIndex<R> {
        R apply(final long long1, final long long2);
    }
    
    @Beta
    public interface IntFunctionWithIndex<R> {
        R apply(final int integer, final long long2);
    }
    
    @Beta
    public interface FunctionWithIndex<T, R> {
        R apply(final T object, final long long2);
    }
}
