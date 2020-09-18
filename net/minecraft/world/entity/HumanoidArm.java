package net.minecraft.world.entity;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;

public enum HumanoidArm {
    LEFT((Component)new TranslatableComponent("options.mainHand.left")), 
    RIGHT((Component)new TranslatableComponent("options.mainHand.right"));
    
    private final Component name;
    
    private HumanoidArm(final Component nr) {
        this.name = nr;
    }
    
    public String toString() {
        return this.name.getString();
    }
}
