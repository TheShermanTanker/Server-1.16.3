package net.minecraft.advancements;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class AdvancementList {
    private static final Logger LOGGER;
    private final Map<ResourceLocation, Advancement> advancements;
    private final Set<Advancement> roots;
    private final Set<Advancement> tasks;
    private Listener listener;
    
    public AdvancementList() {
        this.advancements = Maps.newHashMap();
        this.roots = Sets.newLinkedHashSet();
        this.tasks = Sets.newLinkedHashSet();
    }
    
    public void add(final Map<ResourceLocation, Advancement.Builder> map) {
        final Function<ResourceLocation, Advancement> function3 = Functions.forMap(this.advancements, null);
        while (!map.isEmpty()) {
            boolean boolean4 = false;
            final Iterator<Map.Entry<ResourceLocation, Advancement.Builder>> iterator5 = (Iterator<Map.Entry<ResourceLocation, Advancement.Builder>>)map.entrySet().iterator();
            while (iterator5.hasNext()) {
                final Map.Entry<ResourceLocation, Advancement.Builder> entry6 = (Map.Entry<ResourceLocation, Advancement.Builder>)iterator5.next();
                final ResourceLocation vk7 = (ResourceLocation)entry6.getKey();
                final Advancement.Builder a8 = (Advancement.Builder)entry6.getValue();
                if (a8.canBuild(function3)) {
                    final Advancement y9 = a8.build(vk7);
                    this.advancements.put(vk7, y9);
                    boolean4 = true;
                    iterator5.remove();
                    if (y9.getParent() == null) {
                        this.roots.add(y9);
                        if (this.listener == null) {
                            continue;
                        }
                        this.listener.onAddAdvancementRoot(y9);
                    }
                    else {
                        this.tasks.add(y9);
                        if (this.listener == null) {
                            continue;
                        }
                        this.listener.onAddAdvancementTask(y9);
                    }
                }
            }
            if (!boolean4) {
                for (final Map.Entry<ResourceLocation, Advancement.Builder> entry6 : map.entrySet()) {
                    AdvancementList.LOGGER.error("Couldn't load advancement {}: {}", entry6.getKey(), entry6.getValue());
                }
                break;
            }
        }
        AdvancementList.LOGGER.info("Loaded {} advancements", this.advancements.size());
    }
    
    public Iterable<Advancement> getRoots() {
        return (Iterable<Advancement>)this.roots;
    }
    
    public Collection<Advancement> getAllAdvancements() {
        return (Collection<Advancement>)this.advancements.values();
    }
    
    @Nullable
    public Advancement get(final ResourceLocation vk) {
        return (Advancement)this.advancements.get(vk);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public interface Listener {
        void onAddAdvancementRoot(final Advancement y);
        
        void onAddAdvancementTask(final Advancement y);
    }
}
