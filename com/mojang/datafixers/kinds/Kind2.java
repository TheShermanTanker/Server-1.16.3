package com.mojang.datafixers.kinds;

public interface Kind2<F extends K2, Mu extends Mu> extends App<Mu, F> {
    default <F extends K2, Proof extends Mu> Kind2<F, Proof> unbox(final App<Proof, F> proofBox) {
        return (Kind2<F, Proof>)(Kind2)proofBox;
    }
    
    public interface Mu extends K1 {
    }
}
