package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileFilter;

public interface IOFileFilter extends FileFilter, FilenameFilter {
    boolean accept(final File file);
    
    boolean accept(final File file, final String string);
}
