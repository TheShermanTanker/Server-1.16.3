package net.minecraft.world.level.levelgen.placement;

import java.util.function.Function;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class ChanceDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<ChanceDecoratorConfiguration> CODEC;
    public final int chance;
    
    public ChanceDecoratorConfiguration(final int integer) {
        this.chance = integer;
    }
    
    static {
        CODEC = Codec.INT.fieldOf("chance").<ChanceDecoratorConfiguration>xmap((java.util.function.Function<? super Object, ? extends ChanceDecoratorConfiguration>)ChanceDecoratorConfiguration::new, (java.util.function.Function<? super ChanceDecoratorConfiguration, ?>)(cpk -> cpk.chance)).codec();
    }
}
