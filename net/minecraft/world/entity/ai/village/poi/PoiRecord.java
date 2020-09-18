package net.minecraft.world.entity.ai.village.poi;

import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.core.Registry;
import java.util.Objects;
import com.mojang.datafixers.kinds.App;
import java.util.function.Function;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

public class PoiRecord {
    private final BlockPos pos;
    private final PoiType poiType;
    private int freeTickets;
    private final Runnable setDirty;
    
    public static Codec<PoiRecord> codec(final Runnable runnable) {
        return RecordCodecBuilder.<PoiRecord>create((java.util.function.Function<RecordCodecBuilder.Instance<PoiRecord>, ? extends App<RecordCodecBuilder.Mu<PoiRecord>, PoiRecord>>)(instance -> instance.<BlockPos, PoiType, Integer, Runnable>group(BlockPos.CODEC.fieldOf("pos").forGetter((java.util.function.Function<Object, BlockPos>)(azm -> azm.pos)), Registry.POINT_OF_INTEREST_TYPE.fieldOf("type").forGetter((java.util.function.Function<Object, Object>)(azm -> azm.poiType)), Codec.INT.fieldOf("free_tickets").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(azm -> azm.freeTickets)), RecordCodecBuilder.point(runnable)).<PoiRecord>apply(instance, PoiRecord::new)));
    }
    
    private PoiRecord(final BlockPos fx, final PoiType azo, final int integer, final Runnable runnable) {
        this.pos = fx.immutable();
        this.poiType = azo;
        this.freeTickets = integer;
        this.setDirty = runnable;
    }
    
    public PoiRecord(final BlockPos fx, final PoiType azo, final Runnable runnable) {
        this(fx, azo, azo.getMaxTickets(), runnable);
    }
    
    protected boolean acquireTicket() {
        if (this.freeTickets <= 0) {
            return false;
        }
        --this.freeTickets;
        this.setDirty.run();
        return true;
    }
    
    protected boolean releaseTicket() {
        if (this.freeTickets >= this.poiType.getMaxTickets()) {
            return false;
        }
        ++this.freeTickets;
        this.setDirty.run();
        return true;
    }
    
    public boolean hasSpace() {
        return this.freeTickets > 0;
    }
    
    public boolean isOccupied() {
        return this.freeTickets != this.poiType.getMaxTickets();
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public PoiType getPoiType() {
        return this.poiType;
    }
    
    public boolean equals(final Object object) {
        return this == object || (object != null && this.getClass() == object.getClass() && Objects.equals(this.pos, ((PoiRecord)object).pos));
    }
    
    public int hashCode() {
        return this.pos.hashCode();
    }
}
