package com.lingluo.attackdefendplatform.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeConverter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static LocalDateTime parseLocalDateTime(String timeString) throws DateTimeParseException {
        if (timeString != null && !timeString.trim().isEmpty()) {
            return LocalDateTime.parse(timeString, DATE_TIME_FORMATTER);
        }
        return null;
    }
}
