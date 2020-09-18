package net.minecraft.world.level.levelgen.feature.featuresize;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import com.mojang.serialization.Codec;

public class TwoLayersFeatureSize extends FeatureSize {
    public static final Codec<TwoLayersFeatureSize> CODEC;
    private final int limit;
    private final int lowerSize;
    private final int upperSize;
    
    public TwoLayersFeatureSize(final int integer1, final int integer2, final int integer3) {
        this(integer1, integer2, integer3, OptionalInt.empty());
    }
    
    public TwoLayersFeatureSize(final int integer1, final int integer2, final int integer3, final OptionalInt optionalInt) {
        super(optionalInt);
        this.limit = integer1;
        this.lowerSize = integer2;
        this.upperSize = integer3;
    }
    
    @Override
    protected FeatureSizeType<?> type() {
        return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE;
    }
    
    @Override
    public int getSizeAtHeight(final int integer1, final int integer2) {
        return (integer2 < this.limit) ? this.lowerSize : this.upperSize;
    }
    
    static {
        CODEC = RecordCodecBuilder.<TwoLayersFeatureSize>create((java.util.function.Function<RecordCodecBuilder.Instance<TwoLayersFeatureSize>, ? extends App<RecordCodecBuilder.Mu<TwoLayersFeatureSize>, TwoLayersFeatureSize>>)(instance -> instance.<Integer, Integer, Integer, OptionalInt>group(Codec.intRange(0, 81).fieldOf("limit").orElse(1).forGetter((java.util.function.Function<Object, Integer>)(cnb -> cnb.limit)), Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(cnb -> cnb.lowerSize)), Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter((java.util.function.Function<Object, Integer>)(cnb -> cnb.upperSize)), FeatureSize.minClippedHeightCodec()).<TwoLayersFeatureSize>apply(instance, TwoLayersFeatureSize::new)));
    }
}
