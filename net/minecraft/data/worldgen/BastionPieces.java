package net.minecraft.data.worldgen;

import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class BastionPieces {
    public static final StructureTemplatePool START;
    
    public static void bootstrap() {
        BastionHousingUnitsPools.bootstrap();
        BastionHoglinStablePools.bootstrap();
        BastionTreasureRoomPools.bootstrap();
        BastionBridgePools.bootstrap();
        BastionSharedPools.bootstrap();
    }
    
    static {
        START = Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/starts"), new ResourceLocation("empty"), ImmutableList.of(Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/units/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/hoglin_stable/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/treasure/big_air_full", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1)), StructureTemplatePool.Projection.RIGID));
    }
}
