package net.minecraft.world.level.levelgen.feature.featuresize;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import com.mojang.serialization.Codec;

public class ThreeLayersFeatureSize extends FeatureSize {
    public static final Codec<ThreeLayersFeatureSize> CODEC;
    private final int limit;
    private final int upperLimit;
    private final int lowerSize;
    private final int middleSize;
    private final int upperSize;
    
    public ThreeLayersFeatureSize(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final OptionalInt optionalInt) {
        super(optionalInt);
        this.limit = integer1;
        this.upperLimit = integer2;
        this.lowerSize = integer3;
        this.middleSize = integer4;
        this.upperSize = integer5;
    }
    
    @Override
    protected FeatureSizeType<?> type() {
        return FeatureSizeType.THREE_LAYERS_FEATURE_SIZE;
    }
    
    @Override
    public int getSizeAtHeight(final int integer1, final int integer2) {
        if (integer2 < this.limit) {
            return this.lowerSize;
        }
        if (integer2 >= integer1 - this.upperLimit) {
            return this.upperSize;
        }
        return this.middleSize;
    }
    
    static {
        CODEC = RecordCodecBuilder.<ThreeLayersFeatureSize>create((java.util.function.Function<RecordCodecBuilder.Instance<ThreeLayersFeatureSize>, ? extends App<RecordCodecBuilder.Mu<ThreeLayersFeatureSize>, ThreeLayersFeatureSize>>)(instance -> instance.<Integer, Integer, Integer, Integer, Integer, OptionalInt>group(Codec.intRange(0, 80).fieldOf("limit").orElse(1).forGetter((java.util.function.Function<Object, Integer>)(cna -> cna.limit)), Codec.intRange(0, 80).fieldOf("upper_limit").orElse(1).forGetter((java.util.function.Function<Object, Integer>)(cna -> cna.upperLimit)), Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(cna -> cna.lowerSize)), Codec.intRange(0, 16).fieldOf("middle_size").orElse(1).forGetter((java.util.function.Function<Object, Integer>)(cna -> cna.middleSize)), Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter((java.util.function.Function<Object, Integer>)(cna -> cna.upperSize)), FeatureSize.minClippedHeightCodec()).<ThreeLayersFeatureSize>apply(instance, ThreeLayersFeatureSize::new)));
    }
}
