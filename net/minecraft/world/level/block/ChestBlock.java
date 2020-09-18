package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.inventory.AbstractContainerMenu;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.animal.Cat;
import java.util.function.BiPredicate;
import java.util.function.Function;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Container;
import java.util.Optional;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ChestBlock extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<ChestType> TYPE;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape AABB;
    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER;
    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER;
    
    protected ChestBlock(final Properties c, final Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(c, supplier);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ChestBlock.FACING, Direction.NORTH)).setValue(ChestBlock.TYPE, ChestType.SINGLE)).<Comparable, Boolean>setValue((Property<Comparable>)ChestBlock.WATERLOGGED, false));
    }
    
    public static DoubleBlockCombiner.BlockType getBlockType(final BlockState cee) {
        final ChestType cew2 = cee.<ChestType>getValue(ChestBlock.TYPE);
        if (cew2 == ChestType.SINGLE) {
            return DoubleBlockCombiner.BlockType.SINGLE;
        }
        if (cew2 == ChestType.RIGHT) {
            return DoubleBlockCombiner.BlockType.FIRST;
        }
        return DoubleBlockCombiner.BlockType.SECOND;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)ChestBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (cee3.is(this) && gc.getAxis().isHorizontal()) {
            final ChestType cew8 = cee3.<ChestType>getValue(ChestBlock.TYPE);
            if (cee1.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE && cew8 != ChestType.SINGLE && cee1.<Comparable>getValue((Property<Comparable>)ChestBlock.FACING) == cee3.<Comparable>getValue((Property<Comparable>)ChestBlock.FACING) && getConnectedDirection(cee3) == gc.getOpposite()) {
                return ((StateHolder<O, BlockState>)cee1).<ChestType, ChestType>setValue(ChestBlock.TYPE, cew8.getOpposite());
            }
        }
        else if (getConnectedDirection(cee1) == gc) {
            return ((StateHolder<O, BlockState>)cee1).<ChestType, ChestType>setValue(ChestBlock.TYPE, ChestType.SINGLE);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (cee.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
            return ChestBlock.AABB;
        }
        switch (getConnectedDirection(cee)) {
            default: {
                return ChestBlock.NORTH_AABB;
            }
            case SOUTH: {
                return ChestBlock.SOUTH_AABB;
            }
            case WEST: {
                return ChestBlock.WEST_AABB;
            }
            case EAST: {
                return ChestBlock.EAST_AABB;
            }
        }
    }
    
    public static Direction getConnectedDirection(final BlockState cee) {
        final Direction gc2 = cee.<Direction>getValue((Property<Direction>)ChestBlock.FACING);
        return (cee.<ChestType>getValue(ChestBlock.TYPE) == ChestType.LEFT) ? gc2.getClockWise() : gc2.getCounterClockWise();
    }
    
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_2        /* cew3 */
        //     4: aload_1         /* bnv */
        //     5: invokevirtual   net/minecraft/world/item/context/BlockPlaceContext.getHorizontalDirection:()Lnet/minecraft/core/Direction;
        //     8: invokevirtual   net/minecraft/core/Direction.getOpposite:()Lnet/minecraft/core/Direction;
        //    11: astore_3        /* gc4 */
        //    12: aload_1         /* bnv */
        //    13: invokevirtual   net/minecraft/world/item/context/BlockPlaceContext.getLevel:()Lnet/minecraft/world/level/Level;
        //    16: aload_1         /* bnv */
        //    17: invokevirtual   net/minecraft/world/item/context/BlockPlaceContext.getClickedPos:()Lnet/minecraft/core/BlockPos;
        //    20: invokevirtual   net/minecraft/world/level/Level.getFluidState:(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;
        //    23: astore          cuu5
        //    25: aload_1         /* bnv */
        //    26: invokevirtual   net/minecraft/world/item/context/BlockPlaceContext.isSecondaryUseActive:()Z
        //    29: istore          boolean6
        //    31: aload_1         /* bnv */
        //    32: invokevirtual   net/minecraft/world/item/context/BlockPlaceContext.getClickedFace:()Lnet/minecraft/core/Direction;
        //    35: astore          gc7
        //    37: aload           gc7
        //    39: invokevirtual   net/minecraft/core/Direction.getAxis:()Lnet/minecraft/core/Direction$Axis;
        //    42: invokevirtual   net/minecraft/core/Direction$Axis.isHorizontal:()Z
        //    45: ifeq            108
        //    48: iload           boolean6
        //    50: ifeq            108
        //    53: aload_0         /* this */
        //    54: aload_1         /* bnv */
        //    55: aload           gc7
        //    57: invokevirtual   net/minecraft/core/Direction.getOpposite:()Lnet/minecraft/core/Direction;
        //    60: invokespecial   net/minecraft/world/level/block/ChestBlock.candidatePartnerFacing:(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/core/Direction;)Lnet/minecraft/core/Direction;
        //    63: astore          gc8
        //    65: aload           gc8
        //    67: ifnull          108
        //    70: aload           gc8
        //    72: invokevirtual   net/minecraft/core/Direction.getAxis:()Lnet/minecraft/core/Direction$Axis;
        //    75: aload           gc7
        //    77: invokevirtual   net/minecraft/core/Direction.getAxis:()Lnet/minecraft/core/Direction$Axis;
        //    80: if_acmpeq       108
        //    83: aload           gc8
        //    85: astore_3        /* gc4 */
        //    86: aload_3         /* gc4 */
        //    87: invokevirtual   net/minecraft/core/Direction.getCounterClockWise:()Lnet/minecraft/core/Direction;
        //    90: aload           gc7
        //    92: invokevirtual   net/minecraft/core/Direction.getOpposite:()Lnet/minecraft/core/Direction;
        //    95: if_acmpne       104
        //    98: getstatic       net/minecraft/world/level/block/state/properties/ChestType.RIGHT:Lnet/minecraft/world/level/block/state/properties/ChestType;
        //   101: goto            107
        //   104: getstatic       net/minecraft/world/level/block/state/properties/ChestType.LEFT:Lnet/minecraft/world/level/block/state/properties/ChestType;
        //   107: astore_2        /* cew3 */
        //   108: aload_2         /* cew3 */
        //   109: getstatic       net/minecraft/world/level/block/state/properties/ChestType.SINGLE:Lnet/minecraft/world/level/block/state/properties/ChestType;
        //   112: if_acmpne       157
        //   115: iload           boolean6
        //   117: ifne            157
        //   120: aload_3         /* gc4 */
        //   121: aload_0         /* this */
        //   122: aload_1         /* bnv */
        //   123: aload_3         /* gc4 */
        //   124: invokevirtual   net/minecraft/core/Direction.getClockWise:()Lnet/minecraft/core/Direction;
        //   127: invokespecial   net/minecraft/world/level/block/ChestBlock.candidatePartnerFacing:(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/core/Direction;)Lnet/minecraft/core/Direction;
        //   130: if_acmpne       140
        //   133: getstatic       net/minecraft/world/level/block/state/properties/ChestType.LEFT:Lnet/minecraft/world/level/block/state/properties/ChestType;
        //   136: astore_2        /* cew3 */
        //   137: goto            157
        //   140: aload_3         /* gc4 */
        //   141: aload_0         /* this */
        //   142: aload_1         /* bnv */
        //   143: aload_3         /* gc4 */
        //   144: invokevirtual   net/minecraft/core/Direction.getCounterClockWise:()Lnet/minecraft/core/Direction;
        //   147: invokespecial   net/minecraft/world/level/block/ChestBlock.candidatePartnerFacing:(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/core/Direction;)Lnet/minecraft/core/Direction;
        //   150: if_acmpne       157
        //   153: getstatic       net/minecraft/world/level/block/state/properties/ChestType.RIGHT:Lnet/minecraft/world/level/block/state/properties/ChestType;
        //   156: astore_2        /* cew3 */
        //   157: aload_0         /* this */
        //   158: invokevirtual   net/minecraft/world/level/block/ChestBlock.defaultBlockState:()Lnet/minecraft/world/level/block/state/BlockState;
        //   161: getstatic       net/minecraft/world/level/block/ChestBlock.FACING:Lnet/minecraft/world/level/block/state/properties/DirectionProperty;
        //   164: aload_3         /* gc4 */
        //   165: invokevirtual   net/minecraft/world/level/block/state/BlockState.setValue:(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
        //   168: checkcast       Lnet/minecraft/world/level/block/state/BlockState;
        //   171: getstatic       net/minecraft/world/level/block/ChestBlock.TYPE:Lnet/minecraft/world/level/block/state/properties/EnumProperty;
        //   174: aload_2         /* cew3 */
        //   175: invokevirtual   net/minecraft/world/level/block/state/BlockState.setValue:(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
        //   178: checkcast       Lnet/minecraft/world/level/block/state/BlockState;
        //   181: getstatic       net/minecraft/world/level/block/ChestBlock.WATERLOGGED:Lnet/minecraft/world/level/block/state/properties/BooleanProperty;
        //   184: aload           cuu5
        //   186: invokevirtual   net/minecraft/world/level/material/FluidState.getType:()Lnet/minecraft/world/level/material/Fluid;
        //   189: getstatic       net/minecraft/world/level/material/Fluids.WATER:Lnet/minecraft/world/level/material/FlowingFluid;
        //   192: if_acmpne       199
        //   195: iconst_1       
        //   196: goto            200
        //   199: iconst_0       
        //   200: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   203: invokevirtual   net/minecraft/world/level/block/state/BlockState.setValue:(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
        //   206: checkcast       Lnet/minecraft/world/level/block/state/BlockState;
        //   209: areturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  bnv   
        //    StackMapTable: 00 07 FF 00 68 00 06 07 00 02 07 00 F7 00 07 00 27 07 01 14 01 00 00 42 07 00 5E FF 00 00 00 06 07 00 02 07 00 F7 07 00 5E 07 00 27 07 01 14 01 00 00 FA 00 1F FF 00 10 00 05 07 00 02 00 07 00 5E 07 00 27 07 01 14 00 00 FF 00 29 00 00 00 02 07 00 50 07 01 1E FF 00 00 00 00 00 03 07 00 50 07 01 1E 01
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:201)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2607)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
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
    
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)ChestBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    @Nullable
    private Direction candidatePartnerFacing(final BlockPlaceContext bnv, final Direction gc) {
        final BlockState cee4 = bnv.getLevel().getBlockState(bnv.getClickedPos().relative(gc));
        return (cee4.is(this) && cee4.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE) ? cee4.<Direction>getValue((Property<Direction>)ChestBlock.FACING) : null;
    }
    
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof ChestBlockEntity) {
                ((ChestBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof Container) {
            Containers.dropContents(bru, fx, (Container)ccg7);
            bru.updateNeighbourForOutputSignal(fx, this);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final MenuProvider aou8 = this.getMenuProvider(cee, bru, fx);
        if (aou8 != null) {
            bft.openMenu(aou8);
            bft.awardStat(this.getOpenChestStat());
            PiglinAi.angerNearbyPiglins(bft, true);
        }
        return InteractionResult.CONSUME;
    }
    
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }
    
    @Nullable
    public static Container getContainer(final ChestBlock bvb, final BlockState cee, final Level bru, final BlockPos fx, final boolean boolean5) {
        return (Container)bvb.combine(cee, bru, fx, boolean5).<Optional<Container>>apply(ChestBlock.CHEST_COMBINER).orElse(null);
    }
    
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(final BlockState cee, final Level bru, final BlockPos fx, final boolean boolean4) {
        BiPredicate<LevelAccessor, BlockPos> biPredicate6;
        if (boolean4) {
            biPredicate6 = (BiPredicate<LevelAccessor, BlockPos>)((brv, fx) -> false);
        }
        else {
            biPredicate6 = (BiPredicate<LevelAccessor, BlockPos>)ChestBlock::isChestBlockedAt;
        }
        return DoubleBlockCombiner.combineWithNeigbour(this.blockEntityType.get(), (Function<BlockState, DoubleBlockCombiner.BlockType>)ChestBlock::getBlockType, (Function<BlockState, Direction>)ChestBlock::getConnectedDirection, ChestBlock.FACING, cee, bru, fx, biPredicate6);
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return (MenuProvider)this.combine(cee, bru, fx, false).<Optional<MenuProvider>>apply(ChestBlock.MENU_PROVIDER_COMBINER).orElse(null);
    }
    
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new ChestBlockEntity();
    }
    
    public static boolean isChestBlockedAt(final LevelAccessor brv, final BlockPos fx) {
        return isBlockedChestByBlock(brv, fx) || isCatSittingOnChest(brv, fx);
    }
    
    private static boolean isBlockedChestByBlock(final BlockGetter bqz, final BlockPos fx) {
        final BlockPos fx2 = fx.above();
        return bqz.getBlockState(fx2).isRedstoneConductor(bqz, fx2);
    }
    
    private static boolean isCatSittingOnChest(final LevelAccessor brv, final BlockPos fx) {
        final List<Cat> list3 = brv.<Cat>getEntitiesOfClass((java.lang.Class<? extends Cat>)Cat.class, new AABB(fx.getX(), fx.getY() + 1, fx.getZ(), fx.getX() + 1, fx.getY() + 2, fx.getZ() + 1));
        if (!list3.isEmpty()) {
            for (final Cat azy5 : list3) {
                if (azy5.isInSittingPose()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, cee, bru, fx, false));
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)ChestBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)ChestBlock.FACING)));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)ChestBlock.FACING)));
    }
    
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ChestBlock.FACING, ChestBlock.TYPE, ChestBlock.WATERLOGGED);
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        TYPE = BlockStateProperties.CHEST_TYPE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        NORTH_AABB = Block.box(1.0, 0.0, 0.0, 15.0, 14.0, 15.0);
        SOUTH_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 1.0, 15.0, 14.0, 15.0);
        EAST_AABB = Block.box(1.0, 0.0, 1.0, 16.0, 14.0, 15.0);
        AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
        CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>() {
            public Optional<Container> acceptDouble(final ChestBlockEntity cck1, final ChestBlockEntity cck2) {
                return (Optional<Container>)Optional.of(new CompoundContainer(cck1, cck2));
            }
            
            public Optional<Container> acceptSingle(final ChestBlockEntity cck) {
                return (Optional<Container>)Optional.of(cck);
            }
            
            public Optional<Container> acceptNone() {
                return (Optional<Container>)Optional.empty();
            }
        };
        MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
            public Optional<MenuProvider> acceptDouble(final ChestBlockEntity cck1, final ChestBlockEntity cck2) {
                final Container aok4 = new CompoundContainer(cck1, cck2);
                return (Optional<MenuProvider>)Optional.of(new MenuProvider() {
                    @Nullable
                    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft) {
                        if (cck1.canOpen(bft) && cck2.canOpen(bft)) {
                            cck1.unpackLootTable(bfs.player);
                            cck2.unpackLootTable(bfs.player);
                            return ChestMenu.sixRows(integer, bfs, aok4);
                        }
                        return null;
                    }
                    
                    public Component getDisplayName() {
                        if (cck1.hasCustomName()) {
                            return cck1.getDisplayName();
                        }
                        if (cck2.hasCustomName()) {
                            return cck2.getDisplayName();
                        }
                        return new TranslatableComponent("container.chestDouble");
                    }
                });
            }
            
            public Optional<MenuProvider> acceptSingle(final ChestBlockEntity cck) {
                return (Optional<MenuProvider>)Optional.of(cck);
            }
            
            public Optional<MenuProvider> acceptNone() {
                return (Optional<MenuProvider>)Optional.empty();
            }
        };
    }
}
