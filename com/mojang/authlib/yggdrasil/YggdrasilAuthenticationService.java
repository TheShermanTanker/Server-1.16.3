package com.mojang.authlib.yggdrasil;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonParseException;
import java.io.IOException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.UserMigratedException;
import org.apache.commons.lang3.StringUtils;
import com.mojang.authlib.yggdrasil.response.Response;
import java.net.URL;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.Agent;
import com.mojang.authlib.EnvironmentParser;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.mojang.util.UUIDTypeAdapter;
import java.util.UUID;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Type;
import com.mojang.authlib.GameProfile;
import com.google.gson.GsonBuilder;
import java.net.Proxy;
import com.mojang.authlib.Environment;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import com.mojang.authlib.HttpAuthenticationService;

public class YggdrasilAuthenticationService extends HttpAuthenticationService {
    private static final Logger LOGGER;
    private final String clientToken;
    private final Gson gson;
    private final Environment environment;
    
    public YggdrasilAuthenticationService(final Proxy proxy, final String clientToken) {
        this(proxy, clientToken, determineEnvironment());
    }
    
    public YggdrasilAuthenticationService(final Proxy proxy, final String clientToken, final Environment environment) {
        super(proxy);
        this.clientToken = clientToken;
        this.environment = environment;
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter((Type)GameProfile.class, new GameProfileSerializer());
        builder.registerTypeAdapter((Type)PropertyMap.class, new PropertyMap.Serializer());
        builder.registerTypeAdapter((Type)UUID.class, new UUIDTypeAdapter());
        builder.registerTypeAdapter((Type)ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer());
        this.gson = builder.create();
        YggdrasilAuthenticationService.LOGGER.info("Environment: " + environment.asString());
    }
    
    private static Environment determineEnvironment() {
        return (Environment)EnvironmentParser.getEnvironmentFromProperties().orElse(YggdrasilEnvironment.PROD);
    }
    
    public UserAuthentication createUserAuthentication(final Agent agent) {
        return new YggdrasilUserAuthentication(this, agent, this.environment);
    }
    
    public MinecraftSessionService createMinecraftSessionService() {
        return new YggdrasilMinecraftSessionService(this, this.environment);
    }
    
    public GameProfileRepository createProfileRepository() {
        return new YggdrasilGameProfileRepository(this, this.environment);
    }
    
    protected <T extends Response> T makeRequest(final URL url, final Object input, final Class<T> classOfT) throws AuthenticationException {
        try {
            final String jsonResult = (input == null) ? this.performGetRequest(url) : this.performPostRequest(url, this.gson.toJson(input), "application/json");
            final T result = this.gson.<T>fromJson(jsonResult, classOfT);
            if (result == null) {
                return null;
            }
            try {
                if (!StringUtils.isNotBlank((CharSequence)result.getError())) {
                    return result;
                }
                if ("UserMigratedException".equals(result.getCause())) {
                    throw new UserMigratedException(result.getErrorMessage());
                }
                if ("ForbiddenOperationException".equals(result.getError())) {
                    throw new InvalidCredentialsException(result.getErrorMessage());
                }
                throw new AuthenticationException(result.getErrorMessage());
            }
            catch (IllegalStateException e) {
                throw new AuthenticationUnavailableException("Cannot contact authentication server", (Throwable)e);
            }
        }
        catch (IOException ex) {}
        catch (IllegalStateException ex2) {}
        catch (JsonParseException ex3) {}
    }
    
    public String getClientToken() {
        return this.clientToken;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {
        public GameProfile deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject object = (JsonObject)json;
            final UUID id = object.has("id") ? context.<UUID>deserialize(object.get("id"), (Type)UUID.class) : null;
            final String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
            return new GameProfile(id, name);
        }
        
        public JsonElement serialize(final GameProfile src, final Type typeOfSrc, final JsonSerializationContext context) {
            final JsonObject result = new JsonObject();
            if (src.getId() != null) {
                result.add("id", context.serialize(src.getId()));
            }
            if (src.getName() != null) {
                result.addProperty("name", src.getName());
            }
            return result;
        }
    }
}
