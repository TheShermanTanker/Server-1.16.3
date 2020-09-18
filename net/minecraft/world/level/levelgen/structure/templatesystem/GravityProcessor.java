package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.serialization.Codec;

public class GravityProcessor extends StructureProcessor {
    public static final Codec<GravityProcessor> CODEC;
    private final Heightmap.Types heightmap;
    private final int offset;
    
    public GravityProcessor(final Heightmap.Types a, final int integer) {
        this.heightmap = a;
        this.offset = integer;
    }
    
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(final LevelReader brw, final BlockPos fx2, final BlockPos fx3, final StructureTemplate.StructureBlockInfo c4, final StructureTemplate.StructureBlockInfo c5, final StructurePlaceSettings csu) {
        Heightmap.Types a8;
        if (brw instanceof ServerLevel) {
            if (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG) {
                a8 = Heightmap.Types.WORLD_SURFACE;
            }
            else if (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG) {
                a8 = Heightmap.Types.OCEAN_FLOOR;
            }
            else {
                a8 = this.heightmap;
            }
        }
        else {
            a8 = this.heightmap;
        }
        final int integer9 = brw.getHeight(a8, c5.pos.getX(), c5.pos.getZ()) + this.offset;
        final int integer10 = c4.pos.getY();
        return new StructureTemplate.StructureBlockInfo(new BlockPos(c5.pos.getX(), integer9 + integer10, c5.pos.getZ()), c5.state, c5.nbt);
    }
    
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.GRAVITY;
    }
    
    static {
        CODEC = RecordCodecBuilder.<GravityProcessor>create((java.util.function.Function<RecordCodecBuilder.Instance<GravityProcessor>, ? extends App<RecordCodecBuilder.Mu<GravityProcessor>, GravityProcessor>>)(instance -> instance.<Heightmap.Types, Integer>group(Heightmap.Types.CODEC.fieldOf("heightmap").orElse(Heightmap.Types.WORLD_SURFACE_WG).forGetter((java.util.function.Function<Object, Heightmap.Types>)(csf -> csf.heightmap)), Codec.INT.fieldOf("offset").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(csf -> csf.offset))).apply(instance, (java.util.function.BiFunction<Heightmap.Types, Integer, Object>)GravityProcessor::new)));
    }
}
