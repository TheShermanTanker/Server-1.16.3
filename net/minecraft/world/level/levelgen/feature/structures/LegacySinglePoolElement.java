package net.minecraft.world.level.levelgen.feature.structures;

import java.util.function.Function;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public class LegacySinglePoolElement extends SinglePoolElement {
    public static final Codec<LegacySinglePoolElement> CODEC;
    
    protected LegacySinglePoolElement(final Either<ResourceLocation, StructureTemplate> either, final Supplier<StructureProcessorList> supplier, final StructureTemplatePool.Projection a) {
        super(either, supplier, a);
    }
    
    @Override
    protected StructurePlaceSettings getSettings(final Rotation bzj, final BoundingBox cqx, final boolean boolean3) {
        final StructurePlaceSettings csu5 = super.getSettings(bzj, cqx, boolean3);
        csu5.popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        csu5.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        return csu5;
    }
    
    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.LEGACY;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append("LegacySingle[").append(this.template).append("]").toString();
    }
    
    static {
        CODEC = RecordCodecBuilder.<LegacySinglePoolElement>create((java.util.function.Function<RecordCodecBuilder.Instance<LegacySinglePoolElement>, ? extends App<RecordCodecBuilder.Mu<LegacySinglePoolElement>, LegacySinglePoolElement>>)(instance -> instance.<Either<ResourceLocation, StructureTemplate>, Supplier<StructureProcessorList>, StructureTemplatePool.Projection>group(SinglePoolElement.templateCodec(), SinglePoolElement.processorsCodec(), StructurePoolElement.projectionCodec()).<LegacySinglePoolElement>apply(instance, LegacySinglePoolElement::new)));
    }
}
