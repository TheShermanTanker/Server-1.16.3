package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class LayerConfiguration implements FeatureConfiguration {
    public static final Codec<LayerConfiguration> CODEC;
    public final int height;
    public final BlockState state;
    
    public LayerConfiguration(final int integer, final BlockState cee) {
        this.height = integer;
        this.state = cee;
    }
    
    static {
        CODEC = RecordCodecBuilder.<LayerConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<LayerConfiguration>, ? extends App<RecordCodecBuilder.Mu<LayerConfiguration>, LayerConfiguration>>)(instance -> instance.<Integer, BlockState>group(Codec.intRange(0, 255).fieldOf("height").forGetter((java.util.function.Function<Object, Integer>)(cma -> cma.height)), BlockState.CODEC.fieldOf("state").forGetter((java.util.function.Function<Object, BlockState>)(cma -> cma.state))).apply(instance, (java.util.function.BiFunction<Integer, BlockState, Object>)LayerConfiguration::new)));
    }
}
