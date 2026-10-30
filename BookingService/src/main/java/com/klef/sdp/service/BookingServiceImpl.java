package com.klef.sdp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.klef.sdp.entity.Booking;
import com.klef.sdp.client.EventClient;
import com.klef.sdp.client.UserClient;
import com.klef.sdp.dto.BookingRequest;
import com.klef.sdp.dto.BookingResponse;
import com.klef.sdp.dto.EventResponse;
import com.klef.sdp.dto.UserResponse;
import com.klef.sdp.repo.BookingRepository;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.klef.sdp.exception.ResourceNotFoundException;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserClient userClient;
 
    @Autowired
    private EventClient eventClient;
    
    public ResponseEntity<?> saveBookingFallback(
            BookingRequest request, Throwable ex) {

        Throwable cause = ex;

        while (cause != null) {

            if (cause instanceof FeignException.NotFound) {
                throw new ResourceNotFoundException(
                    "User not found with id : " + request.getUserId()
                );
            }

            cause = cause.getCause();
        }

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User Service is currently unavailable or\nThe User with given ID is not found.\nCheck the user ID or Please try again later.");
    }
    
    @Override
    @CircuitBreaker(
        name = "UserService",
        fallbackMethod = "saveBookingFallback"
    )
    public ResponseEntity<?> saveBooking(BookingRequest request) {
    	
    	 UserResponse user = userClient.getuserById(request.getUserId());
    	 EventResponse event = eventClient.getEventById(request.getEventId()); 
    	 
    	 try {
    		    event = eventClient.getEventById(request.getEventId());
    		} catch (FeignException.NotFound e) {
    		    throw new ResourceNotFoundException(
    		        "Event not found with id : " + request.getEventId()
    		    );
    		}

    	    if (user == null) {
    	        throw new ResourceNotFoundException(
    	            "User not found with id : " + request.getUserId()
    	        );
    	    }

        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setEventId(request.getEventId());
        booking.setSeatsBooked(request.getSeatsBooked());
        booking.setBookingStatus("CONFIRMED");

        Booking saved = bookingRepository.save(booking);

        return ResponseEntity.ok(mapToResponse(saved));
    }

    @Override
    public List<BookingResponse> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookingResponse getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));

        return mapToResponse(booking);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest request) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));
        
        EventResponse event = eventClient.getEventById(request.getEventId()); 
   	 
   	 try {
   		    event = eventClient.getEventById(request.getEventId());
   		} catch (FeignException.NotFound e) {
   		    throw new ResourceNotFoundException(
   		        "Event not found with id : " + request.getEventId()
   		    );
   		}
        
        booking.setUserId(request.getUserId());
        booking.setEventId(request.getEventId());
        booking.setSeatsBooked(request.getSeatsBooked());

        Booking updated = bookingRepository.save(booking);

        return mapToResponse(updated);
    }

    @Override
    public void deleteBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));

        bookingRepository.delete(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByUserId(Long userId) {

        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ResponseEntity<?> cancelBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));

        booking.setBookingStatus("CANCELLED");

        Booking updated = bookingRepository.save(booking);

        return ResponseEntity.ok(mapToResponse(updated));
    }

    private BookingResponse mapToResponse(Booking booking) {

        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getBookingId());
        response.setUserId(booking.getUserId());
        response.setEventId(booking.getEventId());
        response.setSeatsBooked(booking.getSeatsBooked());
        response.setBookingStatus(booking.getBookingStatus());

        return response;
    }
}