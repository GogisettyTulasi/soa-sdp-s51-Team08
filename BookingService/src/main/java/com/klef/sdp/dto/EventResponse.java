package com.klef.sdp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse
{
    private Long eventId;
    private String eventName;
    private String location;
    private LocalDate eventDate;
    private int totalSeats;
    private int availableSeats;
}