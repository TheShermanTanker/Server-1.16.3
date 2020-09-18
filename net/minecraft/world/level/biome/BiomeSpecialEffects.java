package net.minecraft.world.level.biome;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.Map;
import net.minecraft.util.StringRepresentable;
import java.util.OptionalInt;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import java.util.Optional;
import com.mojang.serialization.Codec;

public class BiomeSpecialEffects {
    public static final Codec<BiomeSpecialEffects> CODEC;
    private final int fogColor;
    private final int waterColor;
    private final int waterFogColor;
    private final int skyColor;
    private final Optional<Integer> foliageColorOverride;
    private final Optional<Integer> grassColorOverride;
    private final GrassColorModifier grassColorModifier;
    private final Optional<AmbientParticleSettings> ambientParticleSettings;
    private final Optional<SoundEvent> ambientLoopSoundEvent;
    private final Optional<AmbientMoodSettings> ambientMoodSettings;
    private final Optional<AmbientAdditionsSettings> ambientAdditionsSettings;
    private final Optional<Music> backgroundMusic;
    
    private BiomeSpecialEffects(final int integer1, final int integer2, final int integer3, final int integer4, final Optional<Integer> optional5, final Optional<Integer> optional6, final GrassColorModifier b, final Optional<AmbientParticleSettings> optional8, final Optional<SoundEvent> optional9, final Optional<AmbientMoodSettings> optional10, final Optional<AmbientAdditionsSettings> optional11, final Optional<Music> optional12) {
        this.fogColor = integer1;
        this.waterColor = integer2;
        this.waterFogColor = integer3;
        this.skyColor = integer4;
        this.foliageColorOverride = optional5;
        this.grassColorOverride = optional6;
        this.grassColorModifier = b;
        this.ambientParticleSettings = optional8;
        this.ambientLoopSoundEvent = optional9;
        this.ambientMoodSettings = optional10;
        this.ambientAdditionsSettings = optional11;
        this.backgroundMusic = optional12;
    }
    
    static {
        CODEC = RecordCodecBuilder.<BiomeSpecialEffects>create((java.util.function.Function<RecordCodecBuilder.Instance<BiomeSpecialEffects>, ? extends App<RecordCodecBuilder.Mu<BiomeSpecialEffects>, BiomeSpecialEffects>>)(instance -> instance.<Integer, Integer, Integer, Integer, java.util.Optional<Object>, java.util.Optional<Object>, GrassColorModifier, java.util.Optional<AmbientParticleSettings>, java.util.Optional<SoundEvent>, java.util.Optional<AmbientMoodSettings>, java.util.Optional<AmbientAdditionsSettings>, java.util.Optional<Music>>group(Codec.INT.fieldOf("fog_color").forGetter((java.util.function.Function<Object, Object>)(bsw -> bsw.fogColor)), Codec.INT.fieldOf("water_color").forGetter((java.util.function.Function<Object, Object>)(bsw -> bsw.waterColor)), Codec.INT.fieldOf("water_fog_color").forGetter((java.util.function.Function<Object, Object>)(bsw -> bsw.waterFogColor)), Codec.INT.fieldOf("sky_color").forGetter((java.util.function.Function<Object, Object>)(bsw -> bsw.skyColor)), Codec.INT.optionalFieldOf("foliage_color").forGetter((java.util.function.Function<Object, java.util.Optional<Object>>)(bsw -> bsw.foliageColorOverride)), Codec.INT.optionalFieldOf("grass_color").forGetter((java.util.function.Function<Object, java.util.Optional<Object>>)(bsw -> bsw.grassColorOverride)), GrassColorModifier.CODEC.optionalFieldOf("grass_color_modifier", GrassColorModifier.NONE).forGetter((java.util.function.Function<Object, GrassColorModifier>)(bsw -> bsw.grassColorModifier)), AmbientParticleSettings.CODEC.optionalFieldOf("particle").forGetter((java.util.function.Function<Object, java.util.Optional<AmbientParticleSettings>>)(bsw -> bsw.ambientParticleSettings)), SoundEvent.CODEC.optionalFieldOf("ambient_sound").forGetter((java.util.function.Function<Object, java.util.Optional<SoundEvent>>)(bsw -> bsw.ambientLoopSoundEvent)), AmbientMoodSettings.CODEC.optionalFieldOf("mood_sound").forGetter((java.util.function.Function<Object, java.util.Optional<AmbientMoodSettings>>)(bsw -> bsw.ambientMoodSettings)), AmbientAdditionsSettings.CODEC.optionalFieldOf("additions_sound").forGetter((java.util.function.Function<Object, java.util.Optional<AmbientAdditionsSettings>>)(bsw -> bsw.ambientAdditionsSettings)), Music.CODEC.optionalFieldOf("music").forGetter((java.util.function.Function<Object, java.util.Optional<Music>>)(bsw -> bsw.backgroundMusic))).<BiomeSpecialEffects>apply(instance, BiomeSpecialEffects::new)));
    }
    
    public static class Builder {
        private OptionalInt fogColor;
        private OptionalInt waterColor;
        private OptionalInt waterFogColor;
        private OptionalInt skyColor;
        private Optional<Integer> foliageColorOverride;
        private Optional<Integer> grassColorOverride;
        private GrassColorModifier grassColorModifier;
        private Optional<AmbientParticleSettings> ambientParticle;
        private Optional<SoundEvent> ambientLoopSoundEvent;
        private Optional<AmbientMoodSettings> ambientMoodSettings;
        private Optional<AmbientAdditionsSettings> ambientAdditionsSettings;
        private Optional<Music> backgroundMusic;
        
        public Builder() {
            this.fogColor = OptionalInt.empty();
            this.waterColor = OptionalInt.empty();
            this.waterFogColor = OptionalInt.empty();
            this.skyColor = OptionalInt.empty();
            this.foliageColorOverride = (Optional<Integer>)Optional.empty();
            this.grassColorOverride = (Optional<Integer>)Optional.empty();
            this.grassColorModifier = GrassColorModifier.NONE;
            this.ambientParticle = (Optional<AmbientParticleSettings>)Optional.empty();
            this.ambientLoopSoundEvent = (Optional<SoundEvent>)Optional.empty();
            this.ambientMoodSettings = (Optional<AmbientMoodSettings>)Optional.empty();
            this.ambientAdditionsSettings = (Optional<AmbientAdditionsSettings>)Optional.empty();
            this.backgroundMusic = (Optional<Music>)Optional.empty();
        }
        
        public Builder fogColor(final int integer) {
            this.fogColor = OptionalInt.of(integer);
            return this;
        }
        
        public Builder waterColor(final int integer) {
            this.waterColor = OptionalInt.of(integer);
            return this;
        }
        
        public Builder waterFogColor(final int integer) {
            this.waterFogColor = OptionalInt.of(integer);
            return this;
        }
        
        public Builder skyColor(final int integer) {
            this.skyColor = OptionalInt.of(integer);
            return this;
        }
        
        public Builder foliageColorOverride(final int integer) {
            this.foliageColorOverride = (Optional<Integer>)Optional.of(integer);
            return this;
        }
        
        public Builder grassColorOverride(final int integer) {
            this.grassColorOverride = (Optional<Integer>)Optional.of(integer);
            return this;
        }
        
        public Builder grassColorModifier(final GrassColorModifier b) {
            this.grassColorModifier = b;
            return this;
        }
        
        public Builder ambientParticle(final AmbientParticleSettings bsr) {
            this.ambientParticle = (Optional<AmbientParticleSettings>)Optional.of(bsr);
            return this;
        }
        
        public Builder ambientLoopSound(final SoundEvent adn) {
            this.ambientLoopSoundEvent = (Optional<SoundEvent>)Optional.of(adn);
            return this;
        }
        
        public Builder ambientMoodSound(final AmbientMoodSettings bsq) {
            this.ambientMoodSettings = (Optional<AmbientMoodSettings>)Optional.of(bsq);
            return this;
        }
        
        public Builder ambientAdditionsSound(final AmbientAdditionsSettings bsp) {
            this.ambientAdditionsSettings = (Optional<AmbientAdditionsSettings>)Optional.of(bsp);
            return this;
        }
        
        public Builder backgroundMusic(final Music adl) {
            this.backgroundMusic = (Optional<Music>)Optional.of(adl);
            return this;
        }
        
        public BiomeSpecialEffects build() {
            return new BiomeSpecialEffects(this.fogColor.orElseThrow(() -> new IllegalStateException("Missing 'fog' color.")), this.waterColor.orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.waterFogColor.orElseThrow(() -> new IllegalStateException("Missing 'water fog' color.")), this.skyColor.orElseThrow(() -> new IllegalStateException("Missing 'sky' color.")), this.foliageColorOverride, this.grassColorOverride, this.grassColorModifier, this.ambientParticle, this.ambientLoopSoundEvent, this.ambientMoodSettings, this.ambientAdditionsSettings, this.backgroundMusic, null);
        }
    }
    
    public enum GrassColorModifier implements StringRepresentable {
        NONE("none") {
        }, 
        DARK_FOREST("dark_forest") {
        }, 
        SWAMP("swamp") {
        };
        
        private final String name;
        public static final Codec<GrassColorModifier> CODEC;
        private static final Map<String, GrassColorModifier> BY_NAME;
        
        private GrassColorModifier(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        public static GrassColorModifier byName(final String string) {
            return (GrassColorModifier)GrassColorModifier.BY_NAME.get(string);
        }
        
        static {
            CODEC = StringRepresentable.<GrassColorModifier>fromEnum((java.util.function.Supplier<GrassColorModifier[]>)GrassColorModifier::values, (java.util.function.Function<? super String, ? extends GrassColorModifier>)GrassColorModifier::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(GrassColorModifier::getName, b -> b));
        }
    }
}
