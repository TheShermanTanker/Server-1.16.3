package com.mojang.authlib;

import com.mojang.authlib.properties.PropertyMap;
import java.util.Map;
import com.mojang.authlib.exceptions.AuthenticationException;

public interface UserAuthentication {
    boolean canLogIn();
    
    void logIn() throws AuthenticationException;
    
    void logOut();
    
    boolean isLoggedIn();
    
    boolean canPlayOnline();
    
    GameProfile[] getAvailableProfiles();
    
    GameProfile getSelectedProfile();
    
    void selectGameProfile(final GameProfile gameProfile) throws AuthenticationException;
    
    void loadFromStorage(final Map<String, Object> map);
    
    Map<String, Object> saveForStorage();
    
    void setUsername(final String string);
    
    void setPassword(final String string);
    
    String getAuthenticatedToken();
    
    String getUserID();
    
    PropertyMap getUserProperties();
    
    UserType getUserType();
}
