package com.example.eventplannerapp.service;

import com.example.eventplannerapp.shared.dto.EventDto;

import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto eventDto);
    EventDto getEvent(String eventId);
    EventDto updateEvent(String eventId, EventDto eventDto);
    void deleteEvent(String eventId);
    List<EventDto> getEvents(String userId);
}
