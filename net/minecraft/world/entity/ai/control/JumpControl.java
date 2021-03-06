package net.minecraft.world.entity.ai.control;

import net.minecraft.world.entity.Mob;

public class JumpControl {
    private final Mob mob;
    protected boolean jump;
    
    public JumpControl(final Mob aqk) {
        this.mob = aqk;
    }
    
    public void jump() {
        this.jump = true;
    }
    
    public void tick() {
        this.mob.setJumping(this.jump);
        this.jump = false;
    }
}
