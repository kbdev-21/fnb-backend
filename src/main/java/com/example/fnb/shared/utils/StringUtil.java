package com.example.fnb.shared.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {
    public static String normalizeVietnamese(String text) {
        if (text == null) return null;
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}");
        normalized = pattern.matcher(normalized).replaceAll("");
        normalized = normalized.replaceAll("đ", "d").replaceAll("Đ", "D");
        normalized = normalized.trim().replaceAll("\\s+", " ");
        normalized = normalized.toLowerCase();
        return normalized;
    }
}
