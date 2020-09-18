package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import com.mojang.serialization.Codec;

public class ShipwreckConfiguration implements FeatureConfiguration {
    public static final Codec<ShipwreckConfiguration> CODEC;
    public final boolean isBeached;
    
    public ShipwreckConfiguration(final boolean boolean1) {
        this.isBeached = boolean1;
    }
    
    static {
        CODEC = Codec.BOOL.fieldOf("is_beached").orElse(false).<ShipwreckConfiguration>xmap((java.util.function.Function<? super Boolean, ? extends ShipwreckConfiguration>)ShipwreckConfiguration::new, (java.util.function.Function<? super ShipwreckConfiguration, ? extends Boolean>)(cmp -> cmp.isBeached)).codec();
    }
}
