package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.TagCollection;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import com.mojang.serialization.Codec;

public class TagMatchTest extends RuleTest {
    public static final Codec<TagMatchTest> CODEC;
    private final Tag<Block> tag;
    
    public TagMatchTest(final Tag<Block> aej) {
        this.tag = aej;
    }
    
    @Override
    public boolean test(final BlockState cee, final Random random) {
        return cee.is(this.tag);
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.TAG_TEST;
    }
    
    static {
        CODEC = Tag.codec((java.util.function.Supplier<TagCollection<Object>>)(() -> SerializationTags.getInstance().getBlocks())).fieldOf("tag").<TagMatchTest>xmap((java.util.function.Function<? super Tag<Object>, ? extends TagMatchTest>)TagMatchTest::new, (java.util.function.Function<? super TagMatchTest, ? extends Tag<Object>>)(csz -> csz.tag)).codec();
    }
}
