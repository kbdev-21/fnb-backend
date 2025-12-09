package com.example.fnb.analytics.domain;

import com.example.fnb.analytics.AnalyticsService;
import com.example.fnb.analytics.dto.EventDto;
import com.example.fnb.shared.utils.AppUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private final EventRepository eventRepository;
    private final ModelMapper mapper;

    public AnalyticsServiceImpl(EventRepository eventRepository, ModelMapper mapper) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<EventDto> getEvents(int pageNumber, int pageSize) {
        var sort = AppUtil.createSort("-occurredAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Event> events = eventRepository.findAll(pageable);

        var eventDtos = events.getContent().stream()
            .map(e -> mapper.map(e, EventDto.class))
            .toList();

        return new PageImpl<>(eventDtos, pageable, events.getTotalElements());
    }
}
