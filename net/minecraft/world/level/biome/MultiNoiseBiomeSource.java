package net.minecraft.world.level.biome;

import com.google.common.collect.Maps;
import java.util.Map;
import com.mojang.datafixers.util.Function3;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryLookupCodec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import com.google.common.collect.ImmutableList;
import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import com.mojang.datafixers.util.Either;
import java.util.Objects;
import net.minecraft.data.worldgen.biome.Biomes;
import java.util.Comparator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import java.util.Optional;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class MultiNoiseBiomeSource extends BiomeSource {
    private static final NoiseParameters DEFAULT_NOISE_PARAMETERS;
    public static final MapCodec<MultiNoiseBiomeSource> DIRECT_CODEC;
    public static final Codec<MultiNoiseBiomeSource> CODEC;
    private final NoiseParameters temperatureParams;
    private final NoiseParameters humidityParams;
    private final NoiseParameters altitudeParams;
    private final NoiseParameters weirdnessParams;
    private final NormalNoise temperatureNoise;
    private final NormalNoise humidityNoise;
    private final NormalNoise altitudeNoise;
    private final NormalNoise weirdnessNoise;
    private final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> parameters;
    private final boolean useY;
    private final long seed;
    private final Optional<Pair<Registry<Biome>, Preset>> preset;
    
    private MultiNoiseBiomeSource(final long long1, final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> list, final Optional<Pair<Registry<Biome>, Preset>> optional) {
        this(long1, list, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, MultiNoiseBiomeSource.DEFAULT_NOISE_PARAMETERS, optional);
    }
    
    private MultiNoiseBiomeSource(final long long1, final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> list, final NoiseParameters a3, final NoiseParameters a4, final NoiseParameters a5, final NoiseParameters a6) {
        this(long1, list, a3, a4, a5, a6, (Optional<Pair<Registry<Biome>, Preset>>)Optional.empty());
    }
    
    private MultiNoiseBiomeSource(final long long1, final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> list, final NoiseParameters a3, final NoiseParameters a4, final NoiseParameters a5, final NoiseParameters a6, final Optional<Pair<Registry<Biome>, Preset>> optional) {
        super((Stream<Supplier<Biome>>)list.stream().map(Pair::getSecond));
        this.seed = long1;
        this.preset = optional;
        this.temperatureParams = a3;
        this.humidityParams = a4;
        this.altitudeParams = a5;
        this.weirdnessParams = a6;
        this.temperatureNoise = NormalNoise.create(new WorldgenRandom(long1), a3.firstOctave(), a3.amplitudes());
        this.humidityNoise = NormalNoise.create(new WorldgenRandom(long1 + 1L), a4.firstOctave(), a4.amplitudes());
        this.altitudeNoise = NormalNoise.create(new WorldgenRandom(long1 + 2L), a5.firstOctave(), a5.amplitudes());
        this.weirdnessNoise = NormalNoise.create(new WorldgenRandom(long1 + 3L), a6.firstOctave(), a6.amplitudes());
        this.parameters = list;
        this.useY = false;
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return MultiNoiseBiomeSource.CODEC;
    }
    
    private Optional<PresetInstance> preset() {
        return (Optional<PresetInstance>)this.preset.map(pair -> new PresetInstance((Preset)pair.getSecond(), (Registry)pair.getFirst(), this.seed));
    }
    
    public Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        final int integer4 = this.useY ? integer2 : 0;
        final Biome.ClimateParameters c6 = new Biome.ClimateParameters((float)this.temperatureNoise.getValue(integer1, integer4, integer3), (float)this.humidityNoise.getValue(integer1, integer4, integer3), (float)this.altitudeNoise.getValue(integer1, integer4, integer3), (float)this.weirdnessNoise.getValue(integer1, integer4, integer3), 0.0f);
        return (Biome)this.parameters.stream().min(Comparator.comparing(pair -> pair.getFirst().fitness(c6))).map(Pair::getSecond).map(Supplier::get).orElse(Biomes.THE_VOID);
    }
    
    public boolean stable(final long long1) {
        return this.seed == long1 && this.preset.isPresent() && Objects.equals(((Pair)this.preset.get()).getSecond(), Preset.NETHER);
    }
    
    static {
        DEFAULT_NOISE_PARAMETERS = new NoiseParameters(-7, ImmutableList.of(1.0, 1.0));
        DIRECT_CODEC = RecordCodecBuilder.<MultiNoiseBiomeSource>mapCodec((java.util.function.Function<RecordCodecBuilder.Instance<MultiNoiseBiomeSource>, ? extends App<RecordCodecBuilder.Mu<MultiNoiseBiomeSource>, MultiNoiseBiomeSource>>)(instance -> instance.<Long, java.util.List<Object>, NoiseParameters, NoiseParameters, NoiseParameters, NoiseParameters>group(Codec.LONG.fieldOf("seed").forGetter((java.util.function.Function<Object, Object>)(bte -> bte.seed)), RecordCodecBuilder.create((java.util.function.Function<RecordCodecBuilder.Instance<Object>, ? extends App<RecordCodecBuilder.Mu<Object>, Object>>)(instance -> instance.<Biome.ClimateParameters, Supplier<Biome>>group(Biome.ClimateParameters.CODEC.fieldOf("parameters").forGetter((java.util.function.Function<Object, Biome.ClimateParameters>)Pair::getFirst), Biome.CODEC.fieldOf("biome").forGetter((java.util.function.Function<Object, Supplier<Biome>>)Pair::getSecond)).apply(instance, (java.util.function.BiFunction<Biome.ClimateParameters, Supplier<Biome>, Object>)Pair::of))).listOf().fieldOf("biomes").forGetter((java.util.function.Function<Object, java.util.List<Object>>)(bte -> bte.parameters)), NoiseParameters.CODEC.fieldOf("temperature_noise").forGetter((java.util.function.Function<Object, NoiseParameters>)(bte -> bte.temperatureParams)), NoiseParameters.CODEC.fieldOf("humidity_noise").forGetter((java.util.function.Function<Object, NoiseParameters>)(bte -> bte.humidityParams)), NoiseParameters.CODEC.fieldOf("altitude_noise").forGetter((java.util.function.Function<Object, NoiseParameters>)(bte -> bte.altitudeParams)), NoiseParameters.CODEC.fieldOf("weirdness_noise").forGetter((java.util.function.Function<Object, NoiseParameters>)(bte -> bte.weirdnessParams))).<MultiNoiseBiomeSource>apply(instance, MultiNoiseBiomeSource::new)));
        CODEC = Codec.<PresetInstance, MultiNoiseBiomeSource>mapEither(PresetInstance.CODEC, MultiNoiseBiomeSource.DIRECT_CODEC).<MultiNoiseBiomeSource>xmap((java.util.function.Function<? super Either<PresetInstance, MultiNoiseBiomeSource>, ? extends MultiNoiseBiomeSource>)(either -> either.<MultiNoiseBiomeSource>map(PresetInstance::biomeSource, Function.identity())), (java.util.function.Function<? super MultiNoiseBiomeSource, ? extends Either<PresetInstance, MultiNoiseBiomeSource>>)(bte -> (Either)bte.preset().map(Either::left).orElseGet(() -> Either.<Object, MultiNoiseBiomeSource>right(bte)))).codec();
    }
    
    static class NoiseParameters {
        private final int firstOctave;
        private final DoubleList amplitudes;
        public static final Codec<NoiseParameters> CODEC;
        
        public NoiseParameters(final int integer, final List<Double> list) {
            this.firstOctave = integer;
            this.amplitudes = new DoubleArrayList(list);
        }
        
        public int firstOctave() {
            return this.firstOctave;
        }
        
        public DoubleList amplitudes() {
            return this.amplitudes;
        }
        
        static {
            CODEC = RecordCodecBuilder.<NoiseParameters>create((java.util.function.Function<RecordCodecBuilder.Instance<NoiseParameters>, ? extends App<RecordCodecBuilder.Mu<NoiseParameters>, NoiseParameters>>)(instance -> instance.<Object, java.util.List<Object>>group(Codec.INT.fieldOf("firstOctave").forGetter((java.util.function.Function<Object, Object>)NoiseParameters::firstOctave), Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter((java.util.function.Function<Object, java.util.List<Object>>)NoiseParameters::amplitudes)).apply(instance, (java.util.function.BiFunction<Object, java.util.List<Object>, Object>)NoiseParameters::new)));
        }
    }
    
    static final class PresetInstance {
        public static final MapCodec<PresetInstance> CODEC;
        private final Preset preset;
        private final Registry<Biome> biomes;
        private final long seed;
        
        private PresetInstance(final Preset b, final Registry<Biome> gm, final long long3) {
            this.preset = b;
            this.biomes = gm;
            this.seed = long3;
        }
        
        public Preset preset() {
            return this.preset;
        }
        
        public Registry<Biome> biomes() {
            return this.biomes;
        }
        
        public long seed() {
            return this.seed;
        }
        
        public MultiNoiseBiomeSource biomeSource() {
            return this.preset.biomeSource(this.biomes, this.seed);
        }
        
        static {
            CODEC = RecordCodecBuilder.<PresetInstance>mapCodec((java.util.function.Function<RecordCodecBuilder.Instance<PresetInstance>, ? extends App<RecordCodecBuilder.Mu<PresetInstance>, PresetInstance>>)(instance -> instance.<Object, Registry<Object>, Object>group(ResourceLocation.CODEC.flatXmap((java.util.function.Function<? super ResourceLocation, ? extends DataResult<?>>)(vk -> (DataResult)Optional.ofNullable(Preset.BY_NAME.get(vk)).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown preset: ").append(vk).toString()))), (java.util.function.Function<? super Object, ? extends DataResult<? extends ResourceLocation>>)(b -> DataResult.<ResourceLocation>success(b.name))).fieldOf("preset").stable().forGetter((java.util.function.Function<Object, Object>)PresetInstance::preset), RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((java.util.function.Function<Object, Registry<Object>>)PresetInstance::biomes), Codec.LONG.fieldOf("seed").stable().forGetter((java.util.function.Function<Object, Object>)PresetInstance::seed)).apply(instance, instance.stable(PresetInstance::new))));
        }
    }
    
    public static class Preset {
        private static final Map<ResourceLocation, Preset> BY_NAME;
        public static final Preset NETHER;
        private final ResourceLocation name;
        private final Function3<Preset, Registry<Biome>, Long, MultiNoiseBiomeSource> biomeSource;
        
        public Preset(final ResourceLocation vk, final Function3<Preset, Registry<Biome>, Long, MultiNoiseBiomeSource> function3) {
            this.name = vk;
            this.biomeSource = function3;
            Preset.BY_NAME.put(vk, this);
        }
        
        public MultiNoiseBiomeSource biomeSource(final Registry<Biome> gm, final long long2) {
            return this.biomeSource.apply(this, gm, long2);
        }
        
        static {
            BY_NAME = (Map)Maps.newHashMap();
            final MultiNoiseBiomeSource multiNoiseBiomeSource;
            NETHER = new Preset(new ResourceLocation("nether"), (b, gm, long3) -> {
                new MultiNoiseBiomeSource(long3, (List)ImmutableList.<Pair<Biome.ClimateParameters, Object>>of(Pair.<Biome.ClimateParameters, Object>of(new Biome.ClimateParameters(0.0f, 0.0f, 0.0f, 0.0f, 0.0f), (() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.NETHER_WASTES))), Pair.<Biome.ClimateParameters, Object>of(new Biome.ClimateParameters(0.0f, -0.5f, 0.0f, 0.0f, 0.0f), (() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.SOUL_SAND_VALLEY))), Pair.<Biome.ClimateParameters, Object>of(new Biome.ClimateParameters(0.4f, 0.0f, 0.0f, 0.0f, 0.0f), (() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.CRIMSON_FOREST))), Pair.<Biome.ClimateParameters, Object>of(new Biome.ClimateParameters(0.0f, 0.5f, 0.0f, 0.0f, 0.375f), (() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.WARPED_FOREST))), Pair.<Biome.ClimateParameters, Object>of(new Biome.ClimateParameters(-0.5f, 0.0f, 0.0f, 0.0f, 0.175f), (() -> gm.getOrThrow(net.minecraft.world.level.biome.Biomes.BASALT_DELTAS)))), Optional.of(Pair.<Registry, Preset>of(gm, b)), null);
                return multiNoiseBiomeSource;
            });
        }
    }
}
