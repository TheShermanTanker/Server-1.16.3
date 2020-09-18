package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.monster.Zombie;

public class ZombieAttackGoal extends MeleeAttackGoal {
    private final Zombie zombie;
    private int raiseArmTicks;
    
    public ZombieAttackGoal(final Zombie beg, final double double2, final boolean boolean3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* beg */
        //     2: dload_2         /* double2 */
        //     3: iload           boolean3
        //     5: invokespecial   net/minecraft/world/entity/ai/goal/MeleeAttackGoal.<init>:(Lnet/minecraft/world/entity/PathfinderMob;DZ)V
        //     8: aload_0         /* this */
        //     9: aload_1         /* beg */
        //    10: putfield        net/minecraft/world/entity/ai/goal/ZombieAttackGoal.zombie:Lnet/minecraft/world/entity/monster/Zombie;
        //    13: return         
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  beg       
        //  double2   
        //  boolean3  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:265)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:158)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:93)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }
    
    @Override
    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.zombie.setAggressive(true);
        }
        else {
            this.zombie.setAggressive(false);
        }
    }
}
