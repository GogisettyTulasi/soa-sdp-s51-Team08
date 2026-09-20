package com.klef.sdp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.klef.sdp.dto.BookingRequest;
import com.klef.sdp.dto.BookingResponse;

public interface BookingService
{
    ResponseEntity<?> saveBooking(BookingRequest request);

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(Long id);

    BookingResponse updateBooking(Long id, BookingRequest request);

    void deleteBooking(Long id);

    List<BookingResponse> getBookingsByUserId(Long userId);

    ResponseEntity<?> cancelBooking(Long id);
}