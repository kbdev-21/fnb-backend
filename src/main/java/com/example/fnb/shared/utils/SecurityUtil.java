package com.example.fnb.shared.utils;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.UUID;

public class SecurityUtil {
    public static UUID getCurrentUserId() {
        Authentication authentication = checkAuthentication();
        return (UUID) authentication.getPrincipal();
    }

    public static UserRole getCurrentUserRole() {
        Authentication authentication = checkAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            return extractUserRoleFromAuthority(authority.getAuthority());
        }
        throw new DomainException(DomainExceptionCode.UNAUTHORIZED);
    }

    public static void onlyAllowUserId(UUID userId) {
        UUID currentUserId = getCurrentUserId();
        if (!currentUserId.equals(userId)) {
            throw new DomainException(DomainExceptionCode.NOT_ALLOWED);
        }
    }

    public static void onlyAllowRoles(UserRole... roles) {
        UserRole currentUserRole = getCurrentUserRole();
        if(!Arrays.asList(roles).contains(currentUserRole)) {
            throw new DomainException(DomainExceptionCode.NOT_ALLOWED);
        }
    }

    private static Authentication checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new DomainException(DomainExceptionCode.UNAUTHORIZED);
        }
        return authentication;
    }

    private static UserRole extractUserRoleFromAuthority(String authorityStr) {
        if (authorityStr != null && authorityStr.startsWith("ROLE_")) {
            return UserRole.valueOf(authorityStr.substring(5));
        }
        throw new DomainException(DomainExceptionCode.UNAUTHORIZED);
    }
}
