package net.minecraft.sounds;

public class Musics {
    public static final Music MENU;
    public static final Music CREATIVE;
    public static final Music CREDITS;
    public static final Music END_BOSS;
    public static final Music END;
    public static final Music UNDER_WATER;
    public static final Music GAME;
    
    public static Music createGameMusic(final SoundEvent adn) {
        return new Music(adn, 12000, 24000, false);
    }
    
    static {
        MENU = new Music(SoundEvents.MUSIC_MENU, 20, 600, true);
        CREATIVE = new Music(SoundEvents.MUSIC_CREATIVE, 12000, 24000, false);
        CREDITS = new Music(SoundEvents.MUSIC_CREDITS, 0, 0, true);
        END_BOSS = new Music(SoundEvents.MUSIC_DRAGON, 0, 0, true);
        END = new Music(SoundEvents.MUSIC_END, 6000, 24000, true);
        UNDER_WATER = createGameMusic(SoundEvents.MUSIC_UNDER_WATER);
        GAME = createGameMusic(SoundEvents.MUSIC_GAME);
    }
}
