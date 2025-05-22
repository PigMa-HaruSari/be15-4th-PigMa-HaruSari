package com.pigma.harusari.task.common;

public class ServiceAssert {
    public static void notNull(Object obj, String message) {
        if (obj == null) throw new IllegalArgumentException(message);
    }
    public static void notEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) throw new IllegalArgumentException(message);
    }
}