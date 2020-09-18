package net.minecraft.server.packs.resources;

import java.io.InputStream;
import java.io.Closeable;

public interface Resource extends Closeable {
    InputStream getInputStream();
    
    String getSourceName();
}
