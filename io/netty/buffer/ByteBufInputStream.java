package io.netty.buffer;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.DataInput;
import java.io.InputStream;

public class ByteBufInputStream extends InputStream implements DataInput {
    private final ByteBuf buffer;
    private final int startIndex;
    private final int endIndex;
    private boolean closed;
    private final boolean releaseOnClose;
    private final StringBuilder lineBuf;
    
    public ByteBufInputStream(final ByteBuf buffer) {
        this(buffer, buffer.readableBytes());
    }
    
    public ByteBufInputStream(final ByteBuf buffer, final int length) {
        this(buffer, length, false);
    }
    
    public ByteBufInputStream(final ByteBuf buffer, final boolean releaseOnClose) {
        this(buffer, buffer.readableBytes(), releaseOnClose);
    }
    
    public ByteBufInputStream(final ByteBuf buffer, final int length, final boolean releaseOnClose) {
        this.lineBuf = new StringBuilder();
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        if (length < 0) {
            if (releaseOnClose) {
                buffer.release();
            }
            throw new IllegalArgumentException(new StringBuilder().append("length: ").append(length).toString());
        }
        if (length > buffer.readableBytes()) {
            if (releaseOnClose) {
                buffer.release();
            }
            throw new IndexOutOfBoundsException(new StringBuilder().append("Too many bytes to be read - Needs ").append(length).append(", maximum is ").append(buffer.readableBytes()).toString());
        }
        this.releaseOnClose = releaseOnClose;
        this.buffer = buffer;
        this.startIndex = buffer.readerIndex();
        this.endIndex = this.startIndex + length;
        buffer.markReaderIndex();
    }
    
    public int readBytes() {
        return this.buffer.readerIndex() - this.startIndex;
    }
    
    public void close() throws IOException {
        try {
            super.close();
        }
        finally {
            if (this.releaseOnClose && !this.closed) {
                this.closed = true;
                this.buffer.release();
            }
        }
    }
    
    public int available() throws IOException {
        return this.endIndex - this.buffer.readerIndex();
    }
    
    public void mark(final int readlimit) {
        this.buffer.markReaderIndex();
    }
    
    public boolean markSupported() {
        return true;
    }
    
    public int read() throws IOException {
        if (!this.buffer.isReadable()) {
            return -1;
        }
        return this.buffer.readByte() & 0xFF;
    }
    
    public int read(final byte[] b, final int off, int len) throws IOException {
        final int available = this.available();
        if (available == 0) {
            return -1;
        }
        len = Math.min(available, len);
        this.buffer.readBytes(b, off, len);
        return len;
    }
    
    public void reset() throws IOException {
        this.buffer.resetReaderIndex();
    }
    
    public long skip(final long n) throws IOException {
        if (n > 2147483647L) {
            return this.skipBytes(Integer.MAX_VALUE);
        }
        return this.skipBytes((int)n);
    }
    
    public boolean readBoolean() throws IOException {
        this.checkAvailable(1);
        return this.read() != 0;
    }
    
    public byte readByte() throws IOException {
        if (!this.buffer.isReadable()) {
            throw new EOFException();
        }
        return this.buffer.readByte();
    }
    
    public char readChar() throws IOException {
        return (char)this.readShort();
    }
    
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }
    
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }
    
    public void readFully(final byte[] b) throws IOException {
        this.readFully(b, 0, b.length);
    }
    
    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        this.checkAvailable(len);
        this.buffer.readBytes(b, off, len);
    }
    
    public int readInt() throws IOException {
        this.checkAvailable(4);
        return this.buffer.readInt();
    }
    
    public String readLine() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/netty/buffer/ByteBufInputStream.lineBuf:Ljava/lang/StringBuilder;
        //     4: iconst_0       
        //     5: invokevirtual   java/lang/StringBuilder.setLength:(I)V
        //     8: aload_0         /* this */
        //     9: getfield        io/netty/buffer/ByteBufInputStream.buffer:Lio/netty/buffer/ByteBuf;
        //    12: invokevirtual   io/netty/buffer/ByteBuf.isReadable:()Z
        //    15: ifne            40
        //    18: aload_0         /* this */
        //    19: getfield        io/netty/buffer/ByteBufInputStream.lineBuf:Ljava/lang/StringBuilder;
        //    22: invokevirtual   java/lang/StringBuilder.length:()I
        //    25: ifle            38
        //    28: aload_0         /* this */
        //    29: getfield        io/netty/buffer/ByteBufInputStream.lineBuf:Ljava/lang/StringBuilder;
        //    32: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    35: goto            39
        //    38: aconst_null    
        //    39: areturn        
        //    40: aload_0         /* this */
        //    41: getfield        io/netty/buffer/ByteBufInputStream.buffer:Lio/netty/buffer/ByteBuf;
        //    44: invokevirtual   io/netty/buffer/ByteBuf.readUnsignedByte:()S
        //    47: istore_1        /* c */
        //    48: iload_1         /* c */
        //    49: lookupswitch {
        //               10: 76
        //               13: 79
        //          default: 121
        //        }
        //    76: goto            134
        //    79: aload_0         /* this */
        //    80: getfield        io/netty/buffer/ByteBufInputStream.buffer:Lio/netty/buffer/ByteBuf;
        //    83: invokevirtual   io/netty/buffer/ByteBuf.isReadable:()Z
        //    86: ifeq            134
        //    89: aload_0         /* this */
        //    90: getfield        io/netty/buffer/ByteBufInputStream.buffer:Lio/netty/buffer/ByteBuf;
        //    93: aload_0         /* this */
        //    94: getfield        io/netty/buffer/ByteBufInputStream.buffer:Lio/netty/buffer/ByteBuf;
        //    97: invokevirtual   io/netty/buffer/ByteBuf.readerIndex:()I
        //   100: invokevirtual   io/netty/buffer/ByteBuf.getUnsignedByte:(I)S
        //   103: i2c            
        //   104: bipush          10
        //   106: if_icmpne       134
        //   109: aload_0         /* this */
        //   110: getfield        io/netty/buffer/ByteBufInputStream.buffer:Lio/netty/buffer/ByteBuf;
        //   113: iconst_1       
        //   114: invokevirtual   io/netty/buffer/ByteBuf.skipBytes:(I)Lio/netty/buffer/ByteBuf;
        //   117: pop            
        //   118: goto            134
        //   121: aload_0         /* this */
        //   122: getfield        io/netty/buffer/ByteBufInputStream.lineBuf:Ljava/lang/StringBuilder;
        //   125: iload_1         /* c */
        //   126: i2c            
        //   127: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: goto            8
        //   134: aload_0         /* this */
        //   135: getfield        io/netty/buffer/ByteBufInputStream.lineBuf:Ljava/lang/StringBuilder;
        //   138: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   141: areturn        
        //    Exceptions:
        //  throws java.io.IOException
        //    StackMapTable: 00 08 08 1D 40 07 00 C5 00 FC 00 23 01 02 29 FA 00 0C
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
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
    
    public long readLong() throws IOException {
        this.checkAvailable(8);
        return this.buffer.readLong();
    }
    
    public short readShort() throws IOException {
        this.checkAvailable(2);
        return this.buffer.readShort();
    }
    
    public String readUTF() throws IOException {
        return DataInputStream.readUTF((DataInput)this);
    }
    
    public int readUnsignedByte() throws IOException {
        return this.readByte() & 0xFF;
    }
    
    public int readUnsignedShort() throws IOException {
        return this.readShort() & 0xFFFF;
    }
    
    public int skipBytes(final int n) throws IOException {
        final int nBytes = Math.min(this.available(), n);
        this.buffer.skipBytes(nBytes);
        return nBytes;
    }
    
    private void checkAvailable(final int fieldSize) throws IOException {
        if (fieldSize < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        }
        if (fieldSize > this.available()) {
            throw new EOFException(new StringBuilder().append("fieldSize is too long! Length is ").append(fieldSize).append(", but maximum is ").append(this.available()).toString());
        }
    }
}
