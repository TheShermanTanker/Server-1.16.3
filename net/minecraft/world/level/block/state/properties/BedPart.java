package net.minecraft.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum BedPart implements StringRepresentable {
    HEAD("head"), 
    FOOT("foot");
    
    private final String name;
    
    private BedPart(final String string3) {
        this.name = string3;
    }
    
    public String toString() {
        return this.name;
    }
    
    public String getSerializedName() {
        return this.name;
    }
}
