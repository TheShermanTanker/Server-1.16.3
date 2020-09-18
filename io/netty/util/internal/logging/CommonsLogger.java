package io.netty.util.internal.logging;

import org.apache.commons.logging.Log;

@Deprecated
class CommonsLogger extends AbstractInternalLogger {
    private static final long serialVersionUID = 8647838678388394885L;
    private final transient Log logger;
    
    CommonsLogger(final Log logger, final String name) {
        super(name);
        if (logger == null) {
            throw new NullPointerException("logger");
        }
        this.logger = logger;
    }
    
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }
    
    public void trace(final String msg) {
        this.logger.trace(msg);
    }
    
    public void trace(final String format, final Object arg) {
        if (this.logger.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.trace(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String format, final Object argA, final Object argB) {
        if (this.logger.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.trace(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String format, final Object... arguments) {
        if (this.logger.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.trace(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String msg, final Throwable t) {
        this.logger.trace(msg, t);
    }
    
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }
    
    public void debug(final String msg) {
        this.logger.debug(msg);
    }
    
    public void debug(final String format, final Object arg) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.debug(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String format, final Object argA, final Object argB) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.debug(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String format, final Object... arguments) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.debug(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String msg, final Throwable t) {
        this.logger.debug(msg, t);
    }
    
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }
    
    public void info(final String msg) {
        this.logger.info(msg);
    }
    
    public void info(final String format, final Object arg) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.info(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String format, final Object argA, final Object argB) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.info(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String format, final Object... arguments) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/netty/util/internal/logging/CommonsLogger.logger:Lorg/apache/commons/logging/Log;
        //     4: invokeinterface org/apache/commons/logging/Log.isInfoEnabled:()Z
        //     9: ifeq            35
        //    12: aload_1         /* format */
        //    13: aload_2         /* arguments */
        //    14: invokestatic    io/netty/util/internal/logging/MessageFormatter.arrayFormat:(Ljava/lang/String;[Ljava/lang/Object;)Lio/netty/util/internal/logging/FormattingTuple;
        //    17: astore_3        /* ft */
        //    18: aload_0         /* this */
        //    19: getfield        io/netty/util/internal/logging/CommonsLogger.logger:Lorg/apache/commons/logging/Log;
        //    22: aload_3         /* ft */
        //    23: invokevirtual   io/netty/util/internal/logging/FormattingTuple.getMessage:()Ljava/lang/String;
        //    26: aload_3         /* ft */
        //    27: invokevirtual   io/netty/util/internal/logging/FormattingTuple.getThrowable:()Ljava/lang/Throwable;
        //    30: invokeinterface org/apache/commons/logging/Log.info:(Ljava/lang/Object;Ljava/lang/Throwable;)V
        //    35: return         
        //    MethodParameters:
        //  Name       Flags  
        //  ---------  -----
        //  format     
        //  arguments  
        //    StackMapTable: 00 01 23
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
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
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:715)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:440)
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
    
    public void info(final String msg, final Throwable t) {
        this.logger.info(msg, t);
    }
    
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }
    
    public void warn(final String msg) {
        this.logger.warn(msg);
    }
    
    public void warn(final String format, final Object arg) {
        if (this.logger.isWarnEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.warn(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String format, final Object argA, final Object argB) {
        if (this.logger.isWarnEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.warn(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String format, final Object... arguments) {
        if (this.logger.isWarnEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.warn(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String msg, final Throwable t) {
        this.logger.warn(msg, t);
    }
    
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }
    
    public void error(final String msg) {
        this.logger.error(msg);
    }
    
    public void error(final String format, final Object arg) {
        if (this.logger.isErrorEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.error(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String format, final Object argA, final Object argB) {
        if (this.logger.isErrorEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.error(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String format, final Object... arguments) {
        if (this.logger.isErrorEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.error(ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String msg, final Throwable t) {
        this.logger.error(msg, t);
    }
}
