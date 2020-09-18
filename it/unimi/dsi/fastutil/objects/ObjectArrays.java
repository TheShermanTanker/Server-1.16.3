package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.concurrent.RecursiveAction;
import java.util.Random;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;
import java.util.Comparator;
import it.unimi.dsi.fastutil.Arrays;
import java.util.Objects;
import java.lang.reflect.Array;
import it.unimi.dsi.fastutil.Hash;

public final class ObjectArrays {
    public static final Object[] EMPTY_ARRAY;
    public static final Object[] DEFAULT_EMPTY_ARRAY;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int MERGESORT_NO_REC = 16;
    public static final Hash.Strategy HASH_STRATEGY;
    
    private ObjectArrays() {
    }
    
    private static <K> K[] newArray(final K[] prototype, final int length) {
        final Class<?> klass = prototype.getClass();
        if (klass == Object[].class) {
            return (K[])((length == 0) ? ObjectArrays.EMPTY_ARRAY : new Object[length]);
        }
        return (K[])Array.newInstance(klass.getComponentType(), length);
    }
    
    public static <K> K[] forceCapacity(final K[] array, final int length, final int preserve) {
        final K[] t = ObjectArrays.<K>newArray(array, length);
        System.arraycopy(array, 0, t, 0, preserve);
        return t;
    }
    
    public static <K> K[] ensureCapacity(final K[] array, final int length) {
        return ObjectArrays.<K>ensureCapacity(array, length, array.length);
    }
    
    public static <K> K[] ensureCapacity(final K[] array, final int length, final int preserve) {
        return (K[])((length > array.length) ? ObjectArrays.forceCapacity((Object[])array, length, preserve) : array);
    }
    
    public static <K> K[] grow(final K[] array, final int length) {
        return ObjectArrays.<K>grow(array, length, array.length);
    }
    
    public static <K> K[] grow(final K[] array, final int length, final int preserve) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(array.length + (long)(array.length >> 1), 2147483639L), (long)length);
            final K[] t = ObjectArrays.<K>newArray(array, newLength);
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static <K> K[] trim(final K[] array, final int length) {
        if (length >= array.length) {
            return array;
        }
        final K[] t = (K[])ObjectArrays.newArray((Object[])array, length);
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }
    
    public static <K> K[] setLength(final K[] array, final int length) {
        if (length == array.length) {
            return array;
        }
        if (length < array.length) {
            return (K[])ObjectArrays.trim((Object[])array, length);
        }
        return (K[])ObjectArrays.ensureCapacity((Object[])array, length);
    }
    
    public static <K> K[] copy(final K[] array, final int offset, final int length) {
        ObjectArrays.<K>ensureOffsetLength(array, offset, length);
        final K[] a = ObjectArrays.<K>newArray(array, length);
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }
    
    public static <K> K[] copy(final K[] array) {
        return array.clone();
    }
    
    @Deprecated
    public static <K> void fill(final K[] array, final K value) {
        int i = array.length;
        while (i-- != 0) {
            array[i] = value;
        }
    }
    
    @Deprecated
    public static <K> void fill(final K[] array, final int from, int to, final K value) {
        ObjectArrays.<K>ensureFromTo(array, from, to);
        if (from == 0) {
            while (to-- != 0) {
                array[to] = value;
            }
        }
        else {
            for (int i = from; i < to; ++i) {
                array[i] = value;
            }
        }
    }
    
    @Deprecated
    public static <K> boolean equals(final K[] a1, final K[] a2) {
        int i = a1.length;
        if (i != a2.length) {
            return false;
        }
        while (i-- != 0) {
            if (!Objects.equals(a1[i], a2[i])) {
                return false;
            }
        }
        return true;
    }
    
    public static <K> void ensureFromTo(final K[] a, final int from, final int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }
    
    public static <K> void ensureOffsetLength(final K[] a, final int offset, final int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }
    
    public static <K> void ensureSameLength(final K[] a, final K[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException(new StringBuilder().append("Array size mismatch: ").append(a.length).append(" != ").append(b.length).toString());
        }
    }
    
    public static <K> void swap(final K[] x, final int a, final int b) {
        final K t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
    
    public static <K> void swap(final K[] x, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            ObjectArrays.<K>swap(x, a, b);
        }
    }
    
    private static <K> int med3(final K[] x, final int a, final int b, final int c, final Comparator<K> comp) {
        final int ab = comp.compare(x[a], x[b]);
        final int ac = comp.compare(x[a], x[c]);
        final int bc = comp.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static <K> void selectionSort(final K[] a, final int from, final int to, final Comparator<K> comp) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                final K u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static <K> void insertionSort(final K[] a, final int from, final int to, final Comparator<K> comp) {
        int i = from;
        while (++i < to) {
            final K t = a[i];
            int j = i;
            for (K u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static <K> void quickSort(final K[] x, final int from, final int to, final Comparator<K> comp) {
        final int len = to - from;
        if (len < 16) {
            ObjectArrays.selectionSort(x, from, to, (java.util.Comparator<Object>)comp);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = ObjectArrays.<K>med3(x, l, l + s, l + 2 * s, comp);
            m = ObjectArrays.<K>med3(x, m - s, m, m + s, comp);
            n = ObjectArrays.<K>med3(x, n - 2 * s, n - s, n, comp);
        }
        m = ObjectArrays.<K>med3(x, l, m, n, comp);
        final K v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
                if (comparison == 0) {
                    ObjectArrays.<K>swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
                    if (comparison == 0) {
                        ObjectArrays.<K>swap(x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                ObjectArrays.<K>swap(x, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        ObjectArrays.<K>swap(x, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        ObjectArrays.<K>swap(x, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            ObjectArrays.quickSort(x, from, from + s2, (java.util.Comparator<Object>)comp);
        }
        if ((s2 = d - c) > 1) {
            ObjectArrays.quickSort(x, to - s2, to, (java.util.Comparator<Object>)comp);
        }
    }
    
    public static <K> void quickSort(final K[] x, final Comparator<K> comp) {
        ObjectArrays.<K>quickSort(x, 0, x.length, comp);
    }
    
    public static <K> void parallelQuickSort(final K[] x, final int from, final int to, final Comparator<K> comp) {
        if (to - from < 8192) {
            ObjectArrays.quickSort(x, from, to, (java.util.Comparator<Object>)comp);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask)new ForkJoinQuickSortComp(x, from, to, (java.util.Comparator<Object>)comp));
            pool.shutdown();
        }
    }
    
    public static <K> void parallelQuickSort(final K[] x, final Comparator<K> comp) {
        ObjectArrays.<K>parallelQuickSort(x, 0, x.length, comp);
    }
    
    private static <K> int med3(final K[] x, final int a, final int b, final int c) {
        final int ab = ((Comparable)x[a]).compareTo(x[b]);
        final int ac = ((Comparable)x[a]).compareTo(x[c]);
        final int bc = ((Comparable)x[b]).compareTo(x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static <K> void selectionSort(final K[] a, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (((Comparable)a[j]).compareTo(a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                final K u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static <K> void insertionSort(final K[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final K t = a[i];
            int j = i;
            for (K u = a[j - 1]; ((Comparable)t).compareTo(u) < 0; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static <K> void quickSort(final K[] x, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            ObjectArrays.selectionSort((Object[])x, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = ObjectArrays.<K>med3(x, l, l + s, l + 2 * s);
            m = ObjectArrays.<K>med3(x, m - s, m, m + s);
            n = ObjectArrays.<K>med3(x, n - 2 * s, n - s, n);
        }
        m = ObjectArrays.<K>med3(x, l, m, n);
        final K v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = ((Comparable)x[b]).compareTo(v)) <= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap((Object[])x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = ((Comparable)x[c]).compareTo(v)) >= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap((Object[])x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                ObjectArrays.swap((Object[])x, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        ObjectArrays.<K>swap(x, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        ObjectArrays.<K>swap(x, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            ObjectArrays.quickSort((Object[])x, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            ObjectArrays.quickSort((Object[])x, to - s2, to);
        }
    }
    
    public static <K> void quickSort(final K[] x) {
        ObjectArrays.<K>quickSort(x, 0, x.length);
    }
    
    public static <K> void parallelQuickSort(final K[] x, final int from, final int to) {
        if (to - from < 8192) {
            ObjectArrays.quickSort((Object[])x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask)new ForkJoinQuickSort(x, from, to));
            pool.shutdown();
        }
    }
    
    public static <K> void parallelQuickSort(final K[] x) {
        ObjectArrays.<K>parallelQuickSort(x, 0, x.length);
    }
    
    private static <K> int med3Indirect(final int[] perm, final K[] x, final int a, final int b, final int c) {
        final K aa = x[perm[a]];
        final K bb = x[perm[b]];
        final K cc = x[perm[c]];
        final int ab = ((Comparable)aa).compareTo(bb);
        final int ac = ((Comparable)aa).compareTo(cc);
        final int bc = ((Comparable)bb).compareTo(cc);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static <K> void insertionSortIndirect(final int[] perm, final K[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; ((Comparable)a[t]).compareTo(a[u]) < 0; u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static <K> void quickSortIndirect(final int[] perm, final K[] x, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            ObjectArrays.insertionSortIndirect(perm, (Object[])x, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = ObjectArrays.<K>med3Indirect(perm, x, l, l + s, l + 2 * s);
            m = ObjectArrays.<K>med3Indirect(perm, x, m - s, m, m + s);
            n = ObjectArrays.<K>med3Indirect(perm, x, n - 2 * s, n - s, n);
        }
        m = ObjectArrays.<K>med3Indirect(perm, x, l, m, n);
        final K v = x[perm[m]];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = ((Comparable)x[perm[b]]).compareTo(v)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(perm, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = ((Comparable)x[perm[c]]).compareTo(v)) >= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(perm, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                IntArrays.swap(perm, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        IntArrays.swap(perm, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        IntArrays.swap(perm, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            ObjectArrays.quickSortIndirect(perm, (Object[])x, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            ObjectArrays.quickSortIndirect(perm, (Object[])x, to - s2, to);
        }
    }
    
    public static <K> void quickSortIndirect(final int[] perm, final K[] x) {
        ObjectArrays.<K>quickSortIndirect(perm, x, 0, x.length);
    }
    
    public static <K> void parallelQuickSortIndirect(final int[] perm, final K[] x, final int from, final int to) {
        if (to - from < 8192) {
            ObjectArrays.quickSortIndirect(perm, (Object[])x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask)new ForkJoinQuickSortIndirect(perm, x, from, to));
            pool.shutdown();
        }
    }
    
    public static <K> void parallelQuickSortIndirect(final int[] perm, final K[] x) {
        ObjectArrays.<K>parallelQuickSortIndirect(perm, x, 0, x.length);
    }
    
    public static <K> void stabilize(final int[] perm, final K[] x, final int from, final int to) {
        int curr = from;
        for (int i = from + 1; i < to; ++i) {
            if (x[perm[i]] != x[perm[curr]]) {
                if (i - curr > 1) {
                    IntArrays.parallelQuickSort(perm, curr, i);
                }
                curr = i;
            }
        }
        if (to - curr > 1) {
            IntArrays.parallelQuickSort(perm, curr, to);
        }
    }
    
    public static <K> void stabilize(final int[] perm, final K[] x) {
        ObjectArrays.<K>stabilize(perm, x, 0, perm.length);
    }
    
    private static <K> int med3(final K[] x, final K[] y, final int a, final int b, final int c) {
        int t;
        final int ab = ((t = ((Comparable)x[a]).compareTo(x[b])) == 0) ? ((Comparable)y[a]).compareTo(y[b]) : t;
        final int ac = ((t = ((Comparable)x[a]).compareTo(x[c])) == 0) ? ((Comparable)y[a]).compareTo(y[c]) : t;
        final int bc = ((t = ((Comparable)x[b]).compareTo(x[c])) == 0) ? ((Comparable)y[b]).compareTo(y[c]) : t;
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static <K> void swap(final K[] x, final K[] y, final int a, final int b) {
        final K t = x[a];
        final K u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }
    
    private static <K> void swap(final K[] x, final K[] y, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            ObjectArrays.<K>swap(x, y, a, b);
        }
    }
    
    private static <K> void selectionSort(final K[] a, final K[] b, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                final int u;
                if ((u = ((Comparable)a[j]).compareTo(a[m])) < 0 || (u == 0 && ((Comparable)b[j]).compareTo(b[m]) < 0)) {
                    m = j;
                }
            }
            if (m != i) {
                K t = a[i];
                a[i] = a[m];
                a[m] = t;
                t = b[i];
                b[i] = b[m];
                b[m] = t;
            }
        }
    }
    
    public static <K> void quickSort(final K[] x, final K[] y, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            ObjectArrays.selectionSort(x, (Object[])y, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = ObjectArrays.<K>med3(x, y, l, l + s, l + 2 * s);
            m = ObjectArrays.<K>med3(x, y, m - s, m, m + s);
            n = ObjectArrays.<K>med3(x, y, n - 2 * s, n - s, n);
        }
        m = ObjectArrays.<K>med3(x, y, l, m, n);
        final K v = x[m];
        final K w = y[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int t;
            int comparison;
            if (b <= c && (comparison = (((t = ((Comparable)x[b]).compareTo(v)) == 0) ? ((Comparable)y[b]).compareTo(w) : t)) <= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap(x, (Object[])y, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = (((t = ((Comparable)x[c]).compareTo(v)) == 0) ? ((Comparable)y[c]).compareTo(w) : t)) >= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x, (Object[])y, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                ObjectArrays.swap(x, (Object[])y, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        ObjectArrays.<K>swap(x, y, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        ObjectArrays.<K>swap(x, y, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            ObjectArrays.quickSort(x, (Object[])y, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            ObjectArrays.quickSort(x, (Object[])y, to - s2, to);
        }
    }
    
    public static <K> void quickSort(final K[] x, final K[] y) {
        ObjectArrays.ensureSameLength(x, (Object[])y);
        ObjectArrays.<K>quickSort(x, y, 0, x.length);
    }
    
    public static <K> void parallelQuickSort(final K[] x, final K[] y, final int from, final int to) {
        if (to - from < 8192) {
            ObjectArrays.quickSort(x, (Object[])y, from, to);
        }
        final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        pool.invoke((ForkJoinTask)new ForkJoinQuickSort2(x, y, from, to));
        pool.shutdown();
    }
    
    public static <K> void parallelQuickSort(final K[] x, final K[] y) {
        ObjectArrays.ensureSameLength(x, (Object[])y);
        ObjectArrays.<K>parallelQuickSort(x, y, 0, x.length);
    }
    
    public static <K> void mergeSort(final K[] a, final int from, final int to, final K[] supp) {
        final int len = to - from;
        if (len < 16) {
            ObjectArrays.<K>insertionSort(a, from, to);
            return;
        }
        final int mid = from + to >>> 1;
        ObjectArrays.mergeSort(supp, from, mid, (Object[])a);
        ObjectArrays.mergeSort(supp, mid, to, (Object[])a);
        if (((Comparable)supp[mid - 1]).compareTo(supp[mid]) <= 0) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && ((Comparable)supp[p]).compareTo(supp[q]) <= 0)) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static <K> void mergeSort(final K[] a, final int from, final int to) {
        ObjectArrays.<K>mergeSort(a, from, to, a.clone());
    }
    
    public static <K> void mergeSort(final K[] a) {
        ObjectArrays.<K>mergeSort(a, 0, a.length);
    }
    
    public static <K> void mergeSort(final K[] a, final int from, final int to, final Comparator<K> comp, final K[] supp) {
        final int len = to - from;
        if (len < 16) {
            ObjectArrays.<K>insertionSort(a, from, to, comp);
            return;
        }
        final int mid = from + to >>> 1;
        ObjectArrays.mergeSort(supp, from, mid, (java.util.Comparator<Object>)comp, a);
        ObjectArrays.mergeSort(supp, mid, to, (java.util.Comparator<Object>)comp, a);
        if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static <K> void mergeSort(final K[] a, final int from, final int to, final Comparator<K> comp) {
        ObjectArrays.<K>mergeSort(a, from, to, comp, a.clone());
    }
    
    public static <K> void mergeSort(final K[] a, final Comparator<K> comp) {
        ObjectArrays.<K>mergeSort(a, 0, a.length, comp);
    }
    
    public static <K> int binarySearch(final K[] a, int from, int to, final K key) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final K midVal = a[mid];
            final int cmp = ((Comparable)midVal).compareTo(key);
            if (cmp < 0) {
                from = mid + 1;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                to = mid - 1;
            }
        }
        return -(from + 1);
    }
    
    public static <K> int binarySearch(final K[] a, final K key) {
        return ObjectArrays.<K>binarySearch(a, 0, a.length, key);
    }
    
    public static <K> int binarySearch(final K[] a, int from, int to, final K key, final Comparator<K> c) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final K midVal = a[mid];
            final int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                from = mid + 1;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                to = mid - 1;
            }
        }
        return -(from + 1);
    }
    
    public static <K> int binarySearch(final K[] a, final K key, final Comparator<K> c) {
        return ObjectArrays.<K>binarySearch(a, 0, a.length, key, c);
    }
    
    public static <K> K[] shuffle(final K[] a, final int from, final int to, final Random random) {
        int i = to - from;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final K t = a[from + i];
            a[from + i] = a[from + p];
            a[from + p] = t;
        }
        return a;
    }
    
    public static <K> K[] shuffle(final K[] a, final Random random) {
        int i = a.length;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final K t = a[i];
            a[i] = a[p];
            a[p] = t;
        }
        return a;
    }
    
    public static <K> K[] reverse(final K[] a) {
        final int length = a.length;
        int i = length / 2;
        while (i-- != 0) {
            final K t = a[length - i - 1];
            a[length - i - 1] = a[i];
            a[i] = t;
        }
        return a;
    }
    
    public static <K> K[] reverse(final K[] a, final int from, final int to) {
        final int length = to - from;
        int i = length / 2;
        while (i-- != 0) {
            final K t = a[from + length - i - 1];
            a[from + length - i - 1] = a[from + i];
            a[from + i] = t;
        }
        return a;
    }
    
    static {
        EMPTY_ARRAY = new Object[0];
        DEFAULT_EMPTY_ARRAY = new Object[0];
        HASH_STRATEGY = new ArrayHashStrategy();
    }
    
    protected static class ForkJoinQuickSortComp<K> extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final K[] x;
        private final Comparator<K> comp;
        
        public ForkJoinQuickSortComp(final K[] x, final int from, final int to, final Comparator<K> comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }
        
        protected void compute() {
            final K[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.<K>quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = ObjectArrays.med3(x, l, l + s, l + 2 * s, (java.util.Comparator<Object>)this.comp);
            m = ObjectArrays.med3(x, m - s, m, m + s, (java.util.Comparator<Object>)this.comp);
            n = ObjectArrays.med3(x, n - 2 * s, n - s, n, (java.util.Comparator<Object>)this.comp);
            m = ObjectArrays.med3(x, l, m, n, (java.util.Comparator<Object>)this.comp);
            final K v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        ObjectArrays.<K>swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            ObjectArrays.<K>swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    ObjectArrays.<K>swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            ObjectArrays.<K>swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            ObjectArrays.<K>swap(x, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                invokeAll((ForkJoinTask)new ForkJoinQuickSortComp(x, this.from, this.from + s, (java.util.Comparator<Object>)this.comp), (ForkJoinTask)new ForkJoinQuickSortComp(x, this.to - t, this.to, (java.util.Comparator<Object>)this.comp));
            }
            else if (s > 1) {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSortComp(x, this.from, this.from + s, (java.util.Comparator<Object>)this.comp) });
            }
            else {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSortComp(x, this.to - t, this.to, (java.util.Comparator<Object>)this.comp) });
            }
        }
    }
    
    protected static class ForkJoinQuickSort<K> extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final K[] x;
        
        public ForkJoinQuickSort(final K[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }
        
        protected void compute() {
            final K[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.<K>quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = ObjectArrays.med3(x, l, l + s, l + 2 * s);
            m = ObjectArrays.med3(x, m - s, m, m + s);
            n = ObjectArrays.med3(x, n - 2 * s, n - s, n);
            m = ObjectArrays.med3(x, l, m, n);
            final K v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = ((Comparable)x[b]).compareTo(v)) <= 0) {
                    if (comparison == 0) {
                        ObjectArrays.<K>swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = ((Comparable)x[c]).compareTo(v)) >= 0) {
                        if (comparison == 0) {
                            ObjectArrays.<K>swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    ObjectArrays.<K>swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            ObjectArrays.<K>swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            ObjectArrays.<K>swap(x, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                invokeAll((ForkJoinTask)new ForkJoinQuickSort(x, this.from, this.from + s), (ForkJoinTask)new ForkJoinQuickSort(x, this.to - t, this.to));
            }
            else if (s > 1) {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSort(x, this.from, this.from + s) });
            }
            else {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSort(x, this.to - t, this.to) });
            }
        }
    }
    
    protected static class ForkJoinQuickSortIndirect<K> extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] perm;
        private final K[] x;
        
        public ForkJoinQuickSortIndirect(final int[] perm, final K[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }
        
        protected void compute() {
            final K[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.<K>quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = ObjectArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
            m = ObjectArrays.med3Indirect(this.perm, x, m - s, m, m + s);
            n = ObjectArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
            m = ObjectArrays.med3Indirect(this.perm, x, l, m, n);
            final K v = x[this.perm[m]];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = ((Comparable)x[this.perm[b]]).compareTo(v)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = ((Comparable)x[this.perm[c]]).compareTo(v)) >= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    IntArrays.swap(this.perm, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            IntArrays.swap(this.perm, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            IntArrays.swap(this.perm, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                invokeAll((ForkJoinTask)new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s), (ForkJoinTask)new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
            }
            else if (s > 1) {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s) });
            }
            else {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to) });
            }
        }
    }
    
    protected static class ForkJoinQuickSort2<K> extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final K[] x;
        private final K[] y;
        
        public ForkJoinQuickSort2(final K[] x, final K[] y, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }
        
        protected void compute() {
            final K[] x = this.x;
            final K[] y = this.y;
            final int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.<K>quickSort(x, y, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = ObjectArrays.med3(x, y, l, l + s, l + 2 * s);
            m = ObjectArrays.med3(x, y, m - s, m, m + s);
            n = ObjectArrays.med3(x, y, n - 2 * s, n - s, n);
            m = ObjectArrays.med3(x, y, l, m, n);
            final K v = x[m];
            final K w = y[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int t;
                int comparison;
                if (b <= c && (comparison = (((t = ((Comparable)x[b]).compareTo(v)) == 0) ? ((Comparable)y[b]).compareTo(w) : t)) <= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x, y, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = (((t = ((Comparable)x[c]).compareTo(v)) == 0) ? ((Comparable)y[c]).compareTo(w) : t)) >= 0) {
                        if (comparison == 0) {
                            ObjectArrays.swap(x, y, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    ObjectArrays.swap(x, y, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            ObjectArrays.swap(x, y, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            ObjectArrays.swap(x, y, b, this.to - s, s);
            s = b - a;
            final int t2 = d - c;
            if (s > 1 && t2 > 1) {
                invokeAll((ForkJoinTask)new ForkJoinQuickSort2(x, y, this.from, this.from + s), (ForkJoinTask)new ForkJoinQuickSort2(x, y, this.to - t2, this.to));
            }
            else if (s > 1) {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSort2(x, y, this.from, this.from + s) });
            }
            else {
                invokeAll(new ForkJoinTask[] { (ForkJoinTask)new ForkJoinQuickSort2(x, y, this.to - t2, this.to) });
            }
        }
    }
    
    private static final class ArrayHashStrategy<K> implements Hash.Strategy<K[]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        public int hashCode(final K[] o) {
            return java.util.Arrays.hashCode((Object[])o);
        }
        
        public boolean equals(final K[] a, final K[] b) {
            return java.util.Arrays.equals((Object[])a, (Object[])b);
        }
    }
}
