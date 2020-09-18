package net.minecraft.world.level.levelgen.placement;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.GenerationStep;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class CarvingMaskDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<CarvingMaskDecoratorConfiguration> CODEC;
    protected final GenerationStep.Carving step;
    protected final float probability;
    
    public CarvingMaskDecoratorConfiguration(final GenerationStep.Carving a, final float float2) {
        this.step = a;
        this.probability = float2;
    }
    
    static {
        CODEC = RecordCodecBuilder.<CarvingMaskDecoratorConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<CarvingMaskDecoratorConfiguration>, ? extends App<RecordCodecBuilder.Mu<CarvingMaskDecoratorConfiguration>, CarvingMaskDecoratorConfiguration>>)(instance -> instance.<GenerationStep.Carving, Object>group(GenerationStep.Carving.CODEC.fieldOf("step").forGetter((java.util.function.Function<Object, GenerationStep.Carving>)(cpi -> cpi.step)), Codec.FLOAT.fieldOf("probability").forGetter((java.util.function.Function<Object, Object>)(cpi -> cpi.probability))).apply(instance, (java.util.function.BiFunction<GenerationStep.Carving, Object, Object>)CarvingMaskDecoratorConfiguration::new)));
    }
}
