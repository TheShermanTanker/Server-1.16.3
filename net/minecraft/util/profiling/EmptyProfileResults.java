package net.minecraft.util.profiling;

import java.io.File;

public class EmptyProfileResults implements ProfileResults {
    public static final EmptyProfileResults EMPTY;
    
    private EmptyProfileResults() {
    }
    
    public boolean saveResults(final File file) {
        return false;
    }
    
    public long getStartTimeNano() {
        return 0L;
    }
    
    public int getStartTimeTicks() {
        return 0;
    }
    
    public long getEndTimeNano() {
        return 0L;
    }
    
    public int getEndTimeTicks() {
        return 0;
    }
    
    static {
        EMPTY = new EmptyProfileResults();
    }
}
