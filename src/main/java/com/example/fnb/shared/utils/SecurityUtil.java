package com.example.fnb.shared.utils;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.UUID;

public class SecurityUtil {
    public static UUID getCurrentUserId() {
        return getAuthenticatedUser().getId();
    }

    public static UserRole getCurrentUserRole() {
        return getAuthenticatedUser().getRole();
    }

    public static String getCurrentUserStoreCode() {
        return getAuthenticatedUser().getStaffOfStoreCode();
    }

    public static void onlyAllowUserId(UUID userId) {
        if (!getCurrentUserId().equals(userId)) {
            throw new DomainException(DomainExceptionCode.USER_NOT_ALLOWED);
        }
    }

    public static void onlyAllowRoles(UserRole... allowedRoles) {
        UserRole currentRole = getCurrentUserRole();
        if (!Arrays.asList(allowedRoles).contains(currentRole)) {
            throw new DomainException(DomainExceptionCode.USER_NOT_ALLOWED);
        }
    }

    public static void onlyAllowStaffOfStoreCode(String storeCode) {
        String currentStoreCode = getCurrentUserStoreCode();
        if (currentStoreCode == null || !currentStoreCode.equals(storeCode)) {
            throw new DomainException(DomainExceptionCode.USER_NOT_ALLOWED);
        }
    }

    private static AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new DomainException(DomainExceptionCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof AuthenticatedUser authUser)) {
            throw new DomainException(DomainExceptionCode.UNAUTHORIZED);
        }

        return authUser;
    }
}
