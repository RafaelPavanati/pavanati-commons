package br.com.pavanati;

import java.util.Objects;

public class DateFormatUtil {

    public static String formatDayMonthYear(String localDateString) {
        if (Objects.nonNull(localDateString)) {
            if (localDateString.matches("^\\d{4}[-]\\d{2}[-]\\d{2}[T].+")) {
                return localDateString.replaceAll("^(\\d{4})[-](\\d{2})[-](\\d{2}).+$", "$3/$2/$1");
            }
        }

        return "";
    }

}