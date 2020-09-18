package net.minecraft.world.level.biome;

import com.google.common.collect.Lists;
import java.util.Collection;
import com.google.common.collect.Maps;
import java.util.function.BiFunction;
import net.minecraft.util.WeighedRandom;
import java.util.stream.Collector;
import com.google.common.collect.ImmutableMap;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Keyable;
import net.minecraft.core.Registry;
import net.minecraft.util.StringRepresentable;
import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.function.Function;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import java.util.List;
import net.minecraft.world.entity.MobCategory;
import java.util.Map;
import com.mojang.serialization.MapCodec;
import org.apache.logging.log4j.Logger;

public class MobSpawnSettings {
    public static final Logger LOGGER;
    public static final MobSpawnSettings EMPTY;
    public static final MapCodec<MobSpawnSettings> CODEC;
    private final float creatureGenerationProbability;
    private final Map<MobCategory, List<SpawnerData>> spawners;
    private final Map<EntityType<?>, MobSpawnCost> mobSpawnCosts;
    private final boolean playerSpawnFriendly;
    
    private MobSpawnSettings(final float float1, final Map<MobCategory, List<SpawnerData>> map2, final Map<EntityType<?>, MobSpawnCost> map3, final boolean boolean4) {
        this.creatureGenerationProbability = float1;
        this.spawners = map2;
        this.mobSpawnCosts = map3;
        this.playerSpawnFriendly = boolean4;
    }
    
    public List<SpawnerData> getMobs(final MobCategory aql) {
        return (List<SpawnerData>)this.spawners.getOrDefault(aql, ImmutableList.of());
    }
    
    @Nullable
    public MobSpawnCost getMobSpawnCost(final EntityType<?> aqb) {
        return (MobSpawnCost)this.mobSpawnCosts.get(aqb);
    }
    
    public float getCreatureProbability() {
        return this.creatureGenerationProbability;
    }
    
    public boolean playerSpawnFriendly() {
        return this.playerSpawnFriendly;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY = new MobSpawnSettings(0.1f, (Map<MobCategory, List<SpawnerData>>)Stream.of((Object[])MobCategory.values()).collect((Collector)ImmutableMap.toImmutableMap((java.util.function.Function<? super Object, ?>)(aql -> aql), (java.util.function.Function<? super Object, ?>)(aql -> ImmutableList.of()))), ImmutableMap.of(), false);
        CODEC = RecordCodecBuilder.<MobSpawnSettings>mapCodec((java.util.function.Function<RecordCodecBuilder.Instance<MobSpawnSettings>, ? extends App<RecordCodecBuilder.Mu<MobSpawnSettings>, MobSpawnSettings>>)(instance -> instance.<Float, Map, Map, Boolean>group(Codec.FLOAT.optionalFieldOf("creature_spawn_probability", Float.valueOf(0.1f)).forGetter((java.util.function.Function<Object, Float>)(btd -> btd.creatureGenerationProbability)), Codec.<MobCategory, java.util.List<SpawnerData>>simpleMap(MobCategory.CODEC, SpawnerData.CODEC.listOf().promotePartial(Util.prefix("Spawn data: ", (Consumer<String>)MobSpawnSettings.LOGGER::error)), StringRepresentable.keys(MobCategory.values())).fieldOf("spawners").forGetter((java.util.function.Function<Object, Object>)(btd -> btd.spawners)), Codec.<EntityType<?>, MobSpawnCost>simpleMap(Registry.ENTITY_TYPE, MobSpawnCost.CODEC, Registry.ENTITY_TYPE).fieldOf("spawn_costs").forGetter((java.util.function.Function<Object, Object>)(btd -> btd.mobSpawnCosts)), Codec.BOOL.fieldOf("player_spawn_friendly").orElse(false).forGetter((java.util.function.Function<Object, Boolean>)MobSpawnSettings::playerSpawnFriendly)).<MobSpawnSettings>apply(instance, MobSpawnSettings::new)));
    }
    
    public static class SpawnerData extends WeighedRandom.WeighedRandomItem {
        public static final Codec<SpawnerData> CODEC;
        public final EntityType<?> type;
        public final int minCount;
        public final int maxCount;
        
        public SpawnerData(final EntityType<?> aqb, final int integer2, final int integer3, final int integer4) {
            super(integer2);
            this.type = ((aqb.getCategory() == MobCategory.MISC) ? EntityType.PIG : aqb);
            this.minCount = integer3;
            this.maxCount = integer4;
        }
        
        public String toString() {
            return new StringBuilder().append(EntityType.getKey(this.type)).append("*(").append(this.minCount).append("-").append(this.maxCount).append("):").append(this.weight).toString();
        }
        
        static {
            CODEC = RecordCodecBuilder.<SpawnerData>create((java.util.function.Function<RecordCodecBuilder.Instance<SpawnerData>, ? extends App<RecordCodecBuilder.Mu<SpawnerData>, SpawnerData>>)(instance -> instance.<EntityType, Integer, Integer, Integer>group(Registry.ENTITY_TYPE.fieldOf("type").forGetter((java.util.function.Function<Object, Object>)(c -> c.type)), Codec.INT.fieldOf("weight").forGetter((java.util.function.Function<Object, Object>)(c -> c.weight)), Codec.INT.fieldOf("minCount").forGetter((java.util.function.Function<Object, Object>)(c -> c.minCount)), Codec.INT.fieldOf("maxCount").forGetter((java.util.function.Function<Object, Object>)(c -> c.maxCount))).<SpawnerData>apply(instance, SpawnerData::new)));
        }
    }
    
    public static class MobSpawnCost {
        public static final Codec<MobSpawnCost> CODEC;
        private final double energyBudget;
        private final double charge;
        
        private MobSpawnCost(final double double1, final double double2) {
            this.energyBudget = double1;
            this.charge = double2;
        }
        
        public double getEnergyBudget() {
            return this.energyBudget;
        }
        
        public double getCharge() {
            return this.charge;
        }
        
        static {
            CODEC = RecordCodecBuilder.<MobSpawnCost>create((java.util.function.Function<RecordCodecBuilder.Instance<MobSpawnCost>, ? extends App<RecordCodecBuilder.Mu<MobSpawnCost>, MobSpawnCost>>)(instance -> instance.group(Codec.DOUBLE.fieldOf("energy_budget").forGetter((java.util.function.Function<Object, Object>)(b -> b.energyBudget)), Codec.DOUBLE.fieldOf("charge").forGetter((java.util.function.Function<Object, Object>)(b -> b.charge))).apply(instance, (java.util.function.BiFunction<Object, Object, Object>)MobSpawnCost::new)));
        }
    }
    
    public static class Builder {
        private final Map<MobCategory, List<SpawnerData>> spawners;
        private final Map<EntityType<?>, MobSpawnCost> mobSpawnCosts;
        private float creatureGenerationProbability;
        private boolean playerCanSpawn;
        
        public Builder() {
            this.spawners = (Map<MobCategory, List<SpawnerData>>)Stream.of((Object[])MobCategory.values()).collect((Collector)ImmutableMap.toImmutableMap((java.util.function.Function<? super Object, ?>)(aql -> aql), (java.util.function.Function<? super Object, ?>)(aql -> Lists.newArrayList())));
            this.mobSpawnCosts = Maps.newLinkedHashMap();
            this.creatureGenerationProbability = 0.1f;
        }
        
        public Builder addSpawn(final MobCategory aql, final SpawnerData c) {
            ((List)this.spawners.get(aql)).add(c);
            return this;
        }
        
        public Builder addMobCharge(final EntityType<?> aqb, final double double2, final double double3) {
            this.mobSpawnCosts.put(aqb, new MobSpawnCost(double3, double2));
            return this;
        }
        
        public Builder creatureGenerationProbability(final float float1) {
            this.creatureGenerationProbability = float1;
            return this;
        }
        
        public Builder setPlayerCanSpawn() {
            this.playerCanSpawn = true;
            return this;
        }
        
        public MobSpawnSettings build() {
            return new MobSpawnSettings(this.creatureGenerationProbability, (Map)this.spawners.entrySet().stream().collect((Collector)ImmutableMap.toImmutableMap((java.util.function.Function<? super Object, ?>)Map.Entry::getKey, (java.util.function.Function<? super Object, ?>)(entry -> ImmutableList.copyOf((java.util.Collection<?>)entry.getValue())))), (Map)ImmutableMap.copyOf((java.util.Map<?, ?>)this.mobSpawnCosts), this.playerCanSpawn, null);
        }
    }
}
