package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public interface FloatSortedSet extends FloatSet, SortedSet<Float>, FloatBidirectionalIterable {
    FloatBidirectionalIterator iterator(final float float1);
    
    FloatBidirectionalIterator iterator();
    
    FloatSortedSet subSet(final float float1, final float float2);
    
    FloatSortedSet headSet(final float float1);
    
    FloatSortedSet tailSet(final float float1);
    
    FloatComparator comparator();
    
    float firstFloat();
    
    float lastFloat();
    
    @Deprecated
    default FloatSortedSet subSet(final Float from, final Float to) {
        return this.subSet((float)from, (float)to);
    }
    
    @Deprecated
    default FloatSortedSet headSet(final Float to) {
        return this.headSet((float)to);
    }
    
    @Deprecated
    default FloatSortedSet tailSet(final Float from) {
        return this.tailSet((float)from);
    }
    
    @Deprecated
    default Float first() {
        return this.firstFloat();
    }
    
    @Deprecated
    default Float last() {
        return this.lastFloat();
    }
}
