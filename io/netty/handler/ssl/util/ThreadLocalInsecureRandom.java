package io.netty.handler.ssl.util;

import io.netty.util.internal.PlatformDependent;
import java.util.Random;
import java.security.SecureRandom;

final class ThreadLocalInsecureRandom extends SecureRandom {
    private static final long serialVersionUID = -8209473337192526191L;
    private static final SecureRandom INSTANCE;
    
    static SecureRandom current() {
        return ThreadLocalInsecureRandom.INSTANCE;
    }
    
    private ThreadLocalInsecureRandom() {
    }
    
    public String getAlgorithm() {
        return "insecure";
    }
    
    public void setSeed(final byte[] seed) {
    }
    
    public void setSeed(final long seed) {
    }
    
    public void nextBytes(final byte[] bytes) {
        random().nextBytes(bytes);
    }
    
    public byte[] generateSeed(final int numBytes) {
        final byte[] seed = new byte[numBytes];
        random().nextBytes(seed);
        return seed;
    }
    
    public int nextInt() {
        return random().nextInt();
    }
    
    public int nextInt(final int n) {
        return random().nextInt(n);
    }
    
    public boolean nextBoolean() {
        return random().nextBoolean();
    }
    
    public long nextLong() {
        return random().nextLong();
    }
    
    public float nextFloat() {
        return random().nextFloat();
    }
    
    public double nextDouble() {
        return random().nextDouble();
    }
    
    public double nextGaussian() {
        return random().nextGaussian();
    }
    
    private static Random random() {
        return PlatformDependent.threadLocalRandom();
    }
    
    static {
        INSTANCE = new ThreadLocalInsecureRandom();
    }
}
