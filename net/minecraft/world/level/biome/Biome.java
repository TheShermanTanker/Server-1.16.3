package net.minecraft.world.level.biome;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import java.util.Arrays;
import net.minecraft.util.StringRepresentable;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Iterator;
import net.minecraft.world.level.levelgen.feature.Feature;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.core.SectionPos;
import java.util.Collections;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import java.util.Map;
import java.util.List;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.Logger;

public final class Biome {
    public static final Logger LOGGER;
    public static final Codec<Biome> DIRECT_CODEC;
    public static final Codec<Biome> NETWORK_CODEC;
    public static final Codec<Supplier<Biome>> CODEC;
    public static final Codec<List<Supplier<Biome>>> LIST_CODEC;
    private final Map<Integer, List<StructureFeature<?>>> structuresByStep;
    private static final PerlinSimplexNoise TEMPERATURE_NOISE;
    private static final PerlinSimplexNoise FROZEN_TEMPERATURE_NOISE;
    public static final PerlinSimplexNoise BIOME_INFO_NOISE;
    private final ClimateSettings climateSettings;
    private final BiomeGenerationSettings generationSettings;
    private final MobSpawnSettings mobSettings;
    private final float depth;
    private final float scale;
    private final BiomeCategory biomeCategory;
    private final BiomeSpecialEffects specialEffects;
    private final ThreadLocal<Long2FloatLinkedOpenHashMap> temperatureCache;
    
    private Biome(final ClimateSettings d, final BiomeCategory b, final float float3, final float float4, final BiomeSpecialEffects bsw, final BiomeGenerationSettings bst, final MobSpawnSettings btd) {
        this.structuresByStep = (Map<Integer, List<StructureFeature<?>>>)Registry.STRUCTURE_FEATURE.stream().collect(Collectors.groupingBy(ckx -> ckx.step().ordinal()));
        this.temperatureCache = (ThreadLocal<Long2FloatLinkedOpenHashMap>)ThreadLocal.withInitial(() -> Util.<Long2FloatLinkedOpenHashMap>make((java.util.function.Supplier<Long2FloatLinkedOpenHashMap>)(() -> {
            final Long2FloatLinkedOpenHashMap long2FloatLinkedOpenHashMap2 = new Long2FloatLinkedOpenHashMap(1024, 0.25f) {
                @Override
                protected void rehash(final int integer) {
                }
            };
            long2FloatLinkedOpenHashMap2.defaultReturnValue(Float.NaN);
            return long2FloatLinkedOpenHashMap2;
        })));
        this.climateSettings = d;
        this.generationSettings = bst;
        this.mobSettings = btd;
        this.biomeCategory = b;
        this.depth = float3;
        this.scale = float4;
        this.specialEffects = bsw;
    }
    
    public MobSpawnSettings getMobSettings() {
        return this.mobSettings;
    }
    
    public Precipitation getPrecipitation() {
        return this.climateSettings.precipitation;
    }
    
    public boolean isHumid() {
        return this.getDownfall() > 0.85f;
    }
    
    private float getHeightAdjustedTemperature(final BlockPos fx) {
        final float float3 = this.climateSettings.temperatureModifier.modifyTemperature(fx, this.getBaseTemperature());
        if (fx.getY() > 64) {
            final float float4 = (float)(Biome.TEMPERATURE_NOISE.getValue(fx.getX() / 8.0f, fx.getZ() / 8.0f, false) * 4.0);
            return float3 - (float4 + fx.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return float3;
    }
    
    public final float getTemperature(final BlockPos fx) {
        final long long3 = fx.asLong();
        final Long2FloatLinkedOpenHashMap long2FloatLinkedOpenHashMap5 = (Long2FloatLinkedOpenHashMap)this.temperatureCache.get();
        final float float6 = long2FloatLinkedOpenHashMap5.get(long3);
        if (!Float.isNaN(float6)) {
            return float6;
        }
        final float float7 = this.getHeightAdjustedTemperature(fx);
        if (long2FloatLinkedOpenHashMap5.size() == 1024) {
            long2FloatLinkedOpenHashMap5.removeFirstFloat();
        }
        long2FloatLinkedOpenHashMap5.put(long3, float7);
        return float7;
    }
    
    public boolean shouldFreeze(final LevelReader brw, final BlockPos fx) {
        return this.shouldFreeze(brw, fx, true);
    }
    
    public boolean shouldFreeze(final LevelReader brw, final BlockPos fx, final boolean boolean3) {
        if (this.getTemperature(fx) >= 0.15f) {
            return false;
        }
        if (fx.getY() >= 0 && fx.getY() < 256 && brw.getBrightness(LightLayer.BLOCK, fx) < 10) {
            final BlockState cee5 = brw.getBlockState(fx);
            final FluidState cuu6 = brw.getFluidState(fx);
            if (cuu6.getType() == Fluids.WATER && cee5.getBlock() instanceof LiquidBlock) {
                if (!boolean3) {
                    return true;
                }
                final boolean boolean4 = brw.isWaterAt(fx.west()) && brw.isWaterAt(fx.east()) && brw.isWaterAt(fx.north()) && brw.isWaterAt(fx.south());
                if (!boolean4) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean shouldSnow(final LevelReader brw, final BlockPos fx) {
        if (this.getTemperature(fx) >= 0.15f) {
            return false;
        }
        if (fx.getY() >= 0 && fx.getY() < 256 && brw.getBrightness(LightLayer.BLOCK, fx) < 10) {
            final BlockState cee4 = brw.getBlockState(fx);
            if (cee4.isAir() && Blocks.SNOW.defaultBlockState().canSurvive(brw, fx)) {
                return true;
            }
        }
        return false;
    }
    
    public BiomeGenerationSettings getGenerationSettings() {
        return this.generationSettings;
    }
    
    public void generate(final StructureFeatureManager bsk, final ChunkGenerator cfv, final WorldGenRegion aam, final long long4, final WorldgenRandom chu, final BlockPos fx) {
        final List<List<Supplier<ConfiguredFeature<?, ?>>>> list9 = this.generationSettings.features();
        for (int integer10 = GenerationStep.Decoration.values().length, integer11 = 0; integer11 < integer10; ++integer11) {
            int integer12 = 0;
            if (bsk.shouldGenerateFeatures()) {
                final List<StructureFeature<?>> list10 = (List<StructureFeature<?>>)this.structuresByStep.getOrDefault(integer11, Collections.emptyList());
                for (final StructureFeature<?> ckx15 : list10) {
                    chu.setFeatureSeed(long4, integer12, integer11);
                    final int integer13 = fx.getX() >> 4;
                    final int integer14 = fx.getZ() >> 4;
                    final int integer15 = integer13 << 4;
                    final int integer16 = integer14 << 4;
                    try {
                        bsk.startsForFeature(SectionPos.of(fx), ckx15).forEach(crs -> crs.placeInChunk(aam, bsk, cfv, chu, new BoundingBox(integer15, integer16, integer15 + 15, integer16 + 15), new ChunkPos(integer13, integer14)));
                    }
                    catch (Exception exception20) {
                        final CrashReport l21 = CrashReport.forThrowable((Throwable)exception20, "Feature placement");
                        l21.addCategory("Feature").setDetail("Id", Registry.STRUCTURE_FEATURE.getKey(ckx15)).setDetail("Description", (CrashReportDetail<String>)(() -> ckx15.toString()));
                        throw new ReportedException(l21);
                    }
                    ++integer12;
                }
            }
            if (list9.size() > integer11) {
                for (final Supplier<ConfiguredFeature<?, ?>> supplier14 : (List)list9.get(integer11)) {
                    final ConfiguredFeature<?, ?> cis15 = supplier14.get();
                    chu.setFeatureSeed(long4, integer12, integer11);
                    try {
                        cis15.place(aam, cfv, chu, fx);
                    }
                    catch (Exception exception21) {
                        final CrashReport l22 = CrashReport.forThrowable((Throwable)exception21, "Feature placement");
                        l22.addCategory("Feature").setDetail("Id", Registry.FEATURE.getKey(cis15.feature)).setDetail("Config", cis15.config).setDetail("Description", (CrashReportDetail<String>)(() -> cis15.feature.toString()));
                        throw new ReportedException(l22);
                    }
                    ++integer12;
                }
            }
        }
    }
    
    public void buildSurfaceAt(final Random random, final ChunkAccess cft, final int integer3, final int integer4, final int integer5, final double double6, final BlockState cee7, final BlockState cee8, final int integer9, final long long10) {
        final ConfiguredSurfaceBuilder<?> ctd14 = this.generationSettings.getSurfaceBuilder().get();
        ctd14.initNoise(long10);
        ctd14.apply(random, cft, this, integer3, integer4, integer5, double6, cee7, cee8, integer9, long10);
    }
    
    public final float getDepth() {
        return this.depth;
    }
    
    public final float getDownfall() {
        return this.climateSettings.downfall;
    }
    
    public final float getScale() {
        return this.scale;
    }
    
    public final float getBaseTemperature() {
        return this.climateSettings.temperature;
    }
    
    public BiomeSpecialEffects getSpecialEffects() {
        return this.specialEffects;
    }
    
    public final BiomeCategory getBiomeCategory() {
        return this.biomeCategory;
    }
    
    public String toString() {
        final ResourceLocation vk2 = BuiltinRegistries.BIOME.getKey(this);
        return (vk2 == null) ? super.toString() : vk2.toString();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DIRECT_CODEC = RecordCodecBuilder.<Biome>create((java.util.function.Function<RecordCodecBuilder.Instance<Biome>, ? extends App<RecordCodecBuilder.Mu<Biome>, Biome>>)(instance -> instance.<ClimateSettings, BiomeCategory, Float, Float, BiomeSpecialEffects, BiomeGenerationSettings, MobSpawnSettings>group(ClimateSettings.CODEC.forGetter((java.util.function.Function<Object, ClimateSettings>)(bss -> bss.climateSettings)), BiomeCategory.CODEC.fieldOf("category").forGetter((java.util.function.Function<Object, BiomeCategory>)(bss -> bss.biomeCategory)), Codec.FLOAT.fieldOf("depth").forGetter((java.util.function.Function<Object, Object>)(bss -> bss.depth)), Codec.FLOAT.fieldOf("scale").forGetter((java.util.function.Function<Object, Object>)(bss -> bss.scale)), BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter((java.util.function.Function<Object, BiomeSpecialEffects>)(bss -> bss.specialEffects)), BiomeGenerationSettings.CODEC.forGetter((java.util.function.Function<Object, BiomeGenerationSettings>)(bss -> bss.generationSettings)), MobSpawnSettings.CODEC.forGetter((java.util.function.Function<Object, MobSpawnSettings>)(bss -> bss.mobSettings))).<Biome>apply(instance, Biome::new)));
        NETWORK_CODEC = RecordCodecBuilder.<Biome>create((java.util.function.Function<RecordCodecBuilder.Instance<Biome>, ? extends App<RecordCodecBuilder.Mu<Biome>, Biome>>)(instance -> instance.<ClimateSettings, BiomeCategory, Float, Float, BiomeSpecialEffects>group(ClimateSettings.CODEC.forGetter((java.util.function.Function<Object, ClimateSettings>)(bss -> bss.climateSettings)), BiomeCategory.CODEC.fieldOf("category").forGetter((java.util.function.Function<Object, BiomeCategory>)(bss -> bss.biomeCategory)), Codec.FLOAT.fieldOf("depth").forGetter((java.util.function.Function<Object, Object>)(bss -> bss.depth)), Codec.FLOAT.fieldOf("scale").forGetter((java.util.function.Function<Object, Object>)(bss -> bss.scale)), BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter((java.util.function.Function<Object, BiomeSpecialEffects>)(bss -> bss.specialEffects))).<Biome>apply(instance, (d, b, float3, float4, bsw) -> new Biome(d, b, float3, float4, bsw, BiomeGenerationSettings.EMPTY, MobSpawnSettings.EMPTY))));
        CODEC = RegistryFileCodec.<Biome>create(Registry.BIOME_REGISTRY, Biome.DIRECT_CODEC);
        LIST_CODEC = RegistryFileCodec.<Biome>homogeneousList(Registry.BIOME_REGISTRY, Biome.DIRECT_CODEC);
        TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(1234L), ImmutableList.of(0));
        FROZEN_TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(3456L), ImmutableList.of(-2, -1, 0));
        BIOME_INFO_NOISE = new PerlinSimplexNoise(new WorldgenRandom(2345L), ImmutableList.of(0));
    }
    
    public enum BiomeCategory implements StringRepresentable {
        NONE("none"), 
        TAIGA("taiga"), 
        EXTREME_HILLS("extreme_hills"), 
        JUNGLE("jungle"), 
        MESA("mesa"), 
        PLAINS("plains"), 
        SAVANNA("savanna"), 
        ICY("icy"), 
        THEEND("the_end"), 
        BEACH("beach"), 
        FOREST("forest"), 
        OCEAN("ocean"), 
        DESERT("desert"), 
        RIVER("river"), 
        SWAMP("swamp"), 
        MUSHROOM("mushroom"), 
        NETHER("nether");
        
        public static final Codec<BiomeCategory> CODEC;
        private static final Map<String, BiomeCategory> BY_NAME;
        private final String name;
        
        private BiomeCategory(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static BiomeCategory byName(final String string) {
            return (BiomeCategory)BiomeCategory.BY_NAME.get(string);
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            CODEC = StringRepresentable.<BiomeCategory>fromEnum((java.util.function.Supplier<BiomeCategory[]>)BiomeCategory::values, (java.util.function.Function<? super String, ? extends BiomeCategory>)BiomeCategory::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(BiomeCategory::getName, b -> b));
        }
    }
    
    public enum Precipitation implements StringRepresentable {
        NONE("none"), 
        RAIN("rain"), 
        SNOW("snow");
        
        public static final Codec<Precipitation> CODEC;
        private static final Map<String, Precipitation> BY_NAME;
        private final String name;
        
        private Precipitation(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Precipitation byName(final String string) {
            return (Precipitation)Precipitation.BY_NAME.get(string);
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            CODEC = StringRepresentable.<Precipitation>fromEnum((java.util.function.Supplier<Precipitation[]>)Precipitation::values, (java.util.function.Function<? super String, ? extends Precipitation>)Precipitation::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Precipitation::getName, e -> e));
        }
    }
    
    public enum TemperatureModifier implements StringRepresentable {
        NONE("none") {
            @Override
            public float modifyTemperature(final BlockPos fx, final float float2) {
                return float2;
            }
        }, 
        FROZEN("frozen") {
            @Override
            public float modifyTemperature(final BlockPos fx, final float float2) {
                final double double4 = Biome.FROZEN_TEMPERATURE_NOISE.getValue(fx.getX() * 0.05, fx.getZ() * 0.05, false) * 7.0;
                final double double5 = Biome.BIOME_INFO_NOISE.getValue(fx.getX() * 0.2, fx.getZ() * 0.2, false);
                final double double6 = double4 + double5;
                if (double6 < 0.3) {
                    final double double7 = Biome.BIOME_INFO_NOISE.getValue(fx.getX() * 0.09, fx.getZ() * 0.09, false);
                    if (double7 < 0.8) {
                        return 0.2f;
                    }
                }
                return float2;
            }
        };
        
        private final String name;
        public static final Codec<TemperatureModifier> CODEC;
        private static final Map<String, TemperatureModifier> BY_NAME;
        
        public abstract float modifyTemperature(final BlockPos fx, final float float2);
        
        private TemperatureModifier(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        public static TemperatureModifier byName(final String string) {
            return (TemperatureModifier)TemperatureModifier.BY_NAME.get(string);
        }
        
        static {
            CODEC = StringRepresentable.<TemperatureModifier>fromEnum((java.util.function.Supplier<TemperatureModifier[]>)TemperatureModifier::values, (java.util.function.Function<? super String, ? extends TemperatureModifier>)TemperatureModifier::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(TemperatureModifier::getName, f -> f));
        }
    }
    
    public static class BiomeBuilder {
        @Nullable
        private Precipitation precipitation;
        @Nullable
        private BiomeCategory biomeCategory;
        @Nullable
        private Float depth;
        @Nullable
        private Float scale;
        @Nullable
        private Float temperature;
        private TemperatureModifier temperatureModifier;
        @Nullable
        private Float downfall;
        @Nullable
        private BiomeSpecialEffects specialEffects;
        @Nullable
        private MobSpawnSettings mobSpawnSettings;
        @Nullable
        private BiomeGenerationSettings generationSettings;
        
        public BiomeBuilder() {
            this.temperatureModifier = TemperatureModifier.NONE;
        }
        
        public BiomeBuilder precipitation(final Precipitation e) {
            this.precipitation = e;
            return this;
        }
        
        public BiomeBuilder biomeCategory(final BiomeCategory b) {
            this.biomeCategory = b;
            return this;
        }
        
        public BiomeBuilder depth(final float float1) {
            this.depth = float1;
            return this;
        }
        
        public BiomeBuilder scale(final float float1) {
            this.scale = float1;
            return this;
        }
        
        public BiomeBuilder temperature(final float float1) {
            this.temperature = float1;
            return this;
        }
        
        public BiomeBuilder downfall(final float float1) {
            this.downfall = float1;
            return this;
        }
        
        public BiomeBuilder specialEffects(final BiomeSpecialEffects bsw) {
            this.specialEffects = bsw;
            return this;
        }
        
        public BiomeBuilder mobSpawnSettings(final MobSpawnSettings btd) {
            this.mobSpawnSettings = btd;
            return this;
        }
        
        public BiomeBuilder generationSettings(final BiomeGenerationSettings bst) {
            this.generationSettings = bst;
            return this;
        }
        
        public BiomeBuilder temperatureAdjustment(final TemperatureModifier f) {
            this.temperatureModifier = f;
            return this;
        }
        
        public Biome build() {
            if (this.precipitation == null || this.biomeCategory == null || this.depth == null || this.scale == null || this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null) {
                throw new IllegalStateException(new StringBuilder().append("You are missing parameters to build a proper biome\n").append(this).toString());
            }
            return new Biome(new ClimateSettings(this.precipitation, (float)this.temperature, this.temperatureModifier, (float)this.downfall), this.biomeCategory, this.depth, this.scale, this.specialEffects, this.generationSettings, this.mobSpawnSettings, null);
        }
        
        public String toString() {
            return new StringBuilder().append("BiomeBuilder{\nprecipitation=").append(this.precipitation).append(",\nbiomeCategory=").append(this.biomeCategory).append(",\ndepth=").append(this.depth).append(",\nscale=").append(this.scale).append(",\ntemperature=").append(this.temperature).append(",\ntemperatureModifier=").append(this.temperatureModifier).append(",\ndownfall=").append(this.downfall).append(",\nspecialEffects=").append(this.specialEffects).append(",\nmobSpawnSettings=").append(this.mobSpawnSettings).append(",\ngenerationSettings=").append(this.generationSettings).append(",\n").append('}').toString();
        }
    }
    
    public static class ClimateParameters {
        public static final Codec<ClimateParameters> CODEC;
        private final float temperature;
        private final float humidity;
        private final float altitude;
        private final float weirdness;
        private final float offset;
        
        public ClimateParameters(final float float1, final float float2, final float float3, final float float4, final float float5) {
            this.temperature = float1;
            this.humidity = float2;
            this.altitude = float3;
            this.weirdness = float4;
            this.offset = float5;
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final ClimateParameters c3 = (ClimateParameters)object;
            return Float.compare(c3.temperature, this.temperature) == 0 && Float.compare(c3.humidity, this.humidity) == 0 && Float.compare(c3.altitude, this.altitude) == 0 && Float.compare(c3.weirdness, this.weirdness) == 0;
        }
        
        public int hashCode() {
            int integer2 = (this.temperature != 0.0f) ? Float.floatToIntBits(this.temperature) : 0;
            integer2 = 31 * integer2 + ((this.humidity != 0.0f) ? Float.floatToIntBits(this.humidity) : 0);
            integer2 = 31 * integer2 + ((this.altitude != 0.0f) ? Float.floatToIntBits(this.altitude) : 0);
            integer2 = 31 * integer2 + ((this.weirdness != 0.0f) ? Float.floatToIntBits(this.weirdness) : 0);
            return integer2;
        }
        
        public float fitness(final ClimateParameters c) {
            return (this.temperature - c.temperature) * (this.temperature - c.temperature) + (this.humidity - c.humidity) * (this.humidity - c.humidity) + (this.altitude - c.altitude) * (this.altitude - c.altitude) + (this.weirdness - c.weirdness) * (this.weirdness - c.weirdness) + (this.offset - c.offset) * (this.offset - c.offset);
        }
        
        static {
            CODEC = RecordCodecBuilder.<ClimateParameters>create((java.util.function.Function<RecordCodecBuilder.Instance<ClimateParameters>, ? extends App<RecordCodecBuilder.Mu<ClimateParameters>, ClimateParameters>>)(instance -> instance.<Float, Float, Float, Float, Float>group(Codec.floatRange(-2.0f, 2.0f).fieldOf("temperature").forGetter((java.util.function.Function<Object, Float>)(c -> c.temperature)), Codec.floatRange(-2.0f, 2.0f).fieldOf("humidity").forGetter((java.util.function.Function<Object, Float>)(c -> c.humidity)), Codec.floatRange(-2.0f, 2.0f).fieldOf("altitude").forGetter((java.util.function.Function<Object, Float>)(c -> c.altitude)), Codec.floatRange(-2.0f, 2.0f).fieldOf("weirdness").forGetter((java.util.function.Function<Object, Float>)(c -> c.weirdness)), Codec.floatRange(0.0f, 1.0f).fieldOf("offset").forGetter((java.util.function.Function<Object, Float>)(c -> c.offset))).<ClimateParameters>apply(instance, ClimateParameters::new)));
        }
    }
    
    static class ClimateSettings {
        public static final MapCodec<ClimateSettings> CODEC;
        private final Precipitation precipitation;
        private final float temperature;
        private final TemperatureModifier temperatureModifier;
        private final float downfall;
        
        private ClimateSettings(final Precipitation e, final float float2, final TemperatureModifier f, final float float4) {
            this.precipitation = e;
            this.temperature = float2;
            this.temperatureModifier = f;
            this.downfall = float4;
        }
        
        static {
            CODEC = RecordCodecBuilder.<ClimateSettings>mapCodec((java.util.function.Function<RecordCodecBuilder.Instance<ClimateSettings>, ? extends App<RecordCodecBuilder.Mu<ClimateSettings>, ClimateSettings>>)(instance -> instance.<Precipitation, Float, TemperatureModifier, Float>group(Precipitation.CODEC.fieldOf("precipitation").forGetter((java.util.function.Function<Object, Precipitation>)(d -> d.precipitation)), Codec.FLOAT.fieldOf("temperature").forGetter((java.util.function.Function<Object, Object>)(d -> d.temperature)), TemperatureModifier.CODEC.optionalFieldOf("temperature_modifier", TemperatureModifier.NONE).forGetter((java.util.function.Function<Object, TemperatureModifier>)(d -> d.temperatureModifier)), Codec.FLOAT.fieldOf("downfall").forGetter((java.util.function.Function<Object, Object>)(d -> d.downfall))).<ClimateSettings>apply(instance, ClimateSettings::new)));
        }
    }
}
