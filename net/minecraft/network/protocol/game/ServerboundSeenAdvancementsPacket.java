package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ServerboundSeenAdvancementsPacket implements Packet<ServerGamePacketListener> {
    private Action action;
    private ResourceLocation tab;
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.action = nf.<Action>readEnum(Action.class);
        if (this.action == Action.OPENED_TAB) {
            this.tab = nf.readResourceLocation();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.action);
        if (this.action == Action.OPENED_TAB) {
            nf.writeResourceLocation(this.tab);
        }
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSeenAdvancements(this);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public ResourceLocation getTab() {
        return this.tab;
    }
    
    public enum Action {
        OPENED_TAB, 
        CLOSED_SCREEN;
    }
}
