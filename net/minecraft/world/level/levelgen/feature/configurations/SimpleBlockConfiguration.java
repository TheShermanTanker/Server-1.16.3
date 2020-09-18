package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class SimpleBlockConfiguration implements FeatureConfiguration {
    public static final Codec<SimpleBlockConfiguration> CODEC;
    public final BlockState toPlace;
    public final List<BlockState> placeOn;
    public final List<BlockState> placeIn;
    public final List<BlockState> placeUnder;
    
    public SimpleBlockConfiguration(final BlockState cee, final List<BlockState> list2, final List<BlockState> list3, final List<BlockState> list4) {
        this.toPlace = cee;
        this.placeOn = list2;
        this.placeIn = list3;
        this.placeUnder = list4;
    }
    
    static {
        CODEC = RecordCodecBuilder.<SimpleBlockConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<SimpleBlockConfiguration>, ? extends App<RecordCodecBuilder.Mu<SimpleBlockConfiguration>, SimpleBlockConfiguration>>)(instance -> instance.<BlockState, java.util.List<BlockState>, java.util.List<BlockState>, java.util.List<BlockState>>group(BlockState.CODEC.fieldOf("to_place").forGetter((java.util.function.Function<Object, BlockState>)(cmq -> cmq.toPlace)), BlockState.CODEC.listOf().fieldOf("place_on").forGetter((java.util.function.Function<Object, java.util.List<BlockState>>)(cmq -> cmq.placeOn)), BlockState.CODEC.listOf().fieldOf("place_in").forGetter((java.util.function.Function<Object, java.util.List<BlockState>>)(cmq -> cmq.placeIn)), BlockState.CODEC.listOf().fieldOf("place_under").forGetter((java.util.function.Function<Object, java.util.List<BlockState>>)(cmq -> cmq.placeUnder))).<SimpleBlockConfiguration>apply(instance, SimpleBlockConfiguration::new)));
    }
}
