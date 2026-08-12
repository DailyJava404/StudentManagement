package com.example.studentsmanagement.Helper;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class DurationConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        String startTime = event.getMDCPropertyMap().get("startTime");
        if (startTime == null) {
            return "";
        }
        try {
            long duration = Long.parseLong(startTime);
            Long durationMs = (System.nanoTime() - duration) / 1_000_000;
            return durationMs +"ms";
        } catch (NumberFormatException e){
            return "";
        }
    }
}
