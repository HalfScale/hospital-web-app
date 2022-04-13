package io.muffin.inventoryservice.utility;

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

    //image indetifiers
    public static final String IMAGE_IDENTIFIER_USER = "profile";

    //pagination
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public  static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    //misc
    public static final Long NEW_ENTITY_ID = -1L;
}
