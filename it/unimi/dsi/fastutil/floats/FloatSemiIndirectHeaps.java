package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.ints.IntArrays;

public final class FloatSemiIndirectHeaps {
    private FloatSemiIndirectHeaps() {
    }
    
    public static int downHeap(final float[] refArray, final int[] heap, final int size, int i, final FloatComparator c) {
        assert i < size;
        final int e = heap[i];
        final float E = refArray[e];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                int t = heap[child];
                final int right = child + 1;
                if (right < size && Float.compare(refArray[heap[right]], refArray[t]) < 0) {
                    t = heap[child = right];
                }
                if (Float.compare(E, refArray[t]) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        else {
            int child;
            while ((child = (i << 1) + 1) < size) {
                int t = heap[child];
                final int right = child + 1;
                if (right < size && c.compare(refArray[heap[right]], refArray[t]) < 0) {
                    t = heap[child = right];
                }
                if (c.compare(E, refArray[t]) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        heap[i] = e;
        return i;
    }
    
    public static int upHeap(final float[] refArray, final int[] heap, final int size, int i, final FloatComparator c) {
        assert i < size;
        final int e = heap[i];
        final float E = refArray[e];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final int t = heap[parent];
                if (Float.compare(refArray[t], E) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        else {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final int t = heap[parent];
                if (c.compare(refArray[t], E) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        heap[i] = e;
        return i;
    }
    
    public static void makeHeap(final float[] refArray, final int offset, final int length, final int[] heap, final FloatComparator c) {
        FloatArrays.ensureOffsetLength(refArray, offset, length);
        if (heap.length < length) {
            throw new IllegalArgumentException(new StringBuilder().append("The heap length (").append(heap.length).append(") is smaller than the number of elements (").append(length).append(")").toString());
        }
        int i = length;
        while (i-- != 0) {
            heap[i] = offset + i;
        }
        i = length >>> 1;
        while (i-- != 0) {
            downHeap(refArray, heap, length, i, c);
        }
    }
    
    public static int[] makeHeap(final float[] refArray, final int offset, final int length, final FloatComparator c) {
        final int[] heap = (length <= 0) ? IntArrays.EMPTY_ARRAY : new int[length];
        makeHeap(refArray, offset, length, heap, c);
        return heap;
    }
    
    public static void makeHeap(final float[] refArray, final int[] heap, final int size, final FloatComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(refArray, heap, size, i, c);
        }
    }
    
    public static int front(final float[] refArray, final int[] heap, final int size, final int[] a) {
        final float top = refArray[heap[0]];
        int j = 0;
        int l = 0;
        int r = 1;
        int f = 0;
        for (int i = 0; i < r; ++i) {
            if (i == f) {
                if (l >= r) {
                    break;
                }
                f = (f << 1) + 1;
                i = l;
                l = -1;
            }
            if (Float.compare(top, refArray[heap[i]]) == 0) {
                a[j++] = heap[i];
                if (l == -1) {
                    l = i * 2 + 1;
                }
                r = Math.min(size, i * 2 + 3);
            }
        }
        return j;
    }
    
    public static int front(final float[] refArray, final int[] heap, final int size, final int[] a, final FloatComparator c) {
        final float top = refArray[heap[0]];
        int j = 0;
        int l = 0;
        int r = 1;
        int f = 0;
        for (int i = 0; i < r; ++i) {
            if (i == f) {
                if (l >= r) {
                    break;
                }
                f = (f << 1) + 1;
                i = l;
                l = -1;
            }
            if (c.compare(top, refArray[heap[i]]) == 0) {
                a[j++] = heap[i];
                if (l == -1) {
                    l = i * 2 + 1;
                }
                r = Math.min(size, i * 2 + 3);
            }
        }
        return j;
    }
}
