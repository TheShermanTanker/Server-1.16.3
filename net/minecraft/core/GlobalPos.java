package net.minecraft.core;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import com.mojang.serialization.Codec;

public final class GlobalPos {
    public static final Codec<GlobalPos> CODEC;
    private final ResourceKey<Level> dimension;
    private final BlockPos pos;
    
    private GlobalPos(final ResourceKey<Level> vj, final BlockPos fx) {
        this.dimension = vj;
        this.pos = fx;
    }
    
    public static GlobalPos of(final ResourceKey<Level> vj, final BlockPos fx) {
        return new GlobalPos(vj, fx);
    }
    
    public ResourceKey<Level> dimension() {
        return this.dimension;
    }
    
    public BlockPos pos() {
        return this.pos;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final GlobalPos gf3 = (GlobalPos)object;
        return Objects.equals(this.dimension, gf3.dimension) && Objects.equals(this.pos, gf3.pos);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.dimension, this.pos });
    }
    
    public String toString() {
        return this.dimension.toString() + " " + this.pos;
    }
    
    static {
        CODEC = RecordCodecBuilder.<GlobalPos>create((java.util.function.Function<RecordCodecBuilder.Instance<GlobalPos>, ? extends App<RecordCodecBuilder.Mu<GlobalPos>, GlobalPos>>)(instance -> instance.<ResourceKey<Level>, BlockPos>group(Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter((java.util.function.Function<Object, ResourceKey<Level>>)GlobalPos::dimension), BlockPos.CODEC.fieldOf("pos").forGetter((java.util.function.Function<Object, BlockPos>)GlobalPos::pos)).apply(instance, (java.util.function.BiFunction<ResourceKey<Level>, BlockPos, Object>)GlobalPos::of)));
    }
}
