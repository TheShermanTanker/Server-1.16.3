package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class HugeMushroomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<HugeMushroomFeatureConfiguration> CODEC;
    public final BlockStateProvider capProvider;
    public final BlockStateProvider stemProvider;
    public final int foliageRadius;
    
    public HugeMushroomFeatureConfiguration(final BlockStateProvider cnq1, final BlockStateProvider cnq2, final int integer) {
        this.capProvider = cnq1;
        this.stemProvider = cnq2;
        this.foliageRadius = integer;
    }
    
    static {
        CODEC = RecordCodecBuilder.<HugeMushroomFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<HugeMushroomFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<HugeMushroomFeatureConfiguration>, HugeMushroomFeatureConfiguration>>)(instance -> instance.<BlockStateProvider, BlockStateProvider, Integer>group(BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter((java.util.function.Function<Object, BlockStateProvider>)(cly -> cly.capProvider)), BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter((java.util.function.Function<Object, BlockStateProvider>)(cly -> cly.stemProvider)), Codec.INT.fieldOf("foliage_radius").orElse(2).forGetter((java.util.function.Function<Object, Integer>)(cly -> cly.foliageRadius))).<HugeMushroomFeatureConfiguration>apply(instance, HugeMushroomFeatureConfiguration::new)));
    }
}
