package com.mojang.authlib;

import org.apache.logging.log4j.LogManager;
import java.util.Arrays;
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import java.util.Optional;
import org.apache.logging.log4j.Logger;

public class EnvironmentParser {
    private static final String PROP_PREFIX = "minecraft.api.";
    private static final Logger LOGGER;
    public static final String PROP_ENV = "minecraft.api.env";
    public static final String PROP_AUTH_HOST = "minecraft.api.auth.host";
    public static final String PROP_ACCOUNT_HOST = "minecraft.api.account.host";
    public static final String PROP_SESSION_HOST = "minecraft.api.session.host";
    
    public static Optional<Environment> getEnvironmentFromProperties() {
        final String envName = System.getProperty("minecraft.api.env");
        final Optional<Environment> env = (Optional<Environment>)YggdrasilEnvironment.fromString(envName).map(Environment.class::cast);
        return env.isPresent() ? env : fromHostNames();
    }
    
    private static Optional<Environment> fromHostNames() {
        final String auth = System.getProperty("minecraft.api.auth.host");
        final String account = System.getProperty("minecraft.api.account.host");
        final String session = System.getProperty("minecraft.api.session.host");
        if (auth != null && account != null && session != null) {
            return (Optional<Environment>)Optional.of(Environment.create(auth, account, session, "properties"));
        }
        if (auth != null || account != null || session != null) {
            EnvironmentParser.LOGGER.info(new StringBuilder().append("Ignoring hosts properties. All need to be set: ").append(Arrays.asList((Object[])new String[] { "minecraft.api.auth.host", "minecraft.api.account.host", "minecraft.api.session.host" })).toString());
        }
        return (Optional<Environment>)Optional.empty();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
