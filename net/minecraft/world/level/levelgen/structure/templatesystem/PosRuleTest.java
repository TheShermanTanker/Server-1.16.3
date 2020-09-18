package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.function.Function;
import net.minecraft.core.Registry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;

public abstract class PosRuleTest {
    public static final Codec<PosRuleTest> CODEC;
    
    public abstract boolean test(final BlockPos fx1, final BlockPos fx2, final BlockPos fx3, final Random random);
    
    protected abstract PosRuleTestType<?> getType();
    
    static {
        CODEC = Registry.POS_RULE_TEST.<PosRuleTest>dispatch("predicate_type", (java.util.function.Function<? super PosRuleTest, ?>)PosRuleTest::getType, (java.util.function.Function<? super Object, ? extends Codec<? extends PosRuleTest>>)PosRuleTestType::codec);
    }
}
