package net.minecraft.world.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class BodyRotationControl {
    private final Mob mob;
    private int headStableTime;
    private float lastStableYHeadRot;
    
    public BodyRotationControl(final Mob aqk) {
        this.mob = aqk;
    }
    
    public void clientTick() {
        if (this.isMoving()) {
            this.mob.yBodyRot = this.mob.yRot;
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.yHeadRot;
            this.headStableTime = 0;
            return;
        }
        if (this.notCarryingMobPassengers()) {
            if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15.0f) {
                this.headStableTime = 0;
                this.lastStableYHeadRot = this.mob.yHeadRot;
                this.rotateBodyIfNecessary();
            }
            else {
                ++this.headStableTime;
                if (this.headStableTime > 10) {
                    this.rotateHeadTowardsFront();
                }
            }
        }
    }
    
    private void rotateBodyIfNecessary() {
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, (float)this.mob.getMaxHeadYRot());
    }
    
    private void rotateHeadIfNecessary() {
        this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot());
    }
    
    private void rotateHeadTowardsFront() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/minecraft/world/entity/ai/control/BodyRotationControl.headStableTime:I
        //     4: bipush          10
        //     6: isub           
        //     7: istore_1        /* integer2 */
        //     8: iload_1         /* integer2 */
        //     9: i2f            
        //    10: ldc             10.0
        //    12: fdiv           
        //    13: fconst_0       
        //    14: fconst_1       
        //    15: invokestatic    net/minecraft/util/Mth.clamp:(FFF)F
        //    18: fstore_2        /* float3 */
        //    19: aload_0         /* this */
        //    20: getfield        net/minecraft/world/entity/ai/control/BodyRotationControl.mob:Lnet/minecraft/world/entity/Mob;
        //    23: invokevirtual   net/minecraft/world/entity/Mob.getMaxHeadYRot:()I
        //    26: i2f            
        //    27: fconst_1       
        //    28: fload_2         /* float3 */
        //    29: fsub           
        //    30: fmul           
        //    31: fstore_3        /* float4 */
        //    32: aload_0         /* this */
        //    33: getfield        net/minecraft/world/entity/ai/control/BodyRotationControl.mob:Lnet/minecraft/world/entity/Mob;
        //    36: aload_0         /* this */
        //    37: getfield        net/minecraft/world/entity/ai/control/BodyRotationControl.mob:Lnet/minecraft/world/entity/Mob;
        //    40: getfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //    43: aload_0         /* this */
        //    44: getfield        net/minecraft/world/entity/ai/control/BodyRotationControl.mob:Lnet/minecraft/world/entity/Mob;
        //    47: getfield        net/minecraft/world/entity/Mob.yHeadRot:F
        //    50: fload_3         /* float4 */
        //    51: invokestatic    net/minecraft/util/Mth.rotateIfNecessary:(FFF)F
        //    54: putfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //    57: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.languages.java.ast.JavaResolver$ResolveVisitor.visitMemberReferenceExpression(JavaResolver.java:219)
        //     at com.strobel.decompiler.languages.java.ast.JavaResolver$ResolveVisitor.visitMemberReferenceExpression(JavaResolver.java:40)
        //     at com.strobel.decompiler.languages.java.ast.MemberReferenceExpression.acceptVisitor(MemberReferenceExpression.java:120)
        //     at com.strobel.decompiler.languages.java.ast.JavaResolver$ResolveVisitor.visitMemberReferenceExpression(JavaResolver.java:198)
        //     at com.strobel.decompiler.languages.java.ast.JavaResolver$ResolveVisitor.visitMemberReferenceExpression(JavaResolver.java:40)
        //     at com.strobel.decompiler.languages.java.ast.MemberReferenceExpression.acceptVisitor(MemberReferenceExpression.java:120)
        //     at com.strobel.decompiler.languages.java.ast.JavaResolver.apply(JavaResolver.java:37)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1304)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:715)
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
    
    private boolean notCarryingMobPassengers() {
        return this.mob.getPassengers().isEmpty() || !(this.mob.getPassengers().get(0) instanceof Mob);
    }
    
    private boolean isMoving() {
        final double double2 = this.mob.getX() - this.mob.xo;
        final double double3 = this.mob.getZ() - this.mob.zo;
        return double2 * double2 + double3 * double3 > 2.500000277905201E-7;
    }
}
