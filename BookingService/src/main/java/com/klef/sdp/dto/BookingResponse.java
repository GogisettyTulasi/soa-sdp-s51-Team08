package com.klef.sdp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse
{
    private Long bookingId;
    private Long userId;
    private Long eventId;
    private Integer seatsBooked;
    private String bookingStatus;
}