package com.example.fnb.analytics.web;

import com.example.fnb.analytics.AnalyticsService;
import com.example.fnb.analytics.dto.EventDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/api/analytics/events")
    public ResponseEntity<Page<EventDto>> getEvents(
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "20") int pageSize
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.ok(analyticsService.getEvents(pageNumber, pageSize));
    }
}
