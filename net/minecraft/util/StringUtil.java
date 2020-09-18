package net.minecraft.util;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class StringUtil {
    private static final Pattern STRIP_COLOR_PATTERN;
    
    public static boolean isNullOrEmpty(@Nullable final String string) {
        return StringUtils.isEmpty((CharSequence)string);
    }
    
    static {
        STRIP_COLOR_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
    }
}
