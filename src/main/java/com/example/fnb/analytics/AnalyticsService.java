package com.example.fnb.analytics;

import com.example.fnb.analytics.dto.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnalyticsService {
    public Page<EventDto> getEvents(int pageNumber, int pageSize);
}
