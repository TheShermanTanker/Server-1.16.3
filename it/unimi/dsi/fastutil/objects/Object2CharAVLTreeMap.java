package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.chars.CharListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.io.Serializable;

public class Object2CharAVLTreeMap<K> extends AbstractObject2CharSortedMap<K> implements Serializable, Cloneable {
    protected transient Entry<K> tree;
    protected int count;
    protected transient Entry<K> firstEntry;
    protected transient Entry<K> lastEntry;
    protected transient ObjectSortedSet<Object2CharMap.Entry<K>> entries;
    protected transient ObjectSortedSet<K> keys;
    protected transient CharCollection values;
    protected transient boolean modified;
    protected Comparator<? super K> storedComparator;
    protected transient Comparator<? super K> actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    
    public Object2CharAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = this.storedComparator;
    }
    
    public Object2CharAVLTreeMap(final Comparator<? super K> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Object2CharAVLTreeMap(final Map<? extends K, ? extends Character> m) {
        this();
        this.putAll(m);
    }
    
    public Object2CharAVLTreeMap(final SortedMap<K, Character> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends Character>)m);
    }
    
    public Object2CharAVLTreeMap(final Object2CharMap<? extends K> m) {
        this();
        this.putAll((java.util.Map<? extends K, ? extends Character>)m);
    }
    
    public Object2CharAVLTreeMap(final Object2CharSortedMap<K> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends Character>)m);
    }
    
    public Object2CharAVLTreeMap(final K[] k, final char[] v, final Comparator<? super K> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2CharAVLTreeMap(final K[] k, final char[] v) {
        this(k, v, null);
    }
    
    final int compare(final K k1, final K k2) {
        return (this.actualComparator == null) ? ((Comparable)k1).compareTo(k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry<K> findKey(final K k) {
        Entry<K> e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry<K> locateKey(final K k) {
        Entry<K> e = this.tree;
        Entry<K> last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }
    
    public char addTo(final K k, final char incr) {
        final Entry<K> e = this.add(k);
        final char oldValue = e.value;
        final Entry<K> entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    public char put(final K k, final char v) {
        final Entry<K> e = this.add(k);
        final char oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<K> add(final K k) {
        this.modified = false;
        Entry<K> e = null;
        if (this.tree != null) {
            Entry<K> p = this.tree;
            Entry<K> q = null;
            Entry<K> y = this.tree;
            Entry<K> z = null;
            Entry<K> w = null;
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
                    e = new Entry<K>(k, this.defRetValue);
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
                    e = new Entry<K>(k, this.defRetValue);
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
                    final Entry<K> x = y.left;
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
                    final Entry<K> x = y.right;
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
        final Entry<K> tree = new Entry<K>(k, this.defRetValue);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        e = tree;
        this.modified = true;
        return e;
    }
    
    private Entry<K> parent(final Entry<K> e) {
        if (e == this.tree) {
            return null;
        }
        Entry<K> y = e;
        Entry<K> x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry<K> p = x.left;
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
        Entry<K> p = y.right;
        if (p == null || p.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            p = x.left;
        }
        return p;
    }
    
    public char removeChar(final Object k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry<K> p = this.tree;
        Entry<K> q = null;
        boolean dir = false;
        final K kk = (K)k;
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
            Entry<K> r = p.right;
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
                Entry<K> s;
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
            final Entry<K> y = q;
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
                final Entry<K> x = y.right;
                assert x != null;
                if (x.balance() == -1) {
                    assert x.balance() == -1;
                    final Entry<K> w = x.left;
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
                final Entry<K> x = y.left;
                assert x != null;
                if (x.balance() == 1) {
                    assert x.balance() == 1;
                    final Entry<K> w = x.right;
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
    
    public boolean containsValue(final char v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final char ev = i.nextChar();
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
        final Entry<K> entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    public boolean containsKey(final Object k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public char getChar(final Object k) {
        final Entry<K> e = (Entry<K>)this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public K firstKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public K lastKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Object2CharMap.Entry<K>> object2CharEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Object2CharMap.Entry<K>>() {
                final Comparator<? super Object2CharMap.Entry<K>> comparator = (x, y) -> Object2CharAVLTreeMap.this.actualComparator.compare(x.getKey(), y.getKey());
                
                public Comparator<? super Object2CharMap.Entry<K>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2CharMap.Entry<K>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2CharMap.Entry<K>> iterator(final Object2CharMap.Entry<K> from) {
                    return new EntryIterator((K)from.getKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                        return false;
                    }
                    final Entry<K> f = (Entry<K>)Object2CharAVLTreeMap.this.findKey(e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                        return false;
                    }
                    final Entry<K> f = (Entry<K>)Object2CharAVLTreeMap.this.findKey(e.getKey());
                    if (f == null || f.getCharValue() != (char)e.getValue()) {
                        return false;
                    }
                    Object2CharAVLTreeMap.this.removeChar(f.key);
                    return true;
                }
                
                public int size() {
                    return Object2CharAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Object2CharAVLTreeMap.this.clear();
                }
                
                public Object2CharMap.Entry<K> first() {
                    return Object2CharAVLTreeMap.this.firstEntry;
                }
                
                public Object2CharMap.Entry<K> last() {
                    return Object2CharAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Object2CharMap.Entry<K>> subSet(final Object2CharMap.Entry<K> from, final Object2CharMap.Entry<K> to) {
                    return (ObjectSortedSet<Object2CharMap.Entry<K>>)Object2CharAVLTreeMap.this.subMap(from.getKey(), to.getKey()).object2CharEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2CharMap.Entry<K>> headSet(final Object2CharMap.Entry<K> to) {
                    return (ObjectSortedSet<Object2CharMap.Entry<K>>)Object2CharAVLTreeMap.this.headMap(to.getKey()).object2CharEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2CharMap.Entry<K>> tailSet(final Object2CharMap.Entry<K> from) {
                    return (ObjectSortedSet<Object2CharMap.Entry<K>>)Object2CharAVLTreeMap.this.tailMap(from.getKey()).object2CharEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        if (this.keys == null) {
            this.keys = (ObjectSortedSet<K>)new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public CharCollection values() {
        if (this.values == null) {
            this.values = new AbstractCharCollection() {
                @Override
                public CharIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final char k) {
                    return Object2CharAVLTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Object2CharAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Object2CharAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public Comparator<? super K> comparator() {
        return this.actualComparator;
    }
    
    public Object2CharSortedMap<K> headMap(final K to) {
        return new Submap(null, true, to, false);
    }
    
    public Object2CharSortedMap<K> tailMap(final K from) {
        return new Submap(from, false, null, true);
    }
    
    public Object2CharSortedMap<K> subMap(final K from, final K to) {
        return new Submap(from, false, to, false);
    }
    
    public Object2CharAVLTreeMap<K> clone() {
        Object2CharAVLTreeMap<K> c;
        try {
            c = (Object2CharAVLTreeMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            final Entry<K> rp = new Entry<K>();
            final Entry<K> rq = new Entry<K>();
            Entry<K> p = rp;
            rp.left(this.tree);
            Entry<K> q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry<K> e = p.left.clone();
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
                    final Entry<K> e = p.right.clone();
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
            final Entry<K> e = i.nextEntry();
            s.writeObject(e.key);
            s.writeChar((int)e.value);
        }
    }
    
    private Entry<K> readTree(final ObjectInputStream s, final int n, final Entry<K> pred, final Entry<K> succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry<K> top = new Entry<K>((K)s.readObject(), s.readChar());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry<K> top = new Entry<K>((K)s.readObject(), s.readChar());
            top.right(new Entry<K>((K)s.readObject(), s.readChar()));
            top.right.pred(top);
            top.balance(1);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry<K> top2 = new Entry<K>();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = (K)s.readObject();
        top2.value = s.readChar();
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
            Entry<K> e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static final class Entry<K> extends BasicEntry<K> implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        Entry<K> left;
        Entry<K> right;
        int info;
        
        Entry() {
            super(null, '\0');
        }
        
        Entry(final K k, final char v) {
            super(k, v);
            this.info = -1073741824;
        }
        
        Entry<K> left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry<K> right() {
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
        
        void pred(final Entry<K> pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry<K> succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry<K> left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry<K> right) {
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
        
        Entry<K> next() {
            Entry<K> next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry<K> prev() {
            Entry<K> prev = this.left;
            if ((this.info & 0x40000000) == 0x0) {
                while ((prev.info & Integer.MIN_VALUE) == 0x0) {
                    prev = prev.right;
                }
            }
            return prev;
        }
        
        @Override
        public char setValue(final char value) {
            final char oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public Entry<K> clone() {
            Entry<K> c;
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
            final Map.Entry<K, Character> e = (Map.Entry<K, Character>)o;
            return Objects.equals(this.key, e.getKey()) && this.value == (char)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.value;
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append(this.key).append("=>").append(this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry<K> prev;
        Entry<K> next;
        Entry<K> curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Object2CharAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final K k) {
            this.index = 0;
            final Entry<K> locateKey = Object2CharAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Object2CharAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
        
        Entry<K> nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry<K> next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry<K> previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry<K> prev = this.prev;
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
            final Entry<K> curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Object2CharAVLTreeMap.this.removeChar(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Object2CharMap.Entry<K>> {
        EntryIterator() {
        }
        
        EntryIterator(final K k) {
            super(k);
        }
        
        public Object2CharMap.Entry<K> next() {
            return this.nextEntry();
        }
        
        public Object2CharMap.Entry<K> previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Object2CharMap.Entry<K> ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Object2CharMap.Entry<K> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements ObjectListIterator<K> {
        public KeyIterator() {
        }
        
        public KeyIterator(final K k) {
            super(k);
        }
        
        public K next() {
            return this.nextEntry().key;
        }
        
        public K previous() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractObject2CharSortedMap.KeySet {
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements CharListIterator {
        public char nextChar() {
            return this.nextEntry().value;
        }
        
        public char previousChar() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractObject2CharSortedMap<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        K from;
        K to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Object2CharMap.Entry<K>> entries;
        protected transient ObjectSortedSet<K> keys;
        protected transient CharCollection values;
        final /* synthetic */ Object2CharAVLTreeMap this$0;
        
        public Submap(final K from, final boolean bottom, final K to, final boolean top) {
            if (!bottom && !top && Object2CharAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Object2CharAVLTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final K k) {
            return (this.bottom || Object2CharAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Object2CharAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Object2CharMap.Entry<K>> object2CharEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Object2CharMap.Entry<K>>() {
                    @Override
                    public ObjectBidirectionalIterator<Object2CharMap.Entry<K>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Object2CharMap.Entry<K>> iterator(final Object2CharMap.Entry<K> from) {
                        return new SubmapEntryIterator((K)from.getKey());
                    }
                    
                    public Comparator<? super Object2CharMap.Entry<K>> comparator() {
                        return Object2CharAVLTreeMap.this.object2CharEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                            return false;
                        }
                        final Object2CharAVLTreeMap.Entry<K> f = (Object2CharAVLTreeMap.Entry<K>)Object2CharAVLTreeMap.this.findKey(e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                            return false;
                        }
                        final Object2CharAVLTreeMap.Entry<K> f = (Object2CharAVLTreeMap.Entry<K>)Object2CharAVLTreeMap.this.findKey(e.getKey());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.removeChar(f.key);
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
                    
                    public Object2CharMap.Entry<K> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Object2CharMap.Entry<K> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2CharMap.Entry<K>> subSet(final Object2CharMap.Entry<K> from, final Object2CharMap.Entry<K> to) {
                        return (ObjectSortedSet<Object2CharMap.Entry<K>>)Submap.this.subMap(from.getKey(), to.getKey()).object2CharEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2CharMap.Entry<K>> headSet(final Object2CharMap.Entry<K> to) {
                        return (ObjectSortedSet<Object2CharMap.Entry<K>>)Submap.this.headMap(to.getKey()).object2CharEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2CharMap.Entry<K>> tailSet(final Object2CharMap.Entry<K> from) {
                        return (ObjectSortedSet<Object2CharMap.Entry<K>>)Submap.this.tailMap(from.getKey()).object2CharEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = (ObjectSortedSet<K>)new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public CharCollection values() {
            if (this.values == null) {
                this.values = new AbstractCharCollection() {
                    @Override
                    public CharIterator iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final char k) {
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
        
        public boolean containsKey(final Object k) {
            return this.in(k) && Object2CharAVLTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final char v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final char ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        public char getChar(final Object k) {
            final K kk = (K)k;
            final Object2CharAVLTreeMap.Entry<K> e;
            return (this.in(kk) && (e = Object2CharAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public char put(final K k, final char v) {
            Object2CharAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final char oldValue = Object2CharAVLTreeMap.this.put(k, v);
            return Object2CharAVLTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public char removeChar(final Object k) {
            Object2CharAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final char oldValue = Object2CharAVLTreeMap.this.removeChar(k);
            return Object2CharAVLTreeMap.this.modified ? oldValue : this.defRetValue;
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
        
        public Comparator<? super K> comparator() {
            return Object2CharAVLTreeMap.this.actualComparator;
        }
        
        public Object2CharSortedMap<K> headMap(final K to) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.top:Z
            //     4: ifeq            29
            //     7: new             Lit/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap;
            //    10: dup            
            //    11: aload_0         /* this */
            //    12: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap;
            //    15: aload_0         /* this */
            //    16: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.from:Ljava/lang/Object;
            //    19: aload_0         /* this */
            //    20: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.bottom:Z
            //    23: aload_1         /* to */
            //    24: iconst_0       
            //    25: invokespecial   it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.<init>:(Lit/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap;Ljava/lang/Object;ZLjava/lang/Object;Z)V
            //    28: areturn        
            //    29: aload_0         /* this */
            //    30: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap;
            //    33: aload_1         /* to */
            //    34: aload_0         /* this */
            //    35: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.to:Ljava/lang/Object;
            //    38: invokevirtual   it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap.compare:(Ljava/lang/Object;Ljava/lang/Object;)I
            //    41: ifge            68
            //    44: new             Lit/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap;
            //    47: dup            
            //    48: aload_0         /* this */
            //    49: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.this$0:Lit/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap;
            //    52: aload_0         /* this */
            //    53: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.from:Ljava/lang/Object;
            //    56: aload_0         /* this */
            //    57: getfield        it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.bottom:Z
            //    60: aload_1         /* to */
            //    61: iconst_0       
            //    62: invokespecial   it/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap$Submap.<init>:(Lit/unimi/dsi/fastutil/objects/Object2CharAVLTreeMap;Ljava/lang/Object;ZLjava/lang/Object;Z)V
            //    65: goto            69
            //    68: aload_0         /* this */
            //    69: areturn        
            //    Signature:
            //  (TK;)Lit/unimi/dsi/fastutil/objects/Object2CharSortedMap<TK;>;
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  to    
            //    StackMapTable: 00 03 1D 26 40 07 00 DD
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2715)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2691)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferInitObject(TypeAnalysis.java:1923)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1306)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
        
        public Object2CharSortedMap<K> tailMap(final K from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Object2CharAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Object2CharSortedMap<K> subMap(K from, K to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Object2CharAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Object2CharAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Object2CharAVLTreeMap.Entry<K> firstEntry() {
            if (Object2CharAVLTreeMap.this.tree == null) {
                return null;
            }
            Object2CharAVLTreeMap.Entry<K> e;
            if (this.bottom) {
                e = Object2CharAVLTreeMap.this.firstEntry;
            }
            else {
                e = Object2CharAVLTreeMap.this.locateKey(this.from);
                if (Object2CharAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Object2CharAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Object2CharAVLTreeMap.Entry<K> lastEntry() {
            if (Object2CharAVLTreeMap.this.tree == null) {
                return null;
            }
            Object2CharAVLTreeMap.Entry<K> e;
            if (this.top) {
                e = Object2CharAVLTreeMap.this.lastEntry;
            }
            else {
                e = Object2CharAVLTreeMap.this.locateKey(this.to);
                if (Object2CharAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Object2CharAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public K firstKey() {
            final Object2CharAVLTreeMap.Entry<K> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public K lastKey() {
            final Object2CharAVLTreeMap.Entry<K> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractObject2CharSortedMap.KeySet {
            @Override
            public ObjectBidirectionalIterator<K> iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public ObjectBidirectionalIterator<K> iterator(final K from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final K k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Object2CharAVLTreeMap this$0 = submap.this$0;
                            final Object2CharAVLTreeMap.Entry<K> lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Object2CharAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Object2CharAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Object2CharMap.Entry<K>> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final K k) {
                super(k);
            }
            
            public Object2CharMap.Entry<K> next() {
                return this.nextEntry();
            }
            
            public Object2CharMap.Entry<K> previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements ObjectListIterator<K> {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final K from) {
                super(from);
            }
            
            public K next() {
                return this.nextEntry().key;
            }
            
            public K previous() {
                return this.previousEntry().key;
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements CharListIterator {
            public char nextChar() {
                return this.nextEntry().value;
            }
            
            public char previousChar() {
                return this.previousEntry().value;
            }
        }
    }
}
