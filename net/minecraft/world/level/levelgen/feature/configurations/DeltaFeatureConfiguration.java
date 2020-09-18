package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class DeltaFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<DeltaFeatureConfiguration> CODEC;
    private final BlockState contents;
    private final BlockState rim;
    private final UniformInt size;
    private final UniformInt rimSize;
    
    public DeltaFeatureConfiguration(final BlockState cee1, final BlockState cee2, final UniformInt aft3, final UniformInt aft4) {
        this.contents = cee1;
        this.rim = cee2;
        this.size = aft3;
        this.rimSize = aft4;
    }
    
    public BlockState contents() {
        return this.contents;
    }
    
    public BlockState rim() {
        return this.rim;
    }
    
    public UniformInt size() {
        return this.size;
    }
    
    public UniformInt rimSize() {
        return this.rimSize;
    }
    
    static {
        CODEC = RecordCodecBuilder.<DeltaFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<DeltaFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<DeltaFeatureConfiguration>, DeltaFeatureConfiguration>>)(instance -> instance.<BlockState, BlockState, UniformInt, UniformInt>group(BlockState.CODEC.fieldOf("contents").forGetter((java.util.function.Function<Object, BlockState>)(clu -> clu.contents)), BlockState.CODEC.fieldOf("rim").forGetter((java.util.function.Function<Object, BlockState>)(clu -> clu.rim)), UniformInt.codec(0, 8, 8).fieldOf("size").forGetter((java.util.function.Function<Object, UniformInt>)(clu -> clu.size)), UniformInt.codec(0, 8, 8).fieldOf("rim_size").forGetter((java.util.function.Function<Object, UniformInt>)(clu -> clu.rimSize))).<DeltaFeatureConfiguration>apply(instance, DeltaFeatureConfiguration::new)));
    }
}
