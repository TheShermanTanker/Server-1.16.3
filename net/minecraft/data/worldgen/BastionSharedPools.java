package net.minecraft.data.worldgen;

import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.resources.ResourceLocation;

public class BastionSharedPools {
    public static void bootstrap() {
    }
    
    static {
        Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/mobs/piglin"), new ResourceLocation("empty"), ImmutableList.of(Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/melee_piglin"), 1), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/sword_piglin"), 4), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/crossbow_piglin"), 4), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/empty"), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/mobs/hoglin"), new ResourceLocation("empty"), ImmutableList.of(Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/hoglin"), 2), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/empty"), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/blocks/gold"), new ResourceLocation("empty"), ImmutableList.of(Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/blocks/air"), 3), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/blocks/gold"), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/mobs/piglin_melee"), new ResourceLocation("empty"), ImmutableList.of(Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/melee_piglin_always"), 1), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/melee_piglin"), 5), Pair.<Function<StructureTemplatePool.Projection, SinglePoolElement>, Integer>of(StructurePoolElement.single("bastion/mobs/sword_piglin"), 1)), StructureTemplatePool.Projection.RIGID));
    }
}
