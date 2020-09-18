package net.minecraft.world.level.levelgen.feature.featuresize;

import net.minecraft.core.Registry;
import java.util.Optional;
import java.util.function.Function;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import com.mojang.serialization.Codec;

public abstract class FeatureSize {
    public static final Codec<FeatureSize> CODEC;
    protected final OptionalInt minClippedHeight;
    
    protected static <S extends FeatureSize> RecordCodecBuilder<S, OptionalInt> minClippedHeightCodec() {
        return Codec.intRange(0, 80).optionalFieldOf("min_clipped_height").<OptionalInt>xmap((java.util.function.Function<? super java.util.Optional<Integer>, ? extends OptionalInt>)(optional -> (OptionalInt)optional.map(OptionalInt::of).orElse(OptionalInt.empty())), (java.util.function.Function<? super OptionalInt, ? extends java.util.Optional<Integer>>)(optionalInt -> optionalInt.isPresent() ? Optional.of(optionalInt.getAsInt()) : Optional.empty())).<S>forGetter((java.util.function.Function<S, OptionalInt>)(cmy -> cmy.minClippedHeight));
    }
    
    public FeatureSize(final OptionalInt optionalInt) {
        this.minClippedHeight = optionalInt;
    }
    
    protected abstract FeatureSizeType<?> type();
    
    public abstract int getSizeAtHeight(final int integer1, final int integer2);
    
    public OptionalInt minClippedHeight() {
        return this.minClippedHeight;
    }
    
    static {
        CODEC = Registry.FEATURE_SIZE_TYPES.<FeatureSize>dispatch((java.util.function.Function<? super FeatureSize, ?>)FeatureSize::type, (java.util.function.Function<? super Object, ? extends Codec<? extends FeatureSize>>)FeatureSizeType::codec);
    }
}
