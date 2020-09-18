package net.minecraft.world.level.block.entity;

import java.util.stream.Collectors;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.BlockGetter;
import java.util.Arrays;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import com.google.common.collect.Lists;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.LockCode;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.MenuProvider;

public class BeaconBlockEntity extends BlockEntity implements MenuProvider, TickableBlockEntity {
    public static final MobEffect[][] BEACON_EFFECTS;
    private static final Set<MobEffect> VALID_EFFECTS;
    private List<BeaconBeamSection> beamSections;
    private List<BeaconBeamSection> checkingBeamSections;
    private int levels;
    private int lastCheckY;
    @Nullable
    private MobEffect primaryPower;
    @Nullable
    private MobEffect secondaryPower;
    @Nullable
    private Component name;
    private LockCode lockKey;
    private final ContainerData dataAccess;
    
    public BeaconBlockEntity() {
        super(BlockEntityType.BEACON);
        this.beamSections = Lists.newArrayList();
        this.checkingBeamSections = Lists.newArrayList();
        this.lastCheckY = -1;
        this.lockKey = LockCode.NO_LOCK;
        this.dataAccess = new ContainerData() {
            public int get(final int integer) {
                switch (integer) {
                    case 0: {
                        return BeaconBlockEntity.this.levels;
                    }
                    case 1: {
                        return MobEffect.getId(BeaconBlockEntity.this.primaryPower);
                    }
                    case 2: {
                        return MobEffect.getId(BeaconBlockEntity.this.secondaryPower);
                    }
                    default: {
                        return 0;
                    }
                }
            }
            
            public void set(final int integer1, final int integer2) {
                switch (integer1) {
                    case 0: {
                        BeaconBlockEntity.this.levels = integer2;
                        break;
                    }
                    case 1: {
                        if (!BeaconBlockEntity.this.level.isClientSide && !BeaconBlockEntity.this.beamSections.isEmpty()) {
                            BeaconBlockEntity.this.playSound(SoundEvents.BEACON_POWER_SELECT);
                        }
                        BeaconBlockEntity.this.primaryPower = getValidEffectById(integer2);
                        break;
                    }
                    case 2: {
                        BeaconBlockEntity.this.secondaryPower = getValidEffectById(integer2);
                        break;
                    }
                }
            }
            
            public int getCount() {
                return 3;
            }
        };
    }
    
    @Override
    public void tick() {
        final int integer2 = this.worldPosition.getX();
        final int integer3 = this.worldPosition.getY();
        final int integer4 = this.worldPosition.getZ();
        BlockPos fx5;
        if (this.lastCheckY < integer3) {
            fx5 = this.worldPosition;
            this.checkingBeamSections = Lists.newArrayList();
            this.lastCheckY = fx5.getY() - 1;
        }
        else {
            fx5 = new BlockPos(integer2, this.lastCheckY + 1, integer4);
        }
        BeaconBeamSection a6 = this.checkingBeamSections.isEmpty() ? null : ((BeaconBeamSection)this.checkingBeamSections.get(this.checkingBeamSections.size() - 1));
        final int integer5 = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, integer2, integer4);
        for (int integer6 = 0; integer6 < 10; ++integer6) {
            if (fx5.getY() > integer5) {
                break;
            }
            final BlockState cee9 = this.level.getBlockState(fx5);
            final Block bul10 = cee9.getBlock();
            if (bul10 instanceof BeaconBeamBlock) {
                final float[] arr11 = ((BeaconBeamBlock)bul10).getColor().getTextureDiffuseColors();
                if (this.checkingBeamSections.size() <= 1) {
                    a6 = new BeaconBeamSection(arr11);
                    this.checkingBeamSections.add(a6);
                }
                else if (a6 != null) {
                    if (Arrays.equals(arr11, a6.color)) {
                        a6.increaseHeight();
                    }
                    else {
                        a6 = new BeaconBeamSection(new float[] { (a6.color[0] + arr11[0]) / 2.0f, (a6.color[1] + arr11[1]) / 2.0f, (a6.color[2] + arr11[2]) / 2.0f });
                        this.checkingBeamSections.add(a6);
                    }
                }
            }
            else {
                if (a6 == null || (cee9.getLightBlock(this.level, fx5) >= 15 && bul10 != Blocks.BEDROCK)) {
                    this.checkingBeamSections.clear();
                    this.lastCheckY = integer5;
                    break;
                }
                a6.increaseHeight();
            }
            fx5 = fx5.above();
            ++this.lastCheckY;
        }
        int integer6 = this.levels;
        if (this.level.getGameTime() % 80L == 0L) {
            if (!this.beamSections.isEmpty()) {
                this.updateBase(integer2, integer3, integer4);
            }
            if (this.levels > 0 && !this.beamSections.isEmpty()) {
                this.applyEffects();
                this.playSound(SoundEvents.BEACON_AMBIENT);
            }
        }
        if (this.lastCheckY >= integer5) {
            this.lastCheckY = -1;
            final boolean boolean9 = integer6 > 0;
            this.beamSections = this.checkingBeamSections;
            if (!this.level.isClientSide) {
                final boolean boolean10 = this.levels > 0;
                if (!boolean9 && boolean10) {
                    this.playSound(SoundEvents.BEACON_ACTIVATE);
                    for (final ServerPlayer aah12 : this.level.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)ServerPlayer.class, new AABB(integer2, integer3, integer4, integer2, integer3 - 4, integer4).inflate(10.0, 5.0, 10.0))) {
                        CriteriaTriggers.CONSTRUCT_BEACON.trigger(aah12, this);
                    }
                }
                else if (boolean9 && !boolean10) {
                    this.playSound(SoundEvents.BEACON_DEACTIVATE);
                }
            }
        }
    }
    
    private void updateBase(final int integer1, final int integer2, final int integer3) {
        this.levels = 0;
        for (int integer4 = 1; integer4 <= 4; ++integer4) {
            final int integer5 = integer2 - integer4;
            if (integer5 < 0) {
                break;
            }
            boolean boolean7 = true;
            for (int integer6 = integer1 - integer4; integer6 <= integer1 + integer4 && boolean7; ++integer6) {
                for (int integer7 = integer3 - integer4; integer7 <= integer3 + integer4; ++integer7) {
                    if (!this.level.getBlockState(new BlockPos(integer6, integer5, integer7)).is(BlockTags.BEACON_BASE_BLOCKS)) {
                        boolean7 = false;
                        break;
                    }
                }
            }
            if (!boolean7) {
                break;
            }
            this.levels = integer4;
        }
    }
    
    @Override
    public void setRemoved() {
        this.playSound(SoundEvents.BEACON_DEACTIVATE);
        super.setRemoved();
    }
    
    private void applyEffects() {
        if (this.level.isClientSide || this.primaryPower == null) {
            return;
        }
        final double double2 = this.levels * 10 + 10;
        int integer4 = 0;
        if (this.levels >= 4 && this.primaryPower == this.secondaryPower) {
            integer4 = 1;
        }
        final int integer5 = (9 + this.levels * 2) * 20;
        final AABB dcf6 = new AABB(this.worldPosition).inflate(double2).expandTowards(0.0, this.level.getMaxBuildHeight(), 0.0);
        final List<Player> list7 = this.level.<Player>getEntitiesOfClass((java.lang.Class<? extends Player>)Player.class, dcf6);
        for (final Player bft9 : list7) {
            bft9.addEffect(new MobEffectInstance(this.primaryPower, integer5, integer4, true, true));
        }
        if (this.levels >= 4 && this.primaryPower != this.secondaryPower && this.secondaryPower != null) {
            for (final Player bft9 : list7) {
                bft9.addEffect(new MobEffectInstance(this.secondaryPower, integer5, 0, true, true));
            }
        }
    }
    
    public void playSound(final SoundEvent adn) {
        this.level.playSound(null, this.worldPosition, adn, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
    
    public int getLevels() {
        return this.levels;
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 3, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    @Nullable
    private static MobEffect getValidEffectById(final int integer) {
        final MobEffect app2 = MobEffect.byId(integer);
        return BeaconBlockEntity.VALID_EFFECTS.contains(app2) ? app2 : null;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.primaryPower = getValidEffectById(md.getInt("Primary"));
        this.secondaryPower = getValidEffectById(md.getInt("Secondary"));
        if (md.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(md.getString("CustomName"));
        }
        this.lockKey = LockCode.fromTag(md);
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.putInt("Primary", MobEffect.getId(this.primaryPower));
        md.putInt("Secondary", MobEffect.getId(this.secondaryPower));
        md.putInt("Levels", this.levels);
        if (this.name != null) {
            md.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        this.lockKey.addToTag(md);
        return md;
    }
    
    public void setCustomName(@Nullable final Component nr) {
        this.name = nr;
    }
    
    @Nullable
    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft) {
        if (BaseContainerBlockEntity.canUnlock(bft, this.lockKey, this.getDisplayName())) {
            return new BeaconMenu(integer, bfs, this.dataAccess, ContainerLevelAccess.create(this.level, this.getBlockPos()));
        }
        return null;
    }
    
    @Override
    public Component getDisplayName() {
        return (this.name != null) ? this.name : new TranslatableComponent("container.beacon");
    }
    
    static {
        BEACON_EFFECTS = new MobEffect[][] { { MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED }, { MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP }, { MobEffects.DAMAGE_BOOST }, { MobEffects.REGENERATION } };
        VALID_EFFECTS = (Set)Arrays.stream((Object[])BeaconBlockEntity.BEACON_EFFECTS).flatMap(Arrays::stream).collect(Collectors.toSet());
    }
    
    public static class BeaconBeamSection {
        private final float[] color;
        private int height;
        
        public BeaconBeamSection(final float[] arr) {
            this.color = arr;
            this.height = 1;
        }
        
        protected void increaseHeight() {
            ++this.height;
        }
    }
}
