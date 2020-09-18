package net.minecraft.world.level.storage;

import java.io.File;
import net.minecraft.world.level.LevelSettings;

public class LevelSummary implements Comparable<LevelSummary> {
    private final LevelSettings settings;
    private final LevelVersion levelVersion;
    private final String levelId;
    private final boolean requiresConversion;
    private final boolean locked;
    private final File icon;
    
    public LevelSummary(final LevelSettings brx, final LevelVersion cyf, final String string, final boolean boolean4, final boolean boolean5, final File file) {
        this.settings = brx;
        this.levelVersion = cyf;
        this.levelId = string;
        this.locked = boolean5;
        this.icon = file;
        this.requiresConversion = boolean4;
    }
    
    public int compareTo(final LevelSummary cye) {
        if (this.levelVersion.lastPlayed() < cye.levelVersion.lastPlayed()) {
            return 1;
        }
        if (this.levelVersion.lastPlayed() > cye.levelVersion.lastPlayed()) {
            return -1;
        }
        return this.levelId.compareTo(cye.levelId);
    }
    
    public LevelVersion levelVersion() {
        return this.levelVersion;
    }
}
