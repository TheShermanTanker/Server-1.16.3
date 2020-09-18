package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class NoneFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<NoneFeatureConfiguration> CODEC;
    public static final NoneFeatureConfiguration INSTANCE;
    
    static {
        CODEC = Codec.<NoneFeatureConfiguration>unit((java.util.function.Supplier<NoneFeatureConfiguration>)(() -> NoneFeatureConfiguration.INSTANCE));
        INSTANCE = new NoneFeatureConfiguration();
    }
}
