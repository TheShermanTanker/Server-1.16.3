package org.apache.commons.io.monitor;

import java.io.File;

public interface FileAlterationListener {
    void onStart(final FileAlterationObserver fileAlterationObserver);
    
    void onDirectoryCreate(final File file);
    
    void onDirectoryChange(final File file);
    
    void onDirectoryDelete(final File file);
    
    void onFileCreate(final File file);
    
    void onFileChange(final File file);
    
    void onFileDelete(final File file);
    
    void onStop(final FileAlterationObserver fileAlterationObserver);
}
