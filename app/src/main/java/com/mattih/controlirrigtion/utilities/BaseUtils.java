package com.mattih.controlirrigtion.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class BaseUtils {

    public static String BASE_URL1 = "http://192.168.43.42/Smart_Irrigation/api/";
    public static String BASE_URL2 = "https://api.openweathermap.org/data/2.5/";

    public static long toMillisecond(String string) {
        LocalDateTime localDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            localDateTime = LocalDateTime.parse(string, simpleDateFormat);
            return localDateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();

        }
        return 0;
    }

    public static String getSimply(Long dateTime, String format) {
        SimpleDateFormat newFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return newFormat.format(new Date(dateTime));
    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        return newFormat.format(new Date(dateTime));
    }

    public static String getFormattedDateEvent(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.ENGLISH);
        return newFormat.format(new Date(dateTime));
    }

    public static String getFormattedTimeEvent(Long time) {
        SimpleDateFormat newFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        return newFormat.format(new Date(time));
    }
}
