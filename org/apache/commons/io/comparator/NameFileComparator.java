package org.apache.commons.io.comparator;

import java.util.List;
import org.apache.commons.io.IOCase;
import java.io.File;
import java.util.Comparator;
import java.io.Serializable;

public class NameFileComparator extends AbstractFileComparator implements Serializable {
    private static final long serialVersionUID = 8397947749814525798L;
    public static final Comparator<File> NAME_COMPARATOR;
    public static final Comparator<File> NAME_REVERSE;
    public static final Comparator<File> NAME_INSENSITIVE_COMPARATOR;
    public static final Comparator<File> NAME_INSENSITIVE_REVERSE;
    public static final Comparator<File> NAME_SYSTEM_COMPARATOR;
    public static final Comparator<File> NAME_SYSTEM_REVERSE;
    private final IOCase caseSensitivity;
    
    public NameFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }
    
    public NameFileComparator(final IOCase caseSensitivity) {
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    public int compare(final File file1, final File file2) {
        return this.caseSensitivity.checkCompareTo(file1.getName(), file2.getName());
    }
    
    @Override
    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
    
    static {
        NAME_COMPARATOR = (Comparator)new NameFileComparator();
        NAME_REVERSE = (Comparator)new ReverseComparator(NameFileComparator.NAME_COMPARATOR);
        NAME_INSENSITIVE_COMPARATOR = (Comparator)new NameFileComparator(IOCase.INSENSITIVE);
        NAME_INSENSITIVE_REVERSE = (Comparator)new ReverseComparator(NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
        NAME_SYSTEM_COMPARATOR = (Comparator)new NameFileComparator(IOCase.SYSTEM);
        NAME_SYSTEM_REVERSE = (Comparator)new ReverseComparator(NameFileComparator.NAME_SYSTEM_COMPARATOR);
    }
}
