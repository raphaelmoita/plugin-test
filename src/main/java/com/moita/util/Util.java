package com.moita.util;

import java.util.UUID;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public class Util {
    private static final String JSON = ".json";

    public static String jsonFileName(String javaFileName) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, javaFileName + JSON);
    }

    public String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        System.out.println(jsonFileName("RaphaelMoita"));
    }
}
