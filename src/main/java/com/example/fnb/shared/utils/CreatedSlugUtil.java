package com.example.fnb.shared.utils;

public class CreatedSlugUtil {

    public static String createSlug(String input) {
        if (input == null) {
            return "";
        }

        return StringUtil.normalizeVietnamese(input)
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}
