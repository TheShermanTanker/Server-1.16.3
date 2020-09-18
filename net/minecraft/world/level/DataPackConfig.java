package net.minecraft.world.level;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import java.util.List;
import com.mojang.serialization.Codec;

public class DataPackConfig {
    public static final DataPackConfig DEFAULT;
    public static final Codec<DataPackConfig> CODEC;
    private final List<String> enabled;
    private final List<String> disabled;
    
    public DataPackConfig(final List<String> list1, final List<String> list2) {
        this.enabled = ImmutableList.copyOf((java.util.Collection<?>)list1);
        this.disabled = ImmutableList.copyOf((java.util.Collection<?>)list2);
    }
    
    public List<String> getEnabled() {
        return this.enabled;
    }
    
    public List<String> getDisabled() {
        return this.disabled;
    }
    
    static {
        DEFAULT = new DataPackConfig(ImmutableList.of("vanilla"), ImmutableList.of());
        CODEC = RecordCodecBuilder.<DataPackConfig>create((java.util.function.Function<RecordCodecBuilder.Instance<DataPackConfig>, ? extends App<RecordCodecBuilder.Mu<DataPackConfig>, DataPackConfig>>)(instance -> instance.<java.util.List<Object>, java.util.List<Object>>group(Codec.STRING.listOf().fieldOf("Enabled").forGetter((java.util.function.Function<Object, java.util.List<Object>>)(brh -> brh.enabled)), Codec.STRING.listOf().fieldOf("Disabled").forGetter((java.util.function.Function<Object, java.util.List<Object>>)(brh -> brh.disabled))).apply(instance, (java.util.function.BiFunction<java.util.List<Object>, java.util.List<Object>, Object>)DataPackConfig::new)));
    }
}
