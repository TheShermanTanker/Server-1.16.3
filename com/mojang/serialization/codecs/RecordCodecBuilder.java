package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function3;
import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.RecordBuilder;
import java.util.stream.Stream;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;

public final class RecordCodecBuilder<O, F> implements App<Mu<O>, F> {
    private final Function<O, F> getter;
    private final Function<O, MapEncoder<F>> encoder;
    private final MapDecoder<F> decoder;
    
    public static <O, F> RecordCodecBuilder<O, F> unbox(final App<Mu<O>, F> box) {
        return (RecordCodecBuilder<O, F>)(RecordCodecBuilder)box;
    }
    
    private RecordCodecBuilder(final Function<O, F> getter, final Function<O, MapEncoder<F>> encoder, final MapDecoder<F> decoder) {
        this.getter = getter;
        this.encoder = encoder;
        this.decoder = decoder;
    }
    
    public static <O> Instance<O> instance() {
        return new Instance<O>();
    }
    
    public static <O, F> RecordCodecBuilder<O, F> of(final Function<O, F> getter, final String name, final Codec<F> fieldCodec) {
        return RecordCodecBuilder.<O, F>of(getter, fieldCodec.fieldOf(name));
    }
    
    public static <O, F> RecordCodecBuilder<O, F> of(final Function<O, F> getter, final MapCodec<F> codec) {
        return new RecordCodecBuilder<O, F>(getter, (java.util.function.Function<O, MapEncoder<F>>)(o -> codec), codec);
    }
    
    public static <O, F> RecordCodecBuilder<O, F> point(final F instance) {
        return new RecordCodecBuilder<O, F>((java.util.function.Function<O, F>)(o -> instance), (java.util.function.Function<O, MapEncoder<F>>)(o -> Encoder.empty()), Decoder.<F>unit(instance));
    }
    
    public static <O, F> RecordCodecBuilder<O, F> stable(final F instance) {
        return RecordCodecBuilder.<O, F>point(instance, Lifecycle.stable());
    }
    
    public static <O, F> RecordCodecBuilder<O, F> deprecated(final F instance, final int since) {
        return RecordCodecBuilder.<O, F>point(instance, Lifecycle.deprecated(since));
    }
    
    public static <O, F> RecordCodecBuilder<O, F> point(final F instance, final Lifecycle lifecycle) {
        return new RecordCodecBuilder<O, F>((java.util.function.Function<O, F>)(o -> instance), (java.util.function.Function<O, MapEncoder<F>>)(o -> Encoder.empty().withLifecycle(lifecycle)), Decoder.<F>unit(instance).withLifecycle(lifecycle));
    }
    
    public static <O> Codec<O> create(final Function<Instance<O>, ? extends App<Mu<O>, O>> builder) {
        return RecordCodecBuilder.<O>build((App<Mu<O>, O>)builder.apply(RecordCodecBuilder.instance())).codec();
    }
    
    public static <O> MapCodec<O> mapCodec(final Function<Instance<O>, ? extends App<Mu<O>, O>> builder) {
        return RecordCodecBuilder.<O>build((App<Mu<O>, O>)builder.apply(RecordCodecBuilder.instance()));
    }
    
    public <E> RecordCodecBuilder<O, E> dependent(final Function<O, E> getter, final MapEncoder<E> encoder, final Function<? super F, ? extends MapDecoder<E>> decoderGetter) {
        return new RecordCodecBuilder<O, E>(getter, (java.util.function.Function<O, MapEncoder<E>>)(o -> encoder), new MapDecoder.Implementation<E>() {
            @Override
            public <T> DataResult<E> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return RecordCodecBuilder.this.decoder.<T>decode(ops, input).map((java.util.function.Function<? super Object, ?>)decoderGetter).<E>flatMap((java.util.function.Function<? super Object, ? extends DataResult<E>>)(decoder1 -> decoder1.decode(ops, input).map(Function.identity())));
            }
            
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return (Stream<T>)encoder.keys(ops);
            }
            
            public String toString() {
                return new StringBuilder().append("Dependent[").append(encoder).append("]").toString();
            }
        });
    }
    
    public static <O> MapCodec<O> build(final App<Mu<O>, O> builderBox) {
        final RecordCodecBuilder<O, O> builder = RecordCodecBuilder.<O, O>unbox(builderBox);
        return new MapCodec<O>() {
            @Override
            public <T> DataResult<O> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                return builder.decoder.<T>decode(ops, input);
            }
            
            @Override
            public <T> RecordBuilder<T> encode(final O input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                return ((MapEncoder)builder.encoder.apply(input)).<T>encode(input, ops, prefix);
            }
            
            @Override
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return (Stream<T>)builder.decoder.keys(ops);
            }
            
            public String toString() {
                return new StringBuilder().append("RecordCodec[").append(builder.decoder).append("]").toString();
            }
        };
    }
    
    public static final class Mu<O> implements K1 {
    }
    
    public static final class Instance<O> implements Applicative<RecordCodecBuilder.Mu<O>, Mu<O>> {
        public <A> App<RecordCodecBuilder.Mu<O>, A> stable(final A a) {
            return RecordCodecBuilder.stable(a);
        }
        
        public <A> App<RecordCodecBuilder.Mu<O>, A> deprecated(final A a, final int since) {
            return RecordCodecBuilder.deprecated(a, since);
        }
        
        public <A> App<RecordCodecBuilder.Mu<O>, A> point(final A a, final Lifecycle lifecycle) {
            return RecordCodecBuilder.point(a, lifecycle);
        }
        
        public <A> App<RecordCodecBuilder.Mu<O>, A> point(final A a) {
            return RecordCodecBuilder.point(a);
        }
        
        public <A, R> Function<App<RecordCodecBuilder.Mu<O>, A>, App<RecordCodecBuilder.Mu<O>, R>> lift1(final App<RecordCodecBuilder.Mu<O>, Function<A, R>> function) {
            return (Function<App<RecordCodecBuilder.Mu<O>, A>, App<RecordCodecBuilder.Mu<O>, R>>)(fa -> {
                final RecordCodecBuilder<O, Function<A, Object>> f = RecordCodecBuilder.<O, Function<A, Object>>unbox((App<RecordCodecBuilder.Mu<O>, Function<A, Object>>)function);
                final RecordCodecBuilder<O, A> a = RecordCodecBuilder.<O, A>unbox((App<RecordCodecBuilder.Mu<O>, A>)fa);
                return new RecordCodecBuilder(o -> ((Function)f.getter.apply(o)).apply(a.getter.apply(o)), o -> {
                    final MapEncoder<Function<A, Object>> fEnc = (MapEncoder<Function<A, Object>>)f.encoder.apply(o);
                    final MapEncoder<A> aEnc = (MapEncoder<A>)a.encoder.apply(o);
                    final A aFromO = (A)a.getter.apply(o);
                    return new MapEncoder.Implementation<Object>() {
                        final /* synthetic */ MapEncoder val$aEnc;
                        final /* synthetic */ Object val$aFromO;
                        final /* synthetic */ MapEncoder val$fEnc;
                        
                        @Override
                        public RecordBuilder encode(final Object input, final DynamicOps ops, final RecordBuilder prefix) {
                            this.val$aEnc.encode(this.val$aFromO, ops, prefix);
                            this.val$fEnc.encode(a1 -> input, ops, prefix);
                            return prefix;
                        }
                        
                        public <T> Stream<T> keys(final DynamicOps<T> ops) {
                            return (Stream<T>)Stream.concat(this.val$aEnc.keys((DynamicOps)ops), this.val$fEnc.keys((DynamicOps)ops));
                        }
                        
                        public String toString() {
                            return new StringBuilder().append(this.val$fEnc).append(" * ").append(this.val$aEnc).toString();
                        }
                    };
                }, new MapDecoder.Implementation<Object>() {
                    final /* synthetic */ RecordCodecBuilder val$a;
                    final /* synthetic */ RecordCodecBuilder val$f;
                    
                    @Override
                    public <T> DataResult<Object> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                        return this.val$a.decoder.<T>decode(ops, input).flatMap((java.util.function.Function<? super Object, ? extends DataResult<Object>>)(ar -> f.decoder.decode(ops, input).map(fr -> fr.apply(ar))));
                    }
                    
                    public <T> Stream<T> keys(final DynamicOps<T> ops) {
                        return (Stream<T>)Stream.concat(this.val$a.decoder.keys(ops), this.val$f.decoder.keys(ops));
                    }
                    
                    public String toString() {
                        return new StringBuilder().append(this.val$f.decoder).append(" * ").append(this.val$a.decoder).toString();
                    }
                }, null);
            });
        }
        
        public <A, B, R> App<RecordCodecBuilder.Mu<O>, R> ap2(final App<RecordCodecBuilder.Mu<O>, BiFunction<A, B, R>> func, final App<RecordCodecBuilder.Mu<O>, A> a, final App<RecordCodecBuilder.Mu<O>, B> b) {
            final RecordCodecBuilder<O, BiFunction<A, B, R>> function = RecordCodecBuilder.<O, BiFunction<A, B, R>>unbox(func);
            final RecordCodecBuilder<O, A> fa = RecordCodecBuilder.<O, A>unbox(a);
            final RecordCodecBuilder<O, B> fb = RecordCodecBuilder.<O, B>unbox(b);
            return new RecordCodecBuilder<O, R>(o -> ((BiFunction)function.getter.apply(o)).apply(fa.getter.apply(o), fb.getter.apply(o)), o -> {
                final MapEncoder<BiFunction<A, Object, Object>> fEncoder = (MapEncoder<BiFunction<A, Object, Object>>)function.encoder.apply(o);
                final MapEncoder<A> aEncoder = (MapEncoder<A>)fa.encoder.apply(o);
                final A aFromO = (A)fa.getter.apply(o);
                final MapEncoder<Object> bEncoder = (MapEncoder<Object>)fb.encoder.apply(o);
                final Object bFromO = fb.getter.apply(o);
                return new MapEncoder.Implementation<Object>() {
                    final /* synthetic */ MapEncoder val$aEncoder;
                    final /* synthetic */ Object val$aFromO;
                    final /* synthetic */ MapEncoder val$bEncoder;
                    final /* synthetic */ Object val$bFromO;
                    final /* synthetic */ MapEncoder val$fEncoder;
                    
                    @Override
                    public RecordBuilder encode(final Object input, final DynamicOps ops, final RecordBuilder prefix) {
                        this.val$aEncoder.encode(this.val$aFromO, ops, prefix);
                        this.val$bEncoder.encode(this.val$bFromO, ops, prefix);
                        this.val$fEncoder.encode((a1, b1) -> input, ops, prefix);
                        return prefix;
                    }
                    
                    public <T> Stream<T> keys(final DynamicOps<T> ops) {
                        return (Stream<T>)Stream.of((Object[])new Stream[] { this.val$fEncoder.keys((DynamicOps)ops), this.val$aEncoder.keys((DynamicOps)ops), this.val$bEncoder.keys((DynamicOps)ops) }).flatMap(Function.identity());
                    }
                    
                    public String toString() {
                        return new StringBuilder().append(this.val$fEncoder).append(" * ").append(this.val$aEncoder).append(" * ").append(this.val$bEncoder).toString();
                    }
                };
            }, new MapDecoder.Implementation<R>() {
                @Override
                public <T> DataResult<R> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                    return DataResult.<R>unbox(DataResult.instance().ap2(function.decoder.decode(ops, input), fa.decoder.<T>decode(ops, input), fb.decoder.<T>decode(ops, input)));
                }
                
                public <T> Stream<T> keys(final DynamicOps<T> ops) {
                    return (Stream<T>)Stream.of((Object[])new Stream[] { function.decoder.keys(ops), fa.decoder.keys(ops), fb.decoder.keys(ops) }).flatMap(Function.identity());
                }
                
                public String toString() {
                    return new StringBuilder().append(function.decoder).append(" * ").append(fa.decoder).append(" * ").append(fb.decoder).toString();
                }
            }, null);
        }
        
        public <T1, T2, T3, R> App<RecordCodecBuilder.Mu<O>, R> ap3(final App<RecordCodecBuilder.Mu<O>, Function3<T1, T2, T3, R>> func, final App<RecordCodecBuilder.Mu<O>, T1> t1, final App<RecordCodecBuilder.Mu<O>, T2> t2, final App<RecordCodecBuilder.Mu<O>, T3> t3) {
            final RecordCodecBuilder<O, Function3<T1, T2, T3, R>> function = RecordCodecBuilder.<O, Function3<T1, T2, T3, R>>unbox(func);
            final RecordCodecBuilder<O, T1> f1 = RecordCodecBuilder.<O, T1>unbox(t1);
            final RecordCodecBuilder<O, T2> f2 = RecordCodecBuilder.<O, T2>unbox(t2);
            final RecordCodecBuilder<O, T3> f3 = RecordCodecBuilder.<O, T3>unbox(t3);
            return new RecordCodecBuilder<O, R>(o -> ((Function3)function.getter.apply(o)).apply(f1.getter.apply(o), f2.getter.apply(o), f3.getter.apply(o)), o -> {
                final MapEncoder<Function3<Object, Object, Object, Object>> fEncoder = (MapEncoder<Function3<Object, Object, Object, Object>>)function.encoder.apply(o);
                final MapEncoder<Object> e1 = (MapEncoder<Object>)f1.encoder.apply(o);
                final Object v1 = f1.getter.apply(o);
                final MapEncoder<Object> e2 = (MapEncoder<Object>)f2.encoder.apply(o);
                final Object v2 = f2.getter.apply(o);
                final MapEncoder<Object> e3 = (MapEncoder<Object>)f3.encoder.apply(o);
                final Object v3 = f3.getter.apply(o);
                return new MapEncoder.Implementation<Object>() {
                    final /* synthetic */ MapEncoder val$e1;
                    final /* synthetic */ Object val$v1;
                    final /* synthetic */ MapEncoder val$e2;
                    final /* synthetic */ Object val$v2;
                    final /* synthetic */ MapEncoder val$e3;
                    final /* synthetic */ Object val$v3;
                    final /* synthetic */ MapEncoder val$fEncoder;
                    
                    @Override
                    public RecordBuilder encode(final Object input, final DynamicOps ops, final RecordBuilder prefix) {
                        this.val$e1.encode(this.val$v1, ops, prefix);
                        this.val$e2.encode(this.val$v2, ops, prefix);
                        this.val$e3.encode(this.val$v3, ops, prefix);
                        this.val$fEncoder.encode((t1, t2, t3) -> input, ops, prefix);
                        return prefix;
                    }
                    
                    public <T> Stream<T> keys(final DynamicOps<T> ops) {
                        return (Stream<T>)Stream.of((Object[])new Stream[] { this.val$fEncoder.keys((DynamicOps)ops), this.val$e1.keys((DynamicOps)ops), this.val$e2.keys((DynamicOps)ops), this.val$e3.keys((DynamicOps)ops) }).flatMap(Function.identity());
                    }
                    
                    public String toString() {
                        return new StringBuilder().append(this.val$fEncoder).append(" * ").append(this.val$e1).append(" * ").append(this.val$e2).append(" * ").append(this.val$e3).toString();
                    }
                };
            }, new MapDecoder.Implementation<R>() {
                @Override
                public <T> DataResult<R> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                    return DataResult.<R>unbox(DataResult.instance().ap3(function.decoder.decode(ops, input), f1.decoder.<T>decode(ops, input), f2.decoder.<T>decode(ops, input), f3.decoder.<T>decode(ops, input)));
                }
                
                public <T> Stream<T> keys(final DynamicOps<T> ops) {
                    return (Stream<T>)Stream.of((Object[])new Stream[] { function.decoder.keys(ops), f1.decoder.keys(ops), f2.decoder.keys(ops), f3.decoder.keys(ops) }).flatMap(Function.identity());
                }
                
                public String toString() {
                    return new StringBuilder().append(function.decoder).append(" * ").append(f1.decoder).append(" * ").append(f2.decoder).append(" * ").append(f3.decoder).toString();
                }
            }, null);
        }
        
        public <T1, T2, T3, T4, R> App<RecordCodecBuilder.Mu<O>, R> ap4(final App<RecordCodecBuilder.Mu<O>, Function4<T1, T2, T3, T4, R>> func, final App<RecordCodecBuilder.Mu<O>, T1> t1, final App<RecordCodecBuilder.Mu<O>, T2> t2, final App<RecordCodecBuilder.Mu<O>, T3> t3, final App<RecordCodecBuilder.Mu<O>, T4> t4) {
            final RecordCodecBuilder<O, Function4<T1, T2, T3, T4, R>> function = RecordCodecBuilder.<O, Function4<T1, T2, T3, T4, R>>unbox(func);
            final RecordCodecBuilder<O, T1> f1 = RecordCodecBuilder.<O, T1>unbox(t1);
            final RecordCodecBuilder<O, T2> f2 = RecordCodecBuilder.<O, T2>unbox(t2);
            final RecordCodecBuilder<O, T3> f3 = RecordCodecBuilder.<O, T3>unbox(t3);
            final RecordCodecBuilder<O, T4> f4 = RecordCodecBuilder.<O, T4>unbox(t4);
            return new RecordCodecBuilder<O, R>(o -> ((Function4)function.getter.apply(o)).apply(f1.getter.apply(o), f2.getter.apply(o), f3.getter.apply(o), f4.getter.apply(o)), o -> {
                final MapEncoder<Function4<Object, Object, Object, Object, Object>> fEncoder = (MapEncoder<Function4<Object, Object, Object, Object, Object>>)function.encoder.apply(o);
                final MapEncoder<Object> e1 = (MapEncoder<Object>)f1.encoder.apply(o);
                final Object v1 = f1.getter.apply(o);
                final MapEncoder<Object> e2 = (MapEncoder<Object>)f2.encoder.apply(o);
                final Object v2 = f2.getter.apply(o);
                final MapEncoder<Object> e3 = (MapEncoder<Object>)f3.encoder.apply(o);
                final Object v3 = f3.getter.apply(o);
                final MapEncoder<Object> e4 = (MapEncoder<Object>)f4.encoder.apply(o);
                final Object v4 = f4.getter.apply(o);
                return new MapEncoder.Implementation<Object>() {
                    final /* synthetic */ MapEncoder val$e1;
                    final /* synthetic */ Object val$v1;
                    final /* synthetic */ MapEncoder val$e2;
                    final /* synthetic */ Object val$v2;
                    final /* synthetic */ MapEncoder val$e3;
                    final /* synthetic */ Object val$v3;
                    final /* synthetic */ MapEncoder val$e4;
                    final /* synthetic */ Object val$v4;
                    final /* synthetic */ MapEncoder val$fEncoder;
                    
                    @Override
                    public RecordBuilder encode(final Object input, final DynamicOps ops, final RecordBuilder prefix) {
                        this.val$e1.encode(this.val$v1, ops, prefix);
                        this.val$e2.encode(this.val$v2, ops, prefix);
                        this.val$e3.encode(this.val$v3, ops, prefix);
                        this.val$e4.encode(this.val$v4, ops, prefix);
                        this.val$fEncoder.encode((t1, t2, t3, t4) -> input, ops, prefix);
                        return prefix;
                    }
                    
                    public <T> Stream<T> keys(final DynamicOps<T> ops) {
                        return (Stream<T>)Stream.of((Object[])new Stream[] { this.val$fEncoder.keys((DynamicOps)ops), this.val$e1.keys((DynamicOps)ops), this.val$e2.keys((DynamicOps)ops), this.val$e3.keys((DynamicOps)ops), this.val$e4.keys((DynamicOps)ops) }).flatMap(Function.identity());
                    }
                    
                    public String toString() {
                        return new StringBuilder().append(this.val$fEncoder).append(" * ").append(this.val$e1).append(" * ").append(this.val$e2).append(" * ").append(this.val$e3).append(" * ").append(this.val$e4).toString();
                    }
                };
            }, new MapDecoder.Implementation<R>() {
                @Override
                public <T> DataResult<R> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                    return DataResult.<R>unbox(((Applicative<DataResult.Mu, Mu>)DataResult.instance()).ap4(function.decoder.decode(ops, input), f1.decoder.<T>decode(ops, input), f2.decoder.<T>decode(ops, input), f3.decoder.<T>decode(ops, input), f4.decoder.<T>decode(ops, input)));
                }
                
                public <T> Stream<T> keys(final DynamicOps<T> ops) {
                    return (Stream<T>)Stream.of((Object[])new Stream[] { function.decoder.keys(ops), f1.decoder.keys(ops), f2.decoder.keys(ops), f3.decoder.keys(ops), f4.decoder.keys(ops) }).flatMap(Function.identity());
                }
                
                public String toString() {
                    return new StringBuilder().append(function.decoder).append(" * ").append(f1.decoder).append(" * ").append(f2.decoder).append(" * ").append(f3.decoder).append(" * ").append(f4.decoder).toString();
                }
            }, null);
        }
        
        public <T, R> App<RecordCodecBuilder.Mu<O>, R> map(final Function<? super T, ? extends R> func, final App<RecordCodecBuilder.Mu<O>, T> ts) {
            final RecordCodecBuilder<O, T> unbox = RecordCodecBuilder.<O, T>unbox(ts);
            final Function<O, T> getter = (Function<O, T>)((RecordCodecBuilder<Object, Object>)unbox).getter;
            return new RecordCodecBuilder<O, R>(getter.andThen((Function)func), o -> new MapEncoder.Implementation<Object>() {
                private final MapEncoder encoder;
                final /* synthetic */ RecordCodecBuilder val$unbox;
                final /* synthetic */ Object val$o;
                final /* synthetic */ Function val$getter;
                
                {
                    this.encoder = (MapEncoder)this.val$unbox.encoder.apply(this.val$o);
                }
                
                @Override
                public RecordBuilder encode(final Object input, final DynamicOps ops, final RecordBuilder prefix) {
                    return this.encoder.encode(this.val$getter.apply(this.val$o), ops, prefix);
                }
                
                public <U> Stream<U> keys(final DynamicOps<U> ops) {
                    return (Stream<U>)this.encoder.keys((DynamicOps)ops);
                }
                
                public String toString() {
                    return new StringBuilder().append(this.encoder).append("[mapped]").toString();
                }
            }, ((RecordCodecBuilder<Object, Object>)unbox).decoder.map(func), null);
        }
        
        private static final class Mu<O> implements Applicative.Mu {
        }
    }
}
