package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;
import java.util.Objects;
import java.io.File;

public final class GzCompressAction extends AbstractAction {
    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    
    public GzCompressAction(final File source, final File destination, final boolean deleteSource) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
    }
    
    @Override
    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource);
    }
    
    public static boolean execute(final File source, final File destination, final boolean deleteSource) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/io/File.exists:()Z
        //     4: ifeq            295
        //     7: new             Ljava/io/FileInputStream;
        //    10: dup            
        //    11: aload_0         /* source */
        //    12: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    15: astore_3        /* fis */
        //    16: aconst_null    
        //    17: astore          4
        //    19: new             Ljava/io/BufferedOutputStream;
        //    22: dup            
        //    23: new             Ljava/util/zip/GZIPOutputStream;
        //    26: dup            
        //    27: new             Ljava/io/FileOutputStream;
        //    30: dup            
        //    31: aload_1         /* destination */
        //    32: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    35: invokespecial   java/util/zip/GZIPOutputStream.<init>:(Ljava/io/OutputStream;)V
        //    38: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //    41: astore          os
        //    43: aconst_null    
        //    44: astore          6
        //    46: sipush          8102
        //    49: newarray        B
        //    51: astore          inbuf
        //    53: aload_3         /* fis */
        //    54: aload           inbuf
        //    56: invokevirtual   java/io/FileInputStream.read:([B)I
        //    59: dup            
        //    60: istore          n
        //    62: iconst_m1      
        //    63: if_icmpeq       79
        //    66: aload           os
        //    68: aload           inbuf
        //    70: iconst_0       
        //    71: iload           n
        //    73: invokevirtual   java/io/BufferedOutputStream.write:([BII)V
        //    76: goto            53
        //    79: aload           os
        //    81: ifnull          166
        //    84: aload           6
        //    86: ifnull          109
        //    89: aload           os
        //    91: invokevirtual   java/io/BufferedOutputStream.close:()V
        //    94: goto            166
        //    97: astore          x2
        //    99: aload           6
        //   101: aload           x2
        //   103: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   106: goto            166
        //   109: aload           os
        //   111: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   114: goto            166
        //   117: astore          7
        //   119: aload           7
        //   121: astore          6
        //   123: aload           7
        //   125: athrow         
        //   126: astore          9
        //   128: aload           os
        //   130: ifnull          163
        //   133: aload           6
        //   135: ifnull          158
        //   138: aload           os
        //   140: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   143: goto            163
        //   146: astore          x2
        //   148: aload           6
        //   150: aload           x2
        //   152: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   155: goto            163
        //   158: aload           os
        //   160: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   163: aload           9
        //   165: athrow         
        //   166: aload_3         /* fis */
        //   167: ifnull          247
        //   170: aload           4
        //   172: ifnull          194
        //   175: aload_3         /* fis */
        //   176: invokevirtual   java/io/FileInputStream.close:()V
        //   179: goto            247
        //   182: astore          x2
        //   184: aload           4
        //   186: aload           x2
        //   188: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   191: goto            247
        //   194: aload_3         /* fis */
        //   195: invokevirtual   java/io/FileInputStream.close:()V
        //   198: goto            247
        //   201: astore          5
        //   203: aload           5
        //   205: astore          4
        //   207: aload           5
        //   209: athrow         
        //   210: astore          11
        //   212: aload_3         /* fis */
        //   213: ifnull          244
        //   216: aload           4
        //   218: ifnull          240
        //   221: aload_3         /* fis */
        //   222: invokevirtual   java/io/FileInputStream.close:()V
        //   225: goto            244
        //   228: astore          x2
        //   230: aload           4
        //   232: aload           x2
        //   234: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   237: goto            244
        //   240: aload_3         /* fis */
        //   241: invokevirtual   java/io/FileInputStream.close:()V
        //   244: aload           11
        //   246: athrow         
        //   247: iload_2         /* deleteSource */
        //   248: ifeq            293
        //   251: aload_0         /* source */
        //   252: invokevirtual   java/io/File.delete:()Z
        //   255: ifne            293
        //   258: getstatic       org/apache/logging/log4j/core/appender/rolling/action/GzCompressAction.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   261: new             Ljava/lang/StringBuilder;
        //   264: dup            
        //   265: invokespecial   java/lang/StringBuilder.<init>:()V
        //   268: ldc             "Unable to delete "
        //   270: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   273: aload_0         /* source */
        //   274: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //   277: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   280: bipush          46
        //   282: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   285: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   288: invokeinterface org/apache/logging/log4j/Logger.warn:(Ljava/lang/String;)V
        //   293: iconst_1       
        //   294: ireturn        
        //   295: iconst_0       
        //   296: ireturn        
        //    Exceptions:
        //  throws java.io.IOException
        //    MethodParameters:
        //  Name          Flags  
        //  ------------  -----
        //  source        
        //  destination   
        //  deleteSource  
        //    StackMapTable: 00 14 FF 00 35 00 08 07 00 2D 07 00 2D 01 07 00 32 07 00 2B 07 00 37 07 00 2B 07 00 42 00 00 FA 00 19 51 07 00 2B 0B 47 07 00 2B 48 07 00 2B FF 00 13 00 0A 07 00 2D 07 00 2D 01 07 00 32 07 00 2B 07 00 37 07 00 2B 00 00 07 00 2B 00 01 07 00 2B 0B 04 FF 00 02 00 05 07 00 2D 07 00 2D 01 07 00 32 07 00 2B 00 00 4F 07 00 2B 0B 46 07 00 2B 48 07 00 2B FF 00 11 00 0C 07 00 2D 07 00 2D 01 07 00 32 07 00 2B 00 00 00 00 00 00 07 00 2B 00 01 07 00 2B 0B 03 FF 00 02 00 03 07 00 2D 07 00 2D 01 00 00 2D 01
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  89     94     97     109    Ljava/lang/Throwable;
        //  46     79     117    126    Ljava/lang/Throwable;
        //  46     79     126    166    Any
        //  138    143    146    158    Ljava/lang/Throwable;
        //  117    128    126    166    Any
        //  175    179    182    194    Ljava/lang/Throwable;
        //  19     166    201    210    Ljava/lang/Throwable;
        //  19     166    210    247    Any
        //  221    225    228    240    Ljava/lang/Throwable;
        //  201    212    210    247    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:259)
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
    
    @Override
    protected void reportException(final Exception ex) {
        GzCompressAction.LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", (Throwable)ex);
    }
    
    public String toString() {
        return GzCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
    }
    
    public File getSource() {
        return this.source;
    }
    
    public File getDestination() {
        return this.destination;
    }
    
    public boolean isDeleteSource() {
        return this.deleteSource;
    }
}
