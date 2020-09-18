package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class Runnables {
    private static final Runnable EMPTY_RUNNABLE;
    
    public static Runnable doNothing() {
        return Runnables.EMPTY_RUNNABLE;
    }
    
    private Runnables() {
    }
    
    static {
        EMPTY_RUNNABLE = (Runnable)new Runnable() {
            public void run() {
            }
        };
    }
}
