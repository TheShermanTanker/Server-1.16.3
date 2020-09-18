package net.minecraft.locale;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.Map;
import com.google.gson.JsonParseException;
import java.io.IOException;
import com.google.common.collect.ImmutableMap;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public abstract class Language {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final Pattern UNSUPPORTED_FORMAT_PATTERN;
    private static volatile Language instance;
    
    private static Language loadDefault() {
        final ImmutableMap.Builder<String, String> builder1 = ImmutableMap.<String, String>builder();
        final BiConsumer<String, String> biConsumer2 = (BiConsumer<String, String>)builder1::put;
        try (final InputStream inputStream3 = Language.class.getResourceAsStream("/assets/minecraft/lang/en_us.json")) {
            loadFromJson(inputStream3, biConsumer2);
        }
        catch (IOException | JsonParseException ex2) {
            final Exception ex;
            final Exception exception3 = ex;
            Language.LOGGER.error("Couldn't read strings from /assets/minecraft/lang/en_us.json", (Throwable)exception3);
        }
        final Map<String, String> map3 = (Map<String, String>)builder1.build();
        return new Language() {
            @Override
            public String getOrDefault(final String string) {
                return (String)map3.getOrDefault(string, string);
            }
            
            @Override
            public boolean has(final String string) {
                return map3.containsKey(string);
            }
        };
    }
    
    public static void loadFromJson(final InputStream inputStream, final BiConsumer<String, String> biConsumer) {
        final JsonObject jsonObject3 = Language.GSON.<JsonObject>fromJson((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
        for (final Map.Entry<String, JsonElement> entry5 : jsonObject3.entrySet()) {
            final String string6 = Language.UNSUPPORTED_FORMAT_PATTERN.matcher((CharSequence)GsonHelper.convertToString((JsonElement)entry5.getValue(), (String)entry5.getKey())).replaceAll("%$1s");
            biConsumer.accept(entry5.getKey(), string6);
        }
    }
    
    public static Language getInstance() {
        return Language.instance;
    }
    
    public abstract String getOrDefault(final String string);
    
    public abstract boolean has(final String string);
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new Gson();
        UNSUPPORTED_FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
        Language.instance = loadDefault();
    }
}
