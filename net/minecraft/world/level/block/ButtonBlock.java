package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.List;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.Entity;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import javax.annotation.Nullable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class ButtonBlock extends FaceAttachedHorizontalDirectionalBlock {
    public static final BooleanProperty POWERED;
    protected static final VoxelShape CEILING_AABB_X;
    protected static final VoxelShape CEILING_AABB_Z;
    protected static final VoxelShape FLOOR_AABB_X;
    protected static final VoxelShape FLOOR_AABB_Z;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape PRESSED_CEILING_AABB_X;
    protected static final VoxelShape PRESSED_CEILING_AABB_Z;
    protected static final VoxelShape PRESSED_FLOOR_AABB_X;
    protected static final VoxelShape PRESSED_FLOOR_AABB_Z;
    protected static final VoxelShape PRESSED_NORTH_AABB;
    protected static final VoxelShape PRESSED_SOUTH_AABB;
    protected static final VoxelShape PRESSED_WEST_AABB;
    protected static final VoxelShape PRESSED_EAST_AABB;
    private final boolean sensitive;
    
    protected ButtonBlock(final boolean boolean1, final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ButtonBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)ButtonBlock.POWERED, false)).<AttachFace, AttachFace>setValue(ButtonBlock.FACE, AttachFace.WALL));
        this.sensitive = boolean1;
    }
    
    private int getPressDuration() {
        return this.sensitive ? 30 : 20;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)ButtonBlock.FACING);
        final boolean boolean7 = cee.<Boolean>getValue((Property<Boolean>)ButtonBlock.POWERED);
        switch (cee.<AttachFace>getValue(ButtonBlock.FACE)) {
            case FLOOR: {
                if (gc6.getAxis() == Direction.Axis.X) {
                    return boolean7 ? ButtonBlock.PRESSED_FLOOR_AABB_X : ButtonBlock.FLOOR_AABB_X;
                }
                return boolean7 ? ButtonBlock.PRESSED_FLOOR_AABB_Z : ButtonBlock.FLOOR_AABB_Z;
            }
            case WALL: {
                switch (gc6) {
                    case EAST: {
                        return boolean7 ? ButtonBlock.PRESSED_EAST_AABB : ButtonBlock.EAST_AABB;
                    }
                    case WEST: {
                        return boolean7 ? ButtonBlock.PRESSED_WEST_AABB : ButtonBlock.WEST_AABB;
                    }
                    case SOUTH: {
                        return boolean7 ? ButtonBlock.PRESSED_SOUTH_AABB : ButtonBlock.SOUTH_AABB;
                    }
                    default: {
                        return boolean7 ? ButtonBlock.PRESSED_NORTH_AABB : ButtonBlock.NORTH_AABB;
                    }
                }
                break;
            }
            default: {
                if (gc6.getAxis() == Direction.Axis.X) {
                    return boolean7 ? ButtonBlock.PRESSED_CEILING_AABB_X : ButtonBlock.CEILING_AABB_X;
                }
                return boolean7 ? ButtonBlock.PRESSED_CEILING_AABB_Z : ButtonBlock.CEILING_AABB_Z;
            }
        }
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (cee.<Boolean>getValue((Property<Boolean>)ButtonBlock.POWERED)) {
            return InteractionResult.CONSUME;
        }
        this.press(cee, bru, fx);
        this.playSound(bft, bru, fx, true);
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    public void press(final BlockState cee, final Level bru, final BlockPos fx) {
        bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)ButtonBlock.POWERED, true), 3);
        this.updateNeighbours(cee, bru, fx);
        bru.getBlockTicks().scheduleTick(fx, this, this.getPressDuration());
    }
    
    protected void playSound(@Nullable final Player bft, final LevelAccessor brv, final BlockPos fx, final boolean boolean4) {
        brv.playSound(boolean4 ? bft : null, fx, this.getSound(boolean4), SoundSource.BLOCKS, 0.3f, boolean4 ? 0.6f : 0.5f);
    }
    
    protected abstract SoundEvent getSound(final boolean boolean1);
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5 || cee1.is(cee4.getBlock())) {
            return;
        }
        if (cee1.<Boolean>getValue((Property<Boolean>)ButtonBlock.POWERED)) {
            this.updateNeighbours(cee1, bru, fx);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.<Boolean>getValue((Property<Boolean>)ButtonBlock.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (cee.<Boolean>getValue((Property<Boolean>)ButtonBlock.POWERED) && FaceAttachedHorizontalDirectionalBlock.getConnectedDirection(cee) == gc) {
            return 15;
        }
        return 0;
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)ButtonBlock.POWERED)) {
            return;
        }
        if (this.sensitive) {
            this.checkPressed(cee, aag, fx);
        }
        else {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)ButtonBlock.POWERED, false), 3);
            this.updateNeighbours(cee, aag, fx);
            this.playSound(null, aag, fx, false);
        }
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/minecraft/world/level/Level.isClientSide:Z
        //     4: ifne            30
        //     7: aload_0         /* this */
        //     8: getfield        net/minecraft/world/level/block/ButtonBlock.sensitive:Z
        //    11: ifeq            30
        //    14: aload_1         /* cee */
        //    15: getstatic       net/minecraft/world/level/block/ButtonBlock.POWERED:Lnet/minecraft/world/level/block/state/properties/BooleanProperty;
        //    18: invokevirtual   net/minecraft/world/level/block/state/BlockState.getValue:(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
        //    21: checkcast       Ljava/lang/Boolean;
        //    24: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    27: ifeq            31
        //    30: return         
        //    31: aload_0         /* this */
        //    32: aload_1         /* cee */
        //    33: aload_2         /* bru */
        //    34: aload_3         /* fx */
        //    35: invokespecial   net/minecraft/world/level/block/ButtonBlock.checkPressed:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
        //    38: return         
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  cee   
        //  bru   
        //  fx    
        //  apx   
        //    StackMapTable: 00 02 FF 00 1E 00 00 00 00 FF 00 00 00 04 07 00 02 07 00 3C 07 00 C4 07 00 E7 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:265)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:158)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:93)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void checkPressed(final BlockState cee, final Level bru, final BlockPos fx) {
        final List<? extends Entity> list5 = bru.getEntitiesOfClass((java.lang.Class<? extends Entity>)AbstractArrow.class, cee.getShape(bru, fx).bounds().move(fx));
        final boolean boolean6 = !list5.isEmpty();
        final boolean boolean7 = cee.<Boolean>getValue((Property<Boolean>)ButtonBlock.POWERED);
        if (boolean6 != boolean7) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)ButtonBlock.POWERED, boolean6), 3);
            this.updateNeighbours(cee, bru, fx);
            this.playSound(null, bru, fx, boolean6);
        }
        if (boolean6) {
            bru.getBlockTicks().scheduleTick(new BlockPos(fx), this, this.getPressDuration());
        }
    }
    
    private void updateNeighbours(final BlockState cee, final Level bru, final BlockPos fx) {
        bru.updateNeighborsAt(fx, this);
        bru.updateNeighborsAt(fx.relative(FaceAttachedHorizontalDirectionalBlock.getConnectedDirection(cee).getOpposite()), this);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ButtonBlock.FACING, ButtonBlock.POWERED, ButtonBlock.FACE);
    }
    
    static {
        POWERED = BlockStateProperties.POWERED;
        CEILING_AABB_X = Block.box(6.0, 14.0, 5.0, 10.0, 16.0, 11.0);
        CEILING_AABB_Z = Block.box(5.0, 14.0, 6.0, 11.0, 16.0, 10.0);
        FLOOR_AABB_X = Block.box(6.0, 0.0, 5.0, 10.0, 2.0, 11.0);
        FLOOR_AABB_Z = Block.box(5.0, 0.0, 6.0, 11.0, 2.0, 10.0);
        NORTH_AABB = Block.box(5.0, 6.0, 14.0, 11.0, 10.0, 16.0);
        SOUTH_AABB = Block.box(5.0, 6.0, 0.0, 11.0, 10.0, 2.0);
        WEST_AABB = Block.box(14.0, 6.0, 5.0, 16.0, 10.0, 11.0);
        EAST_AABB = Block.box(0.0, 6.0, 5.0, 2.0, 10.0, 11.0);
        PRESSED_CEILING_AABB_X = Block.box(6.0, 15.0, 5.0, 10.0, 16.0, 11.0);
        PRESSED_CEILING_AABB_Z = Block.box(5.0, 15.0, 6.0, 11.0, 16.0, 10.0);
        PRESSED_FLOOR_AABB_X = Block.box(6.0, 0.0, 5.0, 10.0, 1.0, 11.0);
        PRESSED_FLOOR_AABB_Z = Block.box(5.0, 0.0, 6.0, 11.0, 1.0, 10.0);
        PRESSED_NORTH_AABB = Block.box(5.0, 6.0, 15.0, 11.0, 10.0, 16.0);
        PRESSED_SOUTH_AABB = Block.box(5.0, 6.0, 0.0, 11.0, 10.0, 1.0);
        PRESSED_WEST_AABB = Block.box(15.0, 6.0, 5.0, 16.0, 10.0, 11.0);
        PRESSED_EAST_AABB = Block.box(0.0, 6.0, 5.0, 1.0, 10.0, 11.0);
    }
}
