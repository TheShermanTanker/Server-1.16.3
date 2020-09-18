package com.mojang.bridge.launcher;

import com.mojang.bridge.game.GameSession;

public interface SessionEventListener {
    public static final SessionEventListener NONE = new SessionEventListener() {};
    
    default void onStartGameSession(final GameSession session) {
    }
    
    default void onLeaveGameSession(final GameSession session) {
    }
}
