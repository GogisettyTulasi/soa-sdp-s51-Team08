package com.klef.sdp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.dto.EventRequest;
import com.klef.sdp.dto.EventResponse;
import com.klef.sdp.entity.Event;
import com.klef.sdp.exception.ResourceNotFoundException;
import com.klef.sdp.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository repository;

    @Override
    public EventResponse saveEvent(EventRequest request) {

        Event event = new Event();

        event.setEventName(request.getEventName());
        event.setLocation(request.getLocation());
        event.setEventDate(request.getEventDate());
        event.setTotalSeats(request.getTotalSeats());
        event.setAvailableSeats(request.getTotalSeats());

        Event savedEvent = repository.save(event);

        return mapToResponse(savedEvent);
    }

    @Override
    public List<EventResponse> getAllEvents() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse getEventById(Long id) {

        Event event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id : " + id));

        return mapToResponse(event);
    }

    @Override
    public EventResponse updateEvent(Long id, EventRequest request) {

        Event event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id : " + id));

        event.setEventName(request.getEventName());
        event.setLocation(request.getLocation());
        event.setEventDate(request.getEventDate());
        event.setTotalSeats(request.getTotalSeats());

        Event updatedEvent = repository.save(event);

        return mapToResponse(updatedEvent);
    }

    @Override
    public void deleteEvent(Long id) {

        Event event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id : " + id));

        repository.delete(event);
    }

    @Override
    public EventResponse bookSeats(Long id, int seats)
    {
        Event event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id : " + id));

        if (seats > event.getAvailableSeats())
        {
            throw new IllegalArgumentException("Insufficient available seats");
        }

        event.setAvailableSeats(event.getAvailableSeats() - seats);

        Event updatedEvent = repository.save(event);

        return mapToResponse(updatedEvent);
    }
    
    @Override
    public EventResponse releaseSeats(Long id, int seats)
    {
        Event event = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id : " + id));

        event.setAvailableSeats(event.getAvailableSeats() + seats);

        Event updatedEvent = repository.save(event);

        return mapToResponse(updatedEvent);
    }
    
    private EventResponse mapToResponse(Event event) {

        return EventResponse.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .location(event.getLocation())
                .eventDate(event.getEventDate())
                .totalSeats(event.getTotalSeats())
                .availableSeats(event.getAvailableSeats())
                .build();
    }
}