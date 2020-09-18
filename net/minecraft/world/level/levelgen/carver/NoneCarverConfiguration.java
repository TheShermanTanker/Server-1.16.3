package net.minecraft.world.level.levelgen.carver;

import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class NoneCarverConfiguration implements CarverConfiguration {
    public static final Codec<NoneCarverConfiguration> CODEC;
    public static final NoneCarverConfiguration INSTANCE;
    
    static {
        CODEC = Codec.<NoneCarverConfiguration>unit((java.util.function.Supplier<NoneCarverConfiguration>)(() -> NoneCarverConfiguration.INSTANCE));
        INSTANCE = new NoneCarverConfiguration();
    }
}
