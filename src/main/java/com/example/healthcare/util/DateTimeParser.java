package com.example.healthcare.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeParser {

    public static LocalDateTime dateParser(String date) {
        try {
            // 날짜와 시간 정보를 연결하여 정확한 LocalDateTime 형태로 변환합니다.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            StringBuilder sb = new StringBuilder(date);
            sb.append(":00"); // 초 부분을 추가하여 "yyyy-MM-dd HH:mm:ss" 형식으로 맞춤
            return  LocalDateTime.parse(sb.toString(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("잘못된 날짜 형식입니다.", e);
        }

    }

}
