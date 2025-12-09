package com.example.fnb.shared.utils;

import org.springframework.data.domain.Sort;

public class AppUtil {
    /* sortBy is somethings like "createdAt", "-createAt" */
    public static Sort createSort(String sortBy) {
        Sort sort;
        if (sortBy.startsWith("-")) {
            sort = Sort.by(sortBy.substring(1)).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }
        return sort;
    }
}
