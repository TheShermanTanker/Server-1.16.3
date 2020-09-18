package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
interface PatternCompiler {
    CommonPattern compile(final String string);
}
