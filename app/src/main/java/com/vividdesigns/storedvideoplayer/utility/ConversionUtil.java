package com.vividdesigns.storedvideoplayer.utility;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConversionUtil {

    private static final String[] SI_UNITS = {"B", "kB", "MB", "GB", "TB", "PB", "EB"};
    private static final String[] BINARY_UNITS = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    /**
     * Return duration of video in HH:MM:SS format
     * @param duration string representing milliseconds of video duration
     */
    public static String getDurationString(long duration, Locale locale) {

        long totalSecs = duration / 1000;
        int hours = (int) (totalSecs / 3600);
        int minutes = (int) ((totalSecs % 3600) / 60);
        int seconds = (int) (totalSecs % 60);

        return String.format(locale, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Returns the size of the video in readable format
     */
    public static String humanReadableByteCount(final long bytes, final boolean useSIUnits, final Locale locale) {
        final String[] units = useSIUnits ? SI_UNITS : BINARY_UNITS;
        final int base = useSIUnits ? 1000 : 1024;

        // When using the smallest unit no decimal point is needed, because it's the exact number.
        if (bytes < base) {
            return bytes + " " + units[0];
        }

        final int exponent = (int) (Math.log(bytes) / Math.log(base));
        final String unit = units[exponent];
        return String.format(locale, "%.1f %s", bytes / Math.pow(base, exponent), unit);
    }

    /**
     * @param time timestamp
     * @param locale instance of Locale class
     * @return readable date string for long timestamp
     */
    public static String getDateString(long time, Locale locale) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd MMM, yyyy", locale);
        return format.format(date);
    }

}
