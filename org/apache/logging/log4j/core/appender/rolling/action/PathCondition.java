package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;

public interface PathCondition {
    void beforeFileTreeWalk();
    
    boolean accept(final Path path1, final Path path2, final BasicFileAttributes basicFileAttributes);
}
