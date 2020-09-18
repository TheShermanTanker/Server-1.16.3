package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetCameraPacket implements Packet<ClientGamePacketListener> {
    public int cameraId;
    
    public ClientboundSetCameraPacket() {
    }
    
    public ClientboundSetCameraPacket(final Entity apx) {
        this.cameraId = apx.getId();
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.cameraId = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.cameraId);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetCamera(this);
    }
}
