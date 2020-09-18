package net.minecraft.util;

public class FastColor {
    public static class ARGB32 {
        public static int red(final int integer) {
            return integer >> 16 & 0xFF;
        }
        
        public static int green(final int integer) {
            return integer >> 8 & 0xFF;
        }
        
        public static int blue(final int integer) {
            return integer & 0xFF;
        }
    }
}
