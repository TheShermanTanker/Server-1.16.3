package io.netty.buffer;

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import io.netty.util.ReferenceCounted;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.util.ListIterator;
import io.netty.util.internal.EmptyArrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.nio.ByteOrder;
import io.netty.util.internal.ObjectUtil;
import java.util.Iterator;
import java.nio.ByteBuffer;

public class CompositeByteBuf extends AbstractReferenceCountedByteBuf implements Iterable<ByteBuf> {
    private static final ByteBuffer EMPTY_NIO_BUFFER;
    private static final Iterator<ByteBuf> EMPTY_ITERATOR;
    private final ByteBufAllocator alloc;
    private final boolean direct;
    private final ComponentList components;
    private final int maxNumComponents;
    private boolean freed;
    
    public CompositeByteBuf(final ByteBufAllocator alloc, final boolean direct, final int maxNumComponents) {
        super(Integer.MAX_VALUE);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.components = newList(maxNumComponents);
    }
    
    public CompositeByteBuf(final ByteBufAllocator alloc, final boolean direct, final int maxNumComponents, final ByteBuf... buffers) {
        this(alloc, direct, maxNumComponents, buffers, 0, buffers.length);
    }
    
    CompositeByteBuf(final ByteBufAllocator alloc, final boolean direct, final int maxNumComponents, final ByteBuf[] buffers, final int offset, final int len) {
        super(Integer.MAX_VALUE);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (maxNumComponents < 2) {
            throw new IllegalArgumentException(new StringBuilder().append("maxNumComponents: ").append(maxNumComponents).append(" (expected: >= 2)").toString());
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.components = newList(maxNumComponents);
        this.addComponents0(false, 0, buffers, offset, len);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
    }
    
    public CompositeByteBuf(final ByteBufAllocator alloc, final boolean direct, final int maxNumComponents, final Iterable<ByteBuf> buffers) {
        super(Integer.MAX_VALUE);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (maxNumComponents < 2) {
            throw new IllegalArgumentException(new StringBuilder().append("maxNumComponents: ").append(maxNumComponents).append(" (expected: >= 2)").toString());
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.components = newList(maxNumComponents);
        this.addComponents0(false, 0, buffers);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
    }
    
    private static ComponentList newList(final int maxNumComponents) {
        return new ComponentList(Math.min(16, maxNumComponents));
    }
    
    CompositeByteBuf(final ByteBufAllocator alloc) {
        super(Integer.MAX_VALUE);
        this.alloc = alloc;
        this.direct = false;
        this.maxNumComponents = 0;
        this.components = null;
    }
    
    public CompositeByteBuf addComponent(final ByteBuf buffer) {
        return this.addComponent(false, buffer);
    }
    
    public CompositeByteBuf addComponents(final ByteBuf... buffers) {
        return this.addComponents(false, buffers);
    }
    
    public CompositeByteBuf addComponents(final Iterable<ByteBuf> buffers) {
        return this.addComponents(false, buffers);
    }
    
    public CompositeByteBuf addComponent(final int cIndex, final ByteBuf buffer) {
        return this.addComponent(false, cIndex, buffer);
    }
    
    public CompositeByteBuf addComponent(final boolean increaseWriterIndex, final ByteBuf buffer) {
        ObjectUtil.<ByteBuf>checkNotNull(buffer, "buffer");
        this.addComponent0(increaseWriterIndex, this.components.size(), buffer);
        this.consolidateIfNeeded();
        return this;
    }
    
    public CompositeByteBuf addComponents(final boolean increaseWriterIndex, final ByteBuf... buffers) {
        this.addComponents0(increaseWriterIndex, this.components.size(), buffers, 0, buffers.length);
        this.consolidateIfNeeded();
        return this;
    }
    
    public CompositeByteBuf addComponents(final boolean increaseWriterIndex, final Iterable<ByteBuf> buffers) {
        this.addComponents0(increaseWriterIndex, this.components.size(), buffers);
        this.consolidateIfNeeded();
        return this;
    }
    
    public CompositeByteBuf addComponent(final boolean increaseWriterIndex, final int cIndex, final ByteBuf buffer) {
        ObjectUtil.<ByteBuf>checkNotNull(buffer, "buffer");
        this.addComponent0(increaseWriterIndex, cIndex, buffer);
        this.consolidateIfNeeded();
        return this;
    }
    
    private int addComponent0(final boolean increaseWriterIndex, final int cIndex, final ByteBuf buffer) {
        assert buffer != null;
        boolean wasAdded = false;
        try {
            this.checkComponentIndex(cIndex);
            final int readableBytes = buffer.readableBytes();
            final Component c = new Component(buffer.order(ByteOrder.BIG_ENDIAN).slice());
            if (cIndex == this.components.size()) {
                wasAdded = this.components.add(c);
                if (cIndex == 0) {
                    c.endOffset = readableBytes;
                }
                else {
                    final Component prev = (Component)this.components.get(cIndex - 1);
                    c.offset = prev.endOffset;
                    c.endOffset = c.offset + readableBytes;
                }
            }
            else {
                this.components.add(cIndex, c);
                wasAdded = true;
                if (readableBytes != 0) {
                    this.updateComponentOffsets(cIndex);
                }
            }
            if (increaseWriterIndex) {
                this.writerIndex(this.writerIndex() + buffer.readableBytes());
            }
            return cIndex;
        }
        finally {
            if (!wasAdded) {
                buffer.release();
            }
        }
    }
    
    public CompositeByteBuf addComponents(final int cIndex, final ByteBuf... buffers) {
        this.addComponents0(false, cIndex, buffers, 0, buffers.length);
        this.consolidateIfNeeded();
        return this;
    }
    
    private int addComponents0(final boolean increaseWriterIndex, int cIndex, final ByteBuf[] buffers, final int offset, final int len) {
        ObjectUtil.<ByteBuf[]>checkNotNull(buffers, "buffers");
        int i = offset;
        try {
            this.checkComponentIndex(cIndex);
            while (i < len) {
                final ByteBuf b = buffers[i++];
                if (b == null) {
                    break;
                }
                cIndex = this.addComponent0(increaseWriterIndex, cIndex, b) + 1;
                final int size = this.components.size();
                if (cIndex <= size) {
                    continue;
                }
                cIndex = size;
            }
            return cIndex;
        }
        finally {
            while (i < len) {
                final ByteBuf b2 = buffers[i];
                if (b2 != null) {
                    try {
                        b2.release();
                    }
                    catch (Throwable t) {}
                }
                ++i;
            }
        }
    }
    
    public CompositeByteBuf addComponents(final int cIndex, final Iterable<ByteBuf> buffers) {
        this.addComponents0(false, cIndex, buffers);
        this.consolidateIfNeeded();
        return this;
    }
    
    private int addComponents0(final boolean increaseIndex, final int cIndex, Iterable<ByteBuf> buffers) {
        if (buffers instanceof ByteBuf) {
            return this.addComponent0(increaseIndex, cIndex, (ByteBuf)buffers);
        }
        ObjectUtil.<Iterable<ByteBuf>>checkNotNull(buffers, "buffers");
        if (!(buffers instanceof Collection)) {
            final List<ByteBuf> list = (List<ByteBuf>)new ArrayList();
            try {
                for (final ByteBuf b : buffers) {
                    list.add(b);
                }
                buffers = (Iterable<ByteBuf>)list;
            }
            finally {
                if (buffers != list) {
                    for (final ByteBuf b2 : buffers) {
                        if (b2 != null) {
                            try {
                                b2.release();
                            }
                            catch (Throwable t) {}
                        }
                    }
                }
            }
        }
        final Collection<ByteBuf> col = (Collection<ByteBuf>)buffers;
        return this.addComponents0(increaseIndex, cIndex, (ByteBuf[])col.toArray((Object[])new ByteBuf[col.size()]), 0, col.size());
    }
    
    private void consolidateIfNeeded() {
        final int numComponents = this.components.size();
        if (numComponents > this.maxNumComponents) {
            final int capacity = ((Component)this.components.get(numComponents - 1)).endOffset;
            final ByteBuf consolidated = this.allocBuffer(capacity);
            for (int i = 0; i < numComponents; ++i) {
                final Component c = (Component)this.components.get(i);
                final ByteBuf b = c.buf;
                consolidated.writeBytes(b);
                c.freeIfNecessary();
            }
            final Component c2 = new Component(consolidated);
            c2.endOffset = c2.length;
            this.components.clear();
            this.components.add(c2);
        }
    }
    
    private void checkComponentIndex(final int cIndex) {
        this.ensureAccessible();
        if (cIndex < 0 || cIndex > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", new Object[] { cIndex, this.components.size() }));
        }
    }
    
    private void checkComponentIndex(final int cIndex, final int numComponents) {
        this.ensureAccessible();
        if (cIndex < 0 || cIndex + numComponents > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", new Object[] { cIndex, numComponents, this.components.size() }));
        }
    }
    
    private void updateComponentOffsets(int cIndex) {
        final int size = this.components.size();
        if (size <= cIndex) {
            return;
        }
        final Component c = (Component)this.components.get(cIndex);
        if (cIndex == 0) {
            c.offset = 0;
            c.endOffset = c.length;
            ++cIndex;
        }
        for (int i = cIndex; i < size; ++i) {
            final Component prev = (Component)this.components.get(i - 1);
            final Component cur = (Component)this.components.get(i);
            cur.offset = prev.endOffset;
            cur.endOffset = cur.offset + cur.length;
        }
    }
    
    public CompositeByteBuf removeComponent(final int cIndex) {
        this.checkComponentIndex(cIndex);
        final Component comp = (Component)this.components.remove(cIndex);
        comp.freeIfNecessary();
        if (comp.length > 0) {
            this.updateComponentOffsets(cIndex);
        }
        return this;
    }
    
    public CompositeByteBuf removeComponents(final int cIndex, final int numComponents) {
        this.checkComponentIndex(cIndex, numComponents);
        if (numComponents == 0) {
            return this;
        }
        final int endIndex = cIndex + numComponents;
        boolean needsUpdate = false;
        for (int i = cIndex; i < endIndex; ++i) {
            final Component c = (Component)this.components.get(i);
            if (c.length > 0) {
                needsUpdate = true;
            }
            c.freeIfNecessary();
        }
        this.components.removeRange(cIndex, endIndex);
        if (needsUpdate) {
            this.updateComponentOffsets(cIndex);
        }
        return this;
    }
    
    public Iterator<ByteBuf> iterator() {
        this.ensureAccessible();
        if (this.components.isEmpty()) {
            return CompositeByteBuf.EMPTY_ITERATOR;
        }
        return (Iterator<ByteBuf>)new CompositeByteBufIterator();
    }
    
    public List<ByteBuf> decompose(final int offset, final int length) {
        this.checkIndex(offset, length);
        if (length == 0) {
            return (List<ByteBuf>)Collections.emptyList();
        }
        int componentId = this.toComponentIndex(offset);
        final List<ByteBuf> slice = (List<ByteBuf>)new ArrayList(this.components.size());
        final Component firstC = (Component)this.components.get(componentId);
        final ByteBuf first = firstC.buf.duplicate();
        first.readerIndex(offset - firstC.offset);
        ByteBuf buf = first;
        int bytesToSlice = length;
        do {
            final int readableBytes = buf.readableBytes();
            if (bytesToSlice <= readableBytes) {
                buf.writerIndex(buf.readerIndex() + bytesToSlice);
                slice.add(buf);
                break;
            }
            slice.add(buf);
            bytesToSlice -= readableBytes;
            ++componentId;
            buf = ((Component)this.components.get(componentId)).buf.duplicate();
        } while (bytesToSlice > 0);
        for (int i = 0; i < slice.size(); ++i) {
            slice.set(i, ((ByteBuf)slice.get(i)).slice());
        }
        return slice;
    }
    
    public boolean isDirect() {
        final int size = this.components.size();
        if (size == 0) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (!((Component)this.components.get(i)).buf.isDirect()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasArray() {
        switch (this.components.size()) {
            case 0: {
                return true;
            }
            case 1: {
                return ((Component)this.components.get(0)).buf.hasArray();
            }
            default: {
                return false;
            }
        }
    }
    
    public byte[] array() {
        switch (this.components.size()) {
            case 0: {
                return EmptyArrays.EMPTY_BYTES;
            }
            case 1: {
                return ((Component)this.components.get(0)).buf.array();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public int arrayOffset() {
        switch (this.components.size()) {
            case 0: {
                return 0;
            }
            case 1: {
                return ((Component)this.components.get(0)).buf.arrayOffset();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public boolean hasMemoryAddress() {
        switch (this.components.size()) {
            case 0: {
                return Unpooled.EMPTY_BUFFER.hasMemoryAddress();
            }
            case 1: {
                return ((Component)this.components.get(0)).buf.hasMemoryAddress();
            }
            default: {
                return false;
            }
        }
    }
    
    public long memoryAddress() {
        switch (this.components.size()) {
            case 0: {
                return Unpooled.EMPTY_BUFFER.memoryAddress();
            }
            case 1: {
                return ((Component)this.components.get(0)).buf.memoryAddress();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public int capacity() {
        final int numComponents = this.components.size();
        if (numComponents == 0) {
            return 0;
        }
        return ((Component)this.components.get(numComponents - 1)).endOffset;
    }
    
    public CompositeByteBuf capacity(final int newCapacity) {
        this.checkNewCapacity(newCapacity);
        final int oldCapacity = this.capacity();
        if (newCapacity > oldCapacity) {
            final int paddingLength = newCapacity - oldCapacity;
            final int nComponents = this.components.size();
            if (nComponents < this.maxNumComponents) {
                final ByteBuf padding = this.allocBuffer(paddingLength);
                padding.setIndex(0, paddingLength);
                this.addComponent0(false, this.components.size(), padding);
            }
            else {
                final ByteBuf padding = this.allocBuffer(paddingLength);
                padding.setIndex(0, paddingLength);
                this.addComponent0(false, this.components.size(), padding);
                this.consolidateIfNeeded();
            }
        }
        else if (newCapacity < oldCapacity) {
            int bytesToTrim = oldCapacity - newCapacity;
            final ListIterator<Component> i = (ListIterator<Component>)this.components.listIterator(this.components.size());
            while (i.hasPrevious()) {
                final Component c = (Component)i.previous();
                if (bytesToTrim < c.length) {
                    final Component newC = new Component(c.buf.slice(0, c.length - bytesToTrim));
                    newC.offset = c.offset;
                    newC.endOffset = newC.offset + newC.length;
                    i.set(newC);
                    break;
                }
                bytesToTrim -= c.length;
                i.remove();
            }
            if (this.readerIndex() > newCapacity) {
                this.setIndex(newCapacity, newCapacity);
            }
            else if (this.writerIndex() > newCapacity) {
                this.writerIndex(newCapacity);
            }
        }
        return this;
    }
    
    public ByteBufAllocator alloc() {
        return this.alloc;
    }
    
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    public int numComponents() {
        return this.components.size();
    }
    
    public int maxNumComponents() {
        return this.maxNumComponents;
    }
    
    public int toComponentIndex(final int offset) {
        this.checkIndex(offset);
        int low = 0;
        int high = this.components.size();
        while (low <= high) {
            final int mid = low + high >>> 1;
            final Component c = (Component)this.components.get(mid);
            if (offset >= c.endOffset) {
                low = mid + 1;
            }
            else {
                if (offset >= c.offset) {
                    return mid;
                }
                high = mid - 1;
            }
        }
        throw new Error("should not reach here");
    }
    
    public int toByteIndex(final int cIndex) {
        this.checkComponentIndex(cIndex);
        return ((Component)this.components.get(cIndex)).offset;
    }
    
    public byte getByte(final int index) {
        return this._getByte(index);
    }
    
    protected byte _getByte(final int index) {
        final Component c = this.findComponent(index);
        return c.buf.getByte(index - c.offset);
    }
    
    protected short _getShort(final int index) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            return c.buf.getShort(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (short)((this._getByte(index) & 0xFF) << 8 | (this._getByte(index + 1) & 0xFF));
        }
        return (short)((this._getByte(index) & 0xFF) | (this._getByte(index + 1) & 0xFF) << 8);
    }
    
    protected short _getShortLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            return c.buf.getShortLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (short)((this._getByte(index) & 0xFF) | (this._getByte(index + 1) & 0xFF) << 8);
        }
        return (short)((this._getByte(index) & 0xFF) << 8 | (this._getByte(index + 1) & 0xFF));
    }
    
    protected int _getUnsignedMedium(final int index) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            return c.buf.getUnsignedMedium(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 8 | (this._getByte(index + 2) & 0xFF);
        }
        return (this._getShort(index) & 0xFFFF) | (this._getByte(index + 2) & 0xFF) << 16;
    }
    
    protected int _getUnsignedMediumLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            return c.buf.getUnsignedMediumLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShortLE(index) & 0xFFFF) | (this._getByte(index + 2) & 0xFF) << 16;
        }
        return (this._getShortLE(index) & 0xFFFF) << 8 | (this._getByte(index + 2) & 0xFF);
    }
    
    protected int _getInt(final int index) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            return c.buf.getInt(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 16 | (this._getShort(index + 2) & 0xFFFF);
        }
        return (this._getShort(index) & 0xFFFF) | (this._getShort(index + 2) & 0xFFFF) << 16;
    }
    
    protected int _getIntLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            return c.buf.getIntLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShortLE(index) & 0xFFFF) | (this._getShortLE(index + 2) & 0xFFFF) << 16;
        }
        return (this._getShortLE(index) & 0xFFFF) << 16 | (this._getShortLE(index + 2) & 0xFFFF);
    }
    
    protected long _getLong(final int index) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            return c.buf.getLong(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return ((long)this._getInt(index) & 0xFFFFFFFFL) << 32 | ((long)this._getInt(index + 4) & 0xFFFFFFFFL);
        }
        return ((long)this._getInt(index) & 0xFFFFFFFFL) | ((long)this._getInt(index + 4) & 0xFFFFFFFFL) << 32;
    }
    
    protected long _getLongLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            return c.buf.getLongLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return ((long)this._getIntLE(index) & 0xFFFFFFFFL) | ((long)this._getIntLE(index + 4) & 0xFFFFFFFFL) << 32;
        }
        return ((long)this._getIntLE(index) & 0xFFFFFFFFL) << 32 | ((long)this._getIntLE(index + 4) & 0xFFFFFFFFL);
    }
    
    public CompositeByteBuf getBytes(int index, final byte[] dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
        }
        return this;
    }
    
    public CompositeByteBuf getBytes(int index, final ByteBuffer dst) {
        final int limit = dst.limit();
        int length = dst.remaining();
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i = this.toComponentIndex(index);
        try {
            while (length > 0) {
                final Component c = (Component)this.components.get(i);
                final ByteBuf s = c.buf;
                final int adjustment = c.offset;
                final int localLength = Math.min(length, s.capacity() - (index - adjustment));
                dst.limit(dst.position() + localLength);
                s.getBytes(index - adjustment, dst);
                index += localLength;
                length -= localLength;
                ++i;
            }
        }
        finally {
            dst.limit(limit);
        }
        return this;
    }
    
    public CompositeByteBuf getBytes(int index, final ByteBuf dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
        }
        return this;
    }
    
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        final int count = this.nioBufferCount();
        if (count == 1) {
            return out.write(this.internalNioBuffer(index, length));
        }
        final long writtenBytes = out.write(this.nioBuffers(index, length));
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)writtenBytes;
    }
    
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        final int count = this.nioBufferCount();
        if (count == 1) {
            return out.write(this.internalNioBuffer(index, length), position);
        }
        long writtenBytes = 0L;
        for (final ByteBuffer buf : this.nioBuffers(index, length)) {
            writtenBytes += out.write(buf, position + writtenBytes);
        }
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)writtenBytes;
    }
    
    public CompositeByteBuf getBytes(int index, final OutputStream out, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, out, localLength);
            index += localLength;
        }
        return this;
    }
    
    public CompositeByteBuf setByte(final int index, final int value) {
        final Component c = this.findComponent(index);
        c.buf.setByte(index - c.offset, value);
        return this;
    }
    
    protected void _setByte(final int index, final int value) {
        this.setByte(index, value);
    }
    
    public CompositeByteBuf setShort(final int index, final int value) {
        return (CompositeByteBuf)super.setShort(index, value);
    }
    
    protected void _setShort(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            c.buf.setShort(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setByte(index, (byte)(value >>> 8));
            this._setByte(index + 1, (byte)value);
        }
        else {
            this._setByte(index, (byte)value);
            this._setByte(index + 1, (byte)(value >>> 8));
        }
    }
    
    protected void _setShortLE(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            c.buf.setShortLE(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setByte(index, (byte)value);
            this._setByte(index + 1, (byte)(value >>> 8));
        }
        else {
            this._setByte(index, (byte)(value >>> 8));
            this._setByte(index + 1, (byte)value);
        }
    }
    
    public CompositeByteBuf setMedium(final int index, final int value) {
        return (CompositeByteBuf)super.setMedium(index, value);
    }
    
    protected void _setMedium(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            c.buf.setMedium(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(index, (short)(value >> 8));
            this._setByte(index + 2, (byte)value);
        }
        else {
            this._setShort(index, (short)value);
            this._setByte(index + 2, (byte)(value >>> 16));
        }
    }
    
    protected void _setMediumLE(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            c.buf.setMediumLE(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShortLE(index, (short)value);
            this._setByte(index + 2, (byte)(value >>> 16));
        }
        else {
            this._setShortLE(index, (short)(value >> 8));
            this._setByte(index + 2, (byte)value);
        }
    }
    
    public CompositeByteBuf setInt(final int index, final int value) {
        return (CompositeByteBuf)super.setInt(index, value);
    }
    
    protected void _setInt(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            c.buf.setInt(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(index, (short)(value >>> 16));
            this._setShort(index + 2, (short)value);
        }
        else {
            this._setShort(index, (short)value);
            this._setShort(index + 2, (short)(value >>> 16));
        }
    }
    
    protected void _setIntLE(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            c.buf.setIntLE(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShortLE(index, (short)value);
            this._setShortLE(index + 2, (short)(value >>> 16));
        }
        else {
            this._setShortLE(index, (short)(value >>> 16));
            this._setShortLE(index + 2, (short)value);
        }
    }
    
    public CompositeByteBuf setLong(final int index, final long value) {
        return (CompositeByteBuf)super.setLong(index, value);
    }
    
    protected void _setLong(final int index, final long value) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            c.buf.setLong(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setInt(index, (int)(value >>> 32));
            this._setInt(index + 4, (int)value);
        }
        else {
            this._setInt(index, (int)value);
            this._setInt(index + 4, (int)(value >>> 32));
        }
    }
    
    protected void _setLongLE(final int index, final long value) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            c.buf.setLongLE(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setIntLE(index, (int)value);
            this._setIntLE(index + 4, (int)(value >>> 32));
        }
        else {
            this._setIntLE(index, (int)(value >>> 32));
            this._setIntLE(index + 4, (int)value);
        }
    }
    
    public CompositeByteBuf setBytes(int index, final byte[] src, int srcIndex, int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
        }
        return this;
    }
    
    public CompositeByteBuf setBytes(int index, final ByteBuffer src) {
        final int limit = src.limit();
        int length = src.remaining();
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i = this.toComponentIndex(index);
        try {
            while (length > 0) {
                final Component c = (Component)this.components.get(i);
                final ByteBuf s = c.buf;
                final int adjustment = c.offset;
                final int localLength = Math.min(length, s.capacity() - (index - adjustment));
                src.limit(src.position() + localLength);
                s.setBytes(index - adjustment, src);
                index += localLength;
                length -= localLength;
                ++i;
            }
        }
        finally {
            src.limit(limit);
        }
        return this;
    }
    
    public CompositeByteBuf setBytes(int index, final ByteBuf src, int srcIndex, int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
        }
        return this;
    }
    
    public int setBytes(int index, final InputStream in, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return in.read(EmptyArrays.EMPTY_BYTES);
        }
        int i = this.toComponentIndex(index);
        int readBytes = 0;
        do {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            final int localLength = Math.min(length, s.capacity() - (index - adjustment));
            if (localLength == 0) {
                ++i;
            }
            else {
                final int localReadBytes = s.setBytes(index - adjustment, in, localLength);
                if (localReadBytes < 0) {
                    if (readBytes == 0) {
                        return -1;
                    }
                    break;
                }
                else if (localReadBytes == localLength) {
                    index += localLength;
                    length -= localLength;
                    readBytes += localLength;
                    ++i;
                }
                else {
                    index += localReadBytes;
                    length -= localReadBytes;
                    readBytes += localReadBytes;
                }
            }
        } while (length > 0);
        return readBytes;
    }
    
    public int setBytes(int index, final ScatteringByteChannel in, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return in.read(CompositeByteBuf.EMPTY_NIO_BUFFER);
        }
        int i = this.toComponentIndex(index);
        int readBytes = 0;
        do {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            final int localLength = Math.min(length, s.capacity() - (index - adjustment));
            if (localLength == 0) {
                ++i;
            }
            else {
                final int localReadBytes = s.setBytes(index - adjustment, in, localLength);
                if (localReadBytes == 0) {
                    break;
                }
                if (localReadBytes < 0) {
                    if (readBytes == 0) {
                        return -1;
                    }
                    break;
                }
                else if (localReadBytes == localLength) {
                    index += localLength;
                    length -= localLength;
                    readBytes += localLength;
                    ++i;
                }
                else {
                    index += localReadBytes;
                    length -= localReadBytes;
                    readBytes += localReadBytes;
                }
            }
        } while (length > 0);
        return readBytes;
    }
    
    public int setBytes(int index, final FileChannel in, final long position, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return in.read(CompositeByteBuf.EMPTY_NIO_BUFFER, position);
        }
        int i = this.toComponentIndex(index);
        int readBytes = 0;
        do {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            final int localLength = Math.min(length, s.capacity() - (index - adjustment));
            if (localLength == 0) {
                ++i;
            }
            else {
                final int localReadBytes = s.setBytes(index - adjustment, in, position + readBytes, localLength);
                if (localReadBytes == 0) {
                    break;
                }
                if (localReadBytes < 0) {
                    if (readBytes == 0) {
                        return -1;
                    }
                    break;
                }
                else if (localReadBytes == localLength) {
                    index += localLength;
                    length -= localLength;
                    readBytes += localLength;
                    ++i;
                }
                else {
                    index += localReadBytes;
                    length -= localReadBytes;
                    readBytes += localReadBytes;
                }
            }
        } while (length > 0);
        return readBytes;
    }
    
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf dst = this.allocBuffer(length);
        if (length != 0) {
            this.copyTo(index, length, this.toComponentIndex(index), dst);
        }
        return dst;
    }
    
    private void copyTo(int index, int length, final int componentId, final ByteBuf dst) {
        int dstIndex = 0;
        int localLength;
        for (int i = componentId; length > 0; length -= localLength, ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
        }
        dst.writerIndex(dst.capacity());
    }
    
    public ByteBuf component(final int cIndex) {
        return this.internalComponent(cIndex).duplicate();
    }
    
    public ByteBuf componentAtOffset(final int offset) {
        return this.internalComponentAtOffset(offset).duplicate();
    }
    
    public ByteBuf internalComponent(final int cIndex) {
        this.checkComponentIndex(cIndex);
        return ((Component)this.components.get(cIndex)).buf;
    }
    
    public ByteBuf internalComponentAtOffset(final int offset) {
        return this.findComponent(offset).buf;
    }
    
    private Component findComponent(final int offset) {
        this.checkIndex(offset);
        int low = 0;
        int high = this.components.size();
        while (low <= high) {
            final int mid = low + high >>> 1;
            final Component c = (Component)this.components.get(mid);
            if (offset >= c.endOffset) {
                low = mid + 1;
            }
            else if (offset < c.offset) {
                high = mid - 1;
            }
            else {
                assert c.length != 0;
                return c;
            }
        }
        throw new Error("should not reach here");
    }
    
    public int nioBufferCount() {
        switch (this.components.size()) {
            case 0: {
                return 1;
            }
            case 1: {
                return ((Component)this.components.get(0)).buf.nioBufferCount();
            }
            default: {
                int count = 0;
                for (int componentsCount = this.components.size(), i = 0; i < componentsCount; ++i) {
                    final Component c = (Component)this.components.get(i);
                    count += c.buf.nioBufferCount();
                }
                return count;
            }
        }
    }
    
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        switch (this.components.size()) {
            case 0: {
                return CompositeByteBuf.EMPTY_NIO_BUFFER;
            }
            case 1: {
                return ((Component)this.components.get(0)).buf.internalNioBuffer(index, length);
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        switch (this.components.size()) {
            case 0: {
                return CompositeByteBuf.EMPTY_NIO_BUFFER;
            }
            case 1: {
                final ByteBuf buf = ((Component)this.components.get(0)).buf;
                if (buf.nioBufferCount() == 1) {
                    return ((Component)this.components.get(0)).buf.nioBuffer(index, length);
                }
                break;
            }
        }
        final ByteBuffer merged = ByteBuffer.allocate(length).order(this.order());
        final ByteBuffer[] nioBuffers;
        final ByteBuffer[] buffers = nioBuffers = this.nioBuffers(index, length);
        for (final ByteBuffer buf2 : nioBuffers) {
            merged.put(buf2);
        }
        merged.flip();
        return merged;
    }
    
    public ByteBuffer[] nioBuffers(int index, int length) {
        this.checkIndex(index, length);
        if (length == 0) {
            return new ByteBuffer[] { CompositeByteBuf.EMPTY_NIO_BUFFER };
        }
        final List<ByteBuffer> buffers = (List<ByteBuffer>)new ArrayList(this.components.size());
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            switch (s.nioBufferCount()) {
                case 0: {
                    throw new UnsupportedOperationException();
                }
                case 1: {
                    buffers.add(s.nioBuffer(index - adjustment, localLength));
                    break;
                }
                default: {
                    Collections.addAll((Collection)buffers, (Object[])s.nioBuffers(index - adjustment, localLength));
                    break;
                }
            }
            index += localLength;
        }
        return (ByteBuffer[])buffers.toArray((Object[])new ByteBuffer[buffers.size()]);
    }
    
    public CompositeByteBuf consolidate() {
        this.ensureAccessible();
        final int numComponents = this.numComponents();
        if (numComponents <= 1) {
            return this;
        }
        final Component last = (Component)this.components.get(numComponents - 1);
        final int capacity = last.endOffset;
        final ByteBuf consolidated = this.allocBuffer(capacity);
        for (int i = 0; i < numComponents; ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf b = c.buf;
            consolidated.writeBytes(b);
            c.freeIfNecessary();
        }
        this.components.clear();
        this.components.add(new Component(consolidated));
        this.updateComponentOffsets(0);
        return this;
    }
    
    public CompositeByteBuf consolidate(final int cIndex, final int numComponents) {
        this.checkComponentIndex(cIndex, numComponents);
        if (numComponents <= 1) {
            return this;
        }
        final int endCIndex = cIndex + numComponents;
        final Component last = (Component)this.components.get(endCIndex - 1);
        final int capacity = last.endOffset - ((Component)this.components.get(cIndex)).offset;
        final ByteBuf consolidated = this.allocBuffer(capacity);
        for (int i = cIndex; i < endCIndex; ++i) {
            final Component c = (Component)this.components.get(i);
            final ByteBuf b = c.buf;
            consolidated.writeBytes(b);
            c.freeIfNecessary();
        }
        this.components.removeRange(cIndex + 1, endCIndex);
        this.components.set(cIndex, new Component(consolidated));
        this.updateComponentOffsets(cIndex);
        return this;
    }
    
    public CompositeByteBuf discardReadComponents() {
        this.ensureAccessible();
        final int readerIndex = this.readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        final int writerIndex = this.writerIndex();
        if (readerIndex == writerIndex && writerIndex == this.capacity()) {
            for (int size = this.components.size(), i = 0; i < size; ++i) {
                ((Component)this.components.get(i)).freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(readerIndex);
            return this;
        }
        final int firstComponentId = this.toComponentIndex(readerIndex);
        for (int i = 0; i < firstComponentId; ++i) {
            ((Component)this.components.get(i)).freeIfNecessary();
        }
        this.components.removeRange(0, firstComponentId);
        final Component first = (Component)this.components.get(0);
        final int offset = first.offset;
        this.updateComponentOffsets(0);
        this.setIndex(readerIndex - offset, writerIndex - offset);
        this.adjustMarkers(offset);
        return this;
    }
    
    public CompositeByteBuf discardReadBytes() {
        this.ensureAccessible();
        final int readerIndex = this.readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        final int writerIndex = this.writerIndex();
        if (readerIndex == writerIndex && writerIndex == this.capacity()) {
            for (int size = this.components.size(), i = 0; i < size; ++i) {
                ((Component)this.components.get(i)).freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(readerIndex);
            return this;
        }
        int firstComponentId = this.toComponentIndex(readerIndex);
        for (int i = 0; i < firstComponentId; ++i) {
            ((Component)this.components.get(i)).freeIfNecessary();
        }
        final Component c = (Component)this.components.get(firstComponentId);
        final int adjustment = readerIndex - c.offset;
        if (adjustment == c.length) {
            ++firstComponentId;
        }
        else {
            final Component newC = new Component(c.buf.slice(adjustment, c.length - adjustment));
            this.components.set(firstComponentId, newC);
        }
        this.components.removeRange(0, firstComponentId);
        this.updateComponentOffsets(0);
        this.setIndex(0, writerIndex - readerIndex);
        this.adjustMarkers(readerIndex);
        return this;
    }
    
    private ByteBuf allocBuffer(final int capacity) {
        return this.direct ? this.alloc().directBuffer(capacity) : this.alloc().heapBuffer(capacity);
    }
    
    public String toString() {
        String result = super.toString();
        result = result.substring(0, result.length() - 1);
        return result + ", components=" + this.components.size() + ')';
    }
    
    public CompositeByteBuf readerIndex(final int readerIndex) {
        return (CompositeByteBuf)super.readerIndex(readerIndex);
    }
    
    public CompositeByteBuf writerIndex(final int writerIndex) {
        return (CompositeByteBuf)super.writerIndex(writerIndex);
    }
    
    public CompositeByteBuf setIndex(final int readerIndex, final int writerIndex) {
        return (CompositeByteBuf)super.setIndex(readerIndex, writerIndex);
    }
    
    public CompositeByteBuf clear() {
        return (CompositeByteBuf)super.clear();
    }
    
    public CompositeByteBuf markReaderIndex() {
        return (CompositeByteBuf)super.markReaderIndex();
    }
    
    public CompositeByteBuf resetReaderIndex() {
        return (CompositeByteBuf)super.resetReaderIndex();
    }
    
    public CompositeByteBuf markWriterIndex() {
        return (CompositeByteBuf)super.markWriterIndex();
    }
    
    public CompositeByteBuf resetWriterIndex() {
        return (CompositeByteBuf)super.resetWriterIndex();
    }
    
    public CompositeByteBuf ensureWritable(final int minWritableBytes) {
        return (CompositeByteBuf)super.ensureWritable(minWritableBytes);
    }
    
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst) {
        return (CompositeByteBuf)super.getBytes(index, dst);
    }
    
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        return (CompositeByteBuf)super.getBytes(index, dst, length);
    }
    
    public CompositeByteBuf getBytes(final int index, final byte[] dst) {
        return (CompositeByteBuf)super.getBytes(index, dst);
    }
    
    public CompositeByteBuf setBoolean(final int index, final boolean value) {
        return (CompositeByteBuf)super.setBoolean(index, value);
    }
    
    public CompositeByteBuf setChar(final int index, final int value) {
        return (CompositeByteBuf)super.setChar(index, value);
    }
    
    public CompositeByteBuf setFloat(final int index, final float value) {
        return (CompositeByteBuf)super.setFloat(index, value);
    }
    
    public CompositeByteBuf setDouble(final int index, final double value) {
        return (CompositeByteBuf)super.setDouble(index, value);
    }
    
    public CompositeByteBuf setBytes(final int index, final ByteBuf src) {
        return (CompositeByteBuf)super.setBytes(index, src);
    }
    
    public CompositeByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        return (CompositeByteBuf)super.setBytes(index, src, length);
    }
    
    public CompositeByteBuf setBytes(final int index, final byte[] src) {
        return (CompositeByteBuf)super.setBytes(index, src);
    }
    
    public CompositeByteBuf setZero(final int index, final int length) {
        return (CompositeByteBuf)super.setZero(index, length);
    }
    
    public CompositeByteBuf readBytes(final ByteBuf dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }
    
    public CompositeByteBuf readBytes(final ByteBuf dst, final int length) {
        return (CompositeByteBuf)super.readBytes(dst, length);
    }
    
    public CompositeByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
    }
    
    public CompositeByteBuf readBytes(final byte[] dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }
    
    public CompositeByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
    }
    
    public CompositeByteBuf readBytes(final ByteBuffer dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }
    
    public CompositeByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        return (CompositeByteBuf)super.readBytes(out, length);
    }
    
    public CompositeByteBuf skipBytes(final int length) {
        return (CompositeByteBuf)super.skipBytes(length);
    }
    
    public CompositeByteBuf writeBoolean(final boolean value) {
        return (CompositeByteBuf)super.writeBoolean(value);
    }
    
    public CompositeByteBuf writeByte(final int value) {
        return (CompositeByteBuf)super.writeByte(value);
    }
    
    public CompositeByteBuf writeShort(final int value) {
        return (CompositeByteBuf)super.writeShort(value);
    }
    
    public CompositeByteBuf writeMedium(final int value) {
        return (CompositeByteBuf)super.writeMedium(value);
    }
    
    public CompositeByteBuf writeInt(final int value) {
        return (CompositeByteBuf)super.writeInt(value);
    }
    
    public CompositeByteBuf writeLong(final long value) {
        return (CompositeByteBuf)super.writeLong(value);
    }
    
    public CompositeByteBuf writeChar(final int value) {
        return (CompositeByteBuf)super.writeChar(value);
    }
    
    public CompositeByteBuf writeFloat(final float value) {
        return (CompositeByteBuf)super.writeFloat(value);
    }
    
    public CompositeByteBuf writeDouble(final double value) {
        return (CompositeByteBuf)super.writeDouble(value);
    }
    
    public CompositeByteBuf writeBytes(final ByteBuf src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }
    
    public CompositeByteBuf writeBytes(final ByteBuf src, final int length) {
        return (CompositeByteBuf)super.writeBytes(src, length);
    }
    
    public CompositeByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
    }
    
    public CompositeByteBuf writeBytes(final byte[] src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }
    
    public CompositeByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
    }
    
    public CompositeByteBuf writeBytes(final ByteBuffer src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }
    
    public CompositeByteBuf writeZero(final int length) {
        return (CompositeByteBuf)super.writeZero(length);
    }
    
    @Override
    public CompositeByteBuf retain(final int increment) {
        return (CompositeByteBuf)super.retain(increment);
    }
    
    @Override
    public CompositeByteBuf retain() {
        return (CompositeByteBuf)super.retain();
    }
    
    @Override
    public CompositeByteBuf touch() {
        return this;
    }
    
    @Override
    public CompositeByteBuf touch(final Object hint) {
        return this;
    }
    
    public ByteBuffer[] nioBuffers() {
        return this.nioBuffers(this.readerIndex(), this.readableBytes());
    }
    
    public CompositeByteBuf discardSomeReadBytes() {
        return this.discardReadComponents();
    }
    
    @Override
    protected void deallocate() {
        if (this.freed) {
            return;
        }
        this.freed = true;
        for (int size = this.components.size(), i = 0; i < size; ++i) {
            ((Component)this.components.get(i)).freeIfNecessary();
        }
    }
    
    public ByteBuf unwrap() {
        return null;
    }
    
    static {
        EMPTY_NIO_BUFFER = Unpooled.EMPTY_BUFFER.nioBuffer();
        EMPTY_ITERATOR = Collections.emptyList().iterator();
    }
    
    private static final class Component {
        final ByteBuf buf;
        final int length;
        int offset;
        int endOffset;
        
        Component(final ByteBuf buf) {
            this.buf = buf;
            this.length = buf.readableBytes();
        }
        
        void freeIfNecessary() {
            this.buf.release();
        }
    }
    
    private final class CompositeByteBufIterator implements Iterator<ByteBuf> {
        private final int size;
        private int index;
        
        private CompositeByteBufIterator() {
            this.size = CompositeByteBuf.this.components.size();
        }
        
        public boolean hasNext() {
            return this.size > this.index;
        }
        
        public ByteBuf next() {
            if (this.size != CompositeByteBuf.this.components.size()) {
                throw new ConcurrentModificationException();
            }
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                return ((Component)CompositeByteBuf.this.components.get(this.index++)).buf;
            }
            catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Read-Only");
        }
    }
    
    private static final class ComponentList extends ArrayList<Component> {
        ComponentList(final int initialCapacity) {
            super(initialCapacity);
        }
        
        public void removeRange(final int fromIndex, final int toIndex) {
            super.removeRange(fromIndex, toIndex);
        }
    }
}