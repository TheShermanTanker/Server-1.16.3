package net.minecraft.sounds;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;

public class SoundEvent {
    public static final Codec<SoundEvent> CODEC;
    private final ResourceLocation location;
    
    public SoundEvent(final ResourceLocation vk) {
        this.location = vk;
    }
    
    static {
        CODEC = ResourceLocation.CODEC.<SoundEvent>xmap((java.util.function.Function<? super ResourceLocation, ? extends SoundEvent>)SoundEvent::new, (java.util.function.Function<? super SoundEvent, ? extends ResourceLocation>)(adn -> adn.location));
    }
}
