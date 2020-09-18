package com.mojang.brigadier;

public class LiteralMessage implements Message {
    private final String string;
    
    public LiteralMessage(final String string) {
        this.string = string;
    }
    
    public String getString() {
        return this.string;
    }
    
    public String toString() {
        return this.string;
    }
}
