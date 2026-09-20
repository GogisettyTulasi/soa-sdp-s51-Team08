package com.klef.sdp.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventResponse {

    private Long eventId;
    private String eventName;
    private String location;
    private LocalDate eventDate;
    private int totalSeats;
}