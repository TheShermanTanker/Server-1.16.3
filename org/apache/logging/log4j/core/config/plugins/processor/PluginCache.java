package org.apache.logging.log4j.core.config.plugins.processor;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class PluginCache {
    private final Map<String, Map<String, PluginEntry>> categories;
    
    public PluginCache() {
        this.categories = (Map<String, Map<String, PluginEntry>>)new LinkedHashMap();
    }
    
    public Map<String, Map<String, PluginEntry>> getAllCategories() {
        return this.categories;
    }
    
    public Map<String, PluginEntry> getCategory(final String category) {
        final String key = category.toLowerCase();
        if (!this.categories.containsKey(key)) {
            this.categories.put(key, new LinkedHashMap());
        }
        return (Map<String, PluginEntry>)this.categories.get(key);
    }
    
    public void writeCache(final OutputStream os) throws IOException {
        try (final DataOutputStream out = new DataOutputStream((OutputStream)new BufferedOutputStream(os))) {
            out.writeInt(this.categories.size());
            for (final Map.Entry<String, Map<String, PluginEntry>> category : this.categories.entrySet()) {
                out.writeUTF((String)category.getKey());
                final Map<String, PluginEntry> m = (Map<String, PluginEntry>)category.getValue();
                out.writeInt(m.size());
                for (final Map.Entry<String, PluginEntry> entry : m.entrySet()) {
                    final PluginEntry plugin = (PluginEntry)entry.getValue();
                    out.writeUTF(plugin.getKey());
                    out.writeUTF(plugin.getClassName());
                    out.writeUTF(plugin.getName());
                    out.writeBoolean(plugin.isPrintable());
                    out.writeBoolean(plugin.isDefer());
                }
            }
        }
    }
    
    public void loadCacheFiles(final Enumeration<URL> resources) throws IOException {
        this.categories.clear();
        while (resources.hasMoreElements()) {
            final URL url = (URL)resources.nextElement();
            try (final DataInputStream in = new DataInputStream((InputStream)new BufferedInputStream(url.openStream()))) {
                for (int count = in.readInt(), i = 0; i < count; ++i) {
                    final String category = in.readUTF();
                    final Map<String, PluginEntry> m = this.getCategory(category);
                    for (int entries = in.readInt(), j = 0; j < entries; ++j) {
                        final PluginEntry entry = new PluginEntry();
                        entry.setKey(in.readUTF());
                        entry.setClassName(in.readUTF());
                        entry.setName(in.readUTF());
                        entry.setPrintable(in.readBoolean());
                        entry.setDefer(in.readBoolean());
                        entry.setCategory(category);
                        if (!m.containsKey(entry.getKey())) {
                            m.put(entry.getKey(), entry);
                        }
                    }
                }
            }
        }
    }
    
    public int size() {
        return this.categories.size();
    }
}
