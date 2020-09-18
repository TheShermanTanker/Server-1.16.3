package io.netty.buffer;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.Recycler;

class PooledHeapByteBuf extends PooledByteBuf<byte[]> {
    private static final Recycler<PooledHeapByteBuf> RECYCLER;
    
    static PooledHeapByteBuf newInstance(final int maxCapacity) {
        final PooledHeapByteBuf buf = PooledHeapByteBuf.RECYCLER.get();
        buf.reuse(maxCapacity);
        return buf;
    }
    
    PooledHeapByteBuf(final Recycler.Handle<? extends PooledHeapByteBuf> recyclerHandle, final int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }
    
    @Override
    public final boolean isDirect() {
        return false;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return HeapByteBufUtil.getByte((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return HeapByteBufUtil.getShort((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return HeapByteBufUtil.getShortLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return HeapByteBufUtil.getUnsignedMedium((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return HeapByteBufUtil.getUnsignedMediumLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getInt(final int index) {
        return HeapByteBufUtil.getInt((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return HeapByteBufUtil.getIntLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return HeapByteBufUtil.getLong((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return HeapByteBufUtil.getLongLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory((byte[])this.memory, this.idx(index), dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            this.getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, (byte[])this.memory, this.idx(index), length);
        }
        return this;
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        System.arraycopy(this.memory, this.idx(index), dst, dstIndex, length);
        return this;
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.checkIndex(index, dst.remaining());
        dst.put((byte[])this.memory, this.idx(index), dst.remaining());
        return this;
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.checkIndex(index, length);
        out.write((byte[])this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public final int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ByteBuffer.wrap((byte[])this.memory);
        }
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
    }
    
    @Override
    public final int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        return this.getBytes(index, out, position, length, false);
    }
    
    private int getBytes(int index, final FileChannel out, final long position, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        final ByteBuffer tmpBuf = internal ? this.internalNioBuffer() : ByteBuffer.wrap((byte[])this.memory);
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length), position);
    }
    
    @Override
    public final int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public final int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, position, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        HeapByteBufUtil.setByte((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        HeapByteBufUtil.setShort((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        HeapByteBufUtil.setShortLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        HeapByteBufUtil.setMedium((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        HeapByteBufUtil.setMediumLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        HeapByteBufUtil.setInt((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        HeapByteBufUtil.setIntLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        HeapByteBufUtil.setLong((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        HeapByteBufUtil.setLongLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    public final ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.hasMemoryAddress()) {
            PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, (byte[])this.memory, this.idx(index), length);
        }
        else if (src.hasArray()) {
            this.setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
        }
        else {
            src.getBytes(srcIndex, (byte[])this.memory, this.idx(index), length);
        }
        return this;
    }
    
    @Override
    public final ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        System.arraycopy(src, srcIndex, this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public final ByteBuf setBytes(final int index, final ByteBuffer src) {
        final int length = src.remaining();
        this.checkIndex(index, length);
        src.get((byte[])this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public final int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex(index, length);
        return in.read((byte[])this.memory, this.idx(index), length);
    }
    
    @Override
    public final int setBytes(int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length));
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public final int setBytes(int index, final FileChannel in, final long position, final int length) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length), position);
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public final ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf copy = this.alloc().heapBuffer(length, this.maxCapacity());
        copy.writeBytes((byte[])this.memory, this.idx(index), length);
        return copy;
    }
    
    @Override
    public final int nioBufferCount() {
        return 1;
    }
    
    @Override
    public final ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public final ByteBuffer nioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        final ByteBuffer buf = ByteBuffer.wrap((byte[])this.memory, index, length);
        return buf.slice();
    }
    
    @Override
    public final ByteBuffer internalNioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public final boolean hasArray() {
        return true;
    }
    
    @Override
    public final byte[] array() {
        this.ensureAccessible();
        return (byte[])this.memory;
    }
    
    @Override
    public final int arrayOffset() {
        return this.offset;
    }
    
    @Override
    public final boolean hasMemoryAddress() {
        return false;
    }
    
    @Override
    public final long memoryAddress() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected final ByteBuffer newInternalNioBuffer(final byte[] memory) {
        return ByteBuffer.wrap(memory);
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   io/netty/buffer/PooledHeapByteBuf$1.<init>:()V
        //     7: putstatic       io/netty/buffer/PooledHeapByteBuf.RECYCLER:Lio/netty/util/Recycler;
        //    10: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:294)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:173)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:169)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:664)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
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
}
