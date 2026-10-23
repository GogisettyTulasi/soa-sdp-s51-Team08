package com.klef.sdp.client;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.klef.sdp.dto.EventResponse;

@FeignClient(name = "EventService")
public interface EventClient {

    @GetMapping("/event/displayall")
    List<EventResponse> getAllEvents();

    @GetMapping("/event/display/{id}")
    EventResponse getEventById(@PathVariable("id") Long id);
}
