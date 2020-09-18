package com.mojang.datafixers.optics;

import java.util.function.Supplier;
import java.util.function.Function;
import com.mojang.datafixers.kinds.Functor;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.App;

interface PStore<I, J, X> extends App<Mu<I, J>, X> {
    default <I, J, X> PStore<I, J, X> unbox(final App<Mu<I, J>, X> box) {
        return (PStore<I, J, X>)(PStore)box;
    }
    
    X peek(final J object);
    
    I pos();
    
    public static final class Mu<I, J> implements K1 {
    }
    
    public static final class Instance<I, J> implements Functor<PStore.Mu<I, J>, Mu<I, J>> {
        public <T, R> App<PStore.Mu<I, J>, R> map(final Function<? super T, ? extends R> func, final App<PStore.Mu<I, J>, T> ts) {
            final PStore<I, J, T> input = PStore.<I, J, T>unbox(ts);
            return Optics.pStore((java.util.function.Function<Object, Object>)func.compose(input::peek)::apply, (java.util.function.Supplier<Object>)input::pos);
        }
        
        public static final class Mu<I, J> implements Functor.Mu {
        }
    }
}
