package net.minecraft.world.level.levelgen.feature.configurations;

import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.tags.BlockTags;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import com.mojang.serialization.Codec;

public class OreConfiguration implements FeatureConfiguration {
    public static final Codec<OreConfiguration> CODEC;
    public final RuleTest target;
    public final int size;
    public final BlockState state;
    
    public OreConfiguration(final RuleTest csr, final BlockState cee, final int integer) {
        this.size = integer;
        this.state = cee;
        this.target = csr;
    }
    
    static {
        CODEC = RecordCodecBuilder.<OreConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<OreConfiguration>, ? extends App<RecordCodecBuilder.Mu<OreConfiguration>, OreConfiguration>>)(instance -> instance.<RuleTest, BlockState, Integer>group(RuleTest.CODEC.fieldOf("target").forGetter((java.util.function.Function<Object, RuleTest>)(cmg -> cmg.target)), BlockState.CODEC.fieldOf("state").forGetter((java.util.function.Function<Object, BlockState>)(cmg -> cmg.state)), Codec.intRange(0, 64).fieldOf("size").forGetter((java.util.function.Function<Object, Integer>)(cmg -> cmg.size))).<OreConfiguration>apply(instance, OreConfiguration::new)));
    }
    
    public static final class Predicates {
        public static final RuleTest NATURAL_STONE;
        public static final RuleTest NETHERRACK;
        public static final RuleTest NETHER_ORE_REPLACEABLES;
        
        static {
            NATURAL_STONE = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
            NETHERRACK = new BlockMatchTest(Blocks.NETHERRACK);
            NETHER_ORE_REPLACEABLES = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
        }
    }
}
