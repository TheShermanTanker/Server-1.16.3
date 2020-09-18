package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetJigsawBlockPacket implements Packet<ServerGamePacketListener> {
    private BlockPos pos;
    private ResourceLocation name;
    private ResourceLocation target;
    private ResourceLocation pool;
    private String finalState;
    private JigsawBlockEntity.JointType joint;
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.name = nf.readResourceLocation();
        this.target = nf.readResourceLocation();
        this.pool = nf.readResourceLocation();
        this.finalState = nf.readUtf(32767);
        this.joint = (JigsawBlockEntity.JointType)JigsawBlockEntity.JointType.byName(nf.readUtf(32767)).orElse(JigsawBlockEntity.JointType.ALIGNED);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeResourceLocation(this.name);
        nf.writeResourceLocation(this.target);
        nf.writeResourceLocation(this.pool);
        nf.writeUtf(this.finalState);
        nf.writeUtf(this.joint.getSerializedName());
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSetJigsawBlock(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public ResourceLocation getName() {
        return this.name;
    }
    
    public ResourceLocation getTarget() {
        return this.target;
    }
    
    public ResourceLocation getPool() {
        return this.pool;
    }
    
    public String getFinalState() {
        return this.finalState;
    }
    
    public JigsawBlockEntity.JointType getJoint() {
        return this.joint;
    }
}
