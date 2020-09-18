package net.minecraft.world.level.block.entity;

public class TheEndPortalBlockEntity extends BlockEntity {
    public TheEndPortalBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
    }
    
    public TheEndPortalBlockEntity() {
        this(BlockEntityType.END_PORTAL);
    }
}
