package SchedulingProgram.Utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimeHelper {
    /**
     * custom date time formatter used through out to display times
     */
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    /**
     * method to create time blocks for a combo box
     * @return a list of times blocked out in 15 min intervals
     */
    public static ObservableList<LocalTime> getTimeBlocks() {
        LocalTime start = LocalTime.of(1,0);
        LocalTime end = LocalTime.of(23,0);
        ObservableList<LocalTime> timeBlocks = FXCollections.observableArrayList();
        while (start.isBefore(end.plusSeconds(1))) {
            timeBlocks.add(start);
            start = start.plusMinutes(15);
        }
     return timeBlocks;
    }

    /**
     * method to convert UTC time to the time zone set on the computer running the program
     * @param inputDateTime the time at UTC to be converted
     * @return the converted time at the same moment in the local time zone
     */
    public static LocalDateTime utcToZoneDateTime(LocalDateTime inputDateTime) {
        LocalDateTime localDateTime;
        ZonedDateTime zonedDateTime = inputDateTime.atZone(ZoneId.of("UTC"));
        localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime;
    }

    /**
     * method to convert local time zone set on the device running the program to UTC
     * @param inputDateTime the time at the local time zone to be converted
     * @return the converted time at the same moment in UTC
     */
    public static LocalDateTime zoneDateTimeToUtc(LocalDateTime inputDateTime) {
        LocalDateTime localDateTime;
        ZonedDateTime zonedDateTime = inputDateTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return localDateTime;
    }



}
