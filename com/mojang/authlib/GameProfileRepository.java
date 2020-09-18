package com.mojang.authlib;

public interface GameProfileRepository {
    void findProfilesByNames(final String[] arr, final Agent agent, final ProfileLookupCallback profileLookupCallback);
}
