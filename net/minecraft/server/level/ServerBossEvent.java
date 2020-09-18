package net.minecraft.server.level;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.network.protocol.Packet;
import com.google.common.base.Objects;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import java.util.Collections;
import com.google.common.collect.Sets;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import java.util.Set;
import net.minecraft.world.BossEvent;

public class ServerBossEvent extends BossEvent {
    private final Set<ServerPlayer> players;
    private final Set<ServerPlayer> unmodifiablePlayers;
    private boolean visible;
    
    public ServerBossEvent(final Component nr, final BossBarColor a, final BossBarOverlay b) {
        super(Mth.createInsecureUUID(), nr, a, b);
        this.players = Sets.newHashSet();
        this.unmodifiablePlayers = (Set<ServerPlayer>)Collections.unmodifiableSet((Set)this.players);
        this.visible = true;
    }
    
    @Override
    public void setPercent(final float float1) {
        if (float1 != this.percent) {
            super.setPercent(float1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PCT);
        }
    }
    
    @Override
    public void setColor(final BossBarColor a) {
        if (a != this.color) {
            super.setColor(a);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_STYLE);
        }
    }
    
    @Override
    public void setOverlay(final BossBarOverlay b) {
        if (b != this.overlay) {
            super.setOverlay(b);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_STYLE);
        }
    }
    
    @Override
    public BossEvent setDarkenScreen(final boolean boolean1) {
        if (boolean1 != this.darkenScreen) {
            super.setDarkenScreen(boolean1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public BossEvent setPlayBossMusic(final boolean boolean1) {
        if (boolean1 != this.playBossMusic) {
            super.setPlayBossMusic(boolean1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public BossEvent setCreateWorldFog(final boolean boolean1) {
        if (boolean1 != this.createWorldFog) {
            super.setCreateWorldFog(boolean1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public void setName(final Component nr) {
        if (!Objects.equal(nr, this.name)) {
            super.setName(nr);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_NAME);
        }
    }
    
    private void broadcast(final ClientboundBossEventPacket.Operation a) {
        if (this.visible) {
            final ClientboundBossEventPacket oz3 = new ClientboundBossEventPacket(a, this);
            for (final ServerPlayer aah5 : this.players) {
                aah5.connection.send(oz3);
            }
        }
    }
    
    public void addPlayer(final ServerPlayer aah) {
        if (this.players.add(aah) && this.visible) {
            aah.connection.send(new ClientboundBossEventPacket(ClientboundBossEventPacket.Operation.ADD, this));
        }
    }
    
    public void removePlayer(final ServerPlayer aah) {
        if (this.players.remove(aah) && this.visible) {
            aah.connection.send(new ClientboundBossEventPacket(ClientboundBossEventPacket.Operation.REMOVE, this));
        }
    }
    
    public void removeAllPlayers() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/minecraft/server/level/ServerBossEvent.players:Ljava/util/Set;
        //     4: invokeinterface java/util/Set.isEmpty:()Z
        //     9: ifne            50
        //    12: aload_0         /* this */
        //    13: getfield        net/minecraft/server/level/ServerBossEvent.players:Ljava/util/Set;
        //    16: invokestatic    com/google/common/collect/Lists.newArrayList:(Ljava/lang/Iterable;)Ljava/util/ArrayList;
        //    19: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //    22: astore_1       
        //    23: aload_1        
        //    24: invokeinterface java/util/Iterator.hasNext:()Z
        //    29: ifeq            50
        //    32: aload_1        
        //    33: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    38: checkcast       Lnet/minecraft/server/level/ServerPlayer;
        //    41: astore_2       
        //    42: aload_0         /* this */
        //    43: aload_2        
        //    44: invokevirtual   net/minecraft/server/level/ServerBossEvent.removePlayer:(Lnet/minecraft/server/level/ServerPlayer;)V
        //    47: goto            23
        //    50: return         
        //    StackMapTable: 00 02 FC 00 17 07 00 8F F9 00 1A
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
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
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean boolean1) {
        if (boolean1 != this.visible) {
            this.visible = boolean1;
            for (final ServerPlayer aah4 : this.players) {
                aah4.connection.send(new ClientboundBossEventPacket(boolean1 ? ClientboundBossEventPacket.Operation.ADD : ClientboundBossEventPacket.Operation.REMOVE, this));
            }
        }
    }
    
    public Collection<ServerPlayer> getPlayers() {
        return (Collection<ServerPlayer>)this.unmodifiablePlayers;
    }
}
