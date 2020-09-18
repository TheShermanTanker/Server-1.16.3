package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.protocol.Packet;

public class ClientboundUpdateMobEffectPacket implements Packet<ClientGamePacketListener> {
    private int entityId;
    private byte effectId;
    private byte effectAmplifier;
    private int effectDurationTicks;
    private byte flags;
    
    public ClientboundUpdateMobEffectPacket() {
    }
    
    public ClientboundUpdateMobEffectPacket(final int integer, final MobEffectInstance apr) {
        this.entityId = integer;
        this.effectId = (byte)(MobEffect.getId(apr.getEffect()) & 0xFF);
        this.effectAmplifier = (byte)(apr.getAmplifier() & 0xFF);
        if (apr.getDuration() > 32767) {
            this.effectDurationTicks = 32767;
        }
        else {
            this.effectDurationTicks = apr.getDuration();
        }
        this.flags = 0;
        if (apr.isAmbient()) {
            this.flags |= 0x1;
        }
        if (apr.isVisible()) {
            this.flags |= 0x2;
        }
        if (apr.showIcon()) {
            this.flags |= 0x4;
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readVarInt();
        this.effectId = nf.readByte();
        this.effectAmplifier = nf.readByte();
        this.effectDurationTicks = nf.readVarInt();
        this.flags = nf.readByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityId);
        nf.writeByte(this.effectId);
        nf.writeByte(this.effectAmplifier);
        nf.writeVarInt(this.effectDurationTicks);
        nf.writeByte(this.flags);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleUpdateMobEffect(this);
    }
}
