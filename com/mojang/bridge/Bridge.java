package com.mojang.bridge;

import java.util.Iterator;
import java.util.ServiceLoader;
import com.mojang.bridge.launcher.LauncherProvider;
import com.mojang.bridge.launcher.Launcher;

public class Bridge {
    private static boolean INITIALIZED;
    private static Launcher LAUNCHER;
    
    private Bridge() {
    }
    
    public static Launcher getLauncher() {
        if (!Bridge.INITIALIZED) {
            synchronized (Bridge.class) {
                if (!Bridge.INITIALIZED) {
                    Bridge.LAUNCHER = createLauncher();
                    Bridge.INITIALIZED = true;
                }
            }
        }
        return Bridge.LAUNCHER;
    }
    
    private static Launcher createLauncher() {
        for (final LauncherProvider provider : ServiceLoader.load((Class)LauncherProvider.class)) {
            final Launcher launcher = provider.createLauncher();
            if (launcher != null) {
                return launcher;
            }
        }
        return null;
    }
}
