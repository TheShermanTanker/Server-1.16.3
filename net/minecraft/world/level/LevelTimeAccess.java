package net.minecraft.world.level;

import net.minecraft.world.level.dimension.DimensionType;

public interface LevelTimeAccess extends LevelReader {
    long dayTime();
    
    default float getMoonBrightness() {
        return DimensionType.MOON_BRIGHTNESS_PER_PHASE[this.dimensionType().moonPhase(this.dayTime())];
    }
    
    default float getTimeOfDay(final float float1) {
        return this.dimensionType().timeOfDay(this.dayTime());
    }
}
