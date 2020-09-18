package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface ReferenceBigList<K> extends BigList<K>, ReferenceCollection<K>, Size64 {
    ObjectBigListIterator<K> iterator();
    
    ObjectBigListIterator<K> listIterator();
    
    ObjectBigListIterator<K> listIterator(final long long1);
    
    ReferenceBigList<K> subList(final long long1, final long long2);
    
    void getElements(final long long1, final Object[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final K[][] arr);
    
    void addElements(final long long1, final K[][] arr, final long long3, final long long4);
}
