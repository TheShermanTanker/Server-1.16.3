package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.Validate;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.Packet;

public class ClientboundSoundPacket implements Packet<ClientGamePacketListener> {
    private SoundEvent sound;
    private SoundSource source;
    private int x;
    private int y;
    private int z;
    private float volume;
    private float pitch;
    
    public ClientboundSoundPacket() {
    }
    
    public ClientboundSoundPacket(final SoundEvent adn, final SoundSource adp, final double double3, final double double4, final double double5, final float float6, final float float7) {
        Validate.<SoundEvent>notNull(adn, "sound", new Object[0]);
        this.sound = adn;
        this.source = adp;
        this.x = (int)(double3 * 8.0);
        this.y = (int)(double4 * 8.0);
        this.z = (int)(double5 * 8.0);
        this.volume = float6;
        this.pitch = float7;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.sound = Registry.SOUND_EVENT.byId(nf.readVarInt());
        this.source = nf.<SoundSource>readEnum(SoundSource.class);
        this.x = nf.readInt();
        this.y = nf.readInt();
        this.z = nf.readInt();
        this.volume = nf.readFloat();
        this.pitch = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(Registry.SOUND_EVENT.getId(this.sound));
        nf.writeEnum(this.source);
        nf.writeInt(this.x);
        nf.writeInt(this.y);
        nf.writeInt(this.z);
        nf.writeFloat(this.volume);
        nf.writeFloat(this.pitch);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSoundEvent(this);
    }
}
