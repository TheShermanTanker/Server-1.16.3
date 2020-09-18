package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.function.Supplier;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class AlwaysTrueTest extends RuleTest {
    public static final Codec<AlwaysTrueTest> CODEC;
    public static final AlwaysTrueTest INSTANCE;
    
    private AlwaysTrueTest() {
    }
    
    @Override
    public boolean test(final BlockState cee, final Random random) {
        return true;
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.ALWAYS_TRUE_TEST;
    }
    
    static {
        CODEC = Codec.<AlwaysTrueTest>unit((java.util.function.Supplier<AlwaysTrueTest>)(() -> AlwaysTrueTest.INSTANCE));
        INSTANCE = new AlwaysTrueTest();
    }
}
