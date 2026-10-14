package com.steve.skblock.util;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class NameFormatter {

//    private static List<String> blockSimpleExcludeWords = List.of(
//            "DEEPSLATE",
//            "ORE",
//            "NETHER"
//    );
//
//    public static String formatBlockSimple(String enumName) {
//        return Arrays.stream(enumName.split("_"))
//                .map(word -> {
//                    if (!blockSimpleExcludeWords.contains(word)) {
//                        return word.toUpperCase(Locale.ROOT).charAt(0) + word.substring(1).toLowerCase(Locale.ROOT);
//                    }
//                    return "";
//                })
//                .collect(Collectors.joining(""));
//    }

    public static String format(String enumName) {
        return Arrays.stream(enumName.split("_"))
                .map(word -> word.toUpperCase(Locale.ROOT).charAt(0) + word.substring(1).toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(" "));
    }


}
