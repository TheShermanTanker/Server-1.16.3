package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.function.Function;
import net.minecraft.core.Registry;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public abstract class RuleTest {
    public static final Codec<RuleTest> CODEC;
    
    public abstract boolean test(final BlockState cee, final Random random);
    
    protected abstract RuleTestType<?> getType();
    
    static {
        CODEC = Registry.RULE_TEST.<RuleTest>dispatch("predicate_type", (java.util.function.Function<? super RuleTest, ?>)RuleTest::getType, (java.util.function.Function<? super Object, ? extends Codec<? extends RuleTest>>)RuleTestType::codec);
    }
}
