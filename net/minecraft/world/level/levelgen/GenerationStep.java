package net.minecraft.world.level.levelgen;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import java.util.Map;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public class GenerationStep {
    public enum Decoration {
        RAW_GENERATION, 
        LAKES, 
        LOCAL_MODIFICATIONS, 
        UNDERGROUND_STRUCTURES, 
        SURFACE_STRUCTURES, 
        STRONGHOLDS, 
        UNDERGROUND_ORES, 
        UNDERGROUND_DECORATION, 
        VEGETAL_DECORATION, 
        TOP_LAYER_MODIFICATION;
    }
    
    public enum Carving implements StringRepresentable {
        AIR("air"), 
        LIQUID("liquid");
        
        public static final Codec<Carving> CODEC;
        private static final Map<String, Carving> BY_NAME;
        private final String name;
        
        private Carving(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Nullable
        public static Carving byName(final String string) {
            return (Carving)Carving.BY_NAME.get(string);
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        static {
            CODEC = StringRepresentable.<Carving>fromEnum((java.util.function.Supplier<Carving[]>)Carving::values, (java.util.function.Function<? super String, ? extends Carving>)Carving::byName);
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Carving::getName, a -> a));
        }
    }
}
