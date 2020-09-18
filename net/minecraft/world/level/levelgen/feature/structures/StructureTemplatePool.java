package net.minecraft.world.level.levelgen.feature.structures;

import java.util.stream.Collectors;
import java.util.Arrays;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import java.util.Map;
import net.minecraft.util.StringRepresentable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.Registry;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.function.Function;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.Logger;

public class StructureTemplatePool {
    private static final Logger LOGGER;
    public static final Codec<StructureTemplatePool> DIRECT_CODEC;
    public static final Codec<Supplier<StructureTemplatePool>> CODEC;
    private final ResourceLocation name;
    private final List<Pair<StructurePoolElement, Integer>> rawTemplates;
    private final List<StructurePoolElement> templates;
    private final ResourceLocation fallback;
    private int maxSize;
    
    public StructureTemplatePool(final ResourceLocation vk1, final ResourceLocation vk2, final List<Pair<StructurePoolElement, Integer>> list) {
        this.maxSize = Integer.MIN_VALUE;
        this.name = vk1;
        this.rawTemplates = list;
        this.templates = Lists.newArrayList();
        for (final Pair<StructurePoolElement, Integer> pair6 : list) {
            final StructurePoolElement cof7 = pair6.getFirst();
            for (int integer8 = 0; integer8 < pair6.getSecond(); ++integer8) {
                this.templates.add(cof7);
            }
        }
        this.fallback = vk2;
    }
    
    public StructureTemplatePool(final ResourceLocation vk1, final ResourceLocation vk2, final List<Pair<Function<Projection, ? extends StructurePoolElement>, Integer>> list, final Projection a) {
        this.maxSize = Integer.MIN_VALUE;
        this.name = vk1;
        this.rawTemplates = Lists.newArrayList();
        this.templates = Lists.newArrayList();
        for (final Pair<Function<Projection, ? extends StructurePoolElement>, Integer> pair7 : list) {
            final StructurePoolElement cof8 = (StructurePoolElement)pair7.getFirst().apply(a);
            this.rawTemplates.add(Pair.<StructurePoolElement, Integer>of(cof8, pair7.getSecond()));
            for (int integer9 = 0; integer9 < pair7.getSecond(); ++integer9) {
                this.templates.add(cof8);
            }
        }
        this.fallback = vk2;
    }
    
    public int getMaxSize(final StructureManager cst) {
        if (this.maxSize == Integer.MIN_VALUE) {
            this.maxSize = this.templates.stream().mapToInt(cof -> cof.getBoundingBox(cst, BlockPos.ZERO, Rotation.NONE).getYSpan()).max().orElse(0);
        }
        return this.maxSize;
    }
    
    public ResourceLocation getFallback() {
        return this.fallback;
    }
    
    public StructurePoolElement getRandomTemplate(final Random random) {
        return (StructurePoolElement)this.templates.get(random.nextInt(this.templates.size()));
    }
    
    public List<StructurePoolElement> getShuffledTemplates(final Random random) {
        return ImmutableList.copyOf((Object[])ObjectArrays.<E>shuffle(this.templates.toArray((Object[])new StructurePoolElement[0]), random));
    }
    
    public ResourceLocation getName() {
        return this.name;
    }
    
    public int size() {
        return this.templates.size();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DIRECT_CODEC = RecordCodecBuilder.<StructureTemplatePool>create((java.util.function.Function<RecordCodecBuilder.Instance<StructureTemplatePool>, ? extends App<RecordCodecBuilder.Mu<StructureTemplatePool>, StructureTemplatePool>>)(instance -> instance.<ResourceLocation, ResourceLocation, java.util.List<Pair<StructurePoolElement, Integer>>>group(ResourceLocation.CODEC.fieldOf("name").forGetter((java.util.function.Function<Object, ResourceLocation>)StructureTemplatePool::getName), ResourceLocation.CODEC.fieldOf("fallback").forGetter((java.util.function.Function<Object, ResourceLocation>)StructureTemplatePool::getFallback), Codec.<StructurePoolElement, Integer>mapPair(StructurePoolElement.CODEC.fieldOf("element"), Codec.INT.fieldOf("weight")).codec().listOf().promotePartial(Util.prefix("Pool element: ", (Consumer<String>)StructureTemplatePool.LOGGER::error)).fieldOf("elements").forGetter((java.util.function.Function<Object, java.util.List<Pair<StructurePoolElement, Integer>>>)(coh -> coh.rawTemplates))).<StructureTemplatePool>apply(instance, StructureTemplatePool::new)));
        CODEC = RegistryFileCodec.<StructureTemplatePool>create(Registry.TEMPLATE_POOL_REGISTRY, StructureTemplatePool.DIRECT_CODEC);
    }
    
    public enum Projection implements StringRepresentable {
        TERRAIN_MATCHING("terrain_matching", ImmutableList.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1))), 
        RIGID("rigid", ImmutableList.<StructureProcessor>of());
        
        public static final Codec<Projection> CODEC;
        private static final Map<String, Projection> BY_NAME;
        private final String name;
        private final ImmutableList<StructureProcessor> processors;
        
        private Projection(final String string3, final ImmutableList<StructureProcessor> immutableList) {
            this.name = string3;
            this.processors = immutableList;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Projection byName(final String string) {
            return (Projection)Projection.BY_NAME.get(string);
        }
        
        public ImmutableList<StructureProcessor> getProcessors() {
            return this.processors;
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            CODEC = StringRepresentable.<Projection>fromEnum((java.util.function.Supplier<Projection[]>)Projection::values, (java.util.function.Function<? super String, ? extends Projection>)Projection::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Projection::getName, a -> a));
        }
    }
}
