package net.minecraft.util;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.mojang.serialization.Keyable;
import java.util.Optional;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import java.util.function.Supplier;

public interface StringRepresentable {
    String getSerializedName();
    
    default <E extends Enum> Codec<E> fromEnum(final Supplier<E[]> supplier, final Function<? super String, ? extends E> function) {
        final E[] arr3 = (E[])supplier.get();
        return StringRepresentable.<E>fromStringResolver((java.util.function.ToIntFunction<E>)Enum::ordinal, (java.util.function.IntFunction<E>)(integer -> arr3[integer]), function);
    }
    
    default <E extends StringRepresentable> Codec<E> fromStringResolver(final ToIntFunction<E> toIntFunction, final IntFunction<E> intFunction, final Function<? super String, ? extends E> function) {
        return new Codec<E>() {
            public <T> DataResult<T> encode(final E afp, final DynamicOps<T> dynamicOps, final T object) {
                if (dynamicOps.compressMaps()) {
                    return dynamicOps.mergeToPrimitive(object, dynamicOps.createInt(toIntFunction.applyAsInt(afp)));
                }
                return dynamicOps.mergeToPrimitive(object, dynamicOps.createString(afp.getSerializedName()));
            }
            
            public <T> DataResult<Pair<E, T>> decode(final DynamicOps<T> dynamicOps, final T object) {
                if (dynamicOps.compressMaps()) {
                    return dynamicOps.getNumberValue(object).flatMap((java.util.function.Function<? super Number, ? extends DataResult<Object>>)(number -> (DataResult)Optional.ofNullable(intFunction.apply(number.intValue())).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown element id: ").append(number).toString())))).<Pair<E, T>>map((java.util.function.Function<? super Object, ? extends Pair<E, T>>)(afp -> Pair.<StringRepresentable, Object>of(afp, dynamicOps.empty())));
                }
                return dynamicOps.getStringValue(object).flatMap((java.util.function.Function<? super String, ? extends DataResult<Object>>)(string -> (DataResult)Optional.ofNullable(function.apply(string)).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown element name: " + string)))).<Pair<E, T>>map((java.util.function.Function<? super Object, ? extends Pair<E, T>>)(afp -> Pair.<StringRepresentable, Object>of(afp, dynamicOps.empty())));
            }
            
            public String toString() {
                return new StringBuilder().append("StringRepresentable[").append(toIntFunction).append("]").toString();
            }
        };
    }
    
    default Keyable keys(final StringRepresentable[] arr) {
        return new Keyable() {
            public <T> Stream<T> keys(final DynamicOps<T> dynamicOps) {
                if (dynamicOps.compressMaps()) {
                    return (Stream<T>)IntStream.range(0, arr.length).mapToObj(dynamicOps::createInt);
                }
                return (Stream<T>)Arrays.stream((Object[])arr).map(StringRepresentable::getSerializedName).map(dynamicOps::createString);
            }
        };
    }
}
