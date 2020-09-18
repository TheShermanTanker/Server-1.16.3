package com.mojang.datafixers.optics.profunctors;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.kinds.K2;

public interface AffineP<P extends K2, Mu extends Mu> extends Cartesian<P, Mu>, Cocartesian<P, Mu> {
    public interface Mu extends Cartesian.Mu, Cocartesian.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {};
    }
}
