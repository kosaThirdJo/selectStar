package com.threestar.selectstar.config.jwt;

public class JwtProperties {
	public static final int ACCESS_TOKEN_EXPIRATION_TIME = 7200; // 2시간 7200
	public static final int REFRESH_TOKEN_EXPIRATION_TIME = 86400000; // 1일 86400000
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
}