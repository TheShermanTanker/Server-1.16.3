package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class JigsawConfiguration implements FeatureConfiguration {
    public static final Codec<JigsawConfiguration> CODEC;
    private final Supplier<StructureTemplatePool> startPool;
    private final int maxDepth;
    
    public JigsawConfiguration(final Supplier<StructureTemplatePool> supplier, final int integer) {
        this.startPool = supplier;
        this.maxDepth = integer;
    }
    
    public int maxDepth() {
        return this.maxDepth;
    }
    
    public Supplier<StructureTemplatePool> startPool() {
        return this.startPool;
    }
    
    static {
        CODEC = RecordCodecBuilder.<JigsawConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<JigsawConfiguration>, ? extends App<RecordCodecBuilder.Mu<JigsawConfiguration>, JigsawConfiguration>>)(instance -> instance.<Supplier<StructureTemplatePool>, Integer>group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((java.util.function.Function<Object, Supplier<StructureTemplatePool>>)JigsawConfiguration::startPool), Codec.intRange(0, 7).fieldOf("size").forGetter((java.util.function.Function<Object, Integer>)JigsawConfiguration::maxDepth)).apply(instance, (java.util.function.BiFunction<Supplier<StructureTemplatePool>, Integer, Object>)JigsawConfiguration::new)));
    }
}
