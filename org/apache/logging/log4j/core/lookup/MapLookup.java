package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.core.LogEvent;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "map", category = "Lookup")
public class MapLookup implements StrLookup {
    private final Map<String, String> map;
    
    public MapLookup() {
        this.map = null;
    }
    
    public MapLookup(final Map<String, String> map) {
        this.map = map;
    }
    
    static Map<String, String> initMap(final String[] srcArgs, final Map<String, String> destMap) {
        for (int i = 0; i < srcArgs.length; ++i) {
            final int next = i + 1;
            final String value = srcArgs[i];
            destMap.put(Integer.toString(i), value);
            destMap.put(value, ((next < srcArgs.length) ? srcArgs[next] : null));
        }
        return destMap;
    }
    
    static HashMap<String, String> newMap(final int initialCapacity) {
        return (HashMap<String, String>)new HashMap(initialCapacity);
    }
    
    @Deprecated
    public static void setMainArguments(final String... args) {
        MainMapLookup.setMainArguments(args);
    }
    
    static Map<String, String> toMap(final List<String> args) {
        if (args == null) {
            return null;
        }
        final int size = args.size();
        return initMap((String[])args.toArray((Object[])new String[size]), (Map<String, String>)newMap(size));
    }
    
    static Map<String, String> toMap(final String[] args) {
        if (args == null) {
            return null;
        }
        return initMap(args, (Map<String, String>)newMap(args.length));
    }
    
    protected Map<String, String> getMap() {
        return this.map;
    }
    
    public String lookup(final LogEvent event, final String key) {
        final boolean isMapMessage = event != null && event.getMessage() instanceof MapMessage;
        if (this.map == null && !isMapMessage) {
            return null;
        }
        if (this.map != null && this.map.containsKey(key)) {
            final String obj = (String)this.map.get(key);
            if (obj != null) {
                return obj;
            }
        }
        if (isMapMessage) {
            return ((MapMessage)event.getMessage()).get(key);
        }
        return null;
    }
    
    public String lookup(final String key) {
        if (this.map == null) {
            return null;
        }
        return (String)this.map.get(key);
    }
}
