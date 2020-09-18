package it.unimi.dsi.fastutil.bytes;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Comparator;
import java.io.Serializable;

public class ByteAVLTreeSet extends AbstractByteSortedSet implements Serializable, Cloneable, ByteSortedSet {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected Comparator<? super Byte> storedComparator;
    protected transient ByteComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353130L;
    private transient boolean[] dirPath;
    
    public ByteAVLTreeSet() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = ByteComparators.asByteComparator(this.storedComparator);
    }
    
    public ByteAVLTreeSet(final Comparator<? super Byte> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public ByteAVLTreeSet(final Collection<? extends Byte> c) {
        this();
        this.addAll((Collection)c);
    }
    
    public ByteAVLTreeSet(final SortedSet<Byte> s) {
        this(s.comparator());
        this.addAll((Collection)s);
    }
    
    public ByteAVLTreeSet(final ByteCollection c) {
        this();
        this.addAll(c);
    }
    
    public ByteAVLTreeSet(final ByteSortedSet s) {
        this(s.comparator());
        this.addAll(s);
    }
    
    public ByteAVLTreeSet(final ByteIterator i) {
        this.allocatePaths();
        while (i.hasNext()) {
            this.add(i.nextByte());
        }
    }
    
    public ByteAVLTreeSet(final Iterator<?> i) {
        this(ByteIterators.asByteIterator(i));
    }
    
    public ByteAVLTreeSet(final byte[] a, final int offset, final int length, final Comparator<? super Byte> c) {
        this(c);
        ByteArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public ByteAVLTreeSet(final byte[] a, final int offset, final int length) {
        this(a, offset, length, null);
    }
    
    public ByteAVLTreeSet(final byte[] a) {
        this();
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }
    
    public ByteAVLTreeSet(final byte[] a, final Comparator<? super Byte> c) {
        this(c);
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }
    
    final int compare(final byte k1, final byte k2) {
        return (this.actualComparator == null) ? Byte.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    private Entry findKey(final byte k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final byte k) {
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
    
    public boolean add(final byte k) {
        if (this.tree != null) {
            Entry p = this.tree;
            Entry q = null;
            Entry y = this.tree;
            Entry z = null;
            Entry e = null;
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
                    e = new Entry(k);
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
                    e = new Entry(k);
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
                        return true;
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
                    return true;
                }
                if (z.left == y) {
                    z.left = w;
                    return true;
                }
                z.right = w;
                return true;
            }
            return false;
        }
        ++this.count;
        final Entry tree = new Entry(k);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        return true;
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
    
    public boolean remove(final byte k) {
        if (this.tree == null) {
            return false;
        }
        Entry p = this.tree;
        Entry q = null;
        boolean dir = false;
        final byte kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            if (dir = (cmp > 0)) {
                q = p;
                if ((p = p.right()) == null) {
                    return false;
                }
                continue;
            }
            else {
                q = p;
                if ((p = p.left()) == null) {
                    return false;
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
        --this.count;
        return true;
    }
    
    public boolean contains(final byte k) {
        return this.findKey(k) != null;
    }
    
    public void clear() {
        this.count = 0;
        this.tree = null;
        final Entry entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public byte firstByte() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public byte lastByte() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    @Override
    public ByteBidirectionalIterator iterator() {
        return new SetIterator();
    }
    
    public ByteBidirectionalIterator iterator(final byte from) {
        return new SetIterator(from);
    }
    
    public ByteComparator comparator() {
        return this.actualComparator;
    }
    
    public ByteSortedSet headSet(final byte to) {
        return new Subset((byte)0, true, to, false);
    }
    
    public ByteSortedSet tailSet(final byte from) {
        return new Subset(from, false, (byte)0, true);
    }
    
    public ByteSortedSet subSet(final byte from, final byte to) {
        return new Subset(from, false, to, false);
    }
    
    public Object clone() {
        ByteAVLTreeSet c;
        try {
            c = (ByteAVLTreeSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
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
        final SetIterator i = new SetIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            s.writeByte((int)i.nextByte());
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readByte());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readByte());
            top.right(new Entry(s.readByte()));
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
        top2.key = s.readByte();
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
    
    private static final class Entry implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        byte key;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
        }
        
        Entry(final byte k) {
            this.key = k;
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
        
        public Entry clone() {
            Entry c;
            try {
                c = (Entry)super.clone();
            }
            catch (CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.info = this.info;
            return c;
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry e = (Entry)o;
            return this.key == e.key;
        }
        
        public int hashCode() {
            return this.key;
        }
        
        public String toString() {
            return String.valueOf((int)this.key);
        }
    }
    
    private class SetIterator implements ByteListIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        SetIterator() {
            this.index = 0;
            this.next = ByteAVLTreeSet.this.firstEntry;
        }
        
        SetIterator(final byte k) {
            this.index = 0;
            final Entry locateKey = ByteAVLTreeSet.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (ByteAVLTreeSet.this.compare(this.next.key, k) <= 0) {
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
        
        public byte nextByte() {
            return this.nextEntry().key;
        }
        
        public byte previousByte() {
            return this.previousEntry().key;
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
            ByteAVLTreeSet.this.remove(this.curr.key);
            this.curr = null;
        }
    }
    
    private final class Subset extends AbstractByteSortedSet implements Serializable, ByteSortedSet {
        private static final long serialVersionUID = -7046029254386353129L;
        byte from;
        byte to;
        boolean bottom;
        boolean top;
        final /* synthetic */ ByteAVLTreeSet this$0;
        
        public Subset(final ByteAVLTreeSet byteAVLTreeSet, final byte from, final boolean bottom, final byte to, final boolean top) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1        
            //     2: putfield        it/unimi/dsi/fastutil/bytes/ByteAVLTreeSet$Subset.this$0:Lit/unimi/dsi/fastutil/bytes/ByteAVLTreeSet;
            //     5: aload_0         /* this */
            //     6: invokespecial   it/unimi/dsi/fastutil/bytes/AbstractByteSortedSet.<init>:()V
            //     9: iload_3         /* bottom */
            //    10: ifne            70
            //    13: iload           top
            //    15: ifne            70
            //    18: aload_1        
            //    19: iload_2         /* from */
            //    20: iload           to
            //    22: invokevirtual   it/unimi/dsi/fastutil/bytes/ByteAVLTreeSet.compare:(BB)I
            //    25: ifle            70
            //    28: new             Ljava/lang/IllegalArgumentException;
            //    31: dup            
            //    32: new             Ljava/lang/StringBuilder;
            //    35: dup            
            //    36: invokespecial   java/lang/StringBuilder.<init>:()V
            //    39: ldc             "Start element ("
            //    41: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    44: iload_2         /* from */
            //    45: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
            //    48: ldc             ") is larger than end element ("
            //    50: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    53: iload           to
            //    55: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
            //    58: ldc             ")"
            //    60: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    63: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    66: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
            //    69: athrow         
            //    70: aload_0         /* this */
            //    71: iload_2         /* from */
            //    72: putfield        it/unimi/dsi/fastutil/bytes/ByteAVLTreeSet$Subset.from:B
            //    75: aload_0         /* this */
            //    76: iload_3         /* bottom */
            //    77: putfield        it/unimi/dsi/fastutil/bytes/ByteAVLTreeSet$Subset.bottom:Z
            //    80: aload_0         /* this */
            //    81: iload           to
            //    83: putfield        it/unimi/dsi/fastutil/bytes/ByteAVLTreeSet$Subset.to:B
            //    86: aload_0         /* this */
            //    87: iload           top
            //    89: putfield        it/unimi/dsi/fastutil/bytes/ByteAVLTreeSet$Subset.top:Z
            //    92: return         
            //    MethodParameters:
            //  Name            Flags  
            //  --------------  -----
            //  byteAVLTreeSet  
            //  from            
            //  bottom          
            //  to              
            //  top             
            //    StackMapTable: 00 01 FF 00 46 00 06 07 00 02 07 00 0B 01 01 01 01 00 00
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.AstOptimizer$MakeAssignmentExpressionsOptimization.run(AstOptimizer.java:2912)
            //     at com.strobel.decompiler.ast.AstOptimizer.runOptimization(AstOptimizer.java:3876)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:214)
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
        
        public void clear() {
            final SubsetIterator i = new SubsetIterator();
            while (i.hasNext()) {
                i.nextByte();
                i.remove();
            }
        }
        
        final boolean in(final byte k) {
            return (this.bottom || this.this$0.compare(k, this.from) >= 0) && (this.top || this.this$0.compare(k, this.to) < 0);
        }
        
        public boolean contains(final byte k) {
            return this.in(k) && this.this$0.contains(k);
        }
        
        public boolean add(final byte k) {
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Element (").append((int)k).append(") out of range [").append(this.bottom ? "-" : String.valueOf((int)this.from)).append(", ").append(this.top ? "-" : String.valueOf((int)this.to)).append(")").toString());
            }
            return this.this$0.add(k);
        }
        
        public boolean remove(final byte k) {
            return this.in(k) && this.this$0.remove(k);
        }
        
        public int size() {
            final SubsetIterator i = new SubsetIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextByte();
            }
            return n;
        }
        
        public boolean isEmpty() {
            return !new SubsetIterator().hasNext();
        }
        
        public ByteComparator comparator() {
            return this.this$0.actualComparator;
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new SubsetIterator();
        }
        
        public ByteBidirectionalIterator iterator(final byte from) {
            return new SubsetIterator(from);
        }
        
        public ByteSortedSet headSet(final byte to) {
            if (this.top) {
                return this.this$0.new Subset(this.from, this.bottom, to, false);
            }
            return (this.this$0.compare(to, this.to) < 0) ? this.this$0.new Subset(this.from, this.bottom, to, false) : this;
        }
        
        public ByteSortedSet tailSet(final byte from) {
            if (this.bottom) {
                return this.this$0.new Subset(from, false, this.to, this.top);
            }
            return (this.this$0.compare(from, this.from) > 0) ? this.this$0.new Subset(from, false, this.to, this.top) : this;
        }
        
        public ByteSortedSet subSet(byte from, byte to) {
            if (this.top && this.bottom) {
                return this.this$0.new Subset(from, false, to, false);
            }
            if (!this.top) {
                to = ((this.this$0.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((this.this$0.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return this.this$0.new Subset(from, false, to, false);
        }
        
        public Entry firstEntry() {
            if (this.this$0.tree == null) {
                return null;
            }
            Entry e;
            if (this.bottom) {
                e = this.this$0.firstEntry;
            }
            else {
                e = this.this$0.locateKey(this.from);
                if (this.this$0.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && this.this$0.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Entry lastEntry() {
            if (this.this$0.tree == null) {
                return null;
            }
            Entry e;
            if (this.top) {
                e = this.this$0.lastEntry;
            }
            else {
                e = this.this$0.locateKey(this.to);
                if (this.this$0.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && this.this$0.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public byte firstByte() {
            final Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public byte lastByte() {
            final Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private final class SubsetIterator extends SetIterator {
            SubsetIterator() {
                Subset.this.this$0.super();
                this.next = Subset.this.firstEntry();
            }
            
            SubsetIterator(final Subset subset, final byte k) {
                this(subset);
                if (this.next != null) {
                    if (!subset.bottom && subset.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!subset.top) {
                            final ByteAVLTreeSet this$0 = subset.this$0;
                            final Entry lastEntry = subset.lastEntry();
                            this.prev = lastEntry;
                            if (this$0.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = subset.this$0.locateKey(k);
                        if (subset.this$0.compare(this.next.key, k) <= 0) {
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
                if (!Subset.this.bottom && this.prev != null && Subset.this.this$0.compare(this.prev.key, Subset.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Subset.this.top && this.next != null && Subset.this.this$0.compare(this.next.key, Subset.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
    }
}
