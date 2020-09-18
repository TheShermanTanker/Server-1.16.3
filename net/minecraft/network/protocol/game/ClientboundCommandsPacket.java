package net.minecraft.network.protocol.game;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.PacketListener;
import java.util.Iterator;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import javax.annotation.Nullable;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.synchronization.SuggestionProviders;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.synchronization.ArgumentTypes;
import com.mojang.brigadier.builder.ArgumentBuilder;
import java.util.Queue;
import java.util.Collection;
import com.google.common.collect.Queues;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Map;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.network.protocol.Packet;

public class ClientboundCommandsPacket implements Packet<ClientGamePacketListener> {
    private RootCommandNode<SharedSuggestionProvider> root;
    
    public ClientboundCommandsPacket() {
    }
    
    public ClientboundCommandsPacket(final RootCommandNode<SharedSuggestionProvider> rootCommandNode) {
        this.root = rootCommandNode;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        final Entry[] arr3 = new Entry[nf.readVarInt()];
        for (int integer4 = 0; integer4 < arr3.length; ++integer4) {
            arr3[integer4] = readNode(nf);
        }
        resolveEntries(arr3);
        this.root = (RootCommandNode<SharedSuggestionProvider>)(RootCommandNode)arr3[nf.readVarInt()].node;
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        final Object2IntMap<CommandNode<SharedSuggestionProvider>> object2IntMap3 = enumerateNodes(this.root);
        final CommandNode<SharedSuggestionProvider>[] arr4 = getNodesInIdOrder(object2IntMap3);
        nf.writeVarInt(arr4.length);
        for (final CommandNode<SharedSuggestionProvider> commandNode8 : arr4) {
            writeNode(nf, commandNode8, (Map<CommandNode<SharedSuggestionProvider>, Integer>)object2IntMap3);
        }
        nf.writeVarInt(object2IntMap3.get(this.root));
    }
    
    private static void resolveEntries(final Entry[] arr) {
        final List<Entry> list2 = Lists.newArrayList(arr);
        while (!list2.isEmpty()) {
            final boolean boolean3 = list2.removeIf(a -> a.build(arr));
            if (!boolean3) {
                throw new IllegalStateException("Server sent an impossible command tree");
            }
        }
    }
    
    private static Object2IntMap<CommandNode<SharedSuggestionProvider>> enumerateNodes(final RootCommandNode<SharedSuggestionProvider> rootCommandNode) {
        final Object2IntMap<CommandNode<SharedSuggestionProvider>> object2IntMap2 = new Object2IntOpenHashMap<CommandNode<SharedSuggestionProvider>>();
        final Queue<CommandNode<SharedSuggestionProvider>> queue3 = Queues.newArrayDeque();
        queue3.add(rootCommandNode);
        CommandNode<SharedSuggestionProvider> commandNode4;
        while ((commandNode4 = (CommandNode<SharedSuggestionProvider>)queue3.poll()) != null) {
            if (object2IntMap2.containsKey(commandNode4)) {
                continue;
            }
            final int integer5 = object2IntMap2.size();
            object2IntMap2.put(commandNode4, integer5);
            queue3.addAll((Collection)commandNode4.getChildren());
            if (commandNode4.getRedirect() == null) {
                continue;
            }
            queue3.add(commandNode4.getRedirect());
        }
        return object2IntMap2;
    }
    
    private static CommandNode<SharedSuggestionProvider>[] getNodesInIdOrder(final Object2IntMap<CommandNode<SharedSuggestionProvider>> object2IntMap) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface it/unimi/dsi/fastutil/objects/Object2IntMap.size:()I
        //     6: anewarray       Lcom/mojang/brigadier/tree/CommandNode;
        //     9: checkcast       [Lcom/mojang/brigadier/tree/CommandNode;
        //    12: astore_1        /* arr2 */
        //    13: aload_0         /* object2IntMap */
        //    14: invokestatic    it/unimi/dsi/fastutil/objects/Object2IntMaps.fastIterable:(Lit/unimi/dsi/fastutil/objects/Object2IntMap;)Lit/unimi/dsi/fastutil/objects/ObjectIterable;
        //    17: invokeinterface it/unimi/dsi/fastutil/objects/ObjectIterable.iterator:()Lit/unimi/dsi/fastutil/objects/ObjectIterator;
        //    22: astore_2       
        //    23: aload_2        
        //    24: invokeinterface java/util/Iterator.hasNext:()Z
        //    29: ifeq            62
        //    32: aload_2        
        //    33: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    38: checkcast       Lit/unimi/dsi/fastutil/objects/Object2IntMap$Entry;
        //    41: astore_3       
        //    42: aload_1         /* arr2 */
        //    43: aload_3        
        //    44: invokeinterface it/unimi/dsi/fastutil/objects/Object2IntMap$Entry.getIntValue:()I
        //    49: aload_3        
        //    50: invokeinterface it/unimi/dsi/fastutil/objects/Object2IntMap$Entry.getKey:()Ljava/lang/Object;
        //    55: checkcast       Lcom/mojang/brigadier/tree/CommandNode;
        //    58: aastore        
        //    59: goto            23
        //    62: aload_1         /* arr2 */
        //    63: areturn        
        //    Signature:
        //  (Lit/unimi/dsi/fastutil/objects/Object2IntMap<Lcom/mojang/brigadier/tree/CommandNode<Lnet/minecraft/commands/SharedSuggestionProvider;>;>;)[Lcom/mojang/brigadier/tree/CommandNode<Lnet/minecraft/commands/SharedSuggestionProvider;>;
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  object2IntMap  
        //    StackMapTable: 00 02 FF 00 17 00 03 00 07 00 52 07 00 DD 00 00 FA 00 26
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.TypeReference.containsGenericParameters(TypeReference.java:38)
        //     at com.strobel.assembler.metadata.TypeReference.containsGenericParameters(TypeReference.java:48)
        //     at com.strobel.assembler.metadata.MethodReference.containsGenericParameters(MethodReference.java:71)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2497)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    private static Entry readNode(final FriendlyByteBuf nf) {
        final byte byte2 = nf.readByte();
        final int[] arr3 = nf.readVarIntArray();
        final int integer4 = ((byte2 & 0x8) != 0x0) ? nf.readVarInt() : 0;
        final ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder5 = createBuilder(nf, byte2);
        return new Entry((ArgumentBuilder)argumentBuilder5, byte2, integer4, arr3);
    }
    
    @Nullable
    private static ArgumentBuilder<SharedSuggestionProvider, ?> createBuilder(final FriendlyByteBuf nf, final byte byte2) {
        final int integer3 = byte2 & 0x3;
        if (integer3 == 2) {
            final String string4 = nf.readUtf(32767);
            final ArgumentType<?> argumentType5 = ArgumentTypes.deserialize(nf);
            if (argumentType5 == null) {
                return null;
            }
            final RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder6 = RequiredArgumentBuilder.argument(string4, argumentType5);
            if ((byte2 & 0x10) != 0x0) {
                requiredArgumentBuilder6.suggests(SuggestionProviders.getProvider(nf.readResourceLocation()));
            }
            return requiredArgumentBuilder6;
        }
        else {
            if (integer3 == 1) {
                return LiteralArgumentBuilder.literal(nf.readUtf(32767));
            }
            return null;
        }
    }
    
    private static void writeNode(final FriendlyByteBuf nf, final CommandNode<SharedSuggestionProvider> commandNode, final Map<CommandNode<SharedSuggestionProvider>, Integer> map) {
        byte byte4 = 0;
        if (commandNode.getRedirect() != null) {
            byte4 |= 0x8;
        }
        if (commandNode.getCommand() != null) {
            byte4 |= 0x4;
        }
        if (commandNode instanceof RootCommandNode) {
            byte4 |= 0x0;
        }
        else if (commandNode instanceof ArgumentCommandNode) {
            byte4 |= 0x2;
            if (((ArgumentCommandNode)commandNode).getCustomSuggestions() != null) {
                byte4 |= 0x10;
            }
        }
        else {
            if (!(commandNode instanceof LiteralCommandNode)) {
                throw new UnsupportedOperationException(new StringBuilder().append("Unknown node type ").append(commandNode).toString());
            }
            byte4 |= 0x1;
        }
        nf.writeByte(byte4);
        nf.writeVarInt(commandNode.getChildren().size());
        for (final CommandNode<SharedSuggestionProvider> commandNode2 : commandNode.getChildren()) {
            nf.writeVarInt((int)map.get(commandNode2));
        }
        if (commandNode.getRedirect() != null) {
            nf.writeVarInt((int)map.get(commandNode.getRedirect()));
        }
        if (commandNode instanceof ArgumentCommandNode) {
            final ArgumentCommandNode<SharedSuggestionProvider, ?> argumentCommandNode5 = (ArgumentCommandNode)commandNode;
            nf.writeUtf(argumentCommandNode5.getName());
            ArgumentTypes.<ArgumentType<?>>serialize(nf, argumentCommandNode5.getType());
            if (argumentCommandNode5.getCustomSuggestions() != null) {
                nf.writeResourceLocation(SuggestionProviders.getName(argumentCommandNode5.getCustomSuggestions()));
            }
        }
        else if (commandNode instanceof LiteralCommandNode) {
            nf.writeUtf(((LiteralCommandNode)commandNode).getLiteral());
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleCommands(this);
    }
    
    static class Entry {
        @Nullable
        private final ArgumentBuilder<SharedSuggestionProvider, ?> builder;
        private final byte flags;
        private final int redirect;
        private final int[] children;
        @Nullable
        private CommandNode<SharedSuggestionProvider> node;
        
        private Entry(@Nullable final ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder, final byte byte2, final int integer, final int[] arr) {
            this.builder = argumentBuilder;
            this.flags = byte2;
            this.redirect = integer;
            this.children = arr;
        }
        
        public boolean build(final Entry[] arr) {
            if (this.node == null) {
                if (this.builder == null) {
                    this.node = new RootCommandNode<SharedSuggestionProvider>();
                }
                else {
                    if ((this.flags & 0x8) != 0x0) {
                        if (arr[this.redirect].node == null) {
                            return false;
                        }
                        this.builder.redirect(arr[this.redirect].node);
                    }
                    if ((this.flags & 0x4) != 0x0) {
                        this.builder.executes(commandContext -> 0);
                    }
                    this.node = this.builder.build();
                }
            }
            for (final int integer6 : this.children) {
                if (arr[integer6].node == null) {
                    return false;
                }
            }
            for (final int integer6 : this.children) {
                final CommandNode<SharedSuggestionProvider> commandNode7 = arr[integer6].node;
                if (!(commandNode7 instanceof RootCommandNode)) {
                    this.node.addChild(commandNode7);
                }
            }
            return true;
        }
    }
}
