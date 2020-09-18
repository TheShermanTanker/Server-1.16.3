package net.minecraft.server.packs.resources;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.List;
import java.io.IOException;
import net.minecraft.resources.ResourceLocation;

public interface ResourceManager {
    Resource getResource(final ResourceLocation vk) throws IOException;
    
    List<Resource> getResources(final ResourceLocation vk) throws IOException;
    
    Collection<ResourceLocation> listResources(final String string, final Predicate<String> predicate);
    
    public enum Empty implements ResourceManager {
        INSTANCE;
        
        public Resource getResource(final ResourceLocation vk) throws IOException {
            throw new FileNotFoundException(vk.toString());
        }
        
        public List<Resource> getResources(final ResourceLocation vk) {
            return ImmutableList.of();
        }
        
        public Collection<ResourceLocation> listResources(final String string, final Predicate<String> predicate) {
            return ImmutableSet.of();
        }
    }
}
