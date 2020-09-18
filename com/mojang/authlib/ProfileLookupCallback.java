package com.mojang.authlib;

public interface ProfileLookupCallback {
    void onProfileLookupSucceeded(final GameProfile gameProfile);
    
    void onProfileLookupFailed(final GameProfile gameProfile, final Exception exception);
}
