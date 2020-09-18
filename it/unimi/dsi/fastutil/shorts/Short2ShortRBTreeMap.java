package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Short2ShortRBTreeMap extends AbstractShort2ShortSortedMap implements Serializable, Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Short2ShortMap.Entry> entries;
    protected transient ShortSortedSet keys;
    protected transient ShortCollection values;
    protected transient boolean modified;
    protected Comparator<? super Short> storedComparator;
    protected transient ShortComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;
    
    public Short2ShortRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = ShortComparators.asShortComparator(this.storedComparator);
    }
    
    public Short2ShortRBTreeMap(final Comparator<? super Short> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Short2ShortRBTreeMap(final Map<? extends Short, ? extends Short> m) {
        this();
        this.putAll(m);
    }
    
    public Short2ShortRBTreeMap(final SortedMap<Short, Short> m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Short2ShortRBTreeMap(final Short2ShortMap m) {
        this();
        this.putAll(m);
    }
    
    public Short2ShortRBTreeMap(final Short2ShortSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Short2ShortRBTreeMap(final short[] k, final short[] v, final Comparator<? super Short> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Short2ShortRBTreeMap(final short[] k, final short[] v) {
        this(k, v, null);
    }
    
    final int compare(final short k1, final short k2) {
        return (this.actualComparator == null) ? Short.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry findKey(final short k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final short k) {
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
    
    public short addTo(final short k, final short incr) {
        final Entry e = this.add(k);
        final short oldValue = e.value;
        final Entry entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    public short put(final short k, final short v) {
        final Entry e = this.add(k);
        final short oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry add(final short k) {
        this.modified = false;
        int maxDepth = 0;
        Entry e = null;
        Label_0908: {
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
                    break Label_0908;
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
    
    public short remove(final short k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry p = this.tree;
        int i = 0;
        final short kk = k;
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
    
    public boolean containsValue(final short v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final short ev = i.nextShort();
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
    
    public boolean containsKey(final short k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public short get(final short k) {
        final Entry e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public short firstShortKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public short lastShortKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Short2ShortMap.Entry>() {
                final Comparator<? super Short2ShortMap.Entry> comparator = (x, y) -> Short2ShortRBTreeMap.this.actualComparator.compare(x.getShortKey(), y.getShortKey());
                
                public Comparator<? super Short2ShortMap.Entry> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Short2ShortMap.Entry> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Short2ShortMap.Entry> iterator(final Short2ShortMap.Entry from) {
                    return new EntryIterator(from.getShortKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                        return false;
                    }
                    final Entry f = Short2ShortRBTreeMap.this.findKey((short)e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                        return false;
                    }
                    final Entry f = Short2ShortRBTreeMap.this.findKey((short)e.getKey());
                    if (f == null || f.getShortValue() != (short)e.getValue()) {
                        return false;
                    }
                    Short2ShortRBTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Short2ShortRBTreeMap.this.count;
                }
                
                public void clear() {
                    Short2ShortRBTreeMap.this.clear();
                }
                
                public Short2ShortMap.Entry first() {
                    return Short2ShortRBTreeMap.this.firstEntry;
                }
                
                public Short2ShortMap.Entry last() {
                    return Short2ShortRBTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Short2ShortMap.Entry> subSet(final Short2ShortMap.Entry from, final Short2ShortMap.Entry to) {
                    return Short2ShortRBTreeMap.this.subMap(from.getShortKey(), to.getShortKey()).short2ShortEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Short2ShortMap.Entry> headSet(final Short2ShortMap.Entry to) {
                    return Short2ShortRBTreeMap.this.headMap(to.getShortKey()).short2ShortEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Short2ShortMap.Entry> tailSet(final Short2ShortMap.Entry from) {
                    return Short2ShortRBTreeMap.this.tailMap(from.getShortKey()).short2ShortEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public ShortSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ShortCollection values() {
        if (this.values == null) {
            this.values = new AbstractShortCollection() {
                @Override
                public ShortIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final short k) {
                    return Short2ShortRBTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Short2ShortRBTreeMap.this.count;
                }
                
                public void clear() {
                    Short2ShortRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public ShortComparator comparator() {
        return this.actualComparator;
    }
    
    public Short2ShortSortedMap headMap(final short to) {
        return new Submap((short)0, true, to, false);
    }
    
    public Short2ShortSortedMap tailMap(final short from) {
        return new Submap(from, false, (short)0, true);
    }
    
    public Short2ShortSortedMap subMap(final short from, final short to) {
        return new Submap(from, false, to, false);
    }
    
    public Short2ShortRBTreeMap clone() {
        Short2ShortRBTreeMap c;
        try {
            c = (Short2ShortRBTreeMap)super.clone();
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
            s.writeShort((int)e.key);
            s.writeShort((int)e.value);
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readShort(), s.readShort());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readShort(), s.readShort());
            top.black(true);
            top.right(new Entry(s.readShort(), s.readShort()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry top2 = new Entry();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readShort();
        top2.value = s.readShort();
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
            super((short)0, (short)0);
        }
        
        Entry(final short k, final short v) {
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
        public short setValue(final short value) {
            final short oldValue = this.value;
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
            final Map.Entry<Short, Short> e = (Map.Entry<Short, Short>)o;
            return this.key == (short)e.getKey() && this.value == (short)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append((int)this.key).append("=>").append((int)this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Short2ShortRBTreeMap.this.firstEntry;
        }
        
        TreeIterator(final short k) {
            this.index = 0;
            final Entry locateKey = Short2ShortRBTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Short2ShortRBTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Short2ShortRBTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Short2ShortMap.Entry> {
        EntryIterator() {
        }
        
        EntryIterator(final short k) {
            super(k);
        }
        
        public Short2ShortMap.Entry next() {
            return this.nextEntry();
        }
        
        public Short2ShortMap.Entry previous() {
            return this.previousEntry();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements ShortListIterator {
        public KeyIterator() {
        }
        
        public KeyIterator(final short k) {
            super(k);
        }
        
        public short nextShort() {
            return this.nextEntry().key;
        }
        
        public short previousShort() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractShort2ShortSortedMap.KeySet {
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements ShortListIterator {
        public short nextShort() {
            return this.nextEntry().value;
        }
        
        public short previousShort() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractShort2ShortSortedMap implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        short from;
        short to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Short2ShortMap.Entry> entries;
        protected transient ShortSortedSet keys;
        protected transient ShortCollection values;
        final /* synthetic */ Short2ShortRBTreeMap this$0;
        
        public Submap(final short from, final boolean bottom, final short to, final boolean top) {
            if (!bottom && !top && Short2ShortRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append((int)from).append(") is larger than end key (").append((int)to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Short2ShortRBTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final short k) {
            return (this.bottom || Short2ShortRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Short2ShortRBTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Short2ShortMap.Entry>() {
                    @Override
                    public ObjectBidirectionalIterator<Short2ShortMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Short2ShortMap.Entry> iterator(final Short2ShortMap.Entry from) {
                        return new SubmapEntryIterator(from.getShortKey());
                    }
                    
                    public Comparator<? super Short2ShortMap.Entry> comparator() {
                        return Short2ShortRBTreeMap.this.short2ShortEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                            return false;
                        }
                        final Short2ShortRBTreeMap.Entry f = Short2ShortRBTreeMap.this.findKey((short)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        // 
                        // This method could not be decompiled.
                        // 
                        // Original Bytecode:
                        // 
                        //     1: instanceof      Ljava/util/Map$Entry;
                        //     4: ifne            9
                        //     7: iconst_0       
                        //     8: ireturn        
                        //     9: aload_1         /* o */
                        //    10: checkcast       Ljava/util/Map$Entry;
                        //    13: astore_2        /* e */
                        //    14: aload_2         /* e */
                        //    15: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
                        //    20: ifnull          35
                        //    23: aload_2         /* e */
                        //    24: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
                        //    29: instanceof      Ljava/lang/Short;
                        //    32: ifne            37
                        //    35: iconst_0       
                        //    36: ireturn        
                        //    37: aload_2         /* e */
                        //    38: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
                        //    43: ifnull          58
                        //    46: aload_2         /* e */
                        //    47: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
                        //    52: instanceof      Ljava/lang/Short;
                        //    55: ifne            60
                        //    58: iconst_0       
                        //    59: ireturn        
                        //    60: aload_0         /* this */
                        //    61: getfield        it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap$1.this$1:Lit/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap;
                        //    64: getfield        it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap;
                        //    67: aload_2         /* e */
                        //    68: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
                        //    73: checkcast       Ljava/lang/Short;
                        //    76: invokevirtual   java/lang/Short.shortValue:()S
                        //    79: invokevirtual   it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap.findKey:(S)Lit/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Entry;
                        //    82: astore_3        /* f */
                        //    83: aload_3         /* f */
                        //    84: ifnull          113
                        //    87: aload_0         /* this */
                        //    88: getfield        it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap$1.this$1:Lit/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap;
                        //    91: aload_3         /* f */
                        //    92: getfield        it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Entry.key:S
                        //    95: invokevirtual   it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap.in:(S)Z
                        //    98: ifeq            113
                        //   101: aload_0         /* this */
                        //   102: getfield        it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap$1.this$1:Lit/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap;
                        //   105: aload_3         /* f */
                        //   106: getfield        it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Entry.key:S
                        //   109: invokevirtual   it/unimi/dsi/fastutil/shorts/Short2ShortRBTreeMap$Submap.remove:(S)S
                        //   112: pop            
                        //   113: aload_3         /* f */
                        //   114: ifnull          121
                        //   117: iconst_1       
                        //   118: goto            122
                        //   121: iconst_0       
                        //   122: ireturn        
                        //    MethodParameters:
                        //  Name  Flags  
                        //  ----  -----
                        //  o     
                        //    StackMapTable: 00 08 09 FC 00 19 07 00 15 01 14 01 FC 00 34 07 00 19 07 40 01
                        // 
                        // The error that occurred was:
                        // 
                        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
                        //     at java.base/java.util.Vector.get(Vector.java:749)
                        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.isContextWithinType(AstBuilder.java:428)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:340)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:173)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:169)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:547)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:439)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
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
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:440)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
                    
                    public Short2ShortMap.Entry first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Short2ShortMap.Entry last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ShortMap.Entry> subSet(final Short2ShortMap.Entry from, final Short2ShortMap.Entry to) {
                        return Submap.this.subMap(from.getShortKey(), to.getShortKey()).short2ShortEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ShortMap.Entry> headSet(final Short2ShortMap.Entry to) {
                        return Submap.this.headMap(to.getShortKey()).short2ShortEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ShortMap.Entry> tailSet(final Short2ShortMap.Entry from) {
                        return Submap.this.tailMap(from.getShortKey()).short2ShortEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public ShortCollection values() {
            if (this.values == null) {
                this.values = new AbstractShortCollection() {
                    @Override
                    public ShortIterator iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final short k) {
                        return Submap.this.containsValue(k);
                    }
                    
                    public int size() {
                        return Submap.this.size();
                    }
                    
                    public void clear() {
                        Submap.this.clear();
                    }
                };
            }
            return this.values;
        }
        
        public boolean containsKey(final short k) {
            return this.in(k) && Short2ShortRBTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final short v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final short ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        public short get(final short k) {
            final short kk = k;
            final Short2ShortRBTreeMap.Entry e;
            return (this.in(kk) && (e = Short2ShortRBTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public short put(final short k, final short v) {
            Short2ShortRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append((int)k).append(") out of range [").append(this.bottom ? "-" : String.valueOf((int)this.from)).append(", ").append(this.top ? "-" : String.valueOf((int)this.to)).append(")").toString());
            }
            final short oldValue = Short2ShortRBTreeMap.this.put(k, v);
            return Short2ShortRBTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public short remove(final short k) {
            Short2ShortRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final short oldValue = Short2ShortRBTreeMap.this.remove(k);
            return Short2ShortRBTreeMap.this.modified ? oldValue : this.defRetValue;
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
        
        public ShortComparator comparator() {
            return Short2ShortRBTreeMap.this.actualComparator;
        }
        
        public Short2ShortSortedMap headMap(final short to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Short2ShortRBTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Short2ShortSortedMap tailMap(final short from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Short2ShortRBTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Short2ShortSortedMap subMap(short from, short to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Short2ShortRBTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Short2ShortRBTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Short2ShortRBTreeMap.Entry firstEntry() {
            if (Short2ShortRBTreeMap.this.tree == null) {
                return null;
            }
            Short2ShortRBTreeMap.Entry e;
            if (this.bottom) {
                e = Short2ShortRBTreeMap.this.firstEntry;
            }
            else {
                e = Short2ShortRBTreeMap.this.locateKey(this.from);
                if (Short2ShortRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Short2ShortRBTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Short2ShortRBTreeMap.Entry lastEntry() {
            if (Short2ShortRBTreeMap.this.tree == null) {
                return null;
            }
            Short2ShortRBTreeMap.Entry e;
            if (this.top) {
                e = Short2ShortRBTreeMap.this.lastEntry;
            }
            else {
                e = Short2ShortRBTreeMap.this.locateKey(this.to);
                if (Short2ShortRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Short2ShortRBTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public short firstShortKey() {
            final Short2ShortRBTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public short lastShortKey() {
            final Short2ShortRBTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractShort2ShortSortedMap.KeySet {
            @Override
            public ShortBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public ShortBidirectionalIterator iterator(final short from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final short k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Short2ShortRBTreeMap this$0 = submap.this$0;
                            final Short2ShortRBTreeMap.Entry lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Short2ShortRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Short2ShortRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Short2ShortMap.Entry> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final short k) {
                super(k);
            }
            
            public Short2ShortMap.Entry next() {
                return this.nextEntry();
            }
            
            public Short2ShortMap.Entry previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements ShortListIterator {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final short from) {
                super(from);
            }
            
            public short nextShort() {
                return this.nextEntry().key;
            }
            
            public short previousShort() {
                return this.previousEntry().key;
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements ShortListIterator {
            public short nextShort() {
                return this.nextEntry().value;
            }
            
            public short previousShort() {
                return this.previousEntry().value;
            }
        }
    }
}
