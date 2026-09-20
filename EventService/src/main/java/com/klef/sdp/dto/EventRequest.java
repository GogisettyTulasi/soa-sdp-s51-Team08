package com.klef.sdp.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EventRequest {

    @NotBlank(message = "Event name is required")
    private String eventName;

    @NotBlank(message = "Location is required")
    private String location;

    @FutureOrPresent(message = "Event date must be today or in the future")
    private LocalDate eventDate;

    @Positive(message = "Total seats must be positive")
    private int totalSeats;
}