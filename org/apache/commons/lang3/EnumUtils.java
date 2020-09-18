package org.apache.commons.lang3;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnumUtils {
    private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
    private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
    private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
    private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";
    
    public static <E extends Enum<E>> Map<String, E> getEnumMap(final Class<E> enumClass) {
        final Map<String, E> map = (Map<String, E>)new LinkedHashMap();
        for (final E e : (Enum[])enumClass.getEnumConstants()) {
            map.put(e.name(), e);
        }
        return map;
    }
    
    public static <E extends Enum<E>> List<E> getEnumList(final Class<E> enumClass) {
        return (List<E>)new ArrayList((Collection)Arrays.asList(enumClass.getEnumConstants()));
    }
    
    public static <E extends Enum<E>> boolean isValidEnum(final Class<E> enumClass, final String enumName) {
        if (enumName == null) {
            return false;
        }
        try {
            Enum.valueOf((Class)enumClass, enumName);
            return true;
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
    }
    
    public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName) {
        if (enumName == null) {
            return null;
        }
        try {
            return (E)Enum.valueOf((Class)enumClass, enumName);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    
    public static <E extends Enum<E>> long generateBitVector(final Class<E> enumClass, final Iterable<? extends E> values) {
        EnumUtils.<E>checkBitVectorable(enumClass);
        Validate.<Iterable<? extends E>>notNull(values);
        long total = 0L;
        for (final E constant : values) {
            Validate.isTrue(constant != null, "null elements not permitted");
            total |= 1L << constant.ordinal();
        }
        return total;
    }
    
    public static <E extends Enum<E>> long[] generateBitVectors(final Class<E> enumClass, final Iterable<? extends E> values) {
        EnumUtils.<E>asEnum(enumClass);
        Validate.<Iterable<? extends E>>notNull(values);
        final EnumSet<E> condensed = (EnumSet<E>)EnumSet.noneOf((Class)enumClass);
        for (final E constant : values) {
            Validate.isTrue(constant != null, "null elements not permitted");
            condensed.add(constant);
        }
        final long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
        for (final E value : condensed) {
            final long[] array = result;
            final int n = value.ordinal() / 64;
            array[n] |= 1L << value.ordinal() % 64;
        }
        ArrayUtils.reverse(result);
        return result;
    }
    
    public static <E extends Enum<E>> long generateBitVector(final Class<E> enumClass, final E... values) {
        Validate.<E>noNullElements(values);
        return EnumUtils.<E>generateBitVector(enumClass, (java.lang.Iterable<? extends E>)Arrays.asList((Object[])values));
    }
    
    public static <E extends Enum<E>> long[] generateBitVectors(final Class<E> enumClass, final E... values) {
        EnumUtils.<E>asEnum(enumClass);
        Validate.<E>noNullElements(values);
        final EnumSet<E> condensed = (EnumSet<E>)EnumSet.noneOf((Class)enumClass);
        Collections.addAll((Collection)condensed, (Object[])values);
        final long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
        for (final E value : condensed) {
            final long[] array = result;
            final int n = value.ordinal() / 64;
            array[n] |= 1L << value.ordinal() % 64;
        }
        ArrayUtils.reverse(result);
        return result;
    }
    
    public static <E extends Enum<E>> EnumSet<E> processBitVector(final Class<E> enumClass, final long value) {
        EnumUtils.<E>checkBitVectorable(enumClass).getEnumConstants();
        return EnumUtils.<E>processBitVectors(enumClass, value);
    }
    
    public static <E extends Enum<E>> EnumSet<E> processBitVectors(final Class<E> enumClass, final long... values) {
        final EnumSet<E> results = (EnumSet<E>)EnumSet.noneOf((Class)EnumUtils.<E>asEnum(enumClass));
        final long[] lvalues = ArrayUtils.clone(Validate.<long[]>notNull(values));
        ArrayUtils.reverse(lvalues);
        for (final E constant : (Enum[])enumClass.getEnumConstants()) {
            final int block = constant.ordinal() / 64;
            if (block < lvalues.length && (lvalues[block] & 1L << constant.ordinal() % 64) != 0x0L) {
                results.add(constant);
            }
        }
        return results;
    }
    
    private static <E extends Enum<E>> Class<E> checkBitVectorable(final Class<E> enumClass) {
        final E[] constants = (E[])EnumUtils.<Enum>asEnum((java.lang.Class<Enum>)enumClass).getEnumConstants();
        Validate.isTrue(constants.length <= 64, "Cannot store %s %s values in %s bits", constants.length, enumClass.getSimpleName(), 64);
        return enumClass;
    }
    
    private static <E extends Enum<E>> Class<E> asEnum(final Class<E> enumClass) {
        Validate.<Class<E>>notNull(enumClass, "EnumClass must be defined.", new Object[0]);
        Validate.isTrue(enumClass.isEnum(), "%s does not seem to be an Enum type", enumClass);
        return enumClass;
    }
}
