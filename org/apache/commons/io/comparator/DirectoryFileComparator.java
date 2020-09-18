package org.apache.commons.io.comparator;

import java.util.List;
import java.io.File;
import java.util.Comparator;
import java.io.Serializable;

public class DirectoryFileComparator extends AbstractFileComparator implements Serializable {
    private static final long serialVersionUID = 296132640160964395L;
    public static final Comparator<File> DIRECTORY_COMPARATOR;
    public static final Comparator<File> DIRECTORY_REVERSE;
    
    public int compare(final File file1, final File file2) {
        return this.getType(file1) - this.getType(file2);
    }
    
    private int getType(final File file) {
        if (file.isDirectory()) {
            return 1;
        }
        return 2;
    }
    
    static {
        DIRECTORY_COMPARATOR = (Comparator)new DirectoryFileComparator();
        DIRECTORY_REVERSE = (Comparator)new ReverseComparator(DirectoryFileComparator.DIRECTORY_COMPARATOR);
    }
}
