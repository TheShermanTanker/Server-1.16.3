package net.minecraft.world.level.block;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.tags.BlockTags;
import java.util.function.Function;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.Tag;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.BlockItem;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.world.item.Item;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.google.common.cache.LoadingCache;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.IdMapper;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Block extends BlockBehaviour implements ItemLike {
    protected static final Logger LOGGER;
    public static final IdMapper<BlockState> BLOCK_STATE_REGISTRY;
    private static final LoadingCache<VoxelShape, Boolean> SHAPE_FULL_BLOCK_CACHE;
    protected final StateDefinition<Block, BlockState> stateDefinition;
    private BlockState defaultBlockState;
    @Nullable
    private String descriptionId;
    @Nullable
    private Item item;
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<BlockStatePairKey>> OCCLUSION_CACHE;
    
    public static int getId(@Nullable final BlockState cee) {
        if (cee == null) {
            return 0;
        }
        final int integer2 = Block.BLOCK_STATE_REGISTRY.getId(cee);
        return (integer2 == -1) ? 0 : integer2;
    }
    
    public static BlockState stateById(final int integer) {
        final BlockState cee2 = Block.BLOCK_STATE_REGISTRY.byId(integer);
        return (cee2 == null) ? Blocks.AIR.defaultBlockState() : cee2;
    }
    
    public static Block byItem(@Nullable final Item blu) {
        if (blu instanceof BlockItem) {
            return ((BlockItem)blu).getBlock();
        }
        return Blocks.AIR;
    }
    
    public static BlockState pushEntitiesUp(final BlockState cee1, final BlockState cee2, final Level bru, final BlockPos fx) {
        final VoxelShape dde5 = Shapes.joinUnoptimized(cee1.getCollisionShape(bru, fx), cee2.getCollisionShape(bru, fx), BooleanOp.ONLY_SECOND).move(fx.getX(), fx.getY(), fx.getZ());
        final List<Entity> list6 = bru.getEntities(null, dde5.bounds());
        for (final Entity apx8 : list6) {
            final double double9 = Shapes.collide(Direction.Axis.Y, apx8.getBoundingBox().move(0.0, 1.0, 0.0), (Stream<VoxelShape>)Stream.of(dde5), -1.0);
            apx8.teleportTo(apx8.getX(), apx8.getY() + 1.0 + double9, apx8.getZ());
        }
        return cee2;
    }
    
    public static VoxelShape box(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        return Shapes.box(double1 / 16.0, double2 / 16.0, double3 / 16.0, double4 / 16.0, double5 / 16.0, double6 / 16.0);
    }
    
    public boolean is(final Tag<Block> aej) {
        return aej.contains(this);
    }
    
    public boolean is(final Block bul) {
        return this == bul;
    }
    
    public static BlockState updateFromNeighbourShapes(final BlockState cee, final LevelAccessor brv, final BlockPos fx) {
        BlockState cee2 = cee;
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        for (final Direction gc9 : Block.UPDATE_SHAPE_ORDER) {
            a5.setWithOffset(fx, gc9);
            cee2 = cee2.updateShape(gc9, brv.getBlockState(a5), brv, fx, a5);
        }
        return cee2;
    }
    
    public static void updateOrDestroy(final BlockState cee1, final BlockState cee2, final LevelAccessor brv, final BlockPos fx, final int integer) {
        updateOrDestroy(cee1, cee2, brv, fx, integer, 512);
    }
    
    public static void updateOrDestroy(final BlockState cee1, final BlockState cee2, final LevelAccessor brv, final BlockPos fx, final int integer5, final int integer6) {
        if (cee2 != cee1) {
            if (cee2.isAir()) {
                if (!brv.isClientSide()) {
                    brv.destroyBlock(fx, (integer5 & 0x20) == 0x0, null, integer6);
                }
            }
            else {
                brv.setBlock(fx, cee2, integer5 & 0xFFFFFFDF, integer6);
            }
        }
    }
    
    public Block(final Properties c) {
        super(c);
        final StateDefinition.Builder<Block, BlockState> a3 = new StateDefinition.Builder<Block, BlockState>(this);
        this.createBlockStateDefinition(a3);
        this.stateDefinition = a3.create((java.util.function.Function<Block, BlockState>)Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any());
    }
    
    public static boolean isExceptionForConnection(final Block bul) {
        return bul instanceof LeavesBlock || bul == Blocks.BARRIER || bul == Blocks.CARVED_PUMPKIN || bul == Blocks.JACK_O_LANTERN || bul == Blocks.MELON || bul == Blocks.PUMPKIN || bul.is(BlockTags.SHULKER_BOXES);
    }
    
    public boolean isRandomlyTicking(final BlockState cee) {
        return this.isRandomlyTicking;
    }
    
    public static boolean canSupportRigidBlock(final BlockGetter bqz, final BlockPos fx) {
        return bqz.getBlockState(fx).isFaceSturdy(bqz, fx, Direction.UP, SupportType.RIGID);
    }
    
    public static boolean canSupportCenter(final LevelReader brw, final BlockPos fx, final Direction gc) {
        final BlockState cee4 = brw.getBlockState(fx);
        return (gc != Direction.DOWN || !cee4.is(BlockTags.UNSTABLE_BOTTOM_CENTER)) && cee4.isFaceSturdy(brw, fx, gc, SupportType.CENTER);
    }
    
    public static boolean isFaceFull(final VoxelShape dde, final Direction gc) {
        final VoxelShape dde2 = dde.getFaceShape(gc);
        return isShapeFullBlock(dde2);
    }
    
    public static boolean isShapeFullBlock(final VoxelShape dde) {
        return Block.SHAPE_FULL_BLOCK_CACHE.getUnchecked(dde);
    }
    
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return !isShapeFullBlock(cee.getShape(bqz, fx)) && cee.getFluidState().isEmpty();
    }
    
    public void destroy(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
    }
    
    public static List<ItemStack> getDrops(final BlockState cee, final ServerLevel aag, final BlockPos fx, @Nullable final BlockEntity ccg) {
        final LootContext.Builder a5 = new LootContext.Builder(aag).withRandom(aag.random).<Vec3>withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(fx)).<ItemStack>withParameter(LootContextParams.TOOL, ItemStack.EMPTY).<BlockEntity>withOptionalParameter(LootContextParams.BLOCK_ENTITY, ccg);
        return cee.getDrops(a5);
    }
    
    public static List<ItemStack> getDrops(final BlockState cee, final ServerLevel aag, final BlockPos fx, @Nullable final BlockEntity ccg, @Nullable final Entity apx, final ItemStack bly) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_1         /* aag */
        //     5: invokespecial   net/minecraft/world/level/storage/loot/LootContext$Builder.<init>:(Lnet/minecraft/server/level/ServerLevel;)V
        //     8: aload_1         /* aag */
        //     9: getfield        net/minecraft/server/level/ServerLevel.random:Ljava/util/Random;
        //    12: invokevirtual   net/minecraft/world/level/storage/loot/LootContext$Builder.withRandom:(Ljava/util/Random;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;
        //    15: getstatic       net/minecraft/world/level/storage/loot/parameters/LootContextParams.ORIGIN:Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;
        //    18: aload_2         /* fx */
        //    19: invokestatic    net/minecraft/world/phys/Vec3.atCenterOf:(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;
        //    22: invokevirtual   net/minecraft/world/level/storage/loot/LootContext$Builder.withParameter:(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;
        //    25: getstatic       net/minecraft/world/level/storage/loot/parameters/LootContextParams.TOOL:Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;
        //    28: aload           bly
        //    30: invokevirtual   net/minecraft/world/level/storage/loot/LootContext$Builder.withParameter:(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;
        //    33: getstatic       net/minecraft/world/level/storage/loot/parameters/LootContextParams.THIS_ENTITY:Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;
        //    36: aload           apx
        //    38: invokevirtual   net/minecraft/world/level/storage/loot/LootContext$Builder.withOptionalParameter:(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;
        //    41: getstatic       net/minecraft/world/level/storage/loot/parameters/LootContextParams.BLOCK_ENTITY:Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;
        //    44: aload_3         /* ccg */
        //    45: invokevirtual   net/minecraft/world/level/storage/loot/LootContext$Builder.withOptionalParameter:(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;
        //    48: astore          a7
        //    50: aload_0         /* cee */
        //    51: aload           a7
        //    53: invokevirtual   net/minecraft/world/level/block/state/BlockState.getDrops:(Lnet/minecraft/world/level/storage/loot/LootContext$Builder;)Ljava/util/List;
        //    56: areturn        
        //    Signature:
        //  (Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List<Lnet/minecraft/world/item/ItemStack;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  cee   
        //  aag   
        //  fx    
        //  ccg   
        //  apx   
        //  bly   
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.isCastRequired(AstMethodBodyBuilder.java:1357)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1318)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:718)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
    
    public static void dropResources(final BlockState cee, final Level bru, final BlockPos fx) {
        if (bru instanceof ServerLevel) {
            getDrops(cee, (ServerLevel)bru, fx, null).forEach(bly -> popResource(bru, fx, bly));
            cee.spawnAfterBreak((ServerLevel)bru, fx, ItemStack.EMPTY);
        }
    }
    
    public static void dropResources(final BlockState cee, final LevelAccessor brv, final BlockPos fx, @Nullable final BlockEntity ccg) {
        if (brv instanceof ServerLevel) {
            getDrops(cee, (ServerLevel)brv, fx, ccg).forEach(bly -> popResource((Level)brv, fx, bly));
            cee.spawnAfterBreak((ServerLevel)brv, fx, ItemStack.EMPTY);
        }
    }
    
    public static void dropResources(final BlockState cee, final Level bru, final BlockPos fx, @Nullable final BlockEntity ccg, final Entity apx, final ItemStack bly) {
        if (bru instanceof ServerLevel) {
            getDrops(cee, (ServerLevel)bru, fx, ccg, apx, bly).forEach(bly -> popResource(bru, fx, bly));
            cee.spawnAfterBreak((ServerLevel)bru, fx, bly);
        }
    }
    
    public static void popResource(final Level bru, final BlockPos fx, final ItemStack bly) {
        if (bru.isClientSide || bly.isEmpty() || !bru.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            return;
        }
        final float float4 = 0.5f;
        final double double5 = bru.random.nextFloat() * 0.5f + 0.25;
        final double double6 = bru.random.nextFloat() * 0.5f + 0.25;
        final double double7 = bru.random.nextFloat() * 0.5f + 0.25;
        final ItemEntity bcs11 = new ItemEntity(bru, fx.getX() + double5, fx.getY() + double6, fx.getZ() + double7, bly);
        bcs11.setDefaultPickUpDelay();
        bru.addFreshEntity(bcs11);
    }
    
    protected void popExperience(final ServerLevel aag, final BlockPos fx, int integer) {
        if (aag.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            while (integer > 0) {
                final int integer2 = ExperienceOrb.getExperienceValue(integer);
                integer -= integer2;
                aag.addFreshEntity(new ExperienceOrb(aag, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, integer2));
            }
        }
    }
    
    public float getExplosionResistance() {
        return this.explosionResistance;
    }
    
    public void wasExploded(final Level bru, final BlockPos fx, final Explosion brm) {
    }
    
    public void stepOn(final Level bru, final BlockPos fx, final Entity apx) {
    }
    
    @Nullable
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return this.defaultBlockState();
    }
    
    public void playerDestroy(final Level bru, final Player bft, final BlockPos fx, final BlockState cee, @Nullable final BlockEntity ccg, final ItemStack bly) {
        bft.awardStat(Stats.BLOCK_MINED.get(this));
        bft.causeFoodExhaustion(0.005f);
        dropResources(cee, bru, fx, ccg, bft, bly);
    }
    
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
    }
    
    public boolean isPossibleToRespawnInThis() {
        return !this.material.isSolid() && !this.material.isLiquid();
    }
    
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("block", Registry.BLOCK.getKey(this));
        }
        return this.descriptionId;
    }
    
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        apx.causeFallDamage(float4, 1.0f);
    }
    
    public void updateEntityAfterFallOn(final BlockGetter bqz, final Entity apx) {
        apx.setDeltaMovement(apx.getDeltaMovement().multiply(1.0, 0.0, 1.0));
    }
    
    public void fillItemCategory(final CreativeModeTab bkp, final NonNullList<ItemStack> gj) {
        gj.add(new ItemStack(this));
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public float getSpeedFactor() {
        return this.speedFactor;
    }
    
    public float getJumpFactor() {
        return this.jumpFactor;
    }
    
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        bru.levelEvent(bft, 2001, fx, getId(cee));
        if (this.is(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinAi.angerNearbyPiglins(bft, false);
        }
    }
    
    public void handleRain(final Level bru, final BlockPos fx) {
    }
    
    public boolean dropFromExplosion(final Explosion brm) {
        return true;
    }
    
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
    }
    
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return this.stateDefinition;
    }
    
    protected final void registerDefaultState(final BlockState cee) {
        this.defaultBlockState = cee;
    }
    
    public final BlockState defaultBlockState() {
        return this.defaultBlockState;
    }
    
    public SoundType getSoundType(final BlockState cee) {
        return this.soundType;
    }
    
    @Override
    public Item asItem() {
        if (this.item == null) {
            this.item = Item.byBlock(this);
        }
        return this.item;
    }
    
    public boolean hasDynamicShape() {
        return this.dynamicShape;
    }
    
    public String toString() {
        return new StringBuilder().append("Block{").append(Registry.BLOCK.getKey(this)).append("}").toString();
    }
    
    @Override
    protected Block asBlock() {
        return this;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        BLOCK_STATE_REGISTRY = new IdMapper<BlockState>();
        SHAPE_FULL_BLOCK_CACHE = CacheBuilder.newBuilder().maximumSize(512L).weakKeys().<VoxelShape, Boolean>build(new CacheLoader<VoxelShape, Boolean>() {
            @Override
            public Boolean load(final VoxelShape dde) {
                return !Shapes.joinIsNotEmpty(Shapes.block(), dde, BooleanOp.NOT_SAME);
            }
        });
        OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
            final Object2ByteLinkedOpenHashMap<BlockStatePairKey> object2ByteLinkedOpenHashMap1 = new Object2ByteLinkedOpenHashMap<BlockStatePairKey>(2048, 0.25f) {
                @Override
                protected void rehash(final int integer) {
                }
            };
            object2ByteLinkedOpenHashMap1.defaultReturnValue((byte)127);
            return object2ByteLinkedOpenHashMap1;
        });
    }
    
    public static final class BlockStatePairKey {
        private final BlockState first;
        private final BlockState second;
        private final Direction direction;
        
        public BlockStatePairKey(final BlockState cee1, final BlockState cee2, final Direction gc) {
            this.first = cee1;
            this.second = cee2;
            this.direction = gc;
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof BlockStatePairKey)) {
                return false;
            }
            final BlockStatePairKey a3 = (BlockStatePairKey)object;
            return this.first == a3.first && this.second == a3.second && this.direction == a3.direction;
        }
        
        public int hashCode() {
            int integer2 = this.first.hashCode();
            integer2 = 31 * integer2 + this.second.hashCode();
            integer2 = 31 * integer2 + this.direction.hashCode();
            return integer2;
        }
    }
}
