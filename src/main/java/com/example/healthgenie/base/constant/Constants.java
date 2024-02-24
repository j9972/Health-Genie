package com.example.healthgenie.base.constant;

public class Constants {
    public static final String OK_GOOD = "OK_GOOD";

    public static final String BAD = "Bad";

    public static final String DUPLICATE_DATA = "Duplicate Data";

    public static final String SOMETHING_WENT_WRONG = "Something went wrong"; // Exception -> handleException

    public static final String INVALID_DATA = "Invalid Data";

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; // 토큰 만료

//    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 24 * 60 * 60 * 1000L; //  1day -> 30 minutes -> 30 * 60 * 1000L

//    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days
    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 1000L; // 30초
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 120 * 1000L; // 2분
}
