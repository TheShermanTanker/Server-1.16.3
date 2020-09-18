package org.apache.commons.io.comparator;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.Comparator;

abstract class AbstractFileComparator implements Comparator<File> {
    public File[] sort(final File... files) {
        if (files != null) {
            Arrays.sort((Object[])files, (Comparator)this);
        }
        return files;
    }
    
    public List<File> sort(final List<File> files) {
        if (files != null) {
            Collections.sort((List)files, (Comparator)this);
        }
        return files;
    }
    
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
