package com.mojang.authlib.yggdrasil;

import java.util.stream.Stream;
import java.util.Optional;
import javax.annotation.Nullable;
import java.util.StringJoiner;
import com.mojang.authlib.Environment;

public enum YggdrasilEnvironment implements Environment {
    PROD("https://authserver.mojang.com", "https://api.mojang.com", "https://sessionserver.mojang.com"), 
    STAGING("https://yggdrasil-auth-staging.mojang.com", "https://api-staging.mojang.com", "https://yggdrasil-auth-session-staging.mojang.zone");
    
    private final String authHost;
    private final String accountsHost;
    private final String sessionHost;
    
    private YggdrasilEnvironment(final String authHost, final String accountsHost, final String sessionHost) {
        this.authHost = authHost;
        this.accountsHost = accountsHost;
        this.sessionHost = sessionHost;
    }
    
    public String getAuthHost() {
        return this.authHost;
    }
    
    public String getAccountsHost() {
        return this.accountsHost;
    }
    
    public String getSessionHost() {
        return this.sessionHost;
    }
    
    public String getName() {
        return this.name();
    }
    
    public String asString() {
        return new StringJoiner(", ", "", "").add((CharSequence)("authHost='" + this.authHost + "'")).add((CharSequence)("accountsHost='" + this.accountsHost + "'")).add((CharSequence)("sessionHost='" + this.sessionHost + "'")).add((CharSequence)("name='" + this.getName() + "'")).toString();
    }
    
    public static Optional<YggdrasilEnvironment> fromString(@Nullable final String value) {
        return (Optional<YggdrasilEnvironment>)Stream.of((Object[])values()).filter(env -> value != null && value.equalsIgnoreCase(env.name())).findFirst();
    }
}
