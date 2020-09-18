package org.apache.commons.io.serialization;

import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

final class FullClassNameMatcher implements ClassNameMatcher {
    private final Set<String> classesSet;
    
    public FullClassNameMatcher(final String... classes) {
        this.classesSet = (Set<String>)Collections.unmodifiableSet((Set)new HashSet((Collection)Arrays.asList((Object[])classes)));
    }
    
    public boolean matches(final String className) {
        return this.classesSet.contains(className);
    }
}
