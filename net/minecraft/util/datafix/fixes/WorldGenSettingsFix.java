package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.DSL;
import java.util.Locale;
import java.util.stream.Stream;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.stream.Collectors;
import com.mojang.serialization.DataResult;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import com.mojang.serialization.OptionalDynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import java.util.Optional;
import java.util.Map;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicLike;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFix;

public class WorldGenSettingsFix extends DataFix {
    private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS;
    
    public WorldGenSettingsFix(final Schema schema) {
        super(schema, true);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("WorldGenSettings building", this.getInputSchema().getType(References.WORLD_GEN_SETTINGS), (Function<Typed<?>, Typed<?>>)(typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), WorldGenSettingsFix::fix)));
    }
    
    private static <T> Dynamic<T> noise(final long long1, final DynamicLike<T> dynamicLike, final Dynamic<T> dynamic3, final Dynamic<T> dynamic4) {
        return dynamicLike.createMap(ImmutableMap.<Dynamic<T>, Dynamic<T>>of(dynamicLike.createString("type"), dynamicLike.createString("minecraft:noise"), dynamicLike.createString("biome_source"), dynamic4, dynamicLike.createString("seed"), dynamicLike.createLong(long1), dynamicLike.createString("settings"), dynamic3));
    }
    
    private static <T> Dynamic<T> vanillaBiomeSource(final Dynamic<T> dynamic, final long long2, final boolean boolean3, final boolean boolean4) {
        final ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder6 = (ImmutableMap.Builder<Dynamic<T>, Dynamic<T>>)ImmutableMap.<Dynamic<Object>, Dynamic<Object>>builder().put(dynamic.createString("type"), dynamic.createString("minecraft:vanilla_layered")).put(dynamic.createString("seed"), dynamic.createLong(long2)).put(dynamic.createString("large_biomes"), dynamic.createBoolean(boolean4));
        if (boolean3) {
            builder6.put(dynamic.createString("legacy_biome_init_layer"), dynamic.createBoolean(boolean3));
        }
        return dynamic.createMap(builder6.build());
    }
    
    private static <T> Dynamic<T> fix(final Dynamic<T> dynamic) {
        final DynamicOps<T> dynamicOps2 = dynamic.getOps();
        final long long3 = dynamic.get("RandomSeed").asLong(0L);
        final Optional<String> optional6 = dynamic.get("generatorName").asString().<String>map((java.util.function.Function<? super String, ? extends String>)(string -> string.toLowerCase(Locale.ROOT))).result();
        final Optional<String> optional7 = (Optional<String>)dynamic.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> {
            if (optional6.equals(Optional.of("customized"))) {
                return dynamic.get("generatorOptions").asString().result();
            }
            return Optional.empty();
        });
        boolean boolean8 = false;
        Dynamic<T> dynamic2;
        if (optional6.equals(Optional.of("customized"))) {
            dynamic2 = WorldGenSettingsFix.<T>defaultOverworld(dynamic, long3);
        }
        else if (!optional6.isPresent()) {
            dynamic2 = WorldGenSettingsFix.<T>defaultOverworld(dynamic, long3);
        }
        else {
            final String s = (String)optional6.get();
            switch (s) {
                case "flat": {
                    final OptionalDynamic<T> optionalDynamic11 = dynamic.get("generatorOptions");
                    final Map<Dynamic<T>, Dynamic<T>> map12 = WorldGenSettingsFix.<T>fixFlatStructures(dynamicOps2, optionalDynamic11);
                    dynamic2 = dynamic.createMap(ImmutableMap.<Dynamic<Object>, Dynamic<Object>>of(dynamic.createString("type"), dynamic.createString("minecraft:flat"), dynamic.createString("settings"), dynamic.createMap(ImmutableMap.<Dynamic<Object>, Object>of(dynamic.createString("structures"), dynamic.createMap(map12), dynamic.createString("layers"), optionalDynamic11.get("layers").result().orElseGet(() -> dynamic.createList(Stream.of((Object[])new Dynamic[] { dynamic.createMap((Map)ImmutableMap.<Dynamic, Dynamic>of(dynamic.createString("height"), dynamic.createInt(1), dynamic.createString("block"), dynamic.createString("minecraft:bedrock"))), dynamic.createMap((Map)ImmutableMap.<Dynamic, Dynamic>of(dynamic.createString("height"), dynamic.createInt(2), dynamic.createString("block"), dynamic.createString("minecraft:dirt"))), dynamic.createMap((Map)ImmutableMap.<Dynamic, Dynamic>of(dynamic.createString("height"), dynamic.createInt(1), dynamic.createString("block"), dynamic.createString("minecraft:grass_block"))) }))), dynamic.createString("biome"), dynamic.createString(optionalDynamic11.get("biome").asString("minecraft:plains"))))));
                    break;
                }
                case "debug_all_block_states": {
                    dynamic2 = dynamic.createMap(ImmutableMap.<Dynamic<Object>, Dynamic<Object>>of(dynamic.createString("type"), dynamic.createString("minecraft:debug")));
                    break;
                }
                case "buffet": {
                    final OptionalDynamic<T> optionalDynamic12 = dynamic.get("generatorOptions");
                    final OptionalDynamic<?> optionalDynamic13 = optionalDynamic12.get("chunk_generator");
                    final Optional<String> optional8 = optionalDynamic13.get("type").asString().result();
                    Dynamic<T> dynamic3;
                    if (Objects.equals(optional8, Optional.of("minecraft:caves"))) {
                        dynamic3 = dynamic.createString("minecraft:caves");
                        boolean8 = true;
                    }
                    else if (Objects.equals(optional8, Optional.of("minecraft:floating_islands"))) {
                        dynamic3 = dynamic.createString("minecraft:floating_islands");
                    }
                    else {
                        dynamic3 = dynamic.createString("minecraft:overworld");
                    }
                    final Dynamic<T> dynamic4 = (Dynamic<T>)optionalDynamic12.get("biome_source").result().orElseGet(() -> dynamic.createMap((Map)ImmutableMap.<Dynamic, Dynamic>of(dynamic.createString("type"), dynamic.createString("minecraft:fixed"))));
                    Dynamic<T> dynamic5;
                    if (dynamic4.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) {
                        final String string19 = (String)dynamic4.get("options").get("biomes").asStream().findFirst().flatMap(dynamic -> dynamic.asString().result()).orElse("minecraft:ocean");
                        dynamic5 = dynamic4.remove("options").set("biome", dynamic.createString(string19));
                    }
                    else {
                        dynamic5 = dynamic4;
                    }
                    dynamic2 = WorldGenSettingsFix.<T>noise(long3, dynamic, dynamic3, dynamic5);
                    break;
                }
                default: {
                    final boolean boolean9 = ((String)optional6.get()).equals("default");
                    final boolean boolean10 = ((String)optional6.get()).equals("default_1_1") || (boolean9 && dynamic.get("generatorVersion").asInt(0) == 0);
                    final boolean boolean11 = ((String)optional6.get()).equals("amplified");
                    final boolean boolean12 = ((String)optional6.get()).equals("largebiomes");
                    dynamic2 = WorldGenSettingsFix.<T>noise(long3, dynamic, dynamic.createString(boolean11 ? "minecraft:amplified" : "minecraft:overworld"), WorldGenSettingsFix.vanillaBiomeSource((Dynamic<T>)dynamic, long3, boolean10, boolean12));
                    break;
                }
            }
        }
        final boolean boolean13 = dynamic.get("MapFeatures").asBoolean(true);
        final boolean boolean14 = dynamic.get("BonusChest").asBoolean(false);
        final ImmutableMap.Builder<T, T> builder11 = ImmutableMap.<T, T>builder();
        builder11.put(dynamicOps2.createString("seed"), dynamicOps2.createLong(long3));
        builder11.put(dynamicOps2.createString("generate_features"), dynamicOps2.createBoolean(boolean13));
        builder11.put(dynamicOps2.createString("bonus_chest"), dynamicOps2.createBoolean(boolean14));
        builder11.put(dynamicOps2.createString("dimensions"), WorldGenSettingsFix.<T>vanillaLevels(dynamic, long3, dynamic2, boolean8));
        optional7.ifPresent(string -> builder11.put(dynamicOps2.createString("legacy_custom_options"), dynamicOps2.createString(string)));
        return new Dynamic<T>(dynamicOps2, dynamicOps2.createMap((java.util.Map<T, T>)builder11.build()));
    }
    
    protected static <T> Dynamic<T> defaultOverworld(final Dynamic<T> dynamic, final long long2) {
        return WorldGenSettingsFix.<T>noise(long2, dynamic, dynamic.createString("minecraft:overworld"), WorldGenSettingsFix.vanillaBiomeSource((Dynamic<T>)dynamic, long2, false, false));
    }
    
    protected static <T> T vanillaLevels(final Dynamic<T> dynamic1, final long long2, final Dynamic<T> dynamic3, final boolean boolean4) {
        final DynamicOps<T> dynamicOps6 = dynamic1.getOps();
        return dynamicOps6.createMap((java.util.Map<T, T>)ImmutableMap.<T, T>of(dynamicOps6.createString("minecraft:overworld"), dynamicOps6.createMap((java.util.Map<T, T>)ImmutableMap.<T, T>of(dynamicOps6.createString("type"), dynamicOps6.createString(new StringBuilder().append("minecraft:overworld").append(boolean4 ? "_caves" : "").toString()), dynamicOps6.createString("generator"), dynamic3.getValue())), dynamicOps6.createString("minecraft:the_nether"), dynamicOps6.createMap((java.util.Map<T, T>)ImmutableMap.<T, T>of(dynamicOps6.createString("type"), dynamicOps6.createString("minecraft:the_nether"), dynamicOps6.createString("generator"), WorldGenSettingsFix.<T>noise(long2, dynamic1, dynamic1.createString("minecraft:nether"), dynamic1.createMap(ImmutableMap.<Dynamic<Object>, Dynamic<Object>>of(dynamic1.createString("type"), dynamic1.createString("minecraft:multi_noise"), dynamic1.createString("seed"), dynamic1.createLong(long2), dynamic1.createString("preset"), dynamic1.createString("minecraft:nether")))).getValue())), dynamicOps6.createString("minecraft:the_end"), dynamicOps6.createMap((java.util.Map<T, T>)ImmutableMap.<T, T>of(dynamicOps6.createString("type"), dynamicOps6.createString("minecraft:the_end"), dynamicOps6.createString("generator"), WorldGenSettingsFix.<T>noise(long2, dynamic1, dynamic1.createString("minecraft:end"), dynamic1.createMap(ImmutableMap.<Dynamic<Object>, Dynamic<Object>>of(dynamic1.createString("type"), dynamic1.createString("minecraft:the_end"), dynamic1.createString("seed"), dynamic1.createLong(long2)))).getValue()))));
    }
    
    private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(final DynamicOps<T> dynamicOps, final OptionalDynamic<T> optionalDynamic) {
        final MutableInt mutableInt3 = new MutableInt(32);
        final MutableInt mutableInt4 = new MutableInt(3);
        final MutableInt mutableInt5 = new MutableInt(128);
        final MutableBoolean mutableBoolean6 = new MutableBoolean(false);
        final Map<String, StructureFeatureConfiguration> map7 = Maps.newHashMap();
        if (!optionalDynamic.result().isPresent()) {
            mutableBoolean6.setTrue();
            map7.put("minecraft:village", WorldGenSettingsFix.DEFAULTS.get("minecraft:village"));
        }
        optionalDynamic.get("structures").flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<Object>>)Dynamic::getMapValues).result().ifPresent(map6 -> map6.forEach((dynamic6, dynamic7) -> dynamic7.getMapValues().result().ifPresent(map7 -> map7.forEach((dynamic7, dynamic8) -> {
            final String string9 = dynamic6.asString("");
            final String string10 = dynamic7.asString("");
            final String string11 = dynamic8.asString("");
            if ("stronghold".equals(string9)) {
                mutableBoolean6.setTrue();
                final String s = string10;
                switch (s) {
                    case "distance": {
                        mutableInt3.setValue(getInt(string11, mutableInt3.getValue(), 1));
                    }
                    case "spread": {
                        mutableInt4.setValue(getInt(string11, mutableInt4.getValue(), 1));
                    }
                    case "count": {
                        mutableInt5.setValue(getInt(string11, mutableInt5.getValue(), 1));
                    }
                    default: {}
                }
            }
            else {
                final String s2 = string10;
                switch (s2) {
                    case "distance": {
                        final String s3 = string9;
                        switch (s3) {
                            case "village": {
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:village", string11, 9);
                                return;
                            }
                            case "biome_1": {
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:desert_pyramid", string11, 9);
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:igloo", string11, 9);
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:jungle_pyramid", string11, 9);
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:swamp_hut", string11, 9);
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:pillager_outpost", string11, 9);
                                return;
                            }
                            case "endcity": {
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:endcity", string11, 1);
                                return;
                            }
                            case "mansion": {
                                setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:mansion", string11, 1);
                                return;
                            }
                            default: {
                                return;
                            }
                        }
                        break;
                    }
                    case "separation": {
                        if ("oceanmonument".equals(string9)) {
                            final StructureFeatureConfiguration a14 = (StructureFeatureConfiguration)map7.getOrDefault("minecraft:monument", WorldGenSettingsFix.DEFAULTS.get("minecraft:monument"));
                            final int integer15 = getInt(string11, a14.separation, 1);
                            map7.put("minecraft:monument", new StructureFeatureConfiguration(integer15, a14.separation, a14.salt));
                        }
                    }
                    case "spacing": {
                        if ("oceanmonument".equals(string9)) {
                            setSpacing((Map<String, StructureFeatureConfiguration>)map7, "minecraft:monument", string11, 1);
                        }
                    }
                    default: {}
                }
            }
        }))));
        final ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder8 = ImmutableMap.<Dynamic<T>, Dynamic<T>>builder();
        builder8.put(optionalDynamic.createString("structures"), optionalDynamic.createMap(map7.entrySet().stream().collect(Collectors.toMap(entry -> optionalDynamic.createString((String)entry.getKey()), entry -> ((StructureFeatureConfiguration)entry.getValue()).serialize((DynamicOps<Object>)dynamicOps)))));
        if (mutableBoolean6.isTrue()) {
            builder8.put(optionalDynamic.createString("stronghold"), optionalDynamic.createMap(ImmutableMap.<Dynamic<Object>, Dynamic<Object>>of(optionalDynamic.createString("distance"), optionalDynamic.createInt(mutableInt3.getValue()), optionalDynamic.createString("spread"), optionalDynamic.createInt(mutableInt4.getValue()), optionalDynamic.createString("count"), optionalDynamic.createInt(mutableInt5.getValue()))));
        }
        return (Map<Dynamic<T>, Dynamic<T>>)builder8.build();
    }
    
    private static int getInt(final String string, final int integer) {
        return NumberUtils.toInt(string, integer);
    }
    
    private static int getInt(final String string, final int integer2, final int integer3) {
        return Math.max(integer3, getInt(string, integer2));
    }
    
    private static void setSpacing(final Map<String, StructureFeatureConfiguration> map, final String string2, final String string3, final int integer) {
        final StructureFeatureConfiguration a5 = (StructureFeatureConfiguration)map.getOrDefault(string2, WorldGenSettingsFix.DEFAULTS.get(string2));
        final int integer2 = getInt(string3, a5.spacing, integer);
        map.put(string2, new StructureFeatureConfiguration(integer2, a5.separation, a5.salt));
    }
    
    static {
        DEFAULTS = ImmutableMap.<String, StructureFeatureConfiguration>builder().put("minecraft:village", new StructureFeatureConfiguration(32, 8, 10387312)).put("minecraft:desert_pyramid", new StructureFeatureConfiguration(32, 8, 14357617)).put("minecraft:igloo", new StructureFeatureConfiguration(32, 8, 14357618)).put("minecraft:jungle_pyramid", new StructureFeatureConfiguration(32, 8, 14357619)).put("minecraft:swamp_hut", new StructureFeatureConfiguration(32, 8, 14357620)).put("minecraft:pillager_outpost", new StructureFeatureConfiguration(32, 8, 165745296)).put("minecraft:monument", new StructureFeatureConfiguration(32, 5, 10387313)).put("minecraft:endcity", new StructureFeatureConfiguration(20, 11, 10387313)).put("minecraft:mansion", new StructureFeatureConfiguration(80, 20, 10387319)).build();
    }
    
    static final class StructureFeatureConfiguration {
        public static final Codec<StructureFeatureConfiguration> CODEC;
        private final int spacing;
        private final int separation;
        private final int salt;
        
        public StructureFeatureConfiguration(final int integer1, final int integer2, final int integer3) {
            this.spacing = integer1;
            this.separation = integer2;
            this.salt = integer3;
        }
        
        public <T> Dynamic<T> serialize(final DynamicOps<T> dynamicOps) {
            return new Dynamic<T>(dynamicOps, (T)StructureFeatureConfiguration.CODEC.<T>encodeStart(dynamicOps, this).result().orElse(dynamicOps.emptyMap()));
        }
        
        static {
            CODEC = RecordCodecBuilder.<StructureFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<StructureFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<StructureFeatureConfiguration>, StructureFeatureConfiguration>>)(instance -> instance.<Integer, Integer, Integer>group(Codec.INT.fieldOf("spacing").forGetter((java.util.function.Function<Object, Object>)(a -> a.spacing)), Codec.INT.fieldOf("separation").forGetter((java.util.function.Function<Object, Object>)(a -> a.separation)), Codec.INT.fieldOf("salt").forGetter((java.util.function.Function<Object, Object>)(a -> a.salt))).<StructureFeatureConfiguration>apply(instance, StructureFeatureConfiguration::new)));
        }
    }
}
