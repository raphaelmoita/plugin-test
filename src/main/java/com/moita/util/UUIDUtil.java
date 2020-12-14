package com.moita.util;

import java.util.UUID;

public class UUIDUtil {

    public static void main(String[] args) {
        String uuid4 = UUID.randomUUID().toString();

        System.out.printf(uuid4);
    }

    public static String getUuid() {
        return UUID.randomUUID().toString();
    }
}
