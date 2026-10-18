package com.gautamkappagal.calculator;

public record ApiError(String timestamp, int status, String error, String message, String path) {}