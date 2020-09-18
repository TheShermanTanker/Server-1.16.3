package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.UniformInt;
import com.mojang.serialization.Codec;

public class ColumnFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<ColumnFeatureConfiguration> CODEC;
    private final UniformInt reach;
    private final UniformInt height;
    
    public ColumnFeatureConfiguration(final UniformInt aft1, final UniformInt aft2) {
        this.reach = aft1;
        this.height = aft2;
    }
    
    public UniformInt reach() {
        return this.reach;
    }
    
    public UniformInt height() {
        return this.height;
    }
    
    static {
        CODEC = RecordCodecBuilder.<ColumnFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<ColumnFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<ColumnFeatureConfiguration>, ColumnFeatureConfiguration>>)(instance -> instance.<UniformInt, UniformInt>group(UniformInt.codec(0, 2, 1).fieldOf("reach").forGetter((java.util.function.Function<Object, UniformInt>)(clq -> clq.reach)), UniformInt.codec(1, 5, 5).fieldOf("height").forGetter((java.util.function.Function<Object, UniformInt>)(clq -> clq.height))).apply(instance, (java.util.function.BiFunction<UniformInt, UniformInt, Object>)ColumnFeatureConfiguration::new)));
    }
}
