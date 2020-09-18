package org.apache.logging.log4j.core.appender.rolling.action;

import java.util.Comparator;
import java.util.Collections;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public class SortingVisitor extends SimpleFileVisitor<Path> {
    private final PathSorter sorter;
    private final List<PathWithAttributes> collected;
    
    public SortingVisitor(final PathSorter sorter) {
        this.collected = (List<PathWithAttributes>)new ArrayList();
        this.sorter = (PathSorter)Objects.requireNonNull(sorter, "sorter");
    }
    
    public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
        this.collected.add(new PathWithAttributes(path, attrs));
        return FileVisitResult.CONTINUE;
    }
    
    public List<PathWithAttributes> getSortedPaths() {
        Collections.sort((List)this.collected, (Comparator)this.sorter);
        return this.collected;
    }
}
