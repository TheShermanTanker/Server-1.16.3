package net.minecraft.world.entity.decoration;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.LightningBolt;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.Pose;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import java.util.List;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.LivingEntity;

public class ArmorStand extends LivingEntity {
    private static final Rotations DEFAULT_HEAD_POSE;
    private static final Rotations DEFAULT_BODY_POSE;
    private static final Rotations DEFAULT_LEFT_ARM_POSE;
    private static final Rotations DEFAULT_RIGHT_ARM_POSE;
    private static final Rotations DEFAULT_LEFT_LEG_POSE;
    private static final Rotations DEFAULT_RIGHT_LEG_POSE;
    private static final EntityDimensions MARKER_DIMENSIONS;
    private static final EntityDimensions BABY_DIMENSIONS;
    public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS;
    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE;
    public static final EntityDataAccessor<Rotations> DATA_BODY_POSE;
    public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE;
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE;
    public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE;
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE;
    private static final Predicate<Entity> RIDABLE_MINECARTS;
    private final NonNullList<ItemStack> handItems;
    private final NonNullList<ItemStack> armorItems;
    private boolean invisible;
    public long lastHit;
    private int disabledSlots;
    private Rotations headPose;
    private Rotations bodyPose;
    private Rotations leftArmPose;
    private Rotations rightArmPose;
    private Rotations leftLegPose;
    private Rotations rightLegPose;
    
    public ArmorStand(final EntityType<? extends ArmorStand> aqb, final Level bru) {
        super(aqb, bru);
        this.handItems = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
        this.armorItems = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
        this.headPose = ArmorStand.DEFAULT_HEAD_POSE;
        this.bodyPose = ArmorStand.DEFAULT_BODY_POSE;
        this.leftArmPose = ArmorStand.DEFAULT_LEFT_ARM_POSE;
        this.rightArmPose = ArmorStand.DEFAULT_RIGHT_ARM_POSE;
        this.leftLegPose = ArmorStand.DEFAULT_LEFT_LEG_POSE;
        this.rightLegPose = ArmorStand.DEFAULT_RIGHT_LEG_POSE;
        this.maxUpStep = 0.0f;
    }
    
    public ArmorStand(final Level bru, final double double2, final double double3, final double double4) {
        this(EntityType.ARMOR_STAND, bru);
        this.setPos(double2, double3, double4);
    }
    
    @Override
    public void refreshDimensions() {
        final double double2 = this.getX();
        final double double3 = this.getY();
        final double double4 = this.getZ();
        super.refreshDimensions();
        this.setPos(double2, double3, double4);
    }
    
    private boolean hasPhysics() {
        return !this.isMarker() && !this.isNoGravity();
    }
    
    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi() && this.hasPhysics();
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(ArmorStand.DATA_CLIENT_FLAGS, (Byte)0);
        this.entityData.<Rotations>define(ArmorStand.DATA_HEAD_POSE, ArmorStand.DEFAULT_HEAD_POSE);
        this.entityData.<Rotations>define(ArmorStand.DATA_BODY_POSE, ArmorStand.DEFAULT_BODY_POSE);
        this.entityData.<Rotations>define(ArmorStand.DATA_LEFT_ARM_POSE, ArmorStand.DEFAULT_LEFT_ARM_POSE);
        this.entityData.<Rotations>define(ArmorStand.DATA_RIGHT_ARM_POSE, ArmorStand.DEFAULT_RIGHT_ARM_POSE);
        this.entityData.<Rotations>define(ArmorStand.DATA_LEFT_LEG_POSE, ArmorStand.DEFAULT_LEFT_LEG_POSE);
        this.entityData.<Rotations>define(ArmorStand.DATA_RIGHT_LEG_POSE, ArmorStand.DEFAULT_RIGHT_LEG_POSE);
    }
    
    @Override
    public Iterable<ItemStack> getHandSlots() {
        return (Iterable<ItemStack>)this.handItems;
    }
    
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return (Iterable<ItemStack>)this.armorItems;
    }
    
    @Override
    public ItemStack getItemBySlot(final EquipmentSlot aqc) {
        switch (aqc.getType()) {
            case HAND: {
                return this.handItems.get(aqc.getIndex());
            }
            case ARMOR: {
                return this.armorItems.get(aqc.getIndex());
            }
            default: {
                return ItemStack.EMPTY;
            }
        }
    }
    
    @Override
    public void setItemSlot(final EquipmentSlot aqc, final ItemStack bly) {
        switch (aqc.getType()) {
            case HAND: {
                this.playEquipSound(bly);
                this.handItems.set(aqc.getIndex(), bly);
                break;
            }
            case ARMOR: {
                this.playEquipSound(bly);
                this.armorItems.set(aqc.getIndex(), bly);
                break;
            }
        }
    }
    
    @Override
    public boolean setSlot(final int integer, final ItemStack bly) {
        EquipmentSlot aqc4;
        if (integer == 98) {
            aqc4 = EquipmentSlot.MAINHAND;
        }
        else if (integer == 99) {
            aqc4 = EquipmentSlot.OFFHAND;
        }
        else if (integer == 100 + EquipmentSlot.HEAD.getIndex()) {
            aqc4 = EquipmentSlot.HEAD;
        }
        else if (integer == 100 + EquipmentSlot.CHEST.getIndex()) {
            aqc4 = EquipmentSlot.CHEST;
        }
        else if (integer == 100 + EquipmentSlot.LEGS.getIndex()) {
            aqc4 = EquipmentSlot.LEGS;
        }
        else {
            if (integer != 100 + EquipmentSlot.FEET.getIndex()) {
                return false;
            }
            aqc4 = EquipmentSlot.FEET;
        }
        if (bly.isEmpty() || Mob.isValidSlotForItem(aqc4, bly) || aqc4 == EquipmentSlot.HEAD) {
            this.setItemSlot(aqc4, bly);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean canTakeItem(final ItemStack bly) {
        final EquipmentSlot aqc3 = Mob.getEquipmentSlotForItem(bly);
        return this.getItemBySlot(aqc3).isEmpty() && !this.isDisabled(aqc3);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* md */
        //     2: invokespecial   net/minecraft/world/entity/LivingEntity.addAdditionalSaveData:(Lnet/minecraft/nbt/CompoundTag;)V
        //     5: new             Lnet/minecraft/nbt/ListTag;
        //     8: dup            
        //     9: invokespecial   net/minecraft/nbt/ListTag.<init>:()V
        //    12: astore_2        /* mj3 */
        //    13: aload_0         /* this */
        //    14: getfield        net/minecraft/world/entity/decoration/ArmorStand.armorItems:Lnet/minecraft/core/NonNullList;
        //    17: invokevirtual   net/minecraft/core/NonNullList.iterator:()Ljava/util/Iterator;
        //    20: astore_3       
        //    21: aload_3        
        //    22: invokeinterface java/util/Iterator.hasNext:()Z
        //    27: ifeq            76
        //    30: aload_3        
        //    31: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    36: checkcast       Lnet/minecraft/world/item/ItemStack;
        //    39: astore          bly5
        //    41: new             Lnet/minecraft/nbt/CompoundTag;
        //    44: dup            
        //    45: invokespecial   net/minecraft/nbt/CompoundTag.<init>:()V
        //    48: astore          md6
        //    50: aload           bly5
        //    52: invokevirtual   net/minecraft/world/item/ItemStack.isEmpty:()Z
        //    55: ifne            66
        //    58: aload           bly5
        //    60: aload           md6
        //    62: invokevirtual   net/minecraft/world/item/ItemStack.save:(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;
        //    65: pop            
        //    66: aload_2         /* mj3 */
        //    67: aload           md6
        //    69: invokevirtual   net/minecraft/nbt/ListTag.add:(Ljava/lang/Object;)Z
        //    72: pop            
        //    73: goto            21
        //    76: aload_1         /* md */
        //    77: ldc_w           "ArmorItems"
        //    80: aload_2         /* mj3 */
        //    81: invokevirtual   net/minecraft/nbt/CompoundTag.put:(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;
        //    84: pop            
        //    85: new             Lnet/minecraft/nbt/ListTag;
        //    88: dup            
        //    89: invokespecial   net/minecraft/nbt/ListTag.<init>:()V
        //    92: astore_3        /* mj4 */
        //    93: aload_0         /* this */
        //    94: getfield        net/minecraft/world/entity/decoration/ArmorStand.handItems:Lnet/minecraft/core/NonNullList;
        //    97: invokevirtual   net/minecraft/core/NonNullList.iterator:()Ljava/util/Iterator;
        //   100: astore          4
        //   102: aload           4
        //   104: invokeinterface java/util/Iterator.hasNext:()Z
        //   109: ifeq            159
        //   112: aload           4
        //   114: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   119: checkcast       Lnet/minecraft/world/item/ItemStack;
        //   122: astore          bly6
        //   124: new             Lnet/minecraft/nbt/CompoundTag;
        //   127: dup            
        //   128: invokespecial   net/minecraft/nbt/CompoundTag.<init>:()V
        //   131: astore          md7
        //   133: aload           bly6
        //   135: invokevirtual   net/minecraft/world/item/ItemStack.isEmpty:()Z
        //   138: ifne            149
        //   141: aload           bly6
        //   143: aload           md7
        //   145: invokevirtual   net/minecraft/world/item/ItemStack.save:(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;
        //   148: pop            
        //   149: aload_3         /* mj4 */
        //   150: aload           md7
        //   152: invokevirtual   net/minecraft/nbt/ListTag.add:(Ljava/lang/Object;)Z
        //   155: pop            
        //   156: goto            102
        //   159: aload_1         /* md */
        //   160: ldc_w           "HandItems"
        //   163: aload_3         /* mj4 */
        //   164: invokevirtual   net/minecraft/nbt/CompoundTag.put:(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;
        //   167: pop            
        //   168: aload_1         /* md */
        //   169: ldc_w           "Invisible"
        //   172: aload_0         /* this */
        //   173: invokevirtual   net/minecraft/world/entity/decoration/ArmorStand.isInvisible:()Z
        //   176: invokevirtual   net/minecraft/nbt/CompoundTag.putBoolean:(Ljava/lang/String;Z)V
        //   179: aload_1         /* md */
        //   180: ldc_w           "Small"
        //   183: aload_0         /* this */
        //   184: invokevirtual   net/minecraft/world/entity/decoration/ArmorStand.isSmall:()Z
        //   187: invokevirtual   net/minecraft/nbt/CompoundTag.putBoolean:(Ljava/lang/String;Z)V
        //   190: aload_1         /* md */
        //   191: ldc_w           "ShowArms"
        //   194: aload_0         /* this */
        //   195: invokevirtual   net/minecraft/world/entity/decoration/ArmorStand.isShowArms:()Z
        //   198: invokevirtual   net/minecraft/nbt/CompoundTag.putBoolean:(Ljava/lang/String;Z)V
        //   201: aload_1         /* md */
        //   202: ldc_w           "DisabledSlots"
        //   205: aload_0         /* this */
        //   206: getfield        net/minecraft/world/entity/decoration/ArmorStand.disabledSlots:I
        //   209: invokevirtual   net/minecraft/nbt/CompoundTag.putInt:(Ljava/lang/String;I)V
        //   212: aload_1         /* md */
        //   213: ldc_w           "NoBasePlate"
        //   216: aload_0         /* this */
        //   217: invokevirtual   net/minecraft/world/entity/decoration/ArmorStand.isNoBasePlate:()Z
        //   220: invokevirtual   net/minecraft/nbt/CompoundTag.putBoolean:(Ljava/lang/String;Z)V
        //   223: aload_0         /* this */
        //   224: invokevirtual   net/minecraft/world/entity/decoration/ArmorStand.isMarker:()Z
        //   227: ifeq            241
        //   230: aload_1         /* md */
        //   231: ldc_w           "Marker"
        //   234: aload_0         /* this */
        //   235: invokevirtual   net/minecraft/world/entity/decoration/ArmorStand.isMarker:()Z
        //   238: invokevirtual   net/minecraft/nbt/CompoundTag.putBoolean:(Ljava/lang/String;Z)V
        //   241: aload_1         /* md */
        //   242: ldc_w           "Pose"
        //   245: aload_0         /* this */
        //   246: invokespecial   net/minecraft/world/entity/decoration/ArmorStand.writePose:()Lnet/minecraft/nbt/CompoundTag;
        //   249: invokevirtual   net/minecraft/nbt/CompoundTag.put:(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;
        //   252: pop            
        //   253: return         
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  md    
        //    StackMapTable: 00 07 FD 00 15 07 01 1B 07 01 23 FD 00 2C 00 07 01 2C F8 00 09 FF 00 19 00 05 07 00 02 07 01 2C 00 07 01 1B 07 01 23 00 00 FD 00 2E 00 07 01 2C F8 00 09 F9 00 51
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1551)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
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
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("ArmorItems", 9)) {
            final ListTag mj3 = md.getList("ArmorItems", 10);
            for (int integer4 = 0; integer4 < this.armorItems.size(); ++integer4) {
                this.armorItems.set(integer4, ItemStack.of(mj3.getCompound(integer4)));
            }
        }
        if (md.contains("HandItems", 9)) {
            final ListTag mj3 = md.getList("HandItems", 10);
            for (int integer4 = 0; integer4 < this.handItems.size(); ++integer4) {
                this.handItems.set(integer4, ItemStack.of(mj3.getCompound(integer4)));
            }
        }
        this.setInvisible(md.getBoolean("Invisible"));
        this.setSmall(md.getBoolean("Small"));
        this.setShowArms(md.getBoolean("ShowArms"));
        this.disabledSlots = md.getInt("DisabledSlots");
        this.setNoBasePlate(md.getBoolean("NoBasePlate"));
        this.setMarker(md.getBoolean("Marker"));
        this.noPhysics = !this.hasPhysics();
        final CompoundTag md2 = md.getCompound("Pose");
        this.readPose(md2);
    }
    
    private void readPose(final CompoundTag md) {
        final ListTag mj3 = md.getList("Head", 5);
        this.setHeadPose(mj3.isEmpty() ? ArmorStand.DEFAULT_HEAD_POSE : new Rotations(mj3));
        final ListTag mj4 = md.getList("Body", 5);
        this.setBodyPose(mj4.isEmpty() ? ArmorStand.DEFAULT_BODY_POSE : new Rotations(mj4));
        final ListTag mj5 = md.getList("LeftArm", 5);
        this.setLeftArmPose(mj5.isEmpty() ? ArmorStand.DEFAULT_LEFT_ARM_POSE : new Rotations(mj5));
        final ListTag mj6 = md.getList("RightArm", 5);
        this.setRightArmPose(mj6.isEmpty() ? ArmorStand.DEFAULT_RIGHT_ARM_POSE : new Rotations(mj6));
        final ListTag mj7 = md.getList("LeftLeg", 5);
        this.setLeftLegPose(mj7.isEmpty() ? ArmorStand.DEFAULT_LEFT_LEG_POSE : new Rotations(mj7));
        final ListTag mj8 = md.getList("RightLeg", 5);
        this.setRightLegPose(mj8.isEmpty() ? ArmorStand.DEFAULT_RIGHT_LEG_POSE : new Rotations(mj8));
    }
    
    private CompoundTag writePose() {
        final CompoundTag md2 = new CompoundTag();
        if (!ArmorStand.DEFAULT_HEAD_POSE.equals(this.headPose)) {
            md2.put("Head", (Tag)this.headPose.save());
        }
        if (!ArmorStand.DEFAULT_BODY_POSE.equals(this.bodyPose)) {
            md2.put("Body", (Tag)this.bodyPose.save());
        }
        if (!ArmorStand.DEFAULT_LEFT_ARM_POSE.equals(this.leftArmPose)) {
            md2.put("LeftArm", (Tag)this.leftArmPose.save());
        }
        if (!ArmorStand.DEFAULT_RIGHT_ARM_POSE.equals(this.rightArmPose)) {
            md2.put("RightArm", (Tag)this.rightArmPose.save());
        }
        if (!ArmorStand.DEFAULT_LEFT_LEG_POSE.equals(this.leftLegPose)) {
            md2.put("LeftLeg", (Tag)this.leftLegPose.save());
        }
        if (!ArmorStand.DEFAULT_RIGHT_LEG_POSE.equals(this.rightLegPose)) {
            md2.put("RightLeg", (Tag)this.rightLegPose.save());
        }
        return md2;
    }
    
    @Override
    public boolean isPushable() {
        return false;
    }
    
    @Override
    protected void doPush(final Entity apx) {
    }
    
    @Override
    protected void pushEntities() {
        final List<Entity> list2 = this.level.getEntities(this, this.getBoundingBox(), ArmorStand.RIDABLE_MINECARTS);
        for (int integer3 = 0; integer3 < list2.size(); ++integer3) {
            final Entity apx4 = (Entity)list2.get(integer3);
            if (this.distanceToSqr(apx4) <= 0.2) {
                apx4.push(this);
            }
        }
    }
    
    @Override
    public InteractionResult interactAt(final Player bft, final Vec3 dck, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (this.isMarker() || bly5.getItem() == Items.NAME_TAG) {
            return InteractionResult.PASS;
        }
        if (bft.isSpectator()) {
            return InteractionResult.SUCCESS;
        }
        if (bft.level.isClientSide) {
            return InteractionResult.CONSUME;
        }
        final EquipmentSlot aqc6 = Mob.getEquipmentSlotForItem(bly5);
        if (bly5.isEmpty()) {
            final EquipmentSlot aqc7 = this.getClickedSlot(dck);
            final EquipmentSlot aqc8 = this.isDisabled(aqc7) ? aqc6 : aqc7;
            if (this.hasItemInSlot(aqc8) && this.swapItem(bft, aqc8, bly5, aoq)) {
                return InteractionResult.SUCCESS;
            }
        }
        else {
            if (this.isDisabled(aqc6)) {
                return InteractionResult.FAIL;
            }
            if (aqc6.getType() == EquipmentSlot.Type.HAND && !this.isShowArms()) {
                return InteractionResult.FAIL;
            }
            if (this.swapItem(bft, aqc6, bly5, aoq)) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    private EquipmentSlot getClickedSlot(final Vec3 dck) {
        EquipmentSlot aqc3 = EquipmentSlot.MAINHAND;
        final boolean boolean4 = this.isSmall();
        final double double5 = boolean4 ? (dck.y * 2.0) : dck.y;
        final EquipmentSlot aqc4 = EquipmentSlot.FEET;
        if (double5 >= 0.1 && double5 < 0.1 + (boolean4 ? 0.8 : 0.45) && this.hasItemInSlot(aqc4)) {
            aqc3 = EquipmentSlot.FEET;
        }
        else if (double5 >= 0.9 + (boolean4 ? 0.3 : 0.0) && double5 < 0.9 + (boolean4 ? 1.0 : 0.7) && this.hasItemInSlot(EquipmentSlot.CHEST)) {
            aqc3 = EquipmentSlot.CHEST;
        }
        else if (double5 >= 0.4 && double5 < 0.4 + (boolean4 ? 1.0 : 0.8) && this.hasItemInSlot(EquipmentSlot.LEGS)) {
            aqc3 = EquipmentSlot.LEGS;
        }
        else if (double5 >= 1.6 && this.hasItemInSlot(EquipmentSlot.HEAD)) {
            aqc3 = EquipmentSlot.HEAD;
        }
        else if (!this.hasItemInSlot(EquipmentSlot.MAINHAND) && this.hasItemInSlot(EquipmentSlot.OFFHAND)) {
            aqc3 = EquipmentSlot.OFFHAND;
        }
        return aqc3;
    }
    
    private boolean isDisabled(final EquipmentSlot aqc) {
        return (this.disabledSlots & 1 << aqc.getFilterFlag()) != 0x0 || (aqc.getType() == EquipmentSlot.Type.HAND && !this.isShowArms());
    }
    
    private boolean swapItem(final Player bft, final EquipmentSlot aqc, final ItemStack bly, final InteractionHand aoq) {
        final ItemStack bly2 = this.getItemBySlot(aqc);
        if (!bly2.isEmpty() && (this.disabledSlots & 1 << aqc.getFilterFlag() + 8) != 0x0) {
            return false;
        }
        if (bly2.isEmpty() && (this.disabledSlots & 1 << aqc.getFilterFlag() + 16) != 0x0) {
            return false;
        }
        if (bft.abilities.instabuild && bly2.isEmpty() && !bly.isEmpty()) {
            final ItemStack bly3 = bly.copy();
            bly3.setCount(1);
            this.setItemSlot(aqc, bly3);
            return true;
        }
        if (bly.isEmpty() || bly.getCount() <= 1) {
            this.setItemSlot(aqc, bly);
            bft.setItemInHand(aoq, bly2);
            return true;
        }
        if (!bly2.isEmpty()) {
            return false;
        }
        final ItemStack bly3 = bly.copy();
        bly3.setCount(1);
        this.setItemSlot(aqc, bly3);
        bly.shrink(1);
        return true;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.level.isClientSide || this.removed) {
            return false;
        }
        if (DamageSource.OUT_OF_WORLD.equals(aph)) {
            this.remove();
            return false;
        }
        if (this.isInvulnerableTo(aph) || this.invisible || this.isMarker()) {
            return false;
        }
        if (aph.isExplosion()) {
            this.brokenByAnything(aph);
            this.remove();
            return false;
        }
        if (DamageSource.IN_FIRE.equals(aph)) {
            if (this.isOnFire()) {
                this.causeDamage(aph, 0.15f);
            }
            else {
                this.setSecondsOnFire(5);
            }
            return false;
        }
        if (DamageSource.ON_FIRE.equals(aph) && this.getHealth() > 0.5f) {
            this.causeDamage(aph, 4.0f);
            return false;
        }
        final boolean boolean4 = aph.getDirectEntity() instanceof AbstractArrow;
        final boolean boolean5 = boolean4 && ((AbstractArrow)aph.getDirectEntity()).getPierceLevel() > 0;
        final boolean boolean6 = "player".equals(aph.getMsgId());
        if (!boolean6 && !boolean4) {
            return false;
        }
        if (aph.getEntity() instanceof Player && !((Player)aph.getEntity()).abilities.mayBuild) {
            return false;
        }
        if (aph.isCreativePlayer()) {
            this.playBrokenSound();
            this.showBreakingParticles();
            this.remove();
            return boolean5;
        }
        final long long7 = this.level.getGameTime();
        if (long7 - this.lastHit <= 5L || boolean4) {
            this.brokenByPlayer(aph);
            this.showBreakingParticles();
            this.remove();
        }
        else {
            this.level.broadcastEntityEvent(this, (byte)32);
            this.lastHit = long7;
        }
        return true;
    }
    
    private void showBreakingParticles() {
        if (this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).<BlockParticleOption>sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), this.getX(), this.getY(0.6666666666666666), this.getZ(), 10, this.getBbWidth() / 4.0f, this.getBbHeight() / 4.0f, this.getBbWidth() / 4.0f, 0.05);
        }
    }
    
    private void causeDamage(final DamageSource aph, final float float2) {
        float float3 = this.getHealth();
        float3 -= float2;
        if (float3 <= 0.5f) {
            this.brokenByAnything(aph);
            this.remove();
        }
        else {
            this.setHealth(float3);
        }
    }
    
    private void brokenByPlayer(final DamageSource aph) {
        Block.popResource(this.level, this.blockPosition(), new ItemStack(Items.ARMOR_STAND));
        this.brokenByAnything(aph);
    }
    
    private void brokenByAnything(final DamageSource aph) {
        this.playBrokenSound();
        this.dropAllDeathLoot(aph);
        for (int integer3 = 0; integer3 < this.handItems.size(); ++integer3) {
            final ItemStack bly4 = this.handItems.get(integer3);
            if (!bly4.isEmpty()) {
                Block.popResource(this.level, this.blockPosition().above(), bly4);
                this.handItems.set(integer3, ItemStack.EMPTY);
            }
        }
        for (int integer3 = 0; integer3 < this.armorItems.size(); ++integer3) {
            final ItemStack bly4 = this.armorItems.get(integer3);
            if (!bly4.isEmpty()) {
                Block.popResource(this.level, this.blockPosition().above(), bly4);
                this.armorItems.set(integer3, ItemStack.EMPTY);
            }
        }
    }
    
    private void playBrokenSound() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0f, 1.0f);
    }
    
    @Override
    protected float tickHeadTurn(final float float1, final float float2) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.yRot;
        return 0.0f;
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * (this.isBaby() ? 0.5f : 0.9f);
    }
    
    @Override
    public double getMyRidingOffset() {
        return this.isMarker() ? 0.0 : 0.10000000149011612;
    }
    
    @Override
    public void travel(final Vec3 dck) {
        if (!this.hasPhysics()) {
            return;
        }
        super.travel(dck);
    }
    
    @Override
    public void setYBodyRot(final float float1) {
        this.yRotO = float1;
        this.yBodyRotO = float1;
        this.yHeadRot = float1;
        this.yHeadRotO = float1;
    }
    
    @Override
    public void setYHeadRot(final float float1) {
        this.yRotO = float1;
        this.yBodyRotO = float1;
        this.yHeadRot = float1;
        this.yHeadRotO = float1;
    }
    
    @Override
    public void tick() {
        super.tick();
        final Rotations go2 = this.entityData.<Rotations>get(ArmorStand.DATA_HEAD_POSE);
        if (!this.headPose.equals(go2)) {
            this.setHeadPose(go2);
        }
        final Rotations go3 = this.entityData.<Rotations>get(ArmorStand.DATA_BODY_POSE);
        if (!this.bodyPose.equals(go3)) {
            this.setBodyPose(go3);
        }
        final Rotations go4 = this.entityData.<Rotations>get(ArmorStand.DATA_LEFT_ARM_POSE);
        if (!this.leftArmPose.equals(go4)) {
            this.setLeftArmPose(go4);
        }
        final Rotations go5 = this.entityData.<Rotations>get(ArmorStand.DATA_RIGHT_ARM_POSE);
        if (!this.rightArmPose.equals(go5)) {
            this.setRightArmPose(go5);
        }
        final Rotations go6 = this.entityData.<Rotations>get(ArmorStand.DATA_LEFT_LEG_POSE);
        if (!this.leftLegPose.equals(go6)) {
            this.setLeftLegPose(go6);
        }
        final Rotations go7 = this.entityData.<Rotations>get(ArmorStand.DATA_RIGHT_LEG_POSE);
        if (!this.rightLegPose.equals(go7)) {
            this.setRightLegPose(go7);
        }
    }
    
    @Override
    protected void updateInvisibilityStatus() {
        this.setInvisible(this.invisible);
    }
    
    @Override
    public void setInvisible(final boolean boolean1) {
        super.setInvisible(this.invisible = boolean1);
    }
    
    @Override
    public boolean isBaby() {
        return this.isSmall();
    }
    
    @Override
    public void kill() {
        this.remove();
    }
    
    @Override
    public boolean ignoreExplosion() {
        return this.isInvisible();
    }
    
    @Override
    public PushReaction getPistonPushReaction() {
        if (this.isMarker()) {
            return PushReaction.IGNORE;
        }
        return super.getPistonPushReaction();
    }
    
    private void setSmall(final boolean boolean1) {
        this.entityData.<Byte>set(ArmorStand.DATA_CLIENT_FLAGS, this.setBit(this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS), 1, boolean1));
    }
    
    public boolean isSmall() {
        return (this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS) & 0x1) != 0x0;
    }
    
    private void setShowArms(final boolean boolean1) {
        this.entityData.<Byte>set(ArmorStand.DATA_CLIENT_FLAGS, this.setBit(this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS), 4, boolean1));
    }
    
    public boolean isShowArms() {
        return (this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS) & 0x4) != 0x0;
    }
    
    private void setNoBasePlate(final boolean boolean1) {
        this.entityData.<Byte>set(ArmorStand.DATA_CLIENT_FLAGS, this.setBit(this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS), 8, boolean1));
    }
    
    public boolean isNoBasePlate() {
        return (this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS) & 0x8) != 0x0;
    }
    
    private void setMarker(final boolean boolean1) {
        this.entityData.<Byte>set(ArmorStand.DATA_CLIENT_FLAGS, this.setBit(this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS), 16, boolean1));
    }
    
    public boolean isMarker() {
        return (this.entityData.<Byte>get(ArmorStand.DATA_CLIENT_FLAGS) & 0x10) != 0x0;
    }
    
    private byte setBit(byte byte1, final int integer, final boolean boolean3) {
        if (boolean3) {
            byte1 |= (byte)integer;
        }
        else {
            byte1 &= (byte)~integer;
        }
        return byte1;
    }
    
    public void setHeadPose(final Rotations go) {
        this.headPose = go;
        this.entityData.<Rotations>set(ArmorStand.DATA_HEAD_POSE, go);
    }
    
    public void setBodyPose(final Rotations go) {
        this.bodyPose = go;
        this.entityData.<Rotations>set(ArmorStand.DATA_BODY_POSE, go);
    }
    
    public void setLeftArmPose(final Rotations go) {
        this.leftArmPose = go;
        this.entityData.<Rotations>set(ArmorStand.DATA_LEFT_ARM_POSE, go);
    }
    
    public void setRightArmPose(final Rotations go) {
        this.rightArmPose = go;
        this.entityData.<Rotations>set(ArmorStand.DATA_RIGHT_ARM_POSE, go);
    }
    
    public void setLeftLegPose(final Rotations go) {
        this.leftLegPose = go;
        this.entityData.<Rotations>set(ArmorStand.DATA_LEFT_LEG_POSE, go);
    }
    
    public void setRightLegPose(final Rotations go) {
        this.rightLegPose = go;
        this.entityData.<Rotations>set(ArmorStand.DATA_RIGHT_LEG_POSE, go);
    }
    
    public Rotations getHeadPose() {
        return this.headPose;
    }
    
    public Rotations getBodyPose() {
        return this.bodyPose;
    }
    
    @Override
    public boolean isPickable() {
        return super.isPickable() && !this.isMarker();
    }
    
    @Override
    public boolean skipAttackInteraction(final Entity apx) {
        return apx instanceof Player && !this.level.mayInteract((Player)apx, this.blockPosition());
    }
    
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
    
    @Override
    protected SoundEvent getFallDamageSound(final int integer) {
        return SoundEvents.ARMOR_STAND_FALL;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ARMOR_STAND_HIT;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }
    
    @Override
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
    }
    
    @Override
    public boolean isAffectedByPotions() {
        return false;
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (ArmorStand.DATA_CLIENT_FLAGS.equals(us)) {
            this.refreshDimensions();
            this.blocksBuilding = !this.isMarker();
        }
        super.onSyncedDataUpdated(us);
    }
    
    @Override
    public boolean attackable() {
        return false;
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return this.getDimensionsMarker(this.isMarker());
    }
    
    private EntityDimensions getDimensionsMarker(final boolean boolean1) {
        if (boolean1) {
            return ArmorStand.MARKER_DIMENSIONS;
        }
        return this.isBaby() ? ArmorStand.BABY_DIMENSIONS : this.getType().getDimensions();
    }
    
    static {
        DEFAULT_HEAD_POSE = new Rotations(0.0f, 0.0f, 0.0f);
        DEFAULT_BODY_POSE = new Rotations(0.0f, 0.0f, 0.0f);
        DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0f, 0.0f, -10.0f);
        DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0f, 0.0f, 10.0f);
        DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0f, 0.0f, -1.0f);
        DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0f, 0.0f, 1.0f);
        MARKER_DIMENSIONS = new EntityDimensions(0.0f, 0.0f, true);
        BABY_DIMENSIONS = EntityType.ARMOR_STAND.getDimensions().scale(0.5f);
        DATA_CLIENT_FLAGS = SynchedEntityData.<Byte>defineId(ArmorStand.class, EntityDataSerializers.BYTE);
        DATA_HEAD_POSE = SynchedEntityData.<Rotations>defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
        DATA_BODY_POSE = SynchedEntityData.<Rotations>defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
        DATA_LEFT_ARM_POSE = SynchedEntityData.<Rotations>defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
        DATA_RIGHT_ARM_POSE = SynchedEntityData.<Rotations>defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
        DATA_LEFT_LEG_POSE = SynchedEntityData.<Rotations>defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
        DATA_RIGHT_LEG_POSE = SynchedEntityData.<Rotations>defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
        RIDABLE_MINECARTS = (apx -> apx instanceof AbstractMinecart && ((AbstractMinecart)apx).getMinecartType() == AbstractMinecart.Type.RIDEABLE);
    }
}
