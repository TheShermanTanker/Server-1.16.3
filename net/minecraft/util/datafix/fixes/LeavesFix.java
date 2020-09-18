package net.minecraft.util.datafix.fixes;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.google.common.collect.ImmutableList;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.util.datafix.PackedBitStorage;
import com.mojang.datafixers.util.Pair;
import com.google.common.collect.ImmutableSet;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import com.mojang.datafixers.DataFix;

public class LeavesFix extends DataFix {
    private static final int[][] DIRECTIONS;
    private static final Object2IntMap<String> LEAVES;
    private static final Set<String> LOGS;
    
    public LeavesFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final OpticFinder<?> opticFinder3 = type2.findField("Level");
        final OpticFinder<?> opticFinder4 = opticFinder3.type().findField("Sections");
        final Type<?> type3 = opticFinder4.type();
        if (!(type3 instanceof List.ListType)) {
            throw new IllegalStateException("Expecting sections to be a list.");
        }
        final Type<?> type4 = ((List.ListType)type3).getElement();
        final OpticFinder<?> opticFinder5 = DSL.typeFinder(type4);
        return this.fixTypeEverywhereTyped("Leaves fix", type2, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder3, typed -> {
            final int[] arr5 = { 0 };
            Typed<?> typed2 = typed.updateTyped(opticFinder4, typed -> {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: dup            
                //     4: aload_3         /* typed */
                //     5: aload_1         /* opticFinder */
                //     6: invokevirtual   com/mojang/datafixers/Typed.getAllTyped:(Lcom/mojang/datafixers/OpticFinder;)Ljava/util/List;
                //     9: invokeinterface java/util/List.stream:()Ljava/util/stream/Stream;
                //    14: aload_0         /* this */
                //    15: invokedynamic   BootstrapMethod #4, apply:(Lnet/minecraft/util/datafix/fixes/LeavesFix;)Ljava/util/function/Function;
                //    20: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
                //    25: invokedynamic   BootstrapMethod #5, apply:()Ljava/util/function/Function;
                //    30: invokedynamic   BootstrapMethod #6, apply:()Ljava/util/function/Function;
                //    35: invokestatic    java/util/stream/Collectors.toMap:(Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/util/stream/Collector;
                //    38: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
                //    43: checkcast       Ljava/util/Map;
                //    46: invokespecial   it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.<init>:(Ljava/util/Map;)V
                //    49: astore          int2ObjectMap5
                //    51: aload           int2ObjectMap5
                //    53: invokeinterface it/unimi/dsi/fastutil/ints/Int2ObjectMap.values:()Lit/unimi/dsi/fastutil/objects/ObjectCollection;
                //    58: invokeinterface it/unimi/dsi/fastutil/objects/ObjectCollection.stream:()Ljava/util/stream/Stream;
                //    63: invokedynamic   BootstrapMethod #7, test:()Ljava/util/function/Predicate;
                //    68: invokeinterface java/util/stream/Stream.allMatch:(Ljava/util/function/Predicate;)Z
                //    73: ifeq            78
                //    76: aload_3         /* typed */
                //    77: areturn        
                //    78: invokestatic    com/google/common/collect/Lists.newArrayList:()Ljava/util/ArrayList;
                //    81: astore          list6
                //    83: iconst_0       
                //    84: istore          integer7
                //    86: iload           integer7
                //    88: bipush          7
                //    90: if_icmpge       114
                //    93: aload           list6
                //    95: new             Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
                //    98: dup            
                //    99: invokespecial   it/unimi/dsi/fastutil/ints/IntOpenHashSet.<init>:()V
                //   102: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
                //   107: pop            
                //   108: iinc            integer7, 1
                //   111: goto            86
                //   114: aload           int2ObjectMap5
                //   116: invokeinterface it/unimi/dsi/fastutil/ints/Int2ObjectMap.values:()Lit/unimi/dsi/fastutil/objects/ObjectCollection;
                //   121: invokeinterface it/unimi/dsi/fastutil/objects/ObjectCollection.iterator:()Lit/unimi/dsi/fastutil/objects/ObjectIterator;
                //   126: astore          6
                //   128: aload           6
                //   130: invokeinterface java/util/Iterator.hasNext:()Z
                //   135: ifeq            310
                //   138: aload           6
                //   140: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
                //   145: checkcast       Lnet/minecraft/util/datafix/fixes/LeavesFix$LeavesSection;
                //   148: astore          a8
                //   150: aload           a8
                //   152: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.isSkippable:()Z
                //   155: ifeq            161
                //   158: goto            128
                //   161: iconst_0       
                //   162: istore          integer9
                //   164: iload           integer9
                //   166: sipush          4096
                //   169: if_icmpge       307
                //   172: aload           a8
                //   174: iload           integer9
                //   176: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.getBlock:(I)I
                //   179: istore          integer10
                //   181: aload           a8
                //   183: iload           integer10
                //   185: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.isLog:(I)Z
                //   188: ifeq            222
                //   191: aload           list6
                //   193: iconst_0       
                //   194: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
                //   199: checkcast       Lit/unimi/dsi/fastutil/ints/IntSet;
                //   202: aload           a8
                //   204: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.getIndex:()I
                //   207: bipush          12
                //   209: ishl           
                //   210: iload           integer9
                //   212: ior            
                //   213: invokeinterface it/unimi/dsi/fastutil/ints/IntSet.add:(I)Z
                //   218: pop            
                //   219: goto            301
                //   222: aload           a8
                //   224: iload           integer10
                //   226: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.isLeaf:(I)Z
                //   229: ifeq            301
                //   232: aload_0         /* this */
                //   233: iload           integer9
                //   235: invokespecial   net/minecraft/util/datafix/fixes/LeavesFix.getX:(I)I
                //   238: istore          integer11
                //   240: aload_0         /* this */
                //   241: iload           integer9
                //   243: invokespecial   net/minecraft/util/datafix/fixes/LeavesFix.getZ:(I)I
                //   246: istore          integer12
                //   248: aload_2         /* arr */
                //   249: iconst_0       
                //   250: dup2           
                //   251: iaload         
                //   252: iload           integer11
                //   254: ifne            261
                //   257: iconst_1       
                //   258: goto            262
                //   261: iconst_0       
                //   262: iload           integer11
                //   264: bipush          15
                //   266: if_icmpne       273
                //   269: iconst_1       
                //   270: goto            274
                //   273: iconst_0       
                //   274: iload           integer12
                //   276: ifne            283
                //   279: iconst_1       
                //   280: goto            284
                //   283: iconst_0       
                //   284: iload           integer12
                //   286: bipush          15
                //   288: if_icmpne       295
                //   291: iconst_1       
                //   292: goto            296
                //   295: iconst_0       
                //   296: invokestatic    net/minecraft/util/datafix/fixes/LeavesFix.getSideMask:(ZZZZ)I
                //   299: ior            
                //   300: iastore        
                //   301: iinc            integer9, 1
                //   304: goto            164
                //   307: goto            128
                //   310: iconst_1       
                //   311: istore          integer7
                //   313: iload           integer7
                //   315: bipush          7
                //   317: if_icmpge       623
                //   320: aload           list6
                //   322: iload           integer7
                //   324: iconst_1       
                //   325: isub           
                //   326: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
                //   331: checkcast       Lit/unimi/dsi/fastutil/ints/IntSet;
                //   334: astore          intSet8
                //   336: aload           list6
                //   338: iload           integer7
                //   340: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
                //   345: checkcast       Lit/unimi/dsi/fastutil/ints/IntSet;
                //   348: astore          intSet9
                //   350: aload           intSet8
                //   352: invokeinterface it/unimi/dsi/fastutil/ints/IntSet.iterator:()Lit/unimi/dsi/fastutil/ints/IntIterator;
                //   357: astore          intIterator10
                //   359: aload           intIterator10
                //   361: invokeinterface it/unimi/dsi/fastutil/ints/IntIterator.hasNext:()Z
                //   366: ifeq            617
                //   369: aload           intIterator10
                //   371: invokeinterface it/unimi/dsi/fastutil/ints/IntIterator.nextInt:()I
                //   376: istore          integer11
                //   378: aload_0         /* this */
                //   379: iload           integer11
                //   381: invokespecial   net/minecraft/util/datafix/fixes/LeavesFix.getX:(I)I
                //   384: istore          integer12
                //   386: aload_0         /* this */
                //   387: iload           integer11
                //   389: invokespecial   net/minecraft/util/datafix/fixes/LeavesFix.getY:(I)I
                //   392: istore          integer13
                //   394: aload_0         /* this */
                //   395: iload           integer11
                //   397: invokespecial   net/minecraft/util/datafix/fixes/LeavesFix.getZ:(I)I
                //   400: istore          integer14
                //   402: getstatic       net/minecraft/util/datafix/fixes/LeavesFix.DIRECTIONS:[[I
                //   405: astore          14
                //   407: aload           14
                //   409: arraylength    
                //   410: istore          15
                //   412: iconst_0       
                //   413: istore          16
                //   415: iload           16
                //   417: iload           15
                //   419: if_icmpge       614
                //   422: aload           14
                //   424: iload           16
                //   426: aaload         
                //   427: astore          arr18
                //   429: iload           integer12
                //   431: aload           arr18
                //   433: iconst_0       
                //   434: iaload         
                //   435: iadd           
                //   436: istore          integer19
                //   438: iload           integer13
                //   440: aload           arr18
                //   442: iconst_1       
                //   443: iaload         
                //   444: iadd           
                //   445: istore          integer20
                //   447: iload           integer14
                //   449: aload           arr18
                //   451: iconst_2       
                //   452: iaload         
                //   453: iadd           
                //   454: istore          integer21
                //   456: iload           integer19
                //   458: iflt            608
                //   461: iload           integer19
                //   463: bipush          15
                //   465: if_icmpgt       608
                //   468: iload           integer21
                //   470: iflt            608
                //   473: iload           integer21
                //   475: bipush          15
                //   477: if_icmpgt       608
                //   480: iload           integer20
                //   482: iflt            608
                //   485: iload           integer20
                //   487: sipush          255
                //   490: if_icmple       496
                //   493: goto            608
                //   496: aload           int2ObjectMap5
                //   498: iload           integer20
                //   500: iconst_4       
                //   501: ishr           
                //   502: invokeinterface it/unimi/dsi/fastutil/ints/Int2ObjectMap.get:(I)Ljava/lang/Object;
                //   507: checkcast       Lnet/minecraft/util/datafix/fixes/LeavesFix$LeavesSection;
                //   510: astore          a22
                //   512: aload           a22
                //   514: ifnull          608
                //   517: aload           a22
                //   519: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.isSkippable:()Z
                //   522: ifeq            528
                //   525: goto            608
                //   528: iload           integer19
                //   530: iload           integer20
                //   532: bipush          15
                //   534: iand           
                //   535: iload           integer21
                //   537: invokestatic    net/minecraft/util/datafix/fixes/LeavesFix.getIndex:(III)I
                //   540: istore          integer23
                //   542: aload           a22
                //   544: iload           integer23
                //   546: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.getBlock:(I)I
                //   549: istore          integer24
                //   551: aload           a22
                //   553: iload           integer24
                //   555: invokevirtual   net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.isLeaf:(I)Z
                //   558: ifne            564
                //   561: goto            608
                //   564: aload           a22
                //   566: iload           integer24
                //   568: invokestatic    net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.access$200:(Lnet/minecraft/util/datafix/fixes/LeavesFix$LeavesSection;I)I
                //   571: istore          integer25
                //   573: iload           integer25
                //   575: iload           integer7
                //   577: if_icmple       608
                //   580: aload           a22
                //   582: iload           integer23
                //   584: iload           integer24
                //   586: iload           integer7
                //   588: invokestatic    net/minecraft/util/datafix/fixes/LeavesFix$LeavesSection.access$300:(Lnet/minecraft/util/datafix/fixes/LeavesFix$LeavesSection;III)V
                //   591: aload           intSet9
                //   593: iload           integer19
                //   595: iload           integer20
                //   597: iload           integer21
                //   599: invokestatic    net/minecraft/util/datafix/fixes/LeavesFix.getIndex:(III)I
                //   602: invokeinterface it/unimi/dsi/fastutil/ints/IntSet.add:(I)Z
                //   607: pop            
                //   608: iinc            16, 1
                //   611: goto            415
                //   614: goto            359
                //   617: iinc            integer7, 1
                //   620: goto            313
                //   623: aload_3         /* typed */
                //   624: aload_1         /* opticFinder */
                //   625: aload           int2ObjectMap5
                //   627: invokedynamic   BootstrapMethod #8, apply:(Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)Ljava/util/function/Function;
                //   632: invokevirtual   com/mojang/datafixers/Typed.updateTyped:(Lcom/mojang/datafixers/OpticFinder;Ljava/util/function/Function;)Lcom/mojang/datafixers/Typed;
                //   635: areturn        
                //    MethodParameters:
                //  Name         Flags  
                //  -----------  -----
                //  opticFinder  
                //  arr          
                //  typed        
                //    StackMapTable: 00 1C FC 00 4E 07 00 EE FD 00 07 07 01 48 01 FA 00 1B FC 00 0D 07 01 56 FC 00 20 07 00 07 FC 00 02 01 FC 00 39 01 FF 00 26 00 0C 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 01 01 00 03 07 01 77 01 01 FF 00 00 00 0C 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 01 01 00 04 07 01 77 01 01 01 FF 00 0A 00 0C 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 00 01 00 04 07 01 77 01 01 01 FF 00 00 00 0C 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 00 01 00 05 07 01 77 01 01 01 01 FF 00 08 00 0C 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 00 01 00 05 07 01 77 01 01 01 01 FF 00 00 00 0C 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 00 01 00 06 07 01 77 01 01 01 01 01 FF 00 0A 00 09 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 06 07 01 77 01 01 01 01 01 FF 00 00 00 09 07 00 02 07 00 48 07 01 77 07 00 9E 07 00 EE 07 01 48 07 01 56 07 00 07 01 00 07 07 01 77 01 01 01 01 01 01 04 F9 00 05 FF 00 02 00 06 07 00 02 07 00 48 00 07 00 9E 07 00 EE 07 01 48 00 00 FC 00 02 01 FE 00 2D 00 07 01 6C 07 01 7E FF 00 37 00 11 07 00 02 07 00 48 00 07 00 9E 07 00 EE 07 01 48 01 00 07 01 6C 07 01 7E 00 01 01 01 07 01 87 01 01 00 00 FF 00 50 00 15 07 00 02 07 00 48 00 07 00 9E 07 00 EE 07 01 48 01 00 07 01 6C 07 01 7E 00 01 01 01 07 01 87 01 01 00 01 01 01 00 00 FC 00 1F 07 00 07 FD 00 23 01 01 FF 00 2B 00 11 07 00 02 07 00 48 00 07 00 9E 07 00 EE 07 01 48 01 00 07 01 6C 07 01 7E 00 01 01 01 07 01 87 01 01 00 00 FF 00 05 00 0A 07 00 02 07 00 48 00 07 00 9E 07 00 EE 07 01 48 01 00 07 01 6C 07 01 7E 00 00 F8 00 02 FF 00 05 00 05 00 07 00 48 00 07 00 9E 07 00 EE 00 00
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:764)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1011)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
            });
            if (arr5[0] != 0) {
                typed2 = typed2.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)(dynamic -> {
                    final Dynamic<?> dynamic2 = DataFixUtils.<Dynamic<?>>orElse(dynamic.get("UpgradeData").result(), dynamic.emptyMap());
                    return dynamic.set("UpgradeData", (Dynamic)dynamic2.set("Sides", dynamic.createByte((byte)(dynamic2.get("Sides").asByte((byte)0) | arr5[0]))));
                }));
            }
            return typed2;
        })));
    }
    
    public static int getIndex(final int integer1, final int integer2, final int integer3) {
        return integer2 << 8 | integer3 << 4 | integer1;
    }
    
    private int getX(final int integer) {
        return integer & 0xF;
    }
    
    private int getY(final int integer) {
        return integer >> 8 & 0xFF;
    }
    
    private int getZ(final int integer) {
        return integer >> 4 & 0xF;
    }
    
    public static int getSideMask(final boolean boolean1, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        int integer5 = 0;
        if (boolean3) {
            if (boolean2) {
                integer5 |= 0x2;
            }
            else if (boolean1) {
                integer5 |= 0x80;
            }
            else {
                integer5 |= 0x1;
            }
        }
        else if (boolean4) {
            if (boolean1) {
                integer5 |= 0x20;
            }
            else if (boolean2) {
                integer5 |= 0x8;
            }
            else {
                integer5 |= 0x10;
            }
        }
        else if (boolean2) {
            integer5 |= 0x4;
        }
        else if (boolean1) {
            integer5 |= 0x40;
        }
        return integer5;
    }
    
    static {
        DIRECTIONS = new int[][] { { -1, 0, 0 }, { 1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 } };
        LEAVES = DataFixUtils.<Object2IntOpenHashMap<String>>make(new Object2IntOpenHashMap<String>(), (java.util.function.Consumer<Object2IntOpenHashMap<String>>)(object2IntOpenHashMap -> {
            object2IntOpenHashMap.put("minecraft:acacia_leaves", 0);
            object2IntOpenHashMap.put("minecraft:birch_leaves", 1);
            object2IntOpenHashMap.put("minecraft:dark_oak_leaves", 2);
            object2IntOpenHashMap.put("minecraft:jungle_leaves", 3);
            object2IntOpenHashMap.put("minecraft:oak_leaves", 4);
            object2IntOpenHashMap.put("minecraft:spruce_leaves", 5);
        }));
        LOGS = (Set)ImmutableSet.<String>of("minecraft:acacia_bark", "minecraft:birch_bark", "minecraft:dark_oak_bark", "minecraft:jungle_bark", "minecraft:oak_bark", "minecraft:spruce_bark", new String[] { "minecraft:acacia_log", "minecraft:birch_log", "minecraft:dark_oak_log", "minecraft:jungle_log", "minecraft:oak_log", "minecraft:spruce_log", "minecraft:stripped_acacia_log", "minecraft:stripped_birch_log", "minecraft:stripped_dark_oak_log", "minecraft:stripped_jungle_log", "minecraft:stripped_oak_log", "minecraft:stripped_spruce_log" });
    }
    
    public abstract static class Section {
        private final Type<Pair<String, Dynamic<?>>> blockStateType;
        protected final OpticFinder<java.util.List<Pair<String, Dynamic<?>>>> paletteFinder;
        protected final java.util.List<Dynamic<?>> palette;
        protected final int index;
        @Nullable
        protected PackedBitStorage storage;
        
        public Section(final Typed<?> typed, final Schema schema) {
            this.blockStateType = DSL.<Dynamic<?>>named(References.BLOCK_STATE.typeName(), DSL.remainderType());
            this.paletteFinder = DSL.<java.util.List<Pair<String, Dynamic<?>>>>fieldFinder("Palette", DSL.list(this.blockStateType));
            if (!Objects.equals(schema.getType(References.BLOCK_STATE), this.blockStateType)) {
                throw new IllegalStateException("Block state type is not what was expected.");
            }
            final Optional<java.util.List<Pair<String, Dynamic<?>>>> optional4 = typed.<java.util.List<Pair<String, Dynamic<?>>>>getOptional(this.paletteFinder);
            this.palette = (java.util.List<Dynamic<?>>)optional4.map(list -> (java.util.List)list.stream().map(Pair::getSecond).collect(Collectors.toList())).orElse(ImmutableList.of());
            final Dynamic<?> dynamic5 = typed.<Dynamic<?>>get(DSL.remainderFinder());
            this.index = dynamic5.get("Y").asInt(0);
            this.readStorage(dynamic5);
        }
        
        protected void readStorage(final Dynamic<?> dynamic) {
            if (this.skippable()) {
                this.storage = null;
            }
            else {
                final long[] arr3 = dynamic.get("BlockStates").asLongStream().toArray();
                final int integer4 = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
                this.storage = new PackedBitStorage(integer4, 4096, arr3);
            }
        }
        
        public Typed<?> write(final Typed<?> typed) {
            if (this.isSkippable()) {
                return typed;
            }
            return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.set("BlockStates", dynamic.createLongList(Arrays.stream(this.storage.getRaw()))))).set((OpticFinder<Object>)this.paletteFinder, this.palette.stream().map(dynamic -> Pair.<String, Dynamic>of(References.BLOCK_STATE.typeName(), dynamic)).collect(Collectors.toList()));
        }
        
        public boolean isSkippable() {
            return this.storage == null;
        }
        
        public int getBlock(final int integer) {
            return this.storage.get(integer);
        }
        
        protected int getStateId(final String string, final boolean boolean2, final int integer) {
            return LeavesFix.LEAVES.get(string) << 5 | (boolean2 ? 16 : 0) | integer;
        }
        
        int getIndex() {
            return this.index;
        }
        
        protected abstract boolean skippable();
    }
    
    public static final class LeavesSection extends Section {
        @Nullable
        private IntSet leaveIds;
        @Nullable
        private IntSet logIds;
        @Nullable
        private Int2IntMap stateToIdMap;
        
        public LeavesSection(final Typed<?> typed, final Schema schema) {
            super(typed, schema);
        }
        
        @Override
        protected boolean skippable() {
            this.leaveIds = new IntOpenHashSet();
            this.logIds = new IntOpenHashSet();
            this.stateToIdMap = new Int2IntOpenHashMap();
            for (int integer2 = 0; integer2 < this.palette.size(); ++integer2) {
                final Dynamic<?> dynamic3 = this.palette.get(integer2);
                final String string4 = dynamic3.get("Name").asString("");
                if (LeavesFix.LEAVES.containsKey(string4)) {
                    final boolean boolean5 = Objects.equals(dynamic3.get("Properties").get("decayable").asString(""), "false");
                    this.leaveIds.add(integer2);
                    this.stateToIdMap.put(this.getStateId(string4, boolean5, 7), integer2);
                    this.palette.set(integer2, this.makeLeafTag(dynamic3, string4, boolean5, 7));
                }
                if (LeavesFix.LOGS.contains(string4)) {
                    this.logIds.add(integer2);
                }
            }
            return this.leaveIds.isEmpty() && this.logIds.isEmpty();
        }
        
        private Dynamic<?> makeLeafTag(final Dynamic<?> dynamic, final String string, final boolean boolean3, final int integer) {
            Dynamic<?> dynamic2 = dynamic.emptyMap();
            dynamic2 = dynamic2.set("persistent", dynamic2.createString(boolean3 ? "true" : "false"));
            dynamic2 = dynamic2.set("distance", dynamic2.createString(Integer.toString(integer)));
            Dynamic<?> dynamic3 = dynamic.emptyMap();
            dynamic3 = dynamic3.set("Properties", dynamic2);
            dynamic3 = dynamic3.set("Name", dynamic3.createString(string));
            return dynamic3;
        }
        
        public boolean isLog(final int integer) {
            return this.logIds.contains(integer);
        }
        
        public boolean isLeaf(final int integer) {
            return this.leaveIds.contains(integer);
        }
        
        private int getDistance(final int integer) {
            if (this.isLog(integer)) {
                return 0;
            }
            return Integer.parseInt(((Dynamic)this.palette.get(integer)).get("Properties").get("distance").asString(""));
        }
        
        private void setDistance(final int integer1, final int integer2, final int integer3) {
            final Dynamic<?> dynamic5 = this.palette.get(integer2);
            final String string6 = dynamic5.get("Name").asString("");
            final boolean boolean7 = Objects.equals(dynamic5.get("Properties").get("persistent").asString(""), "true");
            final int integer4 = this.getStateId(string6, boolean7, integer3);
            if (!this.stateToIdMap.containsKey(integer4)) {
                final int integer5 = this.palette.size();
                this.leaveIds.add(integer5);
                this.stateToIdMap.put(integer4, integer5);
                this.palette.add(this.makeLeafTag(dynamic5, string6, boolean7, integer3));
            }
            final int integer5 = this.stateToIdMap.get(integer4);
            if (1 << this.storage.getBits() <= integer5) {
                final PackedBitStorage afz10 = new PackedBitStorage(this.storage.getBits() + 1, 4096);
                for (int integer6 = 0; integer6 < 4096; ++integer6) {
                    afz10.set(integer6, this.storage.get(integer6));
                }
                this.storage = afz10;
            }
            this.storage.set(integer1, integer5);
        }
    }
}
