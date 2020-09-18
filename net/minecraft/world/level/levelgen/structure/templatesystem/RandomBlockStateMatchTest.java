package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class RandomBlockStateMatchTest extends RuleTest {
    public static final Codec<RandomBlockStateMatchTest> CODEC;
    private final BlockState blockState;
    private final float probability;
    
    public RandomBlockStateMatchTest(final BlockState cee, final float float2) {
        this.blockState = cee;
        this.probability = float2;
    }
    
    @Override
    public boolean test(final BlockState cee, final Random random) {
        return cee == this.blockState && random.nextFloat() < this.probability;
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.RANDOM_BLOCKSTATE_TEST;
    }
    
    static {
        CODEC = RecordCodecBuilder.<RandomBlockStateMatchTest>create((java.util.function.Function<RecordCodecBuilder.Instance<RandomBlockStateMatchTest>, ? extends App<RecordCodecBuilder.Mu<RandomBlockStateMatchTest>, RandomBlockStateMatchTest>>)(instance -> instance.<BlockState, Object>group(BlockState.CODEC.fieldOf("block_state").forGetter((java.util.function.Function<Object, BlockState>)(csp -> csp.blockState)), Codec.FLOAT.fieldOf("probability").forGetter((java.util.function.Function<Object, Object>)(csp -> csp.probability))).apply(instance, (java.util.function.BiFunction<BlockState, Object, Object>)RandomBlockStateMatchTest::new)));
    }
}
