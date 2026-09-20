package com.klef.sdp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest
{
    @NotNull
    private Long userId;

    @NotNull
    private Long eventId;

    @NotNull
    @Min(1)
    private Integer seatsBooked;
}