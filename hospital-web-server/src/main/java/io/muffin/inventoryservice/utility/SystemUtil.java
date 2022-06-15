package io.muffin.inventoryservice.utility;

import io.muffin.inventoryservice.model.dto.GenericPageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemUtil {

    public static <T> GenericPageableResponse<T> mapToGenericPageableResponse(Page<T> page) {
        return new GenericPageableResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    public static String formatDateTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    public static String formatDate(LocalDate localDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDate.format(formatter);
    }

    public static boolean isNumericToLong(String num) {
        if(!StringUtils.hasText(num)) {
            return false;
        }

        try {
            Long.parseLong(num);
        }catch(NumberFormatException e) {
            return false;
        }

        return true;
    }

}
