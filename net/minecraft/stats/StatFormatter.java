package net.minecraft.stats;

import java.text.NumberFormat;
import java.util.function.Consumer;
import net.minecraft.Util;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.text.DecimalFormat;

public interface StatFormatter {
    public static final DecimalFormat DECIMAL_FORMAT = Util.<DecimalFormat>make(new DecimalFormat("########0.00"), (java.util.function.Consumer<DecimalFormat>)(decimalFormat -> decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT))));
    public static final StatFormatter DEFAULT = NumberFormat.getIntegerInstance(Locale.US)::format;
    public static final StatFormatter DIVIDE_BY_TEN = integer -> StatFormatter.DECIMAL_FORMAT.format(integer * 0.1);
    public static final StatFormatter DISTANCE = integer -> {
        final double double2 = integer / 100.0;
        final double double3 = double2 / 1000.0;
        if (double3 > 0.5) {
            return StatFormatter.DECIMAL_FORMAT.format(double3) + " km";
        }
        if (double2 > 0.5) {
            return StatFormatter.DECIMAL_FORMAT.format(double2) + " m";
        }
        return new StringBuilder().append(integer).append(" cm").toString();
    };
    public static final StatFormatter TIME = integer -> {
        final double double2 = integer / 20.0;
        final double double3 = double2 / 60.0;
        final double double4 = double3 / 60.0;
        final double double5 = double4 / 24.0;
        final double double6 = double5 / 365.0;
        if (double6 > 0.5) {
            return StatFormatter.DECIMAL_FORMAT.format(double6) + " y";
        }
        if (double5 > 0.5) {
            return StatFormatter.DECIMAL_FORMAT.format(double5) + " d";
        }
        if (double4 > 0.5) {
            return StatFormatter.DECIMAL_FORMAT.format(double4) + " h";
        }
        if (double3 > 0.5) {
            return StatFormatter.DECIMAL_FORMAT.format(double3) + " m";
        }
        return new StringBuilder().append(double2).append(" s").toString();
    };
}
