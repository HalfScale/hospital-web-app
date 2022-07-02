package io.muffin.inventoryservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Slf4j
public class TestClass {

    public static void main(String[] args) {
        String testString = null;

        log.info("String test if null: {}", StringUtils.hasText(testString));

        testString = "";

        log.info("String test if empty: {}", StringUtils.hasText(testString));

        testString = " ";

        log.info("String test if with spaces: {}", StringUtils.hasText(testString));
    }
}
