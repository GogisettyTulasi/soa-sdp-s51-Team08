package com.klef.sdp.service;

import java.util.List;

import com.klef.sdp.dto.EventRequest;
import com.klef.sdp.dto.EventResponse;

public interface EventService {

    EventResponse saveEvent(EventRequest request);

    List<EventResponse> getAllEvents();

    EventResponse getEventById(Long id);

    EventResponse updateEvent(Long id, EventRequest request);

    void deleteEvent(Long id);
    
    EventResponse bookSeats(Long id, int seats);
    
    EventResponse releaseSeats(Long id, int seats);
}