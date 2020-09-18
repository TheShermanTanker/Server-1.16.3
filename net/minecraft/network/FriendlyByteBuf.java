package net.minecraft.network;

import io.netty.util.ReferenceCounted;
import io.netty.util.ByteProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import java.util.Date;
import net.minecraft.resources.ResourceLocation;
import java.nio.charset.StandardCharsets;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.io.DataInput;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.nbt.NbtAccounter;
import io.netty.handler.codec.EncoderException;
import java.io.DataOutput;
import net.minecraft.nbt.NbtIo;
import io.netty.buffer.ByteBufOutputStream;
import javax.annotation.Nullable;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import io.netty.handler.codec.DecoderException;
import net.minecraft.nbt.Tag;
import java.io.IOException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;

public class FriendlyByteBuf extends ByteBuf {
    private final ByteBuf source;
    
    public FriendlyByteBuf(final ByteBuf byteBuf) {
        this.source = byteBuf;
    }
    
    public static int getVarIntSize(final int integer) {
        for (int integer2 = 1; integer2 < 5; ++integer2) {
            if ((integer & -1 << integer2 * 7) == 0x0) {
                return integer2;
            }
        }
        return 5;
    }
    
    public <T> T readWithCodec(final Codec<T> codec) throws IOException {
        final CompoundTag md3 = this.readAnySizeNbt();
        final DataResult<T> dataResult4 = codec.<CompoundTag>parse((DynamicOps<CompoundTag>)NbtOps.INSTANCE, md3);
        if (dataResult4.error().isPresent()) {
            throw new IOException("Failed to decode: " + ((DataResult.PartialResult)dataResult4.error().get()).message() + " " + md3);
        }
        return (T)dataResult4.result().get();
    }
    
    public <T> void writeWithCodec(final Codec<T> codec, final T object) throws IOException {
        final DataResult<Tag> dataResult4 = codec.<Tag>encodeStart((DynamicOps<Tag>)NbtOps.INSTANCE, object);
        if (dataResult4.error().isPresent()) {
            throw new IOException("Failed to encode: " + ((DataResult.PartialResult)dataResult4.error().get()).message() + " " + object);
        }
        this.writeNbt((CompoundTag)dataResult4.result().get());
    }
    
    public FriendlyByteBuf writeByteArray(final byte[] arr) {
        this.writeVarInt(arr.length);
        this.writeBytes(arr);
        return this;
    }
    
    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }
    
    public byte[] readByteArray(final int integer) {
        final int integer2 = this.readVarInt();
        if (integer2 > integer) {
            throw new DecoderException(new StringBuilder().append("ByteArray with size ").append(integer2).append(" is bigger than allowed ").append(integer).toString());
        }
        final byte[] arr4 = new byte[integer2];
        this.readBytes(arr4);
        return arr4;
    }
    
    public FriendlyByteBuf writeVarIntArray(final int[] arr) {
        this.writeVarInt(arr.length);
        for (final int integer6 : arr) {
            this.writeVarInt(integer6);
        }
        return this;
    }
    
    public int[] readVarIntArray() {
        return this.readVarIntArray(this.readableBytes());
    }
    
    public int[] readVarIntArray(final int integer) {
        final int integer2 = this.readVarInt();
        if (integer2 > integer) {
            throw new DecoderException(new StringBuilder().append("VarIntArray with size ").append(integer2).append(" is bigger than allowed ").append(integer).toString());
        }
        final int[] arr4 = new int[integer2];
        for (int integer3 = 0; integer3 < arr4.length; ++integer3) {
            arr4[integer3] = this.readVarInt();
        }
        return arr4;
    }
    
    public FriendlyByteBuf writeLongArray(final long[] arr) {
        this.writeVarInt(arr.length);
        for (final long long6 : arr) {
            this.writeLong(long6);
        }
        return this;
    }
    
    public BlockPos readBlockPos() {
        return BlockPos.of(this.readLong());
    }
    
    public FriendlyByteBuf writeBlockPos(final BlockPos fx) {
        this.writeLong(fx.asLong());
        return this;
    }
    
    public Component readComponent() {
        return Component.Serializer.fromJson(this.readUtf(262144));
    }
    
    public FriendlyByteBuf writeComponent(final Component nr) {
        return this.writeUtf(Component.Serializer.toJson(nr), 262144);
    }
    
    public <T extends Enum<T>> T readEnum(final Class<T> class1) {
        return (T)((Enum[])class1.getEnumConstants())[this.readVarInt()];
    }
    
    public FriendlyByteBuf writeEnum(final Enum<?> enum1) {
        return this.writeVarInt(enum1.ordinal());
    }
    
    public int readVarInt() {
        int integer2 = 0;
        int integer3 = 0;
        byte byte4;
        do {
            byte4 = this.readByte();
            integer2 |= (byte4 & 0x7F) << integer3++ * 7;
            if (integer3 > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((byte4 & 0x80) == 0x80);
        return integer2;
    }
    
    public long readVarLong() {
        long long2 = 0L;
        int integer4 = 0;
        byte byte5;
        do {
            byte5 = this.readByte();
            long2 |= (long)(byte5 & 0x7F) << integer4++ * 7;
            if (integer4 > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((byte5 & 0x80) == 0x80);
        return long2;
    }
    
    public FriendlyByteBuf writeUUID(final UUID uUID) {
        this.writeLong(uUID.getMostSignificantBits());
        this.writeLong(uUID.getLeastSignificantBits());
        return this;
    }
    
    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }
    
    public FriendlyByteBuf writeVarInt(int integer) {
        while ((integer & 0xFFFFFF80) != 0x0) {
            this.writeByte((integer & 0x7F) | 0x80);
            integer >>>= 7;
        }
        this.writeByte(integer);
        return this;
    }
    
    public FriendlyByteBuf writeVarLong(long long1) {
        while ((long1 & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
            this.writeByte((int)(long1 & 0x7FL) | 0x80);
            long1 >>>= 7;
        }
        this.writeByte((int)long1);
        return this;
    }
    
    public FriendlyByteBuf writeNbt(@Nullable final CompoundTag md) {
        if (md == null) {
            this.writeByte(0);
        }
        else {
            try {
                NbtIo.write(md, (DataOutput)new ByteBufOutputStream(this));
            }
            catch (IOException iOException3) {
                throw new EncoderException((Throwable)iOException3);
            }
        }
        return this;
    }
    
    @Nullable
    public CompoundTag readNbt() {
        return this.readNbt(new NbtAccounter(2097152L));
    }
    
    @Nullable
    public CompoundTag readAnySizeNbt() {
        return this.readNbt(NbtAccounter.UNLIMITED);
    }
    
    @Nullable
    public CompoundTag readNbt(final NbtAccounter mm) {
        final int integer3 = this.readerIndex();
        final byte byte4 = this.readByte();
        if (byte4 == 0) {
            return null;
        }
        this.readerIndex(integer3);
        try {
            return NbtIo.read((DataInput)new ByteBufInputStream(this), mm);
        }
        catch (IOException iOException5) {
            throw new EncoderException((Throwable)iOException5);
        }
    }
    
    public FriendlyByteBuf writeItem(final ItemStack bly) {
        if (bly.isEmpty()) {
            this.writeBoolean(false);
        }
        else {
            this.writeBoolean(true);
            final Item blu3 = bly.getItem();
            this.writeVarInt(Item.getId(blu3));
            this.writeByte(bly.getCount());
            CompoundTag md4 = null;
            if (blu3.canBeDepleted() || blu3.shouldOverrideMultiplayerNbt()) {
                md4 = bly.getTag();
            }
            this.writeNbt(md4);
        }
        return this;
    }
    
    public ItemStack readItem() {
        if (!this.readBoolean()) {
            return ItemStack.EMPTY;
        }
        final int integer2 = this.readVarInt();
        final int integer3 = this.readByte();
        final ItemStack bly4 = new ItemStack(Item.byId(integer2), integer3);
        bly4.setTag(this.readNbt());
        return bly4;
    }
    
    public String readUtf(final int integer) {
        final int integer2 = this.readVarInt();
        if (integer2 > integer * 4) {
            throw new DecoderException(new StringBuilder().append("The received encoded string buffer length is longer than maximum allowed (").append(integer2).append(" > ").append(integer * 4).append(")").toString());
        }
        if (integer2 < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        final String string4 = this.toString(this.readerIndex(), integer2, StandardCharsets.UTF_8);
        this.readerIndex(this.readerIndex() + integer2);
        if (string4.length() > integer) {
            throw new DecoderException(new StringBuilder().append("The received string length is longer than maximum allowed (").append(integer2).append(" > ").append(integer).append(")").toString());
        }
        return string4;
    }
    
    public FriendlyByteBuf writeUtf(final String string) {
        return this.writeUtf(string, 32767);
    }
    
    public FriendlyByteBuf writeUtf(final String string, final int integer) {
        final byte[] arr4 = string.getBytes(StandardCharsets.UTF_8);
        if (arr4.length > integer) {
            throw new EncoderException(new StringBuilder().append("String too big (was ").append(arr4.length).append(" bytes encoded, max ").append(integer).append(")").toString());
        }
        this.writeVarInt(arr4.length);
        this.writeBytes(arr4);
        return this;
    }
    
    public ResourceLocation readResourceLocation() {
        return new ResourceLocation(this.readUtf(32767));
    }
    
    public FriendlyByteBuf writeResourceLocation(final ResourceLocation vk) {
        this.writeUtf(vk.toString());
        return this;
    }
    
    public Date readDate() {
        return new Date(this.readLong());
    }
    
    public FriendlyByteBuf writeDate(final Date date) {
        this.writeLong(date.getTime());
        return this;
    }
    
    public BlockHitResult readBlockHitResult() {
        final BlockPos fx2 = this.readBlockPos();
        final Direction gc3 = this.<Direction>readEnum(Direction.class);
        final float float4 = this.readFloat();
        final float float5 = this.readFloat();
        final float float6 = this.readFloat();
        final boolean boolean7 = this.readBoolean();
        return new BlockHitResult(new Vec3(fx2.getX() + (double)float4, fx2.getY() + (double)float5, fx2.getZ() + (double)float6), gc3, fx2, boolean7);
    }
    
    public void writeBlockHitResult(final BlockHitResult dcg) {
        final BlockPos fx3 = dcg.getBlockPos();
        this.writeBlockPos(fx3);
        this.writeEnum(dcg.getDirection());
        final Vec3 dck4 = dcg.getLocation();
        this.writeFloat((float)(dck4.x - fx3.getX()));
        this.writeFloat((float)(dck4.y - fx3.getY()));
        this.writeFloat((float)(dck4.z - fx3.getZ()));
        this.writeBoolean(dcg.isInside());
    }
    
    @Override
    public int capacity() {
        return this.source.capacity();
    }
    
    @Override
    public ByteBuf capacity(final int integer) {
        return this.source.capacity(integer);
    }
    
    @Override
    public int maxCapacity() {
        return this.source.maxCapacity();
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.source.alloc();
    }
    
    @Override
    public ByteOrder order() {
        return this.source.order();
    }
    
    @Override
    public ByteBuf order(final ByteOrder byteOrder) {
        return this.source.order(byteOrder);
    }
    
    @Override
    public ByteBuf unwrap() {
        return this.source.unwrap();
    }
    
    @Override
    public boolean isDirect() {
        return this.source.isDirect();
    }
    
    @Override
    public boolean isReadOnly() {
        return this.source.isReadOnly();
    }
    
    @Override
    public ByteBuf asReadOnly() {
        return this.source.asReadOnly();
    }
    
    @Override
    public int readerIndex() {
        return this.source.readerIndex();
    }
    
    @Override
    public ByteBuf readerIndex(final int integer) {
        return this.source.readerIndex(integer);
    }
    
    @Override
    public int writerIndex() {
        return this.source.writerIndex();
    }
    
    @Override
    public ByteBuf writerIndex(final int integer) {
        return this.source.writerIndex(integer);
    }
    
    @Override
    public ByteBuf setIndex(final int integer1, final int integer2) {
        return this.source.setIndex(integer1, integer2);
    }
    
    @Override
    public int readableBytes() {
        return this.source.readableBytes();
    }
    
    @Override
    public int writableBytes() {
        return this.source.writableBytes();
    }
    
    @Override
    public int maxWritableBytes() {
        return this.source.maxWritableBytes();
    }
    
    @Override
    public boolean isReadable() {
        return this.source.isReadable();
    }
    
    @Override
    public boolean isReadable(final int integer) {
        return this.source.isReadable(integer);
    }
    
    @Override
    public boolean isWritable() {
        return this.source.isWritable();
    }
    
    @Override
    public boolean isWritable(final int integer) {
        return this.source.isWritable(integer);
    }
    
    @Override
    public ByteBuf clear() {
        return this.source.clear();
    }
    
    @Override
    public ByteBuf markReaderIndex() {
        return this.source.markReaderIndex();
    }
    
    @Override
    public ByteBuf resetReaderIndex() {
        return this.source.resetReaderIndex();
    }
    
    @Override
    public ByteBuf markWriterIndex() {
        return this.source.markWriterIndex();
    }
    
    @Override
    public ByteBuf resetWriterIndex() {
        return this.source.resetWriterIndex();
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        return this.source.discardReadBytes();
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.source.discardSomeReadBytes();
    }
    
    @Override
    public ByteBuf ensureWritable(final int integer) {
        return this.source.ensureWritable(integer);
    }
    
    @Override
    public int ensureWritable(final int integer, final boolean boolean2) {
        return this.source.ensureWritable(integer, boolean2);
    }
    
    @Override
    public boolean getBoolean(final int integer) {
        return this.source.getBoolean(integer);
    }
    
    @Override
    public byte getByte(final int integer) {
        return this.source.getByte(integer);
    }
    
    @Override
    public short getUnsignedByte(final int integer) {
        return this.source.getUnsignedByte(integer);
    }
    
    @Override
    public short getShort(final int integer) {
        return this.source.getShort(integer);
    }
    
    @Override
    public short getShortLE(final int integer) {
        return this.source.getShortLE(integer);
    }
    
    @Override
    public int getUnsignedShort(final int integer) {
        return this.source.getUnsignedShort(integer);
    }
    
    @Override
    public int getUnsignedShortLE(final int integer) {
        return this.source.getUnsignedShortLE(integer);
    }
    
    @Override
    public int getMedium(final int integer) {
        return this.source.getMedium(integer);
    }
    
    @Override
    public int getMediumLE(final int integer) {
        return this.source.getMediumLE(integer);
    }
    
    @Override
    public int getUnsignedMedium(final int integer) {
        return this.source.getUnsignedMedium(integer);
    }
    
    @Override
    public int getUnsignedMediumLE(final int integer) {
        return this.source.getUnsignedMediumLE(integer);
    }
    
    @Override
    public int getInt(final int integer) {
        return this.source.getInt(integer);
    }
    
    @Override
    public int getIntLE(final int integer) {
        return this.source.getIntLE(integer);
    }
    
    @Override
    public long getUnsignedInt(final int integer) {
        return this.source.getUnsignedInt(integer);
    }
    
    @Override
    public long getUnsignedIntLE(final int integer) {
        return this.source.getUnsignedIntLE(integer);
    }
    
    @Override
    public long getLong(final int integer) {
        return this.source.getLong(integer);
    }
    
    @Override
    public long getLongLE(final int integer) {
        return this.source.getLongLE(integer);
    }
    
    @Override
    public char getChar(final int integer) {
        return this.source.getChar(integer);
    }
    
    @Override
    public float getFloat(final int integer) {
        return this.source.getFloat(integer);
    }
    
    @Override
    public double getDouble(final int integer) {
        return this.source.getDouble(integer);
    }
    
    @Override
    public ByteBuf getBytes(final int integer, final ByteBuf byteBuf) {
        return this.source.getBytes(integer, byteBuf);
    }
    
    @Override
    public ByteBuf getBytes(final int integer1, final ByteBuf byteBuf, final int integer3) {
        return this.source.getBytes(integer1, byteBuf, integer3);
    }
    
    @Override
    public ByteBuf getBytes(final int integer1, final ByteBuf byteBuf, final int integer3, final int integer4) {
        return this.source.getBytes(integer1, byteBuf, integer3, integer4);
    }
    
    @Override
    public ByteBuf getBytes(final int integer, final byte[] arr) {
        return this.source.getBytes(integer, arr);
    }
    
    @Override
    public ByteBuf getBytes(final int integer1, final byte[] arr, final int integer3, final int integer4) {
        return this.source.getBytes(integer1, arr, integer3, integer4);
    }
    
    @Override
    public ByteBuf getBytes(final int integer, final ByteBuffer byteBuffer) {
        return this.source.getBytes(integer, byteBuffer);
    }
    
    @Override
    public ByteBuf getBytes(final int integer1, final OutputStream outputStream, final int integer3) throws IOException {
        return this.source.getBytes(integer1, outputStream, integer3);
    }
    
    @Override
    public int getBytes(final int integer1, final GatheringByteChannel gatheringByteChannel, final int integer3) throws IOException {
        return this.source.getBytes(integer1, gatheringByteChannel, integer3);
    }
    
    @Override
    public int getBytes(final int integer1, final FileChannel fileChannel, final long long3, final int integer4) throws IOException {
        return this.source.getBytes(integer1, fileChannel, long3, integer4);
    }
    
    @Override
    public CharSequence getCharSequence(final int integer1, final int integer2, final Charset charset) {
        return this.source.getCharSequence(integer1, integer2, charset);
    }
    
    @Override
    public ByteBuf setBoolean(final int integer, final boolean boolean2) {
        return this.source.setBoolean(integer, boolean2);
    }
    
    @Override
    public ByteBuf setByte(final int integer1, final int integer2) {
        return this.source.setByte(integer1, integer2);
    }
    
    @Override
    public ByteBuf setShort(final int integer1, final int integer2) {
        return this.source.setShort(integer1, integer2);
    }
    
    @Override
    public ByteBuf setShortLE(final int integer1, final int integer2) {
        return this.source.setShortLE(integer1, integer2);
    }
    
    @Override
    public ByteBuf setMedium(final int integer1, final int integer2) {
        return this.source.setMedium(integer1, integer2);
    }
    
    @Override
    public ByteBuf setMediumLE(final int integer1, final int integer2) {
        return this.source.setMediumLE(integer1, integer2);
    }
    
    @Override
    public ByteBuf setInt(final int integer1, final int integer2) {
        return this.source.setInt(integer1, integer2);
    }
    
    @Override
    public ByteBuf setIntLE(final int integer1, final int integer2) {
        return this.source.setIntLE(integer1, integer2);
    }
    
    @Override
    public ByteBuf setLong(final int integer, final long long2) {
        return this.source.setLong(integer, long2);
    }
    
    @Override
    public ByteBuf setLongLE(final int integer, final long long2) {
        return this.source.setLongLE(integer, long2);
    }
    
    @Override
    public ByteBuf setChar(final int integer1, final int integer2) {
        return this.source.setChar(integer1, integer2);
    }
    
    @Override
    public ByteBuf setFloat(final int integer, final float float2) {
        return this.source.setFloat(integer, float2);
    }
    
    @Override
    public ByteBuf setDouble(final int integer, final double double2) {
        return this.source.setDouble(integer, double2);
    }
    
    @Override
    public ByteBuf setBytes(final int integer, final ByteBuf byteBuf) {
        return this.source.setBytes(integer, byteBuf);
    }
    
    @Override
    public ByteBuf setBytes(final int integer1, final ByteBuf byteBuf, final int integer3) {
        return this.source.setBytes(integer1, byteBuf, integer3);
    }
    
    @Override
    public ByteBuf setBytes(final int integer1, final ByteBuf byteBuf, final int integer3, final int integer4) {
        return this.source.setBytes(integer1, byteBuf, integer3, integer4);
    }
    
    @Override
    public ByteBuf setBytes(final int integer, final byte[] arr) {
        return this.source.setBytes(integer, arr);
    }
    
    @Override
    public ByteBuf setBytes(final int integer1, final byte[] arr, final int integer3, final int integer4) {
        return this.source.setBytes(integer1, arr, integer3, integer4);
    }
    
    @Override
    public ByteBuf setBytes(final int integer, final ByteBuffer byteBuffer) {
        return this.source.setBytes(integer, byteBuffer);
    }
    
    @Override
    public int setBytes(final int integer1, final InputStream inputStream, final int integer3) throws IOException {
        return this.source.setBytes(integer1, inputStream, integer3);
    }
    
    @Override
    public int setBytes(final int integer1, final ScatteringByteChannel scatteringByteChannel, final int integer3) throws IOException {
        return this.source.setBytes(integer1, scatteringByteChannel, integer3);
    }
    
    @Override
    public int setBytes(final int integer1, final FileChannel fileChannel, final long long3, final int integer4) throws IOException {
        return this.source.setBytes(integer1, fileChannel, long3, integer4);
    }
    
    @Override
    public ByteBuf setZero(final int integer1, final int integer2) {
        return this.source.setZero(integer1, integer2);
    }
    
    @Override
    public int setCharSequence(final int integer, final CharSequence charSequence, final Charset charset) {
        return this.source.setCharSequence(integer, charSequence, charset);
    }
    
    @Override
    public boolean readBoolean() {
        return this.source.readBoolean();
    }
    
    @Override
    public byte readByte() {
        return this.source.readByte();
    }
    
    @Override
    public short readUnsignedByte() {
        return this.source.readUnsignedByte();
    }
    
    @Override
    public short readShort() {
        return this.source.readShort();
    }
    
    @Override
    public short readShortLE() {
        return this.source.readShortLE();
    }
    
    @Override
    public int readUnsignedShort() {
        return this.source.readUnsignedShort();
    }
    
    @Override
    public int readUnsignedShortLE() {
        return this.source.readUnsignedShortLE();
    }
    
    @Override
    public int readMedium() {
        return this.source.readMedium();
    }
    
    @Override
    public int readMediumLE() {
        return this.source.readMediumLE();
    }
    
    @Override
    public int readUnsignedMedium() {
        return this.source.readUnsignedMedium();
    }
    
    @Override
    public int readUnsignedMediumLE() {
        return this.source.readUnsignedMediumLE();
    }
    
    @Override
    public int readInt() {
        return this.source.readInt();
    }
    
    @Override
    public int readIntLE() {
        return this.source.readIntLE();
    }
    
    @Override
    public long readUnsignedInt() {
        return this.source.readUnsignedInt();
    }
    
    @Override
    public long readUnsignedIntLE() {
        return this.source.readUnsignedIntLE();
    }
    
    @Override
    public long readLong() {
        return this.source.readLong();
    }
    
    @Override
    public long readLongLE() {
        return this.source.readLongLE();
    }
    
    @Override
    public char readChar() {
        return this.source.readChar();
    }
    
    @Override
    public float readFloat() {
        return this.source.readFloat();
    }
    
    @Override
    public double readDouble() {
        return this.source.readDouble();
    }
    
    @Override
    public ByteBuf readBytes(final int integer) {
        return this.source.readBytes(integer);
    }
    
    @Override
    public ByteBuf readSlice(final int integer) {
        return this.source.readSlice(integer);
    }
    
    @Override
    public ByteBuf readRetainedSlice(final int integer) {
        return this.source.readRetainedSlice(integer);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf byteBuf) {
        return this.source.readBytes(byteBuf);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf byteBuf, final int integer) {
        return this.source.readBytes(byteBuf, integer);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf byteBuf, final int integer2, final int integer3) {
        return this.source.readBytes(byteBuf, integer2, integer3);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] arr) {
        return this.source.readBytes(arr);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] arr, final int integer2, final int integer3) {
        return this.source.readBytes(arr, integer2, integer3);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer byteBuffer) {
        return this.source.readBytes(byteBuffer);
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream outputStream, final int integer) throws IOException {
        return this.source.readBytes(outputStream, integer);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel gatheringByteChannel, final int integer) throws IOException {
        return this.source.readBytes(gatheringByteChannel, integer);
    }
    
    @Override
    public CharSequence readCharSequence(final int integer, final Charset charset) {
        return this.source.readCharSequence(integer, charset);
    }
    
    @Override
    public int readBytes(final FileChannel fileChannel, final long long2, final int integer) throws IOException {
        return this.source.readBytes(fileChannel, long2, integer);
    }
    
    @Override
    public ByteBuf skipBytes(final int integer) {
        return this.source.skipBytes(integer);
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean boolean1) {
        return this.source.writeBoolean(boolean1);
    }
    
    @Override
    public ByteBuf writeByte(final int integer) {
        return this.source.writeByte(integer);
    }
    
    @Override
    public ByteBuf writeShort(final int integer) {
        return this.source.writeShort(integer);
    }
    
    @Override
    public ByteBuf writeShortLE(final int integer) {
        return this.source.writeShortLE(integer);
    }
    
    @Override
    public ByteBuf writeMedium(final int integer) {
        return this.source.writeMedium(integer);
    }
    
    @Override
    public ByteBuf writeMediumLE(final int integer) {
        return this.source.writeMediumLE(integer);
    }
    
    @Override
    public ByteBuf writeInt(final int integer) {
        return this.source.writeInt(integer);
    }
    
    @Override
    public ByteBuf writeIntLE(final int integer) {
        return this.source.writeIntLE(integer);
    }
    
    @Override
    public ByteBuf writeLong(final long long1) {
        return this.source.writeLong(long1);
    }
    
    @Override
    public ByteBuf writeLongLE(final long long1) {
        return this.source.writeLongLE(long1);
    }
    
    @Override
    public ByteBuf writeChar(final int integer) {
        return this.source.writeChar(integer);
    }
    
    @Override
    public ByteBuf writeFloat(final float float1) {
        return this.source.writeFloat(float1);
    }
    
    @Override
    public ByteBuf writeDouble(final double double1) {
        return this.source.writeDouble(double1);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf byteBuf) {
        return this.source.writeBytes(byteBuf);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf byteBuf, final int integer) {
        return this.source.writeBytes(byteBuf, integer);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf byteBuf, final int integer2, final int integer3) {
        return this.source.writeBytes(byteBuf, integer2, integer3);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] arr) {
        return this.source.writeBytes(arr);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] arr, final int integer2, final int integer3) {
        return this.source.writeBytes(arr, integer2, integer3);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer byteBuffer) {
        return this.source.writeBytes(byteBuffer);
    }
    
    @Override
    public int writeBytes(final InputStream inputStream, final int integer) throws IOException {
        return this.source.writeBytes(inputStream, integer);
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel scatteringByteChannel, final int integer) throws IOException {
        return this.source.writeBytes(scatteringByteChannel, integer);
    }
    
    @Override
    public int writeBytes(final FileChannel fileChannel, final long long2, final int integer) throws IOException {
        return this.source.writeBytes(fileChannel, long2, integer);
    }
    
    @Override
    public ByteBuf writeZero(final int integer) {
        return this.source.writeZero(integer);
    }
    
    @Override
    public int writeCharSequence(final CharSequence charSequence, final Charset charset) {
        return this.source.writeCharSequence(charSequence, charset);
    }
    
    @Override
    public int indexOf(final int integer1, final int integer2, final byte byte3) {
        return this.source.indexOf(integer1, integer2, byte3);
    }
    
    @Override
    public int bytesBefore(final byte byte1) {
        return this.source.bytesBefore(byte1);
    }
    
    @Override
    public int bytesBefore(final int integer, final byte byte2) {
        return this.source.bytesBefore(integer, byte2);
    }
    
    @Override
    public int bytesBefore(final int integer1, final int integer2, final byte byte3) {
        return this.source.bytesBefore(integer1, integer2, byte3);
    }
    
    @Override
    public int forEachByte(final ByteProcessor byteProcessor) {
        return this.source.forEachByte(byteProcessor);
    }
    
    @Override
    public int forEachByte(final int integer1, final int integer2, final ByteProcessor byteProcessor) {
        return this.source.forEachByte(integer1, integer2, byteProcessor);
    }
    
    @Override
    public int forEachByteDesc(final ByteProcessor byteProcessor) {
        return this.source.forEachByteDesc(byteProcessor);
    }
    
    @Override
    public int forEachByteDesc(final int integer1, final int integer2, final ByteProcessor byteProcessor) {
        return this.source.forEachByteDesc(integer1, integer2, byteProcessor);
    }
    
    @Override
    public ByteBuf copy() {
        return this.source.copy();
    }
    
    @Override
    public ByteBuf copy(final int integer1, final int integer2) {
        return this.source.copy(integer1, integer2);
    }
    
    @Override
    public ByteBuf slice() {
        return this.source.slice();
    }
    
    @Override
    public ByteBuf retainedSlice() {
        return this.source.retainedSlice();
    }
    
    @Override
    public ByteBuf slice(final int integer1, final int integer2) {
        return this.source.slice(integer1, integer2);
    }
    
    @Override
    public ByteBuf retainedSlice(final int integer1, final int integer2) {
        return this.source.retainedSlice(integer1, integer2);
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.source.duplicate();
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        return this.source.retainedDuplicate();
    }
    
    @Override
    public int nioBufferCount() {
        return this.source.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        return this.source.nioBuffer();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int integer1, final int integer2) {
        return this.source.nioBuffer(integer1, integer2);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int integer1, final int integer2) {
        return this.source.internalNioBuffer(integer1, integer2);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.source.nioBuffers();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int integer1, final int integer2) {
        return this.source.nioBuffers(integer1, integer2);
    }
    
    @Override
    public boolean hasArray() {
        return this.source.hasArray();
    }
    
    @Override
    public byte[] array() {
        return this.source.array();
    }
    
    @Override
    public int arrayOffset() {
        return this.source.arrayOffset();
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return this.source.hasMemoryAddress();
    }
    
    @Override
    public long memoryAddress() {
        return this.source.memoryAddress();
    }
    
    @Override
    public String toString(final Charset charset) {
        return this.source.toString(charset);
    }
    
    @Override
    public String toString(final int integer1, final int integer2, final Charset charset) {
        return this.source.toString(integer1, integer2, charset);
    }
    
    @Override
    public int hashCode() {
        return this.source.hashCode();
    }
    
    @Override
    public boolean equals(final Object object) {
        return this.source.equals(object);
    }
    
    @Override
    public int compareTo(final ByteBuf byteBuf) {
        return this.source.compareTo(byteBuf);
    }
    
    @Override
    public String toString() {
        return this.source.toString();
    }
    
    @Override
    public ByteBuf retain(final int integer) {
        return this.source.retain(integer);
    }
    
    @Override
    public ByteBuf retain() {
        return this.source.retain();
    }
    
    @Override
    public ByteBuf touch() {
        return this.source.touch();
    }
    
    @Override
    public ByteBuf touch(final Object object) {
        return this.source.touch(object);
    }
    
    public int refCnt() {
        return this.source.refCnt();
    }
    
    public boolean release() {
        return this.source.release();
    }
    
    public boolean release(final int integer) {
        return this.source.release(integer);
    }
}
