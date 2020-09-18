package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Long2BooleanRBTreeMap extends AbstractLong2BooleanSortedMap implements Serializable, Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Long2BooleanMap.Entry> entries;
    protected transient LongSortedSet keys;
    protected transient BooleanCollection values;
    protected transient boolean modified;
    protected Comparator<? super Long> storedComparator;
    protected transient LongComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;
    
    public Long2BooleanRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = LongComparators.asLongComparator(this.storedComparator);
    }
    
    public Long2BooleanRBTreeMap(final Comparator<? super Long> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Long2BooleanRBTreeMap(final Map<? extends Long, ? extends Boolean> m) {
        this();
        this.putAll(m);
    }
    
    public Long2BooleanRBTreeMap(final SortedMap<Long, Boolean> m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Long2BooleanRBTreeMap(final Long2BooleanMap m) {
        this();
        this.putAll(m);
    }
    
    public Long2BooleanRBTreeMap(final Long2BooleanSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Long2BooleanRBTreeMap(final long[] k, final boolean[] v, final Comparator<? super Long> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2BooleanRBTreeMap(final long[] k, final boolean[] v) {
        this(k, v, null);
    }
    
    final int compare(final long k1, final long k2) {
        return (this.actualComparator == null) ? Long.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry findKey(final long k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final long k) {
        Entry e = this.tree;
        Entry last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[64];
        this.nodePath = new Entry[64];
    }
    
    public boolean put(final long k, final boolean v) {
        final Entry e = this.add(k);
        final boolean oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry add(final long k) {
        this.modified = false;
        int maxDepth = 0;
        Entry e = null;
        Label_0919: {
            if (this.tree != null) {
                Entry p = this.tree;
                int i = 0;
                int cmp;
                while ((cmp = this.compare(k, p.key)) != 0) {
                    this.nodePath[i] = p;
                    final boolean[] dirPath = this.dirPath;
                    final int n = i++;
                    final boolean b = cmp > 0;
                    dirPath[n] = b;
                    if (b) {
                        if (!p.succ()) {
                            p = p.right;
                            continue;
                        }
                        ++this.count;
                        e = new Entry(k, this.defRetValue);
                        if (p.right == null) {
                            this.lastEntry = e;
                        }
                        e.left = p;
                        e.right = p.right;
                        p.right(e);
                    }
                    else {
                        if (!p.pred()) {
                            p = p.left;
                            continue;
                        }
                        ++this.count;
                        e = new Entry(k, this.defRetValue);
                        if (p.left == null) {
                            this.firstEntry = e;
                        }
                        e.right = p;
                        e.left = p.left;
                        p.left(e);
                    }
                    this.modified = true;
                    maxDepth = i--;
                    while (i > 0 && !this.nodePath[i].black()) {
                        if (!this.dirPath[i - 1]) {
                            Entry y = this.nodePath[i - 1].right;
                            if (!this.nodePath[i - 1].succ() && !y.black()) {
                                this.nodePath[i].black(true);
                                y.black(true);
                                this.nodePath[i - 1].black(false);
                                i -= 2;
                            }
                            else {
                                if (!this.dirPath[i]) {
                                    y = this.nodePath[i];
                                }
                                else {
                                    final Entry x = this.nodePath[i];
                                    y = x.right;
                                    x.right = y.left;
                                    y.left = x;
                                    this.nodePath[i - 1].left = y;
                                    if (y.pred()) {
                                        y.pred(false);
                                        x.succ(y);
                                    }
                                }
                                final Entry x = this.nodePath[i - 1];
                                x.black(false);
                                y.black(true);
                                x.left = y.right;
                                y.right = x;
                                if (i < 2) {
                                    this.tree = y;
                                }
                                else if (this.dirPath[i - 2]) {
                                    this.nodePath[i - 2].right = y;
                                }
                                else {
                                    this.nodePath[i - 2].left = y;
                                }
                                if (y.succ()) {
                                    y.succ(false);
                                    x.pred(y);
                                    break;
                                }
                                break;
                            }
                        }
                        else {
                            Entry y = this.nodePath[i - 1].left;
                            if (!this.nodePath[i - 1].pred() && !y.black()) {
                                this.nodePath[i].black(true);
                                y.black(true);
                                this.nodePath[i - 1].black(false);
                                i -= 2;
                            }
                            else {
                                if (this.dirPath[i]) {
                                    y = this.nodePath[i];
                                }
                                else {
                                    final Entry x = this.nodePath[i];
                                    y = x.left;
                                    x.left = y.right;
                                    y.right = x;
                                    this.nodePath[i - 1].right = y;
                                    if (y.succ()) {
                                        y.succ(false);
                                        x.pred(y);
                                    }
                                }
                                final Entry x = this.nodePath[i - 1];
                                x.black(false);
                                y.black(true);
                                x.right = y.left;
                                y.left = x;
                                if (i < 2) {
                                    this.tree = y;
                                }
                                else if (this.dirPath[i - 2]) {
                                    this.nodePath[i - 2].right = y;
                                }
                                else {
                                    this.nodePath[i - 2].left = y;
                                }
                                if (y.pred()) {
                                    y.pred(false);
                                    x.succ(y);
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    break Label_0919;
                }
                while (i-- != 0) {
                    this.nodePath[i] = null;
                }
                return p;
            }
            ++this.count;
            final Entry tree = new Entry(k, this.defRetValue);
            this.firstEntry = tree;
            this.lastEntry = tree;
            this.tree = tree;
            e = tree;
        }
        this.tree.black(true);
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return e;
    }
    
    public boolean remove(final long k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry p = this.tree;
        int i = 0;
        final long kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            this.dirPath[i] = (cmp > 0);
            this.nodePath[i] = p;
            if (this.dirPath[i++]) {
                if ((p = p.right()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return this.defRetValue;
                }
                continue;
            }
            else {
                if ((p = p.left()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return this.defRetValue;
                }
                continue;
            }
        }
        if (p.left == null) {
            this.firstEntry = p.next();
        }
        if (p.right == null) {
            this.lastEntry = p.prev();
        }
        if (p.succ()) {
            if (p.pred()) {
                if (i == 0) {
                    this.tree = p.left;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].succ(p.right);
                }
                else {
                    this.nodePath[i - 1].pred(p.left);
                }
            }
            else {
                p.prev().right = p.right;
                if (i == 0) {
                    this.tree = p.left;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].right = p.left;
                }
                else {
                    this.nodePath[i - 1].left = p.left;
                }
            }
        }
        else {
            Entry r = p.right;
            if (r.pred()) {
                r.left = p.left;
                r.pred(p.pred());
                if (!r.pred()) {
                    r.prev().right = r;
                }
                if (i == 0) {
                    this.tree = r;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].right = r;
                }
                else {
                    this.nodePath[i - 1].left = r;
                }
                final boolean color = r.black();
                r.black(p.black());
                p.black(color);
                this.dirPath[i] = true;
                this.nodePath[i++] = r;
            }
            else {
                final int j = i++;
                Entry s;
                while (true) {
                    this.dirPath[i] = false;
                    this.nodePath[i++] = r;
                    s = r.left;
                    if (s.pred()) {
                        break;
                    }
                    r = s;
                }
                this.dirPath[j] = true;
                this.nodePath[j] = s;
                if (s.succ()) {
                    r.pred(s);
                }
                else {
                    r.left = s.right;
                }
                s.left = p.left;
                if (!p.pred()) {
                    (p.prev().right = s).pred(false);
                }
                s.right(p.right);
                final boolean color = s.black();
                s.black(p.black());
                p.black(color);
                if (j == 0) {
                    this.tree = s;
                }
                else if (this.dirPath[j - 1]) {
                    this.nodePath[j - 1].right = s;
                }
                else {
                    this.nodePath[j - 1].left = s;
                }
            }
        }
        int maxDepth = i;
        if (p.black()) {
            while (i > 0) {
                if ((this.dirPath[i - 1] && !this.nodePath[i - 1].succ()) || (!this.dirPath[i - 1] && !this.nodePath[i - 1].pred())) {
                    final Entry x = this.dirPath[i - 1] ? this.nodePath[i - 1].right : this.nodePath[i - 1].left;
                    if (!x.black()) {
                        x.black(true);
                        break;
                    }
                }
                if (!this.dirPath[i - 1]) {
                    Entry w = this.nodePath[i - 1].right;
                    if (!w.black()) {
                        w.black(true);
                        this.nodePath[i - 1].black(false);
                        this.nodePath[i - 1].right = w.left;
                        w.left = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        this.nodePath[i] = this.nodePath[i - 1];
                        this.dirPath[i] = false;
                        this.nodePath[i - 1] = w;
                        if (maxDepth == i++) {
                            ++maxDepth;
                        }
                        w = this.nodePath[i - 1].right;
                    }
                    if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                        w.black(false);
                    }
                    else {
                        if (w.succ() || w.right.black()) {
                            final Entry y = w.left;
                            y.black(true);
                            w.black(false);
                            w.left = y.right;
                            y.right = w;
                            final Entry entry = this.nodePath[i - 1];
                            final Entry right = y;
                            entry.right = right;
                            w = right;
                            if (w.succ()) {
                                w.succ(false);
                                w.right.pred(w);
                            }
                        }
                        w.black(this.nodePath[i - 1].black());
                        this.nodePath[i - 1].black(true);
                        w.right.black(true);
                        this.nodePath[i - 1].right = w.left;
                        w.left = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        if (w.pred()) {
                            w.pred(false);
                            this.nodePath[i - 1].succ(w);
                            break;
                        }
                        break;
                    }
                }
                else {
                    Entry w = this.nodePath[i - 1].left;
                    if (!w.black()) {
                        w.black(true);
                        this.nodePath[i - 1].black(false);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        this.nodePath[i] = this.nodePath[i - 1];
                        this.dirPath[i] = true;
                        this.nodePath[i - 1] = w;
                        if (maxDepth == i++) {
                            ++maxDepth;
                        }
                        w = this.nodePath[i - 1].left;
                    }
                    if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                        w.black(false);
                    }
                    else {
                        if (w.pred() || w.left.black()) {
                            final Entry y = w.right;
                            y.black(true);
                            w.black(false);
                            w.right = y.left;
                            y.left = w;
                            final Entry entry2 = this.nodePath[i - 1];
                            final Entry left = y;
                            entry2.left = left;
                            w = left;
                            if (w.pred()) {
                                w.pred(false);
                                w.left.succ(w);
                            }
                        }
                        w.black(this.nodePath[i - 1].black());
                        this.nodePath[i - 1].black(true);
                        w.left.black(true);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        if (w.succ()) {
                            w.succ(false);
                            this.nodePath[i - 1].pred(w);
                            break;
                        }
                        break;
                    }
                }
                --i;
            }
            if (this.tree != null) {
                this.tree.black(true);
            }
        }
        this.modified = true;
        --this.count;
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return p.value;
    }
    
    public boolean containsValue(final boolean v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final boolean ev = i.nextBoolean();
            if (ev == v) {
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.entries = null;
        this.values = null;
        this.keys = null;
        final Entry entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    public boolean containsKey(final long k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public boolean get(final long k) {
        final Entry e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public long firstLongKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public long lastLongKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Long2BooleanMap.Entry> long2BooleanEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Long2BooleanMap.Entry>() {
                final Comparator<? super Long2BooleanMap.Entry> comparator = (x, y) -> Long2BooleanRBTreeMap.this.actualComparator.compare(x.getLongKey(), y.getLongKey());
                
                public Comparator<? super Long2BooleanMap.Entry> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Long2BooleanMap.Entry> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Long2BooleanMap.Entry> iterator(final Long2BooleanMap.Entry from) {
                    return new EntryIterator(from.getLongKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                        return false;
                    }
                    final Entry f = Long2BooleanRBTreeMap.this.findKey((long)e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                        return false;
                    }
                    final Entry f = Long2BooleanRBTreeMap.this.findKey((long)e.getKey());
                    if (f == null || f.getBooleanValue() != (boolean)e.getValue()) {
                        return false;
                    }
                    Long2BooleanRBTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Long2BooleanRBTreeMap.this.count;
                }
                
                public void clear() {
                    Long2BooleanRBTreeMap.this.clear();
                }
                
                public Long2BooleanMap.Entry first() {
                    return Long2BooleanRBTreeMap.this.firstEntry;
                }
                
                public Long2BooleanMap.Entry last() {
                    return Long2BooleanRBTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Long2BooleanMap.Entry> subSet(final Long2BooleanMap.Entry from, final Long2BooleanMap.Entry to) {
                    return Long2BooleanRBTreeMap.this.subMap(from.getLongKey(), to.getLongKey()).long2BooleanEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Long2BooleanMap.Entry> headSet(final Long2BooleanMap.Entry to) {
                    return Long2BooleanRBTreeMap.this.headMap(to.getLongKey()).long2BooleanEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Long2BooleanMap.Entry> tailSet(final Long2BooleanMap.Entry from) {
                    return Long2BooleanRBTreeMap.this.tailMap(from.getLongKey()).long2BooleanEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public LongSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public BooleanCollection values() {
        if (this.values == null) {
            this.values = new AbstractBooleanCollection() {
                @Override
                public BooleanIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final boolean k) {
                    return Long2BooleanRBTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Long2BooleanRBTreeMap.this.count;
                }
                
                public void clear() {
                    Long2BooleanRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public LongComparator comparator() {
        return this.actualComparator;
    }
    
    public Long2BooleanSortedMap headMap(final long to) {
        return new Submap(0L, true, to, false);
    }
    
    public Long2BooleanSortedMap tailMap(final long from) {
        return new Submap(from, false, 0L, true);
    }
    
    public Long2BooleanSortedMap subMap(final long from, final long to) {
        return new Submap(from, false, to, false);
    }
    
    public Long2BooleanRBTreeMap clone() {
        Long2BooleanRBTreeMap c;
        try {
            c = (Long2BooleanRBTreeMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            final Entry rp = new Entry();
            final Entry rq = new Entry();
            Entry p = rp;
            rp.left(this.tree);
            Entry q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry e = p.left.clone();
                    e.pred(q.left);
                    e.succ(q);
                    q.left(e);
                    p = p.left;
                    q = q.left;
                }
                else {
                    while (p.succ()) {
                        p = p.right;
                        if (p == null) {
                            break Block_4;
                        }
                        q = q.right;
                    }
                    p = p.right;
                    q = q.right;
                }
                if (!p.succ()) {
                    final Entry e = p.right.clone();
                    e.succ(q.right);
                    e.pred(q);
                    q.right(e);
                }
            }
            q.right = null;
            c.tree = rq.left;
            c.firstEntry = c.tree;
            while (c.firstEntry.left != null) {
                c.firstEntry = c.firstEntry.left;
            }
            c.lastEntry = c.tree;
            while (c.lastEntry.right != null) {
                c.lastEntry = c.lastEntry.right;
            }
            return c;
        }
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        int n = this.count;
        final EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            final Entry e = i.nextEntry();
            s.writeLong(e.key);
            s.writeBoolean(e.value);
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readLong(), s.readBoolean());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readLong(), s.readBoolean());
            top.black(true);
            top.right(new Entry(s.readLong(), s.readBoolean()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry top2 = new Entry();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readLong();
        top2.value = s.readBoolean();
        top2.black(true);
        top2.right(this.readTree(s, rightN, top2, succ));
        if (n + 2 == (n + 2 & -(n + 2))) {
            top2.right.black(false);
        }
        return top2;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.setActualComparator();
        this.allocatePaths();
        if (this.count != 0) {
            this.tree = this.readTree(s, this.count, null, null);
            Entry e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static final class Entry extends BasicEntry implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
            super(0L, false);
        }
        
        Entry(final long k, final boolean v) {
            super(k, v);
            this.info = -1073741824;
        }
        
        Entry left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry right() {
            return ((this.info & Integer.MIN_VALUE) != 0x0) ? null : this.right;
        }
        
        boolean pred() {
            return (this.info & 0x40000000) != 0x0;
        }
        
        boolean succ() {
            return (this.info & Integer.MIN_VALUE) != 0x0;
        }
        
        void pred(final boolean pred) {
            if (pred) {
                this.info |= 0x40000000;
            }
            else {
                this.info &= 0xBFFFFFFF;
            }
        }
        
        void succ(final boolean succ) {
            if (succ) {
                this.info |= Integer.MIN_VALUE;
            }
            else {
                this.info &= Integer.MAX_VALUE;
            }
        }
        
        void pred(final Entry pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry right) {
            this.info &= Integer.MAX_VALUE;
            this.right = right;
        }
        
        boolean black() {
            return (this.info & 0x1) != 0x0;
        }
        
        void black(final boolean black) {
            if (black) {
                this.info |= 0x1;
            }
            else {
                this.info &= 0xFFFFFFFE;
            }
        }
        
        Entry next() {
            Entry next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry prev() {
            Entry prev = this.left;
            if ((this.info & 0x40000000) == 0x0) {
                while ((prev.info & Integer.MIN_VALUE) == 0x0) {
                    prev = prev.right;
                }
            }
            return prev;
        }
        
        @Override
        public boolean setValue(final boolean value) {
            final boolean oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public Entry clone() {
            Entry c;
            try {
                c = (Entry)super.clone();
            }
            catch (CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.value = this.value;
            c.info = this.info;
            return c;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Long, Boolean> e = (Map.Entry<Long, Boolean>)o;
            return this.key == (long)e.getKey() && this.value == (boolean)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ (this.value ? 1231 : 1237);
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append(this.key).append("=>").append(this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Long2BooleanRBTreeMap.this.firstEntry;
        }
        
        TreeIterator(final long k) {
            this.index = 0;
            final Entry locateKey = Long2BooleanRBTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Long2BooleanRBTreeMap.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                }
                else {
                    this.prev = this.next.prev();
                }
            }
        }
        
        public boolean hasNext() {
            return this.next != null;
        }
        
        public boolean hasPrevious() {
            return this.prev != null;
        }
        
        void updateNext() {
            this.next = this.next.next();
        }
        
        Entry nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry prev = this.prev;
            this.next = prev;
            this.curr = prev;
            --this.index;
            this.updatePrevious();
            return this.curr;
        }
        
        public int nextIndex() {
            return this.index;
        }
        
        public int previousIndex() {
            return this.index - 1;
        }
        
        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
            }
            final Entry curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Long2BooleanRBTreeMap.this.remove(this.curr.key);
            this.curr = null;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
        
        public int back(final int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
    }
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Long2BooleanMap.Entry> {
        EntryIterator() {
        }
        
        EntryIterator(final long k) {
            super(k);
        }
        
        public Long2BooleanMap.Entry next() {
            return this.nextEntry();
        }
        
        public Long2BooleanMap.Entry previous() {
            return this.previousEntry();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements LongListIterator {
        public KeyIterator() {
        }
        
        public KeyIterator(final long k) {
            super(k);
        }
        
        public long nextLong() {
            return this.nextEntry().key;
        }
        
        public long previousLong() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractLong2BooleanSortedMap.KeySet {
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements BooleanListIterator {
        public boolean nextBoolean() {
            return this.nextEntry().value;
        }
        
        public boolean previousBoolean() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractLong2BooleanSortedMap implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        long from;
        long to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Long2BooleanMap.Entry> entries;
        protected transient LongSortedSet keys;
        protected transient BooleanCollection values;
        final /* synthetic */ Long2BooleanRBTreeMap this$0;
        
        public Submap(final long from, final boolean bottom, final long to, final boolean top) {
            if (!bottom && !top && Long2BooleanRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Long2BooleanRBTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final long k) {
            return (this.bottom || Long2BooleanRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Long2BooleanRBTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Long2BooleanMap.Entry> long2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Long2BooleanMap.Entry>() {
                    @Override
                    public ObjectBidirectionalIterator<Long2BooleanMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Long2BooleanMap.Entry> iterator(final Long2BooleanMap.Entry from) {
                        return new SubmapEntryIterator(from.getLongKey());
                    }
                    
                    public Comparator<? super Long2BooleanMap.Entry> comparator() {
                        return Long2BooleanRBTreeMap.this.long2BooleanEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                            return false;
                        }
                        final Long2BooleanRBTreeMap.Entry f = Long2BooleanRBTreeMap.this.findKey((long)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                            return false;
                        }
                        final Long2BooleanRBTreeMap.Entry f = Long2BooleanRBTreeMap.this.findKey((long)e.getKey());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.remove(f.key);
                        }
                        return f != null;
                    }
                    
                    public int size() {
                        int c = 0;
                        final Iterator<?> i = this.iterator();
                        while (i.hasNext()) {
                            ++c;
                            i.next();
                        }
                        return c;
                    }
                    
                    public boolean isEmpty() {
                        return !new SubmapIterator().hasNext();
                    }
                    
                    public void clear() {
                        Submap.this.clear();
                    }
                    
                    public Long2BooleanMap.Entry first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Long2BooleanMap.Entry last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Long2BooleanMap.Entry> subSet(final Long2BooleanMap.Entry from, final Long2BooleanMap.Entry to) {
                        return Submap.this.subMap(from.getLongKey(), to.getLongKey()).long2BooleanEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Long2BooleanMap.Entry> headSet(final Long2BooleanMap.Entry to) {
                        return Submap.this.headMap(to.getLongKey()).long2BooleanEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Long2BooleanMap.Entry> tailSet(final Long2BooleanMap.Entry from) {
                        return Submap.this.tailMap(from.getLongKey()).long2BooleanEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public BooleanCollection values() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/longs/Long2BooleanRBTreeMap$Submap.values:Lit/unimi/dsi/fastutil/booleans/BooleanCollection;
            //     4: ifnonnull       19
            //     7: aload_0         /* this */
            //     8: new             Lit/unimi/dsi/fastutil/longs/Long2BooleanRBTreeMap$Submap$2;
            //    11: dup            
            //    12: aload_0         /* this */
            //    13: invokespecial   it/unimi/dsi/fastutil/longs/Long2BooleanRBTreeMap$Submap$2.<init>:(Lit/unimi/dsi/fastutil/longs/Long2BooleanRBTreeMap$Submap;)V
            //    16: putfield        it/unimi/dsi/fastutil/longs/Long2BooleanRBTreeMap$Submap.values:Lit/unimi/dsi/fastutil/booleans/BooleanCollection;
            //    19: aload_0         /* this */
            //    20: getfield        it/unimi/dsi/fastutil/longs/Long2BooleanRBTreeMap$Submap.values:Lit/unimi/dsi/fastutil/booleans/BooleanCollection;
            //    23: areturn        
            //    StackMapTable: 00 01 13
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 9
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
            //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
            //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public boolean containsKey(final long k) {
            return this.in(k) && Long2BooleanRBTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final boolean v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final boolean ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean get(final long k) {
            final long kk = k;
            final Long2BooleanRBTreeMap.Entry e;
            return (this.in(kk) && (e = Long2BooleanRBTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public boolean put(final long k, final boolean v) {
            Long2BooleanRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final boolean oldValue = Long2BooleanRBTreeMap.this.put(k, v);
            return Long2BooleanRBTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public boolean remove(final long k) {
            Long2BooleanRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final boolean oldValue = Long2BooleanRBTreeMap.this.remove(k);
            return Long2BooleanRBTreeMap.this.modified ? oldValue : this.defRetValue;
        }
        
        public int size() {
            final SubmapIterator i = new SubmapIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextEntry();
            }
            return n;
        }
        
        public boolean isEmpty() {
            return !new SubmapIterator().hasNext();
        }
        
        public LongComparator comparator() {
            return Long2BooleanRBTreeMap.this.actualComparator;
        }
        
        public Long2BooleanSortedMap headMap(final long to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Long2BooleanRBTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Long2BooleanSortedMap tailMap(final long from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Long2BooleanRBTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Long2BooleanSortedMap subMap(long from, long to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Long2BooleanRBTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Long2BooleanRBTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Long2BooleanRBTreeMap.Entry firstEntry() {
            if (Long2BooleanRBTreeMap.this.tree == null) {
                return null;
            }
            Long2BooleanRBTreeMap.Entry e;
            if (this.bottom) {
                e = Long2BooleanRBTreeMap.this.firstEntry;
            }
            else {
                e = Long2BooleanRBTreeMap.this.locateKey(this.from);
                if (Long2BooleanRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Long2BooleanRBTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Long2BooleanRBTreeMap.Entry lastEntry() {
            if (Long2BooleanRBTreeMap.this.tree == null) {
                return null;
            }
            Long2BooleanRBTreeMap.Entry e;
            if (this.top) {
                e = Long2BooleanRBTreeMap.this.lastEntry;
            }
            else {
                e = Long2BooleanRBTreeMap.this.locateKey(this.to);
                if (Long2BooleanRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Long2BooleanRBTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public long firstLongKey() {
            final Long2BooleanRBTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public long lastLongKey() {
            final Long2BooleanRBTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractLong2BooleanSortedMap.KeySet {
            @Override
            public LongBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public LongBidirectionalIterator iterator(final long from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final long k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Long2BooleanRBTreeMap this$0 = submap.this$0;
                            final Long2BooleanRBTreeMap.Entry lastEntry = submap.lastEntry();
                            this.prev = lastEntry;
                            if (this$0.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = submap.this$0.locateKey(k);
                        if (submap.this$0.compare(this.next.key, k) <= 0) {
                            this.prev = this.next;
                            this.next = this.next.next();
                        }
                        else {
                            this.prev = this.next.prev();
                        }
                    }
                }
            }
            
            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!Submap.this.bottom && this.prev != null && Long2BooleanRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Long2BooleanRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Long2BooleanMap.Entry> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final long k) {
                super(k);
            }
            
            public Long2BooleanMap.Entry next() {
                return this.nextEntry();
            }
            
            public Long2BooleanMap.Entry previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements LongListIterator {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final long from) {
                super(from);
            }
            
            public long nextLong() {
                return this.nextEntry().key;
            }
            
            public long previousLong() {
                return this.previousEntry().key;
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements BooleanListIterator {
            public boolean nextBoolean() {
                return this.nextEntry().value;
            }
            
            public boolean previousBoolean() {
                return this.previousEntry().value;
            }
        }
    }
}
