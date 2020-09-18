package com.mojang.datafixers.optics.profunctors;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K2;

public interface Closed<P extends K2, Mu extends Mu> extends Profunctor<P, Mu> {
    default <P extends K2, Proof extends Mu> Closed<P, Proof> unbox(final App<Proof, P> proofBox) {
        return (Closed<P, Proof>)(Closed)proofBox;
    }
    
     <A, B, X> App2<P, FunctionType<X, A>, FunctionType<X, B>> closed(final App2<P, A, B> app2);
    
    public interface Mu extends Profunctor.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {};
    }
}
