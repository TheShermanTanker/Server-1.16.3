package io.netty.handler.codec.smtp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class SmtpUtils {
    static List<CharSequence> toUnmodifiableList(final CharSequence... sequences) {
        if (sequences == null || sequences.length == 0) {
            return (List<CharSequence>)Collections.emptyList();
        }
        return (List<CharSequence>)Collections.unmodifiableList(Arrays.asList((Object[])sequences));
    }
    
    private SmtpUtils() {
    }
}
