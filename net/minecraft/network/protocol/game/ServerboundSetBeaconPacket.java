package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetBeaconPacket implements Packet<ServerGamePacketListener> {
    private int primary;
    private int secondary;
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.primary = nf.readVarInt();
        this.secondary = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.primary);
        nf.writeVarInt(this.secondary);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSetBeaconPacket(this);
    }
    
    public int getPrimary() {
        return this.primary;
    }
    
    public int getSecondary() {
        return this.secondary;
    }
}
