package net.minecraft.world.level.material;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.StateHolder;

public final class FluidState extends StateHolder<Fluid, FluidState> {
    public static final Codec<FluidState> CODEC;
    
    public FluidState(final Fluid cut, final ImmutableMap<Property<?>, Comparable<?>> immutableMap, final MapCodec<FluidState> mapCodec) {
        super(cut, immutableMap, mapCodec);
    }
    
    public Fluid getType() {
        return (Fluid)this.owner;
    }
    
    public boolean isSource() {
        return this.getType().isSource(this);
    }
    
    public boolean isEmpty() {
        return this.getType().isEmpty();
    }
    
    public float getHeight(final BlockGetter bqz, final BlockPos fx) {
        return this.getType().getHeight(this, bqz, fx);
    }
    
    public float getOwnHeight() {
        return this.getType().getOwnHeight(this);
    }
    
    public int getAmount() {
        return this.getType().getAmount(this);
    }
    
    public void tick(final Level bru, final BlockPos fx) {
        this.getType().tick(bru, fx, this);
    }
    
    public boolean isRandomlyTicking() {
        return this.getType().isRandomlyTicking();
    }
    
    public void randomTick(final Level bru, final BlockPos fx, final Random random) {
        this.getType().randomTick(bru, fx, this, random);
    }
    
    public Vec3 getFlow(final BlockGetter bqz, final BlockPos fx) {
        return this.getType().getFlow(bqz, fx, this);
    }
    
    public BlockState createLegacyBlock() {
        return this.getType().createLegacyBlock(this);
    }
    
    public boolean is(final Tag<Fluid> aej) {
        return this.getType().is(aej);
    }
    
    public float getExplosionResistance() {
        return this.getType().getExplosionResistance();
    }
    
    public boolean canBeReplacedWith(final BlockGetter bqz, final BlockPos fx, final Fluid cut, final Direction gc) {
        return this.getType().canBeReplacedWith(this, bqz, fx, cut, gc);
    }
    
    public VoxelShape getShape(final BlockGetter bqz, final BlockPos fx) {
        return this.getType().getShape(this, bqz, fx);
    }
    
    static {
        CODEC = StateHolder.<Fluid, FluidState>codec(Registry.FLUID, (java.util.function.Function<Fluid, FluidState>)Fluid::defaultFluidState).stable();
    }
}
