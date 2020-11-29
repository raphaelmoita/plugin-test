package com.moita.util;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public class Util {

    private static String jsonFileName(String javaFileName) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, javaFileName.substring(0, javaFileName.length() - 5) + ".json");
    }
}
