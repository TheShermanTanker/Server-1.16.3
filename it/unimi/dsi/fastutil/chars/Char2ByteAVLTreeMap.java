package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Char2ByteAVLTreeMap extends AbstractChar2ByteSortedMap implements Serializable, Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Char2ByteMap.Entry> entries;
    protected transient CharSortedSet keys;
    protected transient ByteCollection values;
    protected transient boolean modified;
    protected Comparator<? super Character> storedComparator;
    protected transient CharComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    
    public Char2ByteAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = CharComparators.asCharComparator(this.storedComparator);
    }
    
    public Char2ByteAVLTreeMap(final Comparator<? super Character> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Char2ByteAVLTreeMap(final Map<? extends Character, ? extends Byte> m) {
        this();
        this.putAll(m);
    }
    
    public Char2ByteAVLTreeMap(final SortedMap<Character, Byte> m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Char2ByteAVLTreeMap(final Char2ByteMap m) {
        this();
        this.putAll(m);
    }
    
    public Char2ByteAVLTreeMap(final Char2ByteSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Char2ByteAVLTreeMap(final char[] k, final byte[] v, final Comparator<? super Character> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Char2ByteAVLTreeMap(final char[] k, final byte[] v) {
        this(k, v, null);
    }
    
    final int compare(final char k1, final char k2) {
        return (this.actualComparator == null) ? Character.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry findKey(final char k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final char k) {
        Entry e = this.tree;
        Entry last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }
    
    public byte addTo(final char k, final byte incr) {
        final Entry e = this.add(k);
        final byte oldValue = e.value;
        final Entry entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    public byte put(final char k, final byte v) {
        final Entry e = this.add(k);
        final byte oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry add(final char k) {
        this.modified = false;
        Entry e = null;
        if (this.tree != null) {
            Entry p = this.tree;
            Entry q = null;
            Entry y = this.tree;
            Entry z = null;
            Entry w = null;
            int i = 0;
            int cmp;
            while ((cmp = this.compare(k, p.key)) != 0) {
                if (p.balance() != 0) {
                    i = 0;
                    z = q;
                    y = p;
                }
                final boolean[] dirPath = this.dirPath;
                final int n = i++;
                final boolean b = cmp > 0;
                dirPath[n] = b;
                if (b) {
                    if (!p.succ()) {
                        q = p;
                        p = p.right;
                        continue;
                    }
                    ++this.count;
                    e = new Entry(k, this.defRetValue);
                    this.modified = true;
                    if (p.right == null) {
                        this.lastEntry = e;
                    }
                    e.left = p;
                    e.right = p.right;
                    p.right(e);
                }
                else {
                    if (!p.pred()) {
                        q = p;
                        p = p.left;
                        continue;
                    }
                    ++this.count;
                    e = new Entry(k, this.defRetValue);
                    this.modified = true;
                    if (p.left == null) {
                        this.firstEntry = e;
                    }
                    e.right = p;
                    e.left = p.left;
                    p.left(e);
                }
                for (p = y, i = 0; p != e; p = (this.dirPath[i++] ? p.right : p.left)) {
                    if (this.dirPath[i]) {
                        p.incBalance();
                    }
                    else {
                        p.decBalance();
                    }
                }
                if (y.balance() == -2) {
                    final Entry x = y.left;
                    if (x.balance() == -1) {
                        w = x;
                        if (x.succ()) {
                            x.succ(false);
                            y.pred(x);
                        }
                        else {
                            y.left = x.right;
                        }
                        x.right = y;
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert x.balance() == 1;
                        w = x.right;
                        x.right = w.left;
                        w.left = x;
                        y.left = w.right;
                        w.right = y;
                        if (w.balance() == -1) {
                            x.balance(0);
                            y.balance(1);
                        }
                        else if (w.balance() == 0) {
                            x.balance(0);
                            y.balance(0);
                        }
                        else {
                            x.balance(-1);
                            y.balance(0);
                        }
                        w.balance(0);
                        if (w.pred()) {
                            x.succ(w);
                            w.pred(false);
                        }
                        if (w.succ()) {
                            y.pred(w);
                            w.succ(false);
                        }
                    }
                }
                else {
                    if (y.balance() != 2) {
                        return e;
                    }
                    final Entry x = y.right;
                    if (x.balance() == 1) {
                        w = x;
                        if (x.pred()) {
                            x.pred(false);
                            y.succ(x);
                        }
                        else {
                            y.right = x.left;
                        }
                        x.left = y;
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert x.balance() == -1;
                        w = x.left;
                        x.left = w.right;
                        w.right = x;
                        y.right = w.left;
                        w.left = y;
                        if (w.balance() == 1) {
                            x.balance(0);
                            y.balance(-1);
                        }
                        else if (w.balance() == 0) {
                            x.balance(0);
                            y.balance(0);
                        }
                        else {
                            x.balance(1);
                            y.balance(0);
                        }
                        w.balance(0);
                        if (w.pred()) {
                            y.succ(w);
                            w.pred(false);
                        }
                        if (w.succ()) {
                            x.pred(w);
                            w.succ(false);
                        }
                    }
                }
                if (z == null) {
                    this.tree = w;
                    return e;
                }
                if (z.left == y) {
                    z.left = w;
                    return e;
                }
                z.right = w;
                return e;
            }
            return p;
        }
        ++this.count;
        final Entry tree = new Entry(k, this.defRetValue);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        e = tree;
        this.modified = true;
        return e;
    }
    
    private Entry parent(final Entry e) {
        if (e == this.tree) {
            return null;
        }
        Entry y = e;
        Entry x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry p = x.left;
                if (p == null || p.right != e) {
                    while (!y.succ()) {
                        y = y.right;
                    }
                    p = y.right;
                }
                return p;
            }
            x = x.left;
            y = y.right;
        }
        Entry p = y.right;
        if (p == null || p.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            p = x.left;
        }
        return p;
    }
    
    public byte remove(final char k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry p = this.tree;
        Entry q = null;
        boolean dir = false;
        final char kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            if (dir = (cmp > 0)) {
                q = p;
                if ((p = p.right()) == null) {
                    return this.defRetValue;
                }
                continue;
            }
            else {
                q = p;
                if ((p = p.left()) == null) {
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
                if (q != null) {
                    if (dir) {
                        q.succ(p.right);
                    }
                    else {
                        q.pred(p.left);
                    }
                }
                else {
                    this.tree = (dir ? p.right : p.left);
                }
            }
            else {
                p.prev().right = p.right;
                if (q != null) {
                    if (dir) {
                        q.right = p.left;
                    }
                    else {
                        q.left = p.left;
                    }
                }
                else {
                    this.tree = p.left;
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
                if (q != null) {
                    if (dir) {
                        q.right = r;
                    }
                    else {
                        q.left = r;
                    }
                }
                else {
                    this.tree = r;
                }
                r.balance(p.balance());
                q = r;
                dir = true;
            }
            else {
                Entry s;
                while (true) {
                    s = r.left;
                    if (s.pred()) {
                        break;
                    }
                    r = s;
                }
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
                s.right = p.right;
                s.succ(false);
                if (q != null) {
                    if (dir) {
                        q.right = s;
                    }
                    else {
                        q.left = s;
                    }
                }
                else {
                    this.tree = s;
                }
                s.balance(p.balance());
                q = r;
                dir = false;
            }
        }
        while (q != null) {
            final Entry y = q;
            q = this.parent(y);
            if (!dir) {
                dir = (q != null && q.left != y);
                y.incBalance();
                if (y.balance() == 1) {
                    break;
                }
                if (y.balance() != 2) {
                    continue;
                }
                final Entry x = y.right;
                assert x != null;
                if (x.balance() == -1) {
                    assert x.balance() == -1;
                    final Entry w = x.left;
                    x.left = w.right;
                    w.right = x;
                    y.right = w.left;
                    w.left = y;
                    if (w.balance() == 1) {
                        x.balance(0);
                        y.balance(-1);
                    }
                    else if (w.balance() == 0) {
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert w.balance() == -1;
                        x.balance(1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        y.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        x.pred(w);
                        w.succ(false);
                    }
                    if (q != null) {
                        if (dir) {
                            q.right = w;
                        }
                        else {
                            q.left = w;
                        }
                    }
                    else {
                        this.tree = w;
                    }
                }
                else {
                    if (q != null) {
                        if (dir) {
                            q.right = x;
                        }
                        else {
                            q.left = x;
                        }
                    }
                    else {
                        this.tree = x;
                    }
                    if (x.balance() == 0) {
                        y.right = x.left;
                        x.left = y;
                        x.balance(-1);
                        y.balance(1);
                        break;
                    }
                    assert x.balance() == 1;
                    if (x.pred()) {
                        y.succ(true);
                        x.pred(false);
                    }
                    else {
                        y.right = x.left;
                    }
                    (x.left = y).balance(0);
                    x.balance(0);
                }
            }
            else {
                dir = (q != null && q.left != y);
                y.decBalance();
                if (y.balance() == -1) {
                    break;
                }
                if (y.balance() != -2) {
                    continue;
                }
                final Entry x = y.left;
                assert x != null;
                if (x.balance() == 1) {
                    assert x.balance() == 1;
                    final Entry w = x.right;
                    x.right = w.left;
                    w.left = x;
                    y.left = w.right;
                    w.right = y;
                    if (w.balance() == -1) {
                        x.balance(0);
                        y.balance(1);
                    }
                    else if (w.balance() == 0) {
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert w.balance() == 1;
                        x.balance(-1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        x.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        y.pred(w);
                        w.succ(false);
                    }
                    if (q != null) {
                        if (dir) {
                            q.right = w;
                        }
                        else {
                            q.left = w;
                        }
                    }
                    else {
                        this.tree = w;
                    }
                }
                else {
                    if (q != null) {
                        if (dir) {
                            q.right = x;
                        }
                        else {
                            q.left = x;
                        }
                    }
                    else {
                        this.tree = x;
                    }
                    if (x.balance() == 0) {
                        y.left = x.right;
                        x.right = y;
                        x.balance(1);
                        y.balance(-1);
                        break;
                    }
                    assert x.balance() == -1;
                    if (x.succ()) {
                        y.pred(true);
                        x.succ(false);
                    }
                    else {
                        y.left = x.right;
                    }
                    (x.right = y).balance(0);
                    x.balance(0);
                }
            }
        }
        this.modified = true;
        --this.count;
        return p.value;
    }
    
    public boolean containsValue(final byte v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final byte ev = i.nextByte();
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
    
    public boolean containsKey(final char k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public byte get(final char k) {
        final Entry e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public char firstCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public char lastCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Char2ByteMap.Entry>() {
                final Comparator<? super Char2ByteMap.Entry> comparator = (x, y) -> Char2ByteAVLTreeMap.this.actualComparator.compare(x.getCharKey(), y.getCharKey());
                
                public Comparator<? super Char2ByteMap.Entry> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(final Char2ByteMap.Entry from) {
                    return new EntryIterator(from.getCharKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                        return false;
                    }
                    final Entry f = Char2ByteAVLTreeMap.this.findKey((char)e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                        return false;
                    }
                    final Entry f = Char2ByteAVLTreeMap.this.findKey((char)e.getKey());
                    if (f == null || f.getByteValue() != (byte)e.getValue()) {
                        return false;
                    }
                    Char2ByteAVLTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Char2ByteAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Char2ByteAVLTreeMap.this.clear();
                }
                
                public Char2ByteMap.Entry first() {
                    return Char2ByteAVLTreeMap.this.firstEntry;
                }
                
                public Char2ByteMap.Entry last() {
                    return Char2ByteAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> subSet(final Char2ByteMap.Entry from, final Char2ByteMap.Entry to) {
                    return Char2ByteAVLTreeMap.this.subMap(from.getCharKey(), to.getCharKey()).char2ByteEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> headSet(final Char2ByteMap.Entry to) {
                    return Char2ByteAVLTreeMap.this.headMap(to.getCharKey()).char2ByteEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> tailSet(final Char2ByteMap.Entry from) {
                    return Char2ByteAVLTreeMap.this.tailMap(from.getCharKey()).char2ByteEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public CharSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ByteCollection values() {
        if (this.values == null) {
            this.values = new AbstractByteCollection() {
                @Override
                public ByteIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final byte k) {
                    return Char2ByteAVLTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Char2ByteAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Char2ByteAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public CharComparator comparator() {
        return this.actualComparator;
    }
    
    public Char2ByteSortedMap headMap(final char to) {
        return new Submap('\0', true, to, false);
    }
    
    public Char2ByteSortedMap tailMap(final char from) {
        return new Submap(from, false, '\0', true);
    }
    
    public Char2ByteSortedMap subMap(final char from, final char to) {
        return new Submap(from, false, to, false);
    }
    
    public Char2ByteAVLTreeMap clone() {
        Char2ByteAVLTreeMap c;
        try {
            c = (Char2ByteAVLTreeMap)super.clone();
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
            s.writeChar((int)e.key);
            s.writeByte((int)e.value);
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readChar(), s.readByte());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readChar(), s.readByte());
            top.right(new Entry(s.readChar(), s.readByte()));
            top.right.pred(top);
            top.balance(1);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry top2 = new Entry();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readChar();
        top2.value = s.readByte();
        top2.right(this.readTree(s, rightN, top2, succ));
        if (n == (n & -n)) {
            top2.balance(1);
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
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
            super('\0', (byte)0);
        }
        
        Entry(final char k, final byte v) {
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
        
        int balance() {
            return (byte)this.info;
        }
        
        void balance(final int level) {
            this.info &= 0xFFFFFF00;
            this.info |= (level & 0xFF);
        }
        
        void incBalance() {
            this.info = ((this.info & 0xFFFFFF00) | ((byte)this.info + 1 & 0xFF));
        }
        
        protected void decBalance() {
            this.info = ((this.info & 0xFFFFFF00) | ((byte)this.info - 1 & 0xFF));
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
        public byte setValue(final byte value) {
            final byte oldValue = this.value;
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
            final Map.Entry<Character, Byte> e = (Map.Entry<Character, Byte>)o;
            return this.key == (char)e.getKey() && this.value == (byte)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append(this.key).append("=>").append((int)this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Char2ByteAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final char k) {
            this.index = 0;
            final Entry locateKey = Char2ByteAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Char2ByteAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Char2ByteAVLTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Char2ByteMap.Entry> {
        EntryIterator() {
        }
        
        EntryIterator(final char k) {
            super(k);
        }
        
        public Char2ByteMap.Entry next() {
            return this.nextEntry();
        }
        
        public Char2ByteMap.Entry previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Char2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Char2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements CharListIterator {
        public KeyIterator() {
        }
        
        public KeyIterator(final char k) {
            super(k);
        }
        
        public char nextChar() {
            return this.nextEntry().key;
        }
        
        public char previousChar() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractChar2ByteSortedMap.KeySet {
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements ByteListIterator {
        public byte nextByte() {
            return this.nextEntry().value;
        }
        
        public byte previousByte() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractChar2ByteSortedMap implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        char from;
        char to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Char2ByteMap.Entry> entries;
        protected transient CharSortedSet keys;
        protected transient ByteCollection values;
        final /* synthetic */ Char2ByteAVLTreeMap this$0;
        
        public Submap(final char from, final boolean bottom, final char to, final boolean top) {
            if (!bottom && !top && Char2ByteAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Char2ByteAVLTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final char k) {
            return (this.bottom || Char2ByteAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Char2ByteAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Char2ByteMap.Entry>() {
                    @Override
                    public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(final Char2ByteMap.Entry from) {
                        return new SubmapEntryIterator(from.getCharKey());
                    }
                    
                    public Comparator<? super Char2ByteMap.Entry> comparator() {
                        return Char2ByteAVLTreeMap.this.char2ByteEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                            return false;
                        }
                        final Char2ByteAVLTreeMap.Entry f = Char2ByteAVLTreeMap.this.findKey((char)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                            return false;
                        }
                        final Char2ByteAVLTreeMap.Entry f = Char2ByteAVLTreeMap.this.findKey((char)e.getKey());
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
                    
                    public Char2ByteMap.Entry first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Char2ByteMap.Entry last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> subSet(final Char2ByteMap.Entry from, final Char2ByteMap.Entry to) {
                        return Submap.this.subMap(from.getCharKey(), to.getCharKey()).char2ByteEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> headSet(final Char2ByteMap.Entry to) {
                        return Submap.this.headMap(to.getCharKey()).char2ByteEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> tailSet(final Char2ByteMap.Entry from) {
                        return Submap.this.tailMap(from.getCharKey()).char2ByteEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public ByteCollection values() {
            if (this.values == null) {
                this.values = new AbstractByteCollection() {
                    @Override
                    public ByteIterator iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final byte k) {
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
        
        public boolean containsKey(final char k) {
            return this.in(k) && Char2ByteAVLTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final byte v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final byte ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        public byte get(final char k) {
            final char kk = k;
            final Char2ByteAVLTreeMap.Entry e;
            return (this.in(kk) && (e = Char2ByteAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public byte put(final char k, final byte v) {
            Char2ByteAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final byte oldValue = Char2ByteAVLTreeMap.this.put(k, v);
            return Char2ByteAVLTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public byte remove(final char k) {
            Char2ByteAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final byte oldValue = Char2ByteAVLTreeMap.this.remove(k);
            return Char2ByteAVLTreeMap.this.modified ? oldValue : this.defRetValue;
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
        
        public CharComparator comparator() {
            return Char2ByteAVLTreeMap.this.actualComparator;
        }
        
        public Char2ByteSortedMap headMap(final char to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Char2ByteAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Char2ByteSortedMap tailMap(final char from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Char2ByteAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Char2ByteSortedMap subMap(char from, char to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Char2ByteAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Char2ByteAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Char2ByteAVLTreeMap.Entry firstEntry() {
            if (Char2ByteAVLTreeMap.this.tree == null) {
                return null;
            }
            Char2ByteAVLTreeMap.Entry e;
            if (this.bottom) {
                e = Char2ByteAVLTreeMap.this.firstEntry;
            }
            else {
                e = Char2ByteAVLTreeMap.this.locateKey(this.from);
                if (Char2ByteAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Char2ByteAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Char2ByteAVLTreeMap.Entry lastEntry() {
            if (Char2ByteAVLTreeMap.this.tree == null) {
                return null;
            }
            Char2ByteAVLTreeMap.Entry e;
            if (this.top) {
                e = Char2ByteAVLTreeMap.this.lastEntry;
            }
            else {
                e = Char2ByteAVLTreeMap.this.locateKey(this.to);
                if (Char2ByteAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Char2ByteAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public char firstCharKey() {
            final Char2ByteAVLTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public char lastCharKey() {
            final Char2ByteAVLTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractChar2ByteSortedMap.KeySet {
            @Override
            public CharBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public CharBidirectionalIterator iterator(final char from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final char k) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     2: invokespecial   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.<init>:(Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap;)V
                //     5: aload_0         /* this */
                //     6: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //     9: ifnull          146
                //    12: aload_1        
                //    13: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap.bottom:Z
                //    16: ifne            45
                //    19: aload_1        
                //    20: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap;
                //    23: iload_2         /* k */
                //    24: aload_0         /* this */
                //    25: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //    28: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry.key:C
                //    31: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap.compare:(CC)I
                //    34: ifge            45
                //    37: aload_0         /* this */
                //    38: aconst_null    
                //    39: putfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.prev:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //    42: goto            146
                //    45: aload_1        
                //    46: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap.top:Z
                //    49: ifne            83
                //    52: aload_1        
                //    53: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap;
                //    56: iload_2         /* k */
                //    57: aload_0         /* this */
                //    58: aload_1        
                //    59: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap.lastEntry:()Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //    62: dup_x1         
                //    63: putfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.prev:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //    66: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry.key:C
                //    69: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap.compare:(CC)I
                //    72: iflt            83
                //    75: aload_0         /* this */
                //    76: aconst_null    
                //    77: putfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //    80: goto            146
                //    83: aload_0         /* this */
                //    84: aload_1        
                //    85: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap;
                //    88: iload_2         /* k */
                //    89: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap.locateKey:(C)Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //    92: putfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //    95: aload_1        
                //    96: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap;
                //    99: aload_0         /* this */
                //   100: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   103: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry.key:C
                //   106: iload_2         /* k */
                //   107: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap.compare:(CC)I
                //   110: ifgt            135
                //   113: aload_0         /* this */
                //   114: aload_0         /* this */
                //   115: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   118: putfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.prev:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   121: aload_0         /* this */
                //   122: aload_0         /* this */
                //   123: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   126: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry.next:()Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   129: putfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   132: goto            146
                //   135: aload_0         /* this */
                //   136: aload_0         /* this */
                //   137: getfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.next:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   140: invokevirtual   it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   143: putfield        it/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Submap$SubmapIterator.prev:Lit/unimi/dsi/fastutil/chars/Char2ByteAVLTreeMap$Entry;
                //   146: return         
                //    MethodParameters:
                //  Name    Flags  
                //  ------  -----
                //  submap  
                //  k       
                //    StackMapTable: 00 04 FF 00 2D 00 03 07 00 02 07 00 07 01 00 00 25 33 0A
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
                //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
            
            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!Submap.this.bottom && this.prev != null && Char2ByteAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Char2ByteAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Char2ByteMap.Entry> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final char k) {
                super(k);
            }
            
            public Char2ByteMap.Entry next() {
                return this.nextEntry();
            }
            
            public Char2ByteMap.Entry previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements CharListIterator {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final char from) {
                super(from);
            }
            
            public char nextChar() {
                return this.nextEntry().key;
            }
            
            public char previousChar() {
                return this.previousEntry().key;
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements ByteListIterator {
            public byte nextByte() {
                return this.nextEntry().value;
            }
            
            public byte previousByte() {
                return this.previousEntry().value;
            }
        }
    }
}
