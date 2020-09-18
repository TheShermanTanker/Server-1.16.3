package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import java.util.List;
import com.mojang.serialization.Codec;

public class SpikeConfiguration implements FeatureConfiguration {
    public static final Codec<SpikeConfiguration> CODEC;
    private final boolean crystalInvulnerable;
    private final List<SpikeFeature.EndSpike> spikes;
    @Nullable
    private final BlockPos crystalBeamTarget;
    
    public SpikeConfiguration(final boolean boolean1, final List<SpikeFeature.EndSpike> list, @Nullable final BlockPos fx) {
        this(boolean1, list, (Optional<BlockPos>)Optional.ofNullable(fx));
    }
    
    private SpikeConfiguration(final boolean boolean1, final List<SpikeFeature.EndSpike> list, final Optional<BlockPos> optional) {
        this.crystalInvulnerable = boolean1;
        this.spikes = list;
        this.crystalBeamTarget = (BlockPos)optional.orElse(null);
    }
    
    public boolean isCrystalInvulnerable() {
        return this.crystalInvulnerable;
    }
    
    public List<SpikeFeature.EndSpike> getSpikes() {
        return this.spikes;
    }
    
    @Nullable
    public BlockPos getCrystalBeamTarget() {
        return this.crystalBeamTarget;
    }
    
    static {
        CODEC = RecordCodecBuilder.<SpikeConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<SpikeConfiguration>, ? extends App<RecordCodecBuilder.Mu<SpikeConfiguration>, SpikeConfiguration>>)(instance -> instance.<Boolean, java.util.List<SpikeFeature.EndSpike>, java.util.Optional<BlockPos>>group(Codec.BOOL.fieldOf("crystal_invulnerable").orElse(false).forGetter((java.util.function.Function<Object, Boolean>)(cms -> cms.crystalInvulnerable)), SpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter((java.util.function.Function<Object, java.util.List<SpikeFeature.EndSpike>>)(cms -> cms.spikes)), BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter((java.util.function.Function<Object, java.util.Optional<BlockPos>>)(cms -> Optional.ofNullable(cms.crystalBeamTarget)))).<SpikeConfiguration>apply(instance, SpikeConfiguration::new)));
    }
}
