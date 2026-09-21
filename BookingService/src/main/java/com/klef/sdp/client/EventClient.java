package com.klef.sdp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.klef.sdp.dto.EventResponse;

@FeignClient(name = "EventService")
public interface EventClient
{
    @GetMapping("/event/display/{id}")
    EventResponse getEventById(@PathVariable("id") Long id);

    @PutMapping("/event/book/{id}/{seats}")
    EventResponse bookSeats(@PathVariable("id") Long id, @PathVariable("seats") int seats);
    
    @PutMapping("/event/release/{id}/{seats}")
    EventResponse releaseSeats(@PathVariable("id") Long id, @PathVariable("seats") int seats);
}