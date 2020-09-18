package net.minecraft.stats;

import net.minecraft.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

public class StatsCounter {
    protected final Object2IntMap<Stat<?>> stats;
    
    public StatsCounter() {
        (this.stats = Object2IntMaps.<Stat<?>>synchronize(new Object2IntOpenHashMap<Stat<?>>())).defaultReturnValue(0);
    }
    
    public void increment(final Player bft, final Stat<?> adv, final int integer) {
        final int integer2 = (int)Math.min(this.getValue(adv) + (long)integer, 2147483647L);
        this.setValue(bft, adv, integer2);
    }
    
    public void setValue(final Player bft, final Stat<?> adv, final int integer) {
        this.stats.put(adv, integer);
    }
    
    public int getValue(final Stat<?> adv) {
        return this.stats.getInt(adv);
    }
}
