package net.minecraft.nbt;

import java.util.Map;
import com.google.common.collect.Maps;
import java.util.Objects;
import com.mojang.serialization.RecordBuilder;
import com.google.common.collect.PeekingIterator;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import com.mojang.datafixers.DataFixUtils;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapLike;
import java.util.List;
import com.mojang.serialization.DataResult;
import java.util.Arrays;
import java.nio.ByteBuffer;
import com.mojang.serialization.DynamicOps;

public class NbtOps implements DynamicOps<Tag> {
    public static final NbtOps INSTANCE;
    
    protected NbtOps() {
    }
    
    public Tag empty() {
        return EndTag.INSTANCE;
    }
    
    public <U> U convertTo(final DynamicOps<U> dynamicOps, final Tag mt) {
        switch (mt.getId()) {
            case 0: {
                return dynamicOps.empty();
            }
            case 1: {
                return dynamicOps.createByte(((NumericTag)mt).getAsByte());
            }
            case 2: {
                return dynamicOps.createShort(((NumericTag)mt).getAsShort());
            }
            case 3: {
                return dynamicOps.createInt(((NumericTag)mt).getAsInt());
            }
            case 4: {
                return dynamicOps.createLong(((NumericTag)mt).getAsLong());
            }
            case 5: {
                return dynamicOps.createFloat(((NumericTag)mt).getAsFloat());
            }
            case 6: {
                return dynamicOps.createDouble(((NumericTag)mt).getAsDouble());
            }
            case 7: {
                return dynamicOps.createByteList(ByteBuffer.wrap(((ByteArrayTag)mt).getAsByteArray()));
            }
            case 8: {
                return dynamicOps.createString(mt.getAsString());
            }
            case 9: {
                return this.<U>convertList(dynamicOps, mt);
            }
            case 10: {
                return this.<U>convertMap(dynamicOps, mt);
            }
            case 11: {
                return dynamicOps.createIntList(Arrays.stream(((IntArrayTag)mt).getAsIntArray()));
            }
            case 12: {
                return dynamicOps.createLongList(Arrays.stream(((LongArrayTag)mt).getAsLongArray()));
            }
            default: {
                throw new IllegalStateException(new StringBuilder().append("Unknown tag type: ").append(mt).toString());
            }
        }
    }
    
    public DataResult<Number> getNumberValue(final Tag mt) {
        if (mt instanceof NumericTag) {
            return DataResult.<Number>success(((NumericTag)mt).getAsNumber());
        }
        return DataResult.<Number>error("Not a number");
    }
    
    public Tag createNumeric(final Number number) {
        return DoubleTag.valueOf(number.doubleValue());
    }
    
    public Tag createByte(final byte byte1) {
        return ByteTag.valueOf(byte1);
    }
    
    public Tag createShort(final short short1) {
        return ShortTag.valueOf(short1);
    }
    
    public Tag createInt(final int integer) {
        return IntTag.valueOf(integer);
    }
    
    public Tag createLong(final long long1) {
        return LongTag.valueOf(long1);
    }
    
    public Tag createFloat(final float float1) {
        return FloatTag.valueOf(float1);
    }
    
    public Tag createDouble(final double double1) {
        return DoubleTag.valueOf(double1);
    }
    
    public Tag createBoolean(final boolean boolean1) {
        return ByteTag.valueOf(boolean1);
    }
    
    public DataResult<String> getStringValue(final Tag mt) {
        if (mt instanceof StringTag) {
            return DataResult.<String>success(mt.getAsString());
        }
        return DataResult.<String>error("Not a string");
    }
    
    public Tag createString(final String string) {
        return StringTag.valueOf(string);
    }
    
    private static CollectionTag<?> createGenericList(final byte byte1, final byte byte2) {
        if (typesMatch(byte1, byte2, (byte)4)) {
            return new LongArrayTag(new long[0]);
        }
        if (typesMatch(byte1, byte2, (byte)1)) {
            return new ByteArrayTag(new byte[0]);
        }
        if (typesMatch(byte1, byte2, (byte)3)) {
            return new IntArrayTag(new int[0]);
        }
        return new ListTag();
    }
    
    private static boolean typesMatch(final byte byte1, final byte byte2, final byte byte3) {
        return byte1 == byte3 && (byte2 == byte3 || byte2 == 0);
    }
    
    private static <T extends Tag> void fillOne(final CollectionTag<T> mc, final Tag mt2, final Tag mt3) {
        if (mt2 instanceof CollectionTag) {
            final CollectionTag<?> mc2 = mt2;
            mc2.forEach(mt -> mc.add(mt));
        }
        mc.add(mt3);
    }
    
    private static <T extends Tag> void fillMany(final CollectionTag<T> mc, final Tag mt, final List<Tag> list) {
        if (mt instanceof CollectionTag) {
            final CollectionTag<?> mc2 = mt;
            mc2.forEach(mt -> mc.add(mt));
        }
        list.forEach(mt -> mc.add(mt));
    }
    
    public DataResult<Tag> mergeToList(final Tag mt1, final Tag mt2) {
        if (!(mt1 instanceof CollectionTag) && !(mt1 instanceof EndTag)) {
            return DataResult.<Tag>error(new StringBuilder().append("mergeToList called with not a list: ").append(mt1).toString(), mt1);
        }
        final CollectionTag<?> mc4 = createGenericList((mt1 instanceof CollectionTag) ? ((CollectionTag)mt1).getElementType() : 0, mt2.getId());
        NbtOps.fillOne(mc4, mt1, mt2);
        return DataResult.success(mc4);
    }
    
    public DataResult<Tag> mergeToList(final Tag mt, final List<Tag> list) {
        if (!(mt instanceof CollectionTag) && !(mt instanceof EndTag)) {
            return DataResult.<Tag>error(new StringBuilder().append("mergeToList called with not a list: ").append(mt).toString(), mt);
        }
        final CollectionTag<?> mc4 = createGenericList((mt instanceof CollectionTag) ? ((CollectionTag)mt).getElementType() : 0, (byte)list.stream().findFirst().map(Tag::getId).orElse(0));
        NbtOps.fillMany(mc4, mt, list);
        return DataResult.success(mc4);
    }
    
    public DataResult<Tag> mergeToMap(final Tag mt1, final Tag mt2, final Tag mt3) {
        if (!(mt1 instanceof CompoundTag) && !(mt1 instanceof EndTag)) {
            return DataResult.<Tag>error(new StringBuilder().append("mergeToMap called with not a map: ").append(mt1).toString(), mt1);
        }
        if (!(mt2 instanceof StringTag)) {
            return DataResult.<Tag>error(new StringBuilder().append("key is not a string: ").append(mt2).toString(), mt1);
        }
        final CompoundTag md5 = new CompoundTag();
        if (mt1 instanceof CompoundTag) {
            final CompoundTag md6 = (CompoundTag)mt1;
            md6.getAllKeys().forEach(string -> md5.put(string, md6.get(string)));
        }
        md5.put(mt2.getAsString(), mt3);
        return DataResult.success(md5);
    }
    
    public DataResult<Tag> mergeToMap(final Tag mt, final MapLike<Tag> mapLike) {
        if (!(mt instanceof CompoundTag) && !(mt instanceof EndTag)) {
            return DataResult.<Tag>error(new StringBuilder().append("mergeToMap called with not a map: ").append(mt).toString(), mt);
        }
        final CompoundTag md4 = new CompoundTag();
        if (mt instanceof CompoundTag) {
            final CompoundTag md5 = (CompoundTag)mt;
            md5.getAllKeys().forEach(string -> md4.put(string, md5.get(string)));
        }
        final List<Tag> list5 = Lists.newArrayList();
        mapLike.entries().forEach(pair -> {
            final Tag mt4 = pair.getFirst();
            if (!(mt4 instanceof StringTag)) {
                list5.add(mt4);
                return;
            }
            md4.put(mt4.getAsString(), (Tag)pair.getSecond());
        });
        if (!list5.isEmpty()) {
            return DataResult.error(new StringBuilder().append("some keys are not strings: ").append(list5).toString(), md4);
        }
        return DataResult.success(md4);
    }
    
    public DataResult<Stream<Pair<Tag, Tag>>> getMapValues(final Tag mt) {
        if (!(mt instanceof CompoundTag)) {
            return DataResult.<Stream<Pair<Tag, Tag>>>error(new StringBuilder().append("Not a map: ").append(mt).toString());
        }
        final CompoundTag md3 = (CompoundTag)mt;
        return DataResult.<Stream<Pair<Tag, Tag>>>success(md3.getAllKeys().stream().map(string -> Pair.<Tag, Tag>of(this.a(string), md3.get(string))));
    }
    
    public DataResult<Consumer<BiConsumer<Tag, Tag>>> getMapEntries(final Tag mt) {
        if (!(mt instanceof CompoundTag)) {
            return DataResult.<Consumer<BiConsumer<Tag, Tag>>>error(new StringBuilder().append("Not a map: ").append(mt).toString());
        }
        final CompoundTag md3 = (CompoundTag)mt;
        return DataResult.<Consumer<BiConsumer<Tag, Tag>>>success((Consumer<BiConsumer<Tag, Tag>>)(biConsumer -> md3.getAllKeys().forEach(string -> biConsumer.accept(this.a(string), md3.get(string)))));
    }
    
    public DataResult<MapLike<Tag>> getMap(final Tag mt) {
        if (!(mt instanceof CompoundTag)) {
            return DataResult.<MapLike<Tag>>error(new StringBuilder().append("Not a map: ").append(mt).toString());
        }
        final CompoundTag md3 = (CompoundTag)mt;
        return DataResult.success(new MapLike<Tag>() {
            @Nullable
            public Tag get(final Tag mt) {
                return md3.get(mt.getAsString());
            }
            
            @Nullable
            public Tag get(final String string) {
                return md3.get(string);
            }
            
            public Stream<Pair<Tag, Tag>> entries() {
                return (Stream<Pair<Tag, Tag>>)md3.getAllKeys().stream().map(string -> Pair.<Tag, Tag>of(NbtOps.this.a(string), md.get(string)));
            }
            
            public String toString() {
                return new StringBuilder().append("MapLike[").append(md3).append("]").toString();
            }
        });
    }
    
    public Tag createMap(final Stream<Pair<Tag, Tag>> stream) {
        final CompoundTag md3 = new CompoundTag();
        stream.forEach(pair -> md3.put(pair.getFirst().getAsString(), (Tag)pair.getSecond()));
        return md3;
    }
    
    public DataResult<Stream<Tag>> getStream(final Tag mt) {
        if (mt instanceof CollectionTag) {
            return DataResult.<Stream<Tag>>success(((CollectionTag)mt).stream().map(mt -> mt));
        }
        return DataResult.<Stream<Tag>>error("Not a list");
    }
    
    public DataResult<Consumer<Consumer<Tag>>> getList(final Tag mt) {
        if (mt instanceof CollectionTag) {
            final CollectionTag<?> mc3 = mt;
            return DataResult.<Consumer<Consumer<Tag>>>success((Consumer<Consumer<Tag>>)mc3::forEach);
        }
        return DataResult.<Consumer<Consumer<Tag>>>error(new StringBuilder().append("Not a list: ").append(mt).toString());
    }
    
    public DataResult<ByteBuffer> getByteBuffer(final Tag mt) {
        if (mt instanceof ByteArrayTag) {
            return DataResult.<ByteBuffer>success(ByteBuffer.wrap(((ByteArrayTag)mt).getAsByteArray()));
        }
        return super.getByteBuffer(mt);
    }
    
    public Tag createByteList(final ByteBuffer byteBuffer) {
        return new ByteArrayTag(DataFixUtils.toArray(byteBuffer));
    }
    
    public DataResult<IntStream> getIntStream(final Tag mt) {
        if (mt instanceof IntArrayTag) {
            return DataResult.<IntStream>success(Arrays.stream(((IntArrayTag)mt).getAsIntArray()));
        }
        return super.getIntStream(mt);
    }
    
    public Tag createIntList(final IntStream intStream) {
        return new IntArrayTag(intStream.toArray());
    }
    
    public DataResult<LongStream> getLongStream(final Tag mt) {
        if (mt instanceof LongArrayTag) {
            return DataResult.<LongStream>success(Arrays.stream(((LongArrayTag)mt).getAsLongArray()));
        }
        return super.getLongStream(mt);
    }
    
    public Tag createLongList(final LongStream longStream) {
        return new LongArrayTag(longStream.toArray());
    }
    
    public Tag createList(final Stream<Tag> stream) {
        final PeekingIterator<Tag> peekingIterator3 = Iterators.<Tag>peekingIterator((java.util.Iterator<? extends Tag>)stream.iterator());
        if (!peekingIterator3.hasNext()) {
            return new ListTag();
        }
        final Tag mt2 = peekingIterator3.peek();
        if (mt2 instanceof ByteTag) {
            final List<Byte> list5 = Lists.newArrayList(Iterators.transform((java.util.Iterator<Object>)peekingIterator3, mt -> mt.getAsByte()));
            return new ByteArrayTag(list5);
        }
        if (mt2 instanceof IntTag) {
            final List<Integer> list6 = Lists.newArrayList(Iterators.transform((java.util.Iterator<Object>)peekingIterator3, mt -> mt.getAsInt()));
            return new IntArrayTag(list6);
        }
        if (mt2 instanceof LongTag) {
            final List<Long> list7 = Lists.newArrayList(Iterators.transform((java.util.Iterator<Object>)peekingIterator3, mt -> mt.getAsLong()));
            return new LongArrayTag(list7);
        }
        final ListTag mj5 = new ListTag();
        while (peekingIterator3.hasNext()) {
            final Tag mt3 = peekingIterator3.next();
            if (mt3 instanceof EndTag) {
                continue;
            }
            mj5.add(mt3);
        }
        return mj5;
    }
    
    public Tag remove(final Tag mt, final String string) {
        if (mt instanceof CompoundTag) {
            final CompoundTag md4 = (CompoundTag)mt;
            final CompoundTag md5 = new CompoundTag();
            md4.getAllKeys().stream().filter(string2 -> !Objects.equals(string2, string)).forEach(string -> md5.put(string, md4.get(string)));
            return md5;
        }
        return mt;
    }
    
    public String toString() {
        return "NBT";
    }
    
    public RecordBuilder<Tag> mapBuilder() {
        return new NbtRecordBuilder();
    }
    
    static {
        INSTANCE = new NbtOps();
    }
    
    class NbtRecordBuilder extends RecordBuilder.AbstractStringBuilder<Tag, CompoundTag> {
        protected NbtRecordBuilder() {
            super(NbtOps.this);
        }
        
        @Override
        protected CompoundTag initBuilder() {
            return new CompoundTag();
        }
        
        @Override
        protected CompoundTag append(final String string, final Tag mt, final CompoundTag md) {
            md.put(string, mt);
            return md;
        }
        
        @Override
        protected DataResult<Tag> build(final CompoundTag md, final Tag mt) {
            if (mt == null || mt == EndTag.INSTANCE) {
                return DataResult.success(md);
            }
            if (mt instanceof CompoundTag) {
                final CompoundTag md2 = new CompoundTag(Maps.newHashMap((java.util.Map<?, ?>)((CompoundTag)mt).entries()));
                for (final Map.Entry<String, Tag> entry6 : md.entries().entrySet()) {
                    md2.put((String)entry6.getKey(), (Tag)entry6.getValue());
                }
                return DataResult.success(md2);
            }
            return DataResult.<Tag>error(new StringBuilder().append("mergeToMap called with not a map: ").append(mt).toString(), mt);
        }
    }
}
