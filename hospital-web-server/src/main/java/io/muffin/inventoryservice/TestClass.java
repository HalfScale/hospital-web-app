package io.muffin.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestClass {

    public static void main(String[] args) {
        DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(LocalDateTime.parse("2022-05-19 10:10:00", timeColonFormatter));
    }
}
