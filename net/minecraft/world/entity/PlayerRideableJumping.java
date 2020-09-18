package net.minecraft.world.entity;

public interface PlayerRideableJumping {
    boolean canJump();
    
    void handleStartJump(final int integer);
    
    void handleStopJump();
}
