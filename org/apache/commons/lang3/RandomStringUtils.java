package org.apache.commons.lang3;

import java.util.Random;

public class RandomStringUtils {
    private static final Random RANDOM;
    
    public static String random(final int count) {
        return random(count, false, false);
    }
    
    public static String randomAscii(final int count) {
        return random(count, 32, 127, false, false);
    }
    
    public static String randomAscii(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAscii(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
    }
    
    public static String randomAlphabetic(final int count) {
        return random(count, true, false);
    }
    
    public static String randomAlphabetic(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAlphabetic(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
    }
    
    public static String randomAlphanumeric(final int count) {
        return random(count, true, true);
    }
    
    public static String randomAlphanumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAlphanumeric(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
    }
    
    public static String randomGraph(final int count) {
        return random(count, 33, 126, false, false);
    }
    
    public static String randomGraph(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomGraph(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
    }
    
    public static String randomNumeric(final int count) {
        return random(count, false, true);
    }
    
    public static String randomNumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomNumeric(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
    }
    
    public static String randomPrint(final int count) {
        return random(count, 32, 126, false, false);
    }
    
    public static String randomPrint(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomPrint(RandomUtils.nextInt(minLengthInclusive, maxLengthExclusive));
    }
    
    public static String random(final int count, final boolean letters, final boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }
    
    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers) {
        return random(count, start, end, letters, numbers, null, RandomStringUtils.RANDOM);
    }
    
    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers, final char... chars) {
        return random(count, start, end, letters, numbers, chars, RandomStringUtils.RANDOM);
    }
    
    public static String random(int count, int start, int end, final boolean letters, final boolean numbers, final char[] chars, final Random random) {
        if (count == 0) {
            return "";
        }
        if (count < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Requested random string length ").append(count).append(" is less than 0.").toString());
        }
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }
        if (start == 0 && end == 0) {
            if (chars != null) {
                end = chars.length;
            }
            else if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            }
            else {
                end = 123;
                start = 32;
            }
        }
        else if (end <= start) {
            throw new IllegalArgumentException(new StringBuilder().append("Parameter end (").append(end).append(") must be greater than start (").append(start).append(")").toString());
        }
        final char[] buffer = new char[count];
        final int gap = end - start;
        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char)(random.nextInt(gap) + start);
            }
            else {
                ch = chars[random.nextInt(gap) + start];
            }
            if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
                if (ch >= '\udc00' && ch <= '\udfff') {
                    if (count == 0) {
                        ++count;
                    }
                    else {
                        buffer[count] = ch;
                        --count;
                        buffer[count] = (char)(55296 + random.nextInt(128));
                    }
                }
                else if (ch >= '\ud800' && ch <= '\udb7f') {
                    if (count == 0) {
                        ++count;
                    }
                    else {
                        buffer[count] = (char)(56320 + random.nextInt(128));
                        --count;
                        buffer[count] = ch;
                    }
                }
                else if (ch >= '\udb80' && ch <= '\udbff') {
                    ++count;
                }
                else {
                    buffer[count] = ch;
                }
            }
            else {
                ++count;
            }
        }
        return new String(buffer);
    }
    
    public static String random(final int count, final String chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RandomStringUtils.RANDOM);
        }
        return random(count, chars.toCharArray());
    }
    
    public static String random(final int count, final char... chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RandomStringUtils.RANDOM);
        }
        return random(count, 0, chars.length, false, false, chars, RandomStringUtils.RANDOM);
    }
    
    static {
        RANDOM = new Random();
    }
}