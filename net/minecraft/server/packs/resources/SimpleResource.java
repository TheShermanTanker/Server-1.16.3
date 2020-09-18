package net.minecraft.server.packs.resources;

import java.io.IOException;
import javax.annotation.Nullable;
import java.io.InputStream;
import net.minecraft.resources.ResourceLocation;

public class SimpleResource implements Resource {
    private final String sourceName;
    private final ResourceLocation location;
    private final InputStream resourceStream;
    private final InputStream metadataStream;
    
    public SimpleResource(final String string, final ResourceLocation vk, final InputStream inputStream3, @Nullable final InputStream inputStream4) {
        this.sourceName = string;
        this.location = vk;
        this.resourceStream = inputStream3;
        this.metadataStream = inputStream4;
    }
    
    public InputStream getInputStream() {
        return this.resourceStream;
    }
    
    public String getSourceName() {
        return this.sourceName;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimpleResource)) {
            return false;
        }
        final SimpleResource acl3 = (SimpleResource)object;
        Label_0054: {
            if (this.location != null) {
                if (this.location.equals(acl3.location)) {
                    break Label_0054;
                }
            }
            else if (acl3.location == null) {
                break Label_0054;
            }
            return false;
        }
        if (this.sourceName != null) {
            if (this.sourceName.equals(acl3.sourceName)) {
                return true;
            }
        }
        else if (acl3.sourceName == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int integer2 = (this.sourceName != null) ? this.sourceName.hashCode() : 0;
        integer2 = 31 * integer2 + ((this.location != null) ? this.location.hashCode() : 0);
        return integer2;
    }
    
    public void close() throws IOException {
        this.resourceStream.close();
        if (this.metadataStream != null) {
            this.metadataStream.close();
        }
    }
}
