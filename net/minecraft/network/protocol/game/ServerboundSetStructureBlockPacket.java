package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.util.Mth;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetStructureBlockPacket implements Packet<ServerGamePacketListener> {
    private BlockPos pos;
    private StructureBlockEntity.UpdateType updateType;
    private StructureMode mode;
    private String name;
    private BlockPos offset;
    private BlockPos size;
    private Mirror mirror;
    private Rotation rotation;
    private String data;
    private boolean ignoreEntities;
    private boolean showAir;
    private boolean showBoundingBox;
    private float integrity;
    private long seed;
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.updateType = nf.<StructureBlockEntity.UpdateType>readEnum(StructureBlockEntity.UpdateType.class);
        this.mode = nf.<StructureMode>readEnum(StructureMode.class);
        this.name = nf.readUtf(32767);
        final int integer3 = 48;
        this.offset = new BlockPos(Mth.clamp(nf.readByte(), -48, 48), Mth.clamp(nf.readByte(), -48, 48), Mth.clamp(nf.readByte(), -48, 48));
        final int integer4 = 48;
        this.size = new BlockPos(Mth.clamp(nf.readByte(), 0, 48), Mth.clamp(nf.readByte(), 0, 48), Mth.clamp(nf.readByte(), 0, 48));
        this.mirror = nf.<Mirror>readEnum(Mirror.class);
        this.rotation = nf.<Rotation>readEnum(Rotation.class);
        this.data = nf.readUtf(12);
        this.integrity = Mth.clamp(nf.readFloat(), 0.0f, 1.0f);
        this.seed = nf.readVarLong();
        final int integer5 = nf.readByte();
        this.ignoreEntities = ((integer5 & 0x1) != 0x0);
        this.showAir = ((integer5 & 0x2) != 0x0);
        this.showBoundingBox = ((integer5 & 0x4) != 0x0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeEnum(this.updateType);
        nf.writeEnum(this.mode);
        nf.writeUtf(this.name);
        nf.writeByte(this.offset.getX());
        nf.writeByte(this.offset.getY());
        nf.writeByte(this.offset.getZ());
        nf.writeByte(this.size.getX());
        nf.writeByte(this.size.getY());
        nf.writeByte(this.size.getZ());
        nf.writeEnum(this.mirror);
        nf.writeEnum(this.rotation);
        nf.writeUtf(this.data);
        nf.writeFloat(this.integrity);
        nf.writeVarLong(this.seed);
        int integer3 = 0;
        if (this.ignoreEntities) {
            integer3 |= 0x1;
        }
        if (this.showAir) {
            integer3 |= 0x2;
        }
        if (this.showBoundingBox) {
            integer3 |= 0x4;
        }
        nf.writeByte(integer3);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSetStructureBlock(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public StructureBlockEntity.UpdateType getUpdateType() {
        return this.updateType;
    }
    
    public StructureMode getMode() {
        return this.mode;
    }
    
    public String getName() {
        return this.name;
    }
    
    public BlockPos getOffset() {
        return this.offset;
    }
    
    public BlockPos getSize() {
        return this.size;
    }
    
    public Mirror getMirror() {
        return this.mirror;
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public String getData() {
        return this.data;
    }
    
    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }
    
    public boolean isShowAir() {
        return this.showAir;
    }
    
    public boolean isShowBoundingBox() {
        return this.showBoundingBox;
    }
    
    public float getIntegrity() {
        return this.integrity;
    }
    
    public long getSeed() {
        return this.seed;
    }
}
