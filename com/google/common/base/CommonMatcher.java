package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonMatcher {
    abstract boolean matches();
    
    abstract boolean find();
    
    abstract boolean find(final int integer);
    
    abstract String replaceAll(final String string);
    
    abstract int end();
    
    abstract int start();
}
