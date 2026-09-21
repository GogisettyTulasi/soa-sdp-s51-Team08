package com.klef.sdp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.sdp.dto.EventRequest;
import com.klef.sdp.dto.EventResponse;
import com.klef.sdp.service.EventService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping("/")
    public String home() {
        return "Event Service Project";
    }

    @Value("${server.port}")
    private String port;

   

    @PostMapping("/add")
    public ResponseEntity<EventResponse> saveEvent(
            @Valid @RequestBody EventRequest request) {

        return new ResponseEntity<>(
                service.saveEvent(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/displayall")
    public ResponseEntity<List<EventResponse>> getAllEvents() {

        System.out.println(
                "Event Service instance running on port: " + port);

        return ResponseEntity.ok(service.getAllEvents());
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getEventById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest request) {

        return ResponseEntity.ok(
                service.updateEvent(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {

        service.deleteEvent(id);

        return ResponseEntity.ok(
                "Event deleted successfully.");
    }
    
    @PutMapping("/book/{id}/{seats}")
    public ResponseEntity<EventResponse> bookSeats(@PathVariable Long id, @PathVariable int seats)
    {
        return ResponseEntity.ok(service.bookSeats(id, seats));
    }
    
    @PutMapping("/release/{id}/{seats}")
    public ResponseEntity<EventResponse> releaseSeats(
            @PathVariable Long id,
            @PathVariable int seats)
    {
        return ResponseEntity.ok(service.releaseSeats(id, seats));
    }
}