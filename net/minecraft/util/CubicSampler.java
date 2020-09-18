package net.minecraft.util;

import net.minecraft.world.phys.Vec3;

public class CubicSampler {
    private static final double[] GAUSSIAN_SAMPLE_KERNEL;
    
    static {
        GAUSSIAN_SAMPLE_KERNEL = new double[] { 0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0 };
    }
    
    public interface Vec3Fetcher {
        Vec3 fetch(final int integer1, final int integer2, final int integer3);
    }
}
