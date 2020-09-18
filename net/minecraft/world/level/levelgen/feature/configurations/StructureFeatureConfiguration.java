package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;

public class StructureFeatureConfiguration {
    public static final Codec<StructureFeatureConfiguration> CODEC;
    private final int spacing;
    private final int separation;
    private final int salt;
    
    public StructureFeatureConfiguration(final int integer1, final int integer2, final int integer3) {
        this.spacing = integer1;
        this.separation = integer2;
        this.salt = integer3;
    }
    
    public int spacing() {
        return this.spacing;
    }
    
    public int separation() {
        return this.separation;
    }
    
    public int salt() {
        return this.salt;
    }
    
    static {
        CODEC = RecordCodecBuilder.create((java.util.function.Function<RecordCodecBuilder.Instance<Object>, ? extends App<RecordCodecBuilder.Mu<Object>, Object>>)(instance -> instance.<Integer, Integer, Integer>group(Codec.intRange(0, 4096).fieldOf("spacing").forGetter((java.util.function.Function<Object, Integer>)(cmv -> cmv.spacing)), Codec.intRange(0, 4096).fieldOf("separation").forGetter((java.util.function.Function<Object, Integer>)(cmv -> cmv.separation)), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("salt").forGetter((java.util.function.Function<Object, Integer>)(cmv -> cmv.salt))).<StructureFeatureConfiguration>apply(instance, StructureFeatureConfiguration::new))).<StructureFeatureConfiguration>comapFlatMap((java.util.function.Function<? super Object, ? extends DataResult<? extends StructureFeatureConfiguration>>)(cmv -> {
            if (cmv.spacing <= cmv.separation) {
                return DataResult.error("Spacing has to be smaller than separation");
            }
            return DataResult.<StructureFeatureConfiguration>success(cmv);
        }), (java.util.function.Function<? super StructureFeatureConfiguration, ?>)Function.identity());
    }
}
