package org.apache.logging.log4j.core.appender.rolling.action;

import java.util.Objects;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;

public class PathWithAttributes {
    private final Path path;
    private final BasicFileAttributes attributes;
    
    public PathWithAttributes(final Path path, final BasicFileAttributes attributes) {
        this.path = (Path)Objects.requireNonNull(path, "path");
        this.attributes = (BasicFileAttributes)Objects.requireNonNull(attributes, "attributes");
    }
    
    public String toString() {
        return new StringBuilder().append(this.path).append(" (modified: ").append(this.attributes.lastModifiedTime()).append(")").toString();
    }
    
    public Path getPath() {
        return this.path;
    }
    
    public BasicFileAttributes getAttributes() {
        return this.attributes;
    }
}
