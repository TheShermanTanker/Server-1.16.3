package com.mojang.authlib.yggdrasil;

import org.apache.logging.log4j.LogManager;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.Agent;
import com.mojang.authlib.Environment;
import org.apache.logging.log4j.Logger;
import com.mojang.authlib.GameProfileRepository;

public class YggdrasilGameProfileRepository implements GameProfileRepository {
    private static final Logger LOGGER;
    private final String searchPageUrl;
    private static final int ENTRIES_PER_PAGE = 2;
    private static final int MAX_FAIL_COUNT = 3;
    private static final int DELAY_BETWEEN_PAGES = 100;
    private static final int DELAY_BETWEEN_FAILURES = 750;
    private final YggdrasilAuthenticationService authenticationService;
    
    public YggdrasilGameProfileRepository(final YggdrasilAuthenticationService authenticationService, final Environment environment) {
        this.authenticationService = authenticationService;
        this.searchPageUrl = environment.getAccountsHost() + "/profiles/";
    }
    
    public void findProfilesByNames(final String[] names, final Agent agent, final ProfileLookupCallback callback) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore          criteria
        //     5: aload_1         /* names */
        //     6: astore          5
        //     8: aload           5
        //    10: arraylength    
        //    11: istore          6
        //    13: iconst_0       
        //    14: istore          7
        //    16: iload           7
        //    18: iload           6
        //    20: if_icmpge       57
        //    23: aload           5
        //    25: iload           7
        //    27: aaload         
        //    28: astore          name
        //    30: aload           name
        //    32: invokestatic    com/google/common/base/Strings.isNullOrEmpty:(Ljava/lang/String;)Z
        //    35: ifne            51
        //    38: aload           criteria
        //    40: aload           name
        //    42: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //    45: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //    50: pop            
        //    51: iinc            7, 1
        //    54: goto            16
        //    57: iconst_0       
        //    58: istore          page
        //    60: aload           criteria
        //    62: iconst_2       
        //    63: invokestatic    com/google/common/collect/Iterables.partition:(Ljava/lang/Iterable;I)Ljava/lang/Iterable;
        //    66: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    71: astore          6
        //    73: aload           6
        //    75: invokeinterface java/util/Iterator.hasNext:()Z
        //    80: ifeq            467
        //    83: aload           6
        //    85: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    90: checkcast       Ljava/util/List;
        //    93: astore          request
        //    95: iconst_0       
        //    96: istore          failCount
        //    98: iconst_0       
        //    99: istore          failed
        //   101: aload_0         /* this */
        //   102: getfield        com/mojang/authlib/yggdrasil/YggdrasilGameProfileRepository.authenticationService:Lcom/mojang/authlib/yggdrasil/YggdrasilAuthenticationService;
        //   105: new             Ljava/lang/StringBuilder;
        //   108: dup            
        //   109: invokespecial   java/lang/StringBuilder.<init>:()V
        //   112: aload_0         /* this */
        //   113: getfield        com/mojang/authlib/yggdrasil/YggdrasilGameProfileRepository.searchPageUrl:Ljava/lang/String;
        //   116: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   119: aload_2         /* agent */
        //   120: invokevirtual   com/mojang/authlib/Agent.getName:()Ljava/lang/String;
        //   123: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   126: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   129: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   132: invokestatic    com/mojang/authlib/HttpAuthenticationService.constantURL:(Ljava/lang/String;)Ljava/net/URL;
        //   135: aload           request
        //   137: ldc             Lcom/mojang/authlib/yggdrasil/response/ProfileSearchResultsResponse;.class
        //   139: invokevirtual   com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService.makeRequest:(Ljava/net/URL;Ljava/lang/Object;Ljava/lang/Class;)Lcom/mojang/authlib/yggdrasil/response/Response;
        //   142: checkcast       Lcom/mojang/authlib/yggdrasil/response/ProfileSearchResultsResponse;
        //   145: astore          response
        //   147: iconst_0       
        //   148: istore          failCount
        //   150: getstatic       com/mojang/authlib/yggdrasil/YggdrasilGameProfileRepository.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   153: ldc             "Page {} returned {} results, parsing"
        //   155: iconst_2       
        //   156: anewarray       Ljava/lang/Object;
        //   159: dup            
        //   160: iconst_0       
        //   161: iconst_0       
        //   162: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   165: aastore        
        //   166: dup            
        //   167: iconst_1       
        //   168: aload           response
        //   170: invokevirtual   com/mojang/authlib/yggdrasil/response/ProfileSearchResultsResponse.getProfiles:()[Lcom/mojang/authlib/GameProfile;
        //   173: arraylength    
        //   174: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   177: aastore        
        //   178: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;[Ljava/lang/Object;)V
        //   183: aload           request
        //   185: invokestatic    com/google/common/collect/Sets.newHashSet:(Ljava/lang/Iterable;)Ljava/util/HashSet;
        //   188: astore          missing
        //   190: aload           response
        //   192: invokevirtual   com/mojang/authlib/yggdrasil/response/ProfileSearchResultsResponse.getProfiles:()[Lcom/mojang/authlib/GameProfile;
        //   195: astore          12
        //   197: aload           12
        //   199: arraylength    
        //   200: istore          13
        //   202: iconst_0       
        //   203: istore          14
        //   205: iload           14
        //   207: iload           13
        //   209: if_icmpge       268
        //   212: aload           12
        //   214: iload           14
        //   216: aaload         
        //   217: astore          profile
        //   219: getstatic       com/mojang/authlib/yggdrasil/YggdrasilGameProfileRepository.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   222: ldc             "Successfully looked up profile {}"
        //   224: iconst_1       
        //   225: anewarray       Ljava/lang/Object;
        //   228: dup            
        //   229: iconst_0       
        //   230: aload           profile
        //   232: aastore        
        //   233: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;[Ljava/lang/Object;)V
        //   238: aload           missing
        //   240: aload           profile
        //   242: invokevirtual   com/mojang/authlib/GameProfile.getName:()Ljava/lang/String;
        //   245: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   248: invokeinterface java/util/Set.remove:(Ljava/lang/Object;)Z
        //   253: pop            
        //   254: aload_3         /* callback */
        //   255: aload           profile
        //   257: invokeinterface com/mojang/authlib/ProfileLookupCallback.onProfileLookupSucceeded:(Lcom/mojang/authlib/GameProfile;)V
        //   262: iinc            14, 1
        //   265: goto            205
        //   268: aload           missing
        //   270: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   275: astore          12
        //   277: aload           12
        //   279: invokeinterface java/util/Iterator.hasNext:()Z
        //   284: ifeq            346
        //   287: aload           12
        //   289: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   294: checkcast       Ljava/lang/String;
        //   297: astore          name
        //   299: getstatic       com/mojang/authlib/yggdrasil/YggdrasilGameProfileRepository.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   302: ldc             "Couldn't find profile {}"
        //   304: iconst_1       
        //   305: anewarray       Ljava/lang/Object;
        //   308: dup            
        //   309: iconst_0       
        //   310: aload           name
        //   312: aastore        
        //   313: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;[Ljava/lang/Object;)V
        //   318: aload_3         /* callback */
        //   319: new             Lcom/mojang/authlib/GameProfile;
        //   322: dup            
        //   323: aconst_null    
        //   324: aload           name
        //   326: invokespecial   com/mojang/authlib/GameProfile.<init>:(Ljava/util/UUID;Ljava/lang/String;)V
        //   329: new             Lcom/mojang/authlib/yggdrasil/ProfileNotFoundException;
        //   332: dup            
        //   333: ldc             "Server did not find the requested profile"
        //   335: invokespecial   com/mojang/authlib/yggdrasil/ProfileNotFoundException.<init>:(Ljava/lang/String;)V
        //   338: invokeinterface com/mojang/authlib/ProfileLookupCallback.onProfileLookupFailed:(Lcom/mojang/authlib/GameProfile;Ljava/lang/Exception;)V
        //   343: goto            277
        //   346: ldc2_w          100
        //   349: invokestatic    java/lang/Thread.sleep:(J)V
        //   352: goto            357
        //   355: astore          12
        //   357: goto            459
        //   360: astore          e
        //   362: iinc            failCount, 1
        //   365: iload           failCount
        //   367: iconst_3       
        //   368: if_icmpne       445
        //   371: aload           request
        //   373: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   378: astore          11
        //   380: aload           11
        //   382: invokeinterface java/util/Iterator.hasNext:()Z
        //   387: ifeq            442
        //   390: aload           11
        //   392: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   397: checkcast       Ljava/lang/String;
        //   400: astore          name
        //   402: getstatic       com/mojang/authlib/yggdrasil/YggdrasilGameProfileRepository.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   405: ldc             "Couldn't find profile {} because of a server error"
        //   407: iconst_1       
        //   408: anewarray       Ljava/lang/Object;
        //   411: dup            
        //   412: iconst_0       
        //   413: aload           name
        //   415: aastore        
        //   416: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;[Ljava/lang/Object;)V
        //   421: aload_3         /* callback */
        //   422: new             Lcom/mojang/authlib/GameProfile;
        //   425: dup            
        //   426: aconst_null    
        //   427: aload           name
        //   429: invokespecial   com/mojang/authlib/GameProfile.<init>:(Ljava/util/UUID;Ljava/lang/String;)V
        //   432: aload           e
        //   434: invokeinterface com/mojang/authlib/ProfileLookupCallback.onProfileLookupFailed:(Lcom/mojang/authlib/GameProfile;Ljava/lang/Exception;)V
        //   439: goto            380
        //   442: goto            459
        //   445: ldc2_w          750
        //   448: invokestatic    java/lang/Thread.sleep:(J)V
        //   451: goto            456
        //   454: astore          11
        //   456: iconst_1       
        //   457: istore          failed
        //   459: iload           failed
        //   461: ifne            98
        //   464: goto            73
        //   467: return         
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  names     
        //  agent     
        //  callback  
        //    StackMapTable: 00 13 FF 00 10 00 08 07 00 02 07 00 46 07 00 48 07 00 4A 07 00 4C 07 00 46 01 01 00 00 22 F8 00 05 FD 00 0F 01 07 00 69 FD 00 18 07 00 73 01 FF 00 6A 00 0F 07 00 02 07 00 46 07 00 48 07 00 4A 07 00 4C 01 07 00 69 07 00 73 01 01 07 00 7E 07 00 4C 07 00 9D 01 01 00 00 F8 00 3E FC 00 08 07 00 69 FA 00 44 48 07 00 3C F9 00 01 42 07 00 3E FD 00 13 07 00 3E 07 00 69 FA 00 3D 02 48 07 00 3C 01 FA 00 02 FF 00 07 00 06 07 00 02 07 00 46 07 00 48 07 00 4A 07 00 4C 01 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                   
        //  -----  -----  -----  -----  -------------------------------------------------------
        //  346    352    355    357    Ljava/lang/InterruptedException;
        //  101    357    360    459    Lcom/mojang/authlib/exceptions/AuthenticationException;
        //  445    451    454    456    Ljava/lang/InterruptedException;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
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
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:237)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:273)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
