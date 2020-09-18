package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Random;
import java.util.Comparator;
import java.util.Objects;
import java.util.Arrays;
import java.lang.reflect.Array;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.Hash;

public final class ObjectBigArrays {
    public static final Object[][] EMPTY_BIG_ARRAY;
    public static final Object[][] DEFAULT_EMPTY_BIG_ARRAY;
    public static final Hash.Strategy HASH_STRATEGY;
    private static final int SMALL = 7;
    private static final int MEDIUM = 40;
    
    private ObjectBigArrays() {
    }
    
    public static <K> K get(final K[][] array, final long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }
    
    public static <K> void set(final K[][] array, final long index, final K value) {
        array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
    }
    
    public static <K> void swap(final K[][] array, final long first, final long second) {
        final K t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
    }
    
    public static <K> long length(final K[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (BigArrays.start(length - 1) + array[length - 1].length);
    }
    
    public static <K> void copy(final K[][] srcArray, final long srcPos, final K[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, (long)Math.min(srcDispl, destDispl));
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static <K> void copyFromBig(final K[][] srcArray, final long srcPos, final K[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static <K> void copyToBig(final K[] srcArray, int srcPos, final K[][] destArray, final long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static <K> K[][] newBigArray(final K[][] prototype, final long length) {
        return (K[][])newBigArray(prototype.getClass().getComponentType(), length);
    }
    
    private static Object[][] newBigArray(final Class<?> componentType, final long length) {
        if (length == 0L && componentType == Object[].class) {
            return ObjectBigArrays.EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final Object[][] base = (Object[][])Array.newInstance((Class)componentType, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; ++i) {
                base[i] = (Object[])Array.newInstance(componentType.getComponentType(), 134217728);
            }
            base[baseLength - 1] = (Object[])Array.newInstance(componentType.getComponentType(), residual);
        }
        else {
            for (int i = 0; i < baseLength; ++i) {
                base[i] = (Object[])Array.newInstance(componentType.getComponentType(), 134217728);
            }
        }
        return base;
    }
    
    public static Object[][] newBigArray(final long length) {
        if (length == 0L) {
            return ObjectBigArrays.EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final Object[][] base = new Object[baseLength][];
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; ++i) {
                base[i] = new Object[134217728];
            }
            base[baseLength - 1] = new Object[residual];
        }
        else {
            for (int i = 0; i < baseLength; ++i) {
                base[i] = new Object[134217728];
            }
        }
        return base;
    }
    
    public static <K> K[][] wrap(final K[] array) {
        if (array.length == 0 && array.getClass() == Object[].class) {
            return (K[][])ObjectBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            final K[][] bigArray = (K[][])Array.newInstance(array.getClass(), 1);
            bigArray[0] = array;
            return bigArray;
        }
        final K[][] bigArray = (K[][])newBigArray(array.getClass(), array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)BigArrays.start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static <K> K[][] ensureCapacity(final K[][] array, final long length) {
        return ObjectBigArrays.<K>ensureCapacity(array, length, ObjectBigArrays.<K>length(array));
    }
    
    public static <K> K[][] forceCapacity(final K[][] array, final long length, final long preserve) {
        BigArrays.ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final K[][] base = (K[][])Arrays.copyOf((Object[])array, baseLength);
        final Class<?> componentType = array.getClass().getComponentType();
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = (K[])Array.newInstance(componentType.getComponentType(), 134217728);
            }
            base[baseLength - 1] = (K[])Array.newInstance(componentType.getComponentType(), residual);
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = (K[])Array.newInstance(componentType.getComponentType(), 134217728);
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            ObjectBigArrays.<K>copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static <K> K[][] ensureCapacity(final K[][] array, final long length, final long preserve) {
        return (K[][])((length > ObjectBigArrays.<K>length(array)) ? ObjectBigArrays.forceCapacity((Object[][])array, length, preserve) : array);
    }
    
    public static <K> K[][] grow(final K[][] array, final long length) {
        final long oldLength = ObjectBigArrays.<K>length(array);
        return (length > oldLength) ? ObjectBigArrays.<K>grow(array, length, oldLength) : array;
    }
    
    public static <K> K[][] grow(final K[][] array, final long length, final long preserve) {
        final long oldLength = ObjectBigArrays.<K>length(array);
        return (K[][])((length > oldLength) ? ObjectBigArrays.ensureCapacity((Object[][])array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array);
    }
    
    public static <K> K[][] trim(final K[][] array, final long length) {
        BigArrays.ensureLength(length);
        final long oldLength = ObjectBigArrays.<K>length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final K[][] base = (K[][])Arrays.copyOf((Object[])array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ObjectArrays.<K>trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static <K> K[][] setLength(final K[][] array, final long length) {
        final long oldLength = ObjectBigArrays.<K>length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return (K[][])ObjectBigArrays.trim((Object[][])array, length);
        }
        return (K[][])ObjectBigArrays.ensureCapacity((Object[][])array, length);
    }
    
    public static <K> K[][] copy(final K[][] array, final long offset, final long length) {
        ObjectBigArrays.<K>ensureOffsetLength(array, offset, length);
        final K[][] a = ObjectBigArrays.<K>newBigArray(array, length);
        ObjectBigArrays.<K>copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static <K> K[][] copy(final K[][] array) {
        final K[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static <K> void fill(final K[][] array, final K value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill((Object[])array[i], value);
        }
    }
    
    public static <K> void fill(final K[][] array, final long from, final long to, final K value) {
        final long length = ObjectBigArrays.<K>length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        final int fromDispl = BigArrays.displacement(from);
        final int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill((Object[])array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill((Object[])array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill((Object[])array[toSegment], value);
        }
        Arrays.fill((Object[])array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static <K> boolean equals(final K[][] a1, final K[][] a2) {
        if (ObjectBigArrays.<K>length(a1) != ObjectBigArrays.<K>length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final K[] t = a1[i];
            final K[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (!Objects.equals(t[j], u[j])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static <K> String toString(final K[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = ObjectBigArrays.<K>length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(ObjectBigArrays.<K>get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static <K> void ensureFromTo(final K[][] a, final long from, final long to) {
        BigArrays.ensureFromTo(ObjectBigArrays.<K>length(a), from, to);
    }
    
    public static <K> void ensureOffsetLength(final K[][] a, final long offset, final long length) {
        BigArrays.ensureOffsetLength(ObjectBigArrays.<K>length(a), offset, length);
    }
    
    private static <K> void vecSwap(final K[][] x, long a, long b, final long n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            ObjectBigArrays.<K>swap(x, a, b);
        }
    }
    
    private static <K> long med3(final K[][] x, final long a, final long b, final long c, final Comparator<K> comp) {
        final int ab = comp.compare(ObjectBigArrays.<K>get(x, a), ObjectBigArrays.<K>get(x, b));
        final int ac = comp.compare(ObjectBigArrays.<K>get(x, a), ObjectBigArrays.<K>get(x, c));
        final int bc = comp.compare(ObjectBigArrays.<K>get(x, b), ObjectBigArrays.<K>get(x, c));
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static <K> void selectionSort(final K[][] a, final long from, final long to, final Comparator<K> comp) {
        for (long i = from; i < to - 1L; ++i) {
            long m = i;
            for (long j = i + 1L; j < to; ++j) {
                if (comp.compare(ObjectBigArrays.<K>get(a, j), ObjectBigArrays.<K>get(a, m)) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                ObjectBigArrays.<K>swap(a, i, m);
            }
        }
    }
    
    public static <K> void quickSort(final K[][] x, final long from, final long to, final Comparator<K> comp) {
        final long len = to - from;
        if (len < 7L) {
            ObjectBigArrays.selectionSort(x, from, to, (java.util.Comparator<Object>)comp);
            return;
        }
        long m = from + len / 2L;
        if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
                final long s = len / 8L;
                l = ObjectBigArrays.<K>med3(x, l, l + s, l + 2L * s, comp);
                m = ObjectBigArrays.<K>med3(x, m - s, m, m + s, comp);
                n = ObjectBigArrays.<K>med3(x, n - 2L * s, n - s, n, comp);
            }
            m = ObjectBigArrays.<K>med3(x, l, m, n, comp);
        }
        final K v = ObjectBigArrays.<K>get(x, m);
        long b;
        long a = b = from;
        long d;
        long c = d = to - 1L;
        while (true) {
            int comparison;
            if (b <= c && (comparison = comp.compare(ObjectBigArrays.<K>get(x, b), v)) <= 0) {
                if (comparison == 0) {
                    ObjectBigArrays.<K>swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = comp.compare(ObjectBigArrays.<K>get(x, c), v)) >= 0) {
                    if (comparison == 0) {
                        ObjectBigArrays.<K>swap(x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                ObjectBigArrays.<K>swap(x, b++, c--);
            }
        }
        final long n2 = to;
        long s2 = Math.min(a - from, b - a);
        ObjectBigArrays.<K>vecSwap(x, from, b - s2, s2);
        s2 = Math.min(d - c, n2 - d - 1L);
        ObjectBigArrays.<K>vecSwap(x, b, n2 - s2, s2);
        if ((s2 = b - a) > 1L) {
            ObjectBigArrays.quickSort(x, from, from + s2, (java.util.Comparator<Object>)comp);
        }
        if ((s2 = d - c) > 1L) {
            ObjectBigArrays.quickSort(x, n2 - s2, n2, (java.util.Comparator<Object>)comp);
        }
    }
    
    private static <K> long med3(final K[][] x, final long a, final long b, final long c) {
        final int ab = ObjectBigArrays.<Comparable>get((Comparable[][])x, a).compareTo(ObjectBigArrays.<K>get(x, b));
        final int ac = ObjectBigArrays.<Comparable>get((Comparable[][])x, a).compareTo(ObjectBigArrays.<K>get(x, c));
        final int bc = ObjectBigArrays.<Comparable>get((Comparable[][])x, b).compareTo(ObjectBigArrays.<K>get(x, c));
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static <K> void selectionSort(final K[][] a, final long from, final long to) {
        for (long i = from; i < to - 1L; ++i) {
            long m = i;
            for (long j = i + 1L; j < to; ++j) {
                if (ObjectBigArrays.<Comparable>get((Comparable[][])a, j).compareTo(ObjectBigArrays.<K>get(a, m)) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                ObjectBigArrays.swap((Object[][])a, i, m);
            }
        }
    }
    
    public static <K> void quickSort(final K[][] x, final Comparator<K> comp) {
        ObjectBigArrays.<K>quickSort(x, 0L, ObjectBigArrays.<K>length(x), comp);
    }
    
    public static <K> void quickSort(final K[][] x, final long from, final long to) {
        final long len = to - from;
        if (len < 7L) {
            ObjectBigArrays.selectionSort((Object[][])x, from, to);
            return;
        }
        long m = from + len / 2L;
        if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
                final long s = len / 8L;
                l = ObjectBigArrays.<K>med3(x, l, l + s, l + 2L * s);
                m = ObjectBigArrays.<K>med3(x, m - s, m, m + s);
                n = ObjectBigArrays.<K>med3(x, n - 2L * s, n - s, n);
            }
            m = ObjectBigArrays.<K>med3(x, l, m, n);
        }
        final K v = ObjectBigArrays.<K>get(x, m);
        long b;
        long a = b = from;
        long d;
        long c = d = to - 1L;
        while (true) {
            int comparison;
            if (b <= c && (comparison = ObjectBigArrays.<Comparable>get((Comparable[][])x, b).compareTo(v)) <= 0) {
                if (comparison == 0) {
                    ObjectBigArrays.swap((Object[][])x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = ObjectBigArrays.<Comparable>get((Comparable[][])x, c).compareTo(v)) >= 0) {
                    if (comparison == 0) {
                        ObjectBigArrays.swap((Object[][])x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                ObjectBigArrays.swap((Object[][])x, b++, c--);
            }
        }
        final long n2 = to;
        long s2 = Math.min(a - from, b - a);
        ObjectBigArrays.<K>vecSwap(x, from, b - s2, s2);
        s2 = Math.min(d - c, n2 - d - 1L);
        ObjectBigArrays.<K>vecSwap(x, b, n2 - s2, s2);
        if ((s2 = b - a) > 1L) {
            ObjectBigArrays.quickSort((Object[][])x, from, from + s2);
        }
        if ((s2 = d - c) > 1L) {
            ObjectBigArrays.quickSort((Object[][])x, n2 - s2, n2);
        }
    }
    
    public static <K> void quickSort(final K[][] x) {
        ObjectBigArrays.<K>quickSort(x, 0L, ObjectBigArrays.<K>length(x));
    }
    
    public static <K> long binarySearch(final K[][] a, long from, long to, final K key) {
        --to;
        while (from <= to) {
            final long mid = from + to >>> 1;
            final K midVal = ObjectBigArrays.<K>get(a, mid);
            final int cmp = ((Comparable)midVal).compareTo(key);
            if (cmp < 0) {
                from = mid + 1L;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                to = mid - 1L;
            }
        }
        return -(from + 1L);
    }
    
    public static <K> long binarySearch(final K[][] a, final Object key) {
        return ObjectBigArrays.binarySearch(a, 0L, ObjectBigArrays.<K>length(a), key);
    }
    
    public static <K> long binarySearch(final K[][] a, long from, long to, final K key, final Comparator<K> c) {
        --to;
        while (from <= to) {
            final long mid = from + to >>> 1;
            final K midVal = ObjectBigArrays.<K>get(a, mid);
            final int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                from = mid + 1L;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                to = mid - 1L;
            }
        }
        return -(from + 1L);
    }
    
    public static <K> long binarySearch(final K[][] a, final K key, final Comparator<K> c) {
        return ObjectBigArrays.<K>binarySearch(a, 0L, ObjectBigArrays.<K>length(a), key, c);
    }
    
    public static <K> K[][] shuffle(final K[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final K t = ObjectBigArrays.<K>get(a, from + i);
            ObjectBigArrays.<K>set(a, from + i, (K)ObjectBigArrays.<K>get((K[][])a, from + p));
            ObjectBigArrays.<K>set(a, from + p, t);
        }
        return a;
    }
    
    public static <K> K[][] shuffle(final K[][] a, final Random random) {
        long i = ObjectBigArrays.<K>length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final K t = ObjectBigArrays.<K>get(a, i);
            ObjectBigArrays.<K>set(a, i, (K)ObjectBigArrays.<K>get((K[][])a, p));
            ObjectBigArrays.<K>set(a, p, t);
        }
        return a;
    }
    
    static {
        EMPTY_BIG_ARRAY = new Object[0][];
        DEFAULT_EMPTY_BIG_ARRAY = new Object[0][];
        HASH_STRATEGY = new BigArrayHashStrategy();
    }
    
    private static final class BigArrayHashStrategy<K> implements Hash.Strategy<K[][]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        public int hashCode(final K[][] o) {
            return Arrays.deepHashCode((Object[])o);
        }
        
        public boolean equals(final K[][] a, final K[][] b) {
            return ObjectBigArrays.<K>equals(a, b);
        }
    }
}
