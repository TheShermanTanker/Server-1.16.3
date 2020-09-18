package org.apache.logging.log4j.core.pattern;

public interface TextRenderer {
    void render(final String string1, final StringBuilder stringBuilder, final String string3);
    
    void render(final StringBuilder stringBuilder1, final StringBuilder stringBuilder2);
}
