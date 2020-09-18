package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import net.minecraft.world.level.levelgen.feature.RuinedPortalFeature;
import com.mojang.serialization.Codec;

public class RuinedPortalConfiguration implements FeatureConfiguration {
    public static final Codec<RuinedPortalConfiguration> CODEC;
    public final RuinedPortalFeature.Type portalType;
    
    public RuinedPortalConfiguration(final RuinedPortalFeature.Type b) {
        this.portalType = b;
    }
    
    static {
        CODEC = RuinedPortalFeature.Type.CODEC.fieldOf("portal_type").<RuinedPortalConfiguration>xmap((java.util.function.Function<? super RuinedPortalFeature.Type, ? extends RuinedPortalConfiguration>)RuinedPortalConfiguration::new, (java.util.function.Function<? super RuinedPortalConfiguration, ? extends RuinedPortalFeature.Type>)(cmo -> cmo.portalType)).codec();
    }
}
