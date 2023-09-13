package com.example.demo.security;

import com.zaxxer.hikari.pool.HikariProxyResultSet;

public class SecurityConstants {
    public static final String SECRET = "jiwfoefd";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final long EXPIRATION_TIME = 3600;
    public static final String SIGN_UP_URL = "/api/user/create";
    public static final String ITEM_URL = "/api/item";
    public static final int MINIMUM_PASSWORD_LENGTH = 8;

}
