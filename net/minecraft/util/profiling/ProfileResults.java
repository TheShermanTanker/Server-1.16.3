package net.minecraft.util.profiling;

import java.io.File;

public interface ProfileResults {
    boolean saveResults(final File file);
    
    long getStartTimeNano();
    
    int getStartTimeTicks();
    
    long getEndTimeNano();
    
    int getEndTimeTicks();
    
    default long getNanoDuration() {
        return this.getEndTimeNano() - this.getStartTimeNano();
    }
    
    default int getTickDuration() {
        return this.getEndTimeTicks() - this.getStartTimeTicks();
    }
    
    default String demanglePath(final String string) {
        return string.replace('\u001e', '.');
    }
}
