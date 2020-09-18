package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.function.Function;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class BlockStateMatchTest extends RuleTest {
    public static final Codec<BlockStateMatchTest> CODEC;
    private final BlockState blockState;
    
    public BlockStateMatchTest(final BlockState cee) {
        this.blockState = cee;
    }
    
    @Override
    public boolean test(final BlockState cee, final Random random) {
        return cee == this.blockState;
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.BLOCKSTATE_TEST;
    }
    
    static {
        CODEC = BlockState.CODEC.fieldOf("block_state").<BlockStateMatchTest>xmap((java.util.function.Function<? super BlockState, ? extends BlockStateMatchTest>)BlockStateMatchTest::new, (java.util.function.Function<? super BlockStateMatchTest, ? extends BlockState>)(cse -> cse.blockState)).codec();
    }
}
