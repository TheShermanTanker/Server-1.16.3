package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ServerboundPlaceRecipePacket implements Packet<ServerGamePacketListener> {
    private int containerId;
    private ResourceLocation recipe;
    private boolean shiftDown;
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readByte();
        this.recipe = nf.readResourceLocation();
        this.shiftDown = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeResourceLocation(this.recipe);
        nf.writeBoolean(this.shiftDown);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handlePlaceRecipe(this);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public ResourceLocation getRecipe() {
        return this.recipe;
    }
    
    public boolean isShiftDown() {
        return this.shiftDown;
    }
}
