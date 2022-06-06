package io.muffin.inventoryservice.utility;

import java.util.Arrays;
import java.util.List;

public class Constants {

    //genders
    public static final  int MALE = 1;
    public static final  int FEMALE = 2;

    //user types
    public static final  int USER_ADMIN = 1;
    public static final  int USER_DOCTOR = 2;
    public static final  int USER_PATIENT = 3;

    //authorities
    public static final  String AUTHORITY_ADMIN = "ADMIN";
    public static final  String AUTHORITY_DOCTOR = "DOCTOR";
    public static final  String AUTHORITY_PATIENT = "PATIENT";

    //appointment status
    public static final int APPOINTMENT_PENDING = 1;
    public static final int APPOINTMENT_APPROVED = 2;
    public static final int APPOINTMENT_CANCELLED = 3;
    public static final int APPOINTMENT_REJECTED = 4;


    //image indetifiers
    public static final String IMAGE_IDENTIFIER_USER = "profile";
    public static final String IMAGE_IDENTIFIER_HOSPITAL_ROOM = "hospital_room";

    //pagination
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public  static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    //Reservation status
    public static final Integer RESERVATION_CREATED = 0;
    public static final Integer RESERVATION_CANCELLED = 1;
    public static final Integer RESERVATION_DONE = 2;
    public static final Integer RESERVATION_ALL = 3;
    public static final List<Integer> ALL_RESERVATION_STATUS = Arrays.asList(RESERVATION_CREATED, RESERVATION_CANCELLED,
            RESERVATION_DONE);
    public static final List<String> ALL_RESERVATION_STATUS_AS_STRING = Arrays.asList(String.valueOf(RESERVATION_CREATED),
            String.valueOf(RESERVATION_CANCELLED),
            String.valueOf(RESERVATION_DONE),
            String.valueOf(RESERVATION_ALL));


    //misc
    public static final Long NEW_ENTITY_ID = -1L;
}
