package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundMovePlayerPacket implements Packet<ServerGamePacketListener> {
    protected double x;
    protected double y;
    protected double z;
    protected float yRot;
    protected float xRot;
    protected boolean onGround;
    protected boolean hasPos;
    protected boolean hasRot;
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleMovePlayer(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.onGround = (nf.readUnsignedByte() != 0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.onGround ? 1 : 0);
    }
    
    public double getX(final double double1) {
        return this.hasPos ? this.x : double1;
    }
    
    public double getY(final double double1) {
        return this.hasPos ? this.y : double1;
    }
    
    public double getZ(final double double1) {
        return this.hasPos ? this.z : double1;
    }
    
    public float getYRot(final float float1) {
        return this.hasRot ? this.yRot : float1;
    }
    
    public float getXRot(final float float1) {
        return this.hasRot ? this.xRot : float1;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public static class PosRot extends ServerboundMovePlayerPacket {
        public PosRot() {
            this.hasPos = true;
            this.hasRot = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            this.x = nf.readDouble();
            this.y = nf.readDouble();
            this.z = nf.readDouble();
            this.yRot = nf.readFloat();
            this.xRot = nf.readFloat();
            super.read(nf);
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            nf.writeDouble(this.x);
            nf.writeDouble(this.y);
            nf.writeDouble(this.z);
            nf.writeFloat(this.yRot);
            nf.writeFloat(this.xRot);
            super.write(nf);
        }
    }
    
    public static class Pos extends ServerboundMovePlayerPacket {
        public Pos() {
            this.hasPos = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            this.x = nf.readDouble();
            this.y = nf.readDouble();
            this.z = nf.readDouble();
            super.read(nf);
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            nf.writeDouble(this.x);
            nf.writeDouble(this.y);
            nf.writeDouble(this.z);
            super.write(nf);
        }
    }
    
    public static class Rot extends ServerboundMovePlayerPacket {
        public Rot() {
            this.hasRot = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            this.yRot = nf.readFloat();
            this.xRot = nf.readFloat();
            super.read(nf);
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            nf.writeFloat(this.yRot);
            nf.writeFloat(this.xRot);
            super.write(nf);
        }
    }
}
