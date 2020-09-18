package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class NoneDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<NoneDecoratorConfiguration> CODEC;
    public static final NoneDecoratorConfiguration INSTANCE;
    
    static {
        CODEC = Codec.<NoneDecoratorConfiguration>unit((java.util.function.Supplier<NoneDecoratorConfiguration>)(() -> NoneDecoratorConfiguration.INSTANCE));
        INSTANCE = new NoneDecoratorConfiguration();
    }
}
