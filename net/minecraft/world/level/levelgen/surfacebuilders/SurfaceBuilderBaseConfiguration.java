package net.minecraft.world.level.levelgen.surfacebuilders;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class SurfaceBuilderBaseConfiguration implements SurfaceBuilderConfiguration {
    public static final Codec<SurfaceBuilderBaseConfiguration> CODEC;
    private final BlockState topMaterial;
    private final BlockState underMaterial;
    private final BlockState underwaterMaterial;
    
    public SurfaceBuilderBaseConfiguration(final BlockState cee1, final BlockState cee2, final BlockState cee3) {
        this.topMaterial = cee1;
        this.underMaterial = cee2;
        this.underwaterMaterial = cee3;
    }
    
    public BlockState getTopMaterial() {
        return this.topMaterial;
    }
    
    public BlockState getUnderMaterial() {
        return this.underMaterial;
    }
    
    public BlockState getUnderwaterMaterial() {
        return this.underwaterMaterial;
    }
    
    static {
        CODEC = RecordCodecBuilder.<SurfaceBuilderBaseConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<SurfaceBuilderBaseConfiguration>, ? extends App<RecordCodecBuilder.Mu<SurfaceBuilderBaseConfiguration>, SurfaceBuilderBaseConfiguration>>)(instance -> instance.<BlockState, BlockState, BlockState>group(BlockState.CODEC.fieldOf("top_material").forGetter((java.util.function.Function<Object, BlockState>)(ctr -> ctr.topMaterial)), BlockState.CODEC.fieldOf("under_material").forGetter((java.util.function.Function<Object, BlockState>)(ctr -> ctr.underMaterial)), BlockState.CODEC.fieldOf("underwater_material").forGetter((java.util.function.Function<Object, BlockState>)(ctr -> ctr.underwaterMaterial))).<SurfaceBuilderBaseConfiguration>apply(instance, SurfaceBuilderBaseConfiguration::new)));
    }
}
