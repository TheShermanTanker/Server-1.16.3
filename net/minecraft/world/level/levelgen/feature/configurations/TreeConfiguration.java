package net.minecraft.world.level.levelgen.feature.configurations;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class TreeConfiguration implements FeatureConfiguration {
    public static final Codec<TreeConfiguration> CODEC;
    public final BlockStateProvider trunkProvider;
    public final BlockStateProvider leavesProvider;
    public final List<TreeDecorator> decorators;
    public transient boolean fromSapling;
    public final FoliagePlacer foliagePlacer;
    public final TrunkPlacer trunkPlacer;
    public final FeatureSize minimumSize;
    public final int maxWaterDepth;
    public final boolean ignoreVines;
    public final Heightmap.Types heightmap;
    
    protected TreeConfiguration(final BlockStateProvider cnq1, final BlockStateProvider cnq2, final FoliagePlacer cni, final TrunkPlacer coy, final FeatureSize cmy, final List<TreeDecorator> list, final int integer, final boolean boolean8, final Heightmap.Types a) {
        this.trunkProvider = cnq1;
        this.leavesProvider = cnq2;
        this.decorators = list;
        this.foliagePlacer = cni;
        this.minimumSize = cmy;
        this.trunkPlacer = coy;
        this.maxWaterDepth = integer;
        this.ignoreVines = boolean8;
        this.heightmap = a;
    }
    
    public void setFromSapling() {
        this.fromSapling = true;
    }
    
    public TreeConfiguration withDecorators(final List<TreeDecorator> list) {
        return new TreeConfiguration(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, list, this.maxWaterDepth, this.ignoreVines, this.heightmap);
    }
    
    static {
        CODEC = RecordCodecBuilder.<TreeConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<TreeConfiguration>, ? extends App<RecordCodecBuilder.Mu<TreeConfiguration>, TreeConfiguration>>)(instance -> instance.<BlockStateProvider, BlockStateProvider, FoliagePlacer, TrunkPlacer, FeatureSize, java.util.List<TreeDecorator>, Integer, Boolean, Heightmap.Types>group(BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter((java.util.function.Function<Object, BlockStateProvider>)(cmw -> cmw.trunkProvider)), BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter((java.util.function.Function<Object, BlockStateProvider>)(cmw -> cmw.leavesProvider)), FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter((java.util.function.Function<Object, FoliagePlacer>)(cmw -> cmw.foliagePlacer)), TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter((java.util.function.Function<Object, TrunkPlacer>)(cmw -> cmw.trunkPlacer)), FeatureSize.CODEC.fieldOf("minimum_size").forGetter((java.util.function.Function<Object, FeatureSize>)(cmw -> cmw.minimumSize)), TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter((java.util.function.Function<Object, java.util.List<TreeDecorator>>)(cmw -> cmw.decorators)), Codec.INT.fieldOf("max_water_depth").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(cmw -> cmw.maxWaterDepth)), Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter((java.util.function.Function<Object, Boolean>)(cmw -> cmw.ignoreVines)), Heightmap.Types.CODEC.fieldOf("heightmap").forGetter((java.util.function.Function<Object, Heightmap.Types>)(cmw -> cmw.heightmap))).<TreeConfiguration>apply(instance, TreeConfiguration::new)));
    }
    
    public static class TreeConfigurationBuilder {
        public final BlockStateProvider trunkProvider;
        public final BlockStateProvider leavesProvider;
        private final FoliagePlacer foliagePlacer;
        private final TrunkPlacer trunkPlacer;
        private final FeatureSize minimumSize;
        private List<TreeDecorator> decorators;
        private int maxWaterDepth;
        private boolean ignoreVines;
        private Heightmap.Types heightmap;
        
        public TreeConfigurationBuilder(final BlockStateProvider cnq1, final BlockStateProvider cnq2, final FoliagePlacer cni, final TrunkPlacer coy, final FeatureSize cmy) {
            this.decorators = ImmutableList.of();
            this.heightmap = Heightmap.Types.OCEAN_FLOOR;
            this.trunkProvider = cnq1;
            this.leavesProvider = cnq2;
            this.foliagePlacer = cni;
            this.trunkPlacer = coy;
            this.minimumSize = cmy;
        }
        
        public TreeConfigurationBuilder decorators(final List<TreeDecorator> list) {
            this.decorators = list;
            return this;
        }
        
        public TreeConfigurationBuilder maxWaterDepth(final int integer) {
            this.maxWaterDepth = integer;
            return this;
        }
        
        public TreeConfigurationBuilder ignoreVines() {
            this.ignoreVines = true;
            return this;
        }
        
        public TreeConfigurationBuilder heightmap(final Heightmap.Types a) {
            this.heightmap = a;
            return this;
        }
        
        public TreeConfiguration build() {
            return new TreeConfiguration(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, this.decorators, this.maxWaterDepth, this.ignoreVines, this.heightmap);
        }
    }
}
