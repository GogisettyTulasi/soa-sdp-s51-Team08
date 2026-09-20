package com.klef.sdp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klef.sdp.dto.BookingRequest;
import com.klef.sdp.dto.BookingResponse;
import com.klef.sdp.service.BookingService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/booking")
public class BookingController
{
    @Autowired
    private BookingService service;

    @GetMapping("/")
    public String home()
    {
        return "Booking Service Project";
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveBooking(@Valid @RequestBody BookingRequest request)
    {
        return service.saveBooking(request);
    }

    @GetMapping("/displayall")
    public ResponseEntity<List<BookingResponse>> getAllBookings()
    {
        return ResponseEntity.ok(service.getAllBookings());
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id)
    {
        return ResponseEntity.ok(service.getBookingById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequest request)
    {
        return ResponseEntity.ok(service.updateBooking(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id)
    {
        service.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully.");
    }

    @GetMapping("/displaybyuser")
    public ResponseEntity<List<BookingResponse>> displayBookingsByUserId(
            @RequestParam Long userid)
    {
        return ResponseEntity.ok(service.getBookingsByUserId(userid));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id)
    {
        return service.cancelBooking(id);
    }
}
