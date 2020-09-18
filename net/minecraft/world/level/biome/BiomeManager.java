package net.minecraft.world.level.biome;

import net.minecraft.core.BlockPos;
import com.google.common.hash.Hashing;

public class BiomeManager {
    private final NoiseBiomeSource noiseBiomeSource;
    private final long biomeZoomSeed;
    private final BiomeZoomer zoomer;
    
    public BiomeManager(final NoiseBiomeSource a, final long long2, final BiomeZoomer bsx) {
        this.noiseBiomeSource = a;
        this.biomeZoomSeed = long2;
        this.zoomer = bsx;
    }
    
    public static long obfuscateSeed(final long long1) {
        return Hashing.sha256().hashLong(long1).asLong();
    }
    
    public BiomeManager withDifferentSource(final BiomeSource bsv) {
        return new BiomeManager(bsv, this.biomeZoomSeed, this.zoomer);
    }
    
    public Biome getBiome(final BlockPos fx) {
        return this.zoomer.getBiome(this.biomeZoomSeed, fx.getX(), fx.getY(), fx.getZ(), this.noiseBiomeSource);
    }
    
    public interface NoiseBiomeSource {
        Biome getNoiseBiome(final int integer1, final int integer2, final int integer3);
    }
}
