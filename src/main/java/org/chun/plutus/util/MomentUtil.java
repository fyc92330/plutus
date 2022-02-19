package org.chun.plutus.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MomentUtil {

  public enum Time {
    /**
     * HH:mm
     */
    HH_mm("HH:mm", DateTimeFormatter.ofPattern("HH:mm")),
    /**
     * HH:mm:ss
     */
    HH_mm_ss("HH:mm:ss", DateTimeFormatter.ofPattern("HH:mm:ss"));

    private String formatStr;
    private DateTimeFormatter dateTimeFormatter;

    Time(String formatStr, DateTimeFormatter dateTimeFormatter) {
      this.formatStr = formatStr;
      this.dateTimeFormatter = dateTimeFormatter;
    }

    public String getFormatStr() {
      return formatStr;
    }

    public DateTimeFormatter getDateTimeFormatter() {
      return dateTimeFormatter;
    }

    public String format(LocalTime localTime) {
      return localTime.format(this.dateTimeFormatter);
    }

    public final LocalTime parse(String dateString) {
      return LocalTime.parse(dateString, this.dateTimeFormatter);
    }

  }

  public enum Date {
    /**
     * yyyyMMdd
     */
    yyyyMMdd("yyyyMMdd", DateTimeFormatter.ofPattern("yyyyMMdd")),
    /**
     * yyyy-MM-dd
     */
    yyyy_MM_dd("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private String formatStr;
    private DateTimeFormatter dateTimeFormatter;

    Date(String formatStr, DateTimeFormatter dateTimeFormatter) {
      this.formatStr = formatStr;
      this.dateTimeFormatter = dateTimeFormatter;
    }

    public String getFormatStr() {
      return formatStr;
    }

    public DateTimeFormatter getDateTimeFormatter() {
      return dateTimeFormatter;
    }

    public final String format(LocalDate localDate) {
      return localDate.format(this.dateTimeFormatter);
    }

    public final LocalDate parse(String dateString) {
      return LocalDate.parse(dateString, this.dateTimeFormatter);
    }

  }

  public enum DateTime {
    /**
     * yyyy-MM-dd HH:mm
     */
    yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
    /**
     * yyyyMMddHHmmssSSS, parse method will happen DateTimeParseException
     */
    yyyyMMddHHmmssSSS("yyyyMMddHHmmssSSS", DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    /**
     * yyyy-MM-dd HH:mm:ss.S
     */
    yyyy_MM_dd_HH_mm_ss_S("yyyy-MM-dd HH:mm:ss.S", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));

    private String formatStr;
    private DateTimeFormatter dateTimeFormatter;

    DateTime(String formatStr, DateTimeFormatter dateTimeFormatter) {
      this.formatStr = formatStr;
      this.dateTimeFormatter = dateTimeFormatter;
    }

    public String getFormatStr() {
      return formatStr;
    }

    public DateTimeFormatter getDateTimeFormatter() {
      return dateTimeFormatter;
    }

    public final String format(LocalDateTime localDateTime) {
      return localDateTime.format(this.dateTimeFormatter);
    }

    public final String format(LocalDate localDate, LocalTime localTime) {
      return LocalDateTime.now().with(localDate).with(localTime).format(this.dateTimeFormatter);
    }

    /**
     * @param dateString
     * @return
     * @see "yyyyMMddHHmmssSSS" parse method will happen DateTimeParseException
     */
    public final LocalDateTime parse(String dateString) {
      return LocalDateTime.parse(dateString, this.dateTimeFormatter);
    }

  }
}

