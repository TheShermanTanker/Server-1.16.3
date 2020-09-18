package com.mojang.authlib.minecraft;

import java.util.Map;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.net.InetAddress;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.GameProfile;

public interface MinecraftSessionService {
    void joinServer(final GameProfile gameProfile, final String string2, final String string3) throws AuthenticationException;
    
    GameProfile hasJoinedServer(final GameProfile gameProfile, final String string, final InetAddress inetAddress) throws AuthenticationUnavailableException;
    
    Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(final GameProfile gameProfile, final boolean boolean2);
    
    GameProfile fillProfileProperties(final GameProfile gameProfile, final boolean boolean2);
}
