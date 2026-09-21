package com.klef.sdp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.klef.sdp.client.EventClient;
import com.klef.sdp.dto.BookingRequest;
import com.klef.sdp.dto.BookingResponse;
import com.klef.sdp.dto.EventResponse;
import com.klef.sdp.entity.Booking;
import com.klef.sdp.exception.ResourceNotFoundException;
import com.klef.sdp.repo.BookingRepository;

@Service
public class BookingServiceImpl implements BookingService
{
    @Autowired
    private BookingRepository repository;

    @Autowired
    private EventClient eventClient; 

    @Override
    public ResponseEntity<?> saveBooking(BookingRequest request)
    {
        EventResponse event = eventClient.getEventById(request.getEventId());

        if (request.getSeatsBooked() > event.getAvailableSeats())
        {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient available seats");
        }

        eventClient.bookSeats(
                request.getEventId(),
                request.getSeatsBooked());

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .eventId(request.getEventId())
                .seatsBooked(request.getSeatsBooked())
                .bookingStatus("CONFIRMED")
                .build();

        Booking savedBooking = repository.save(booking);

        return new ResponseEntity<>(
                mapToResponse(savedBooking),
                HttpStatus.CREATED);
    }

    @Override
    public List<BookingResponse> getAllBookings()
    {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
 
    @Override
    public BookingResponse getBookingById(Long id)
    {
        Booking booking = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id : " + id));

        return mapToResponse(booking);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest request)
    {
        Booking booking = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id : " + id));

        if ("CONFIRMED".equals(booking.getBookingStatus()))
        {
            if (!booking.getEventId().equals(request.getEventId()))
            {
                eventClient.releaseSeats(
                        booking.getEventId(),
                        booking.getSeatsBooked());

                EventResponse newEvent =
                        eventClient.getEventById(request.getEventId());

                if (request.getSeatsBooked() > newEvent.getAvailableSeats())
                {
                    eventClient.bookSeats(
                            booking.getEventId(),
                            booking.getSeatsBooked());

                    throw new IllegalArgumentException(
                            "Insufficient available seats");
                }

                eventClient.bookSeats(
                        request.getEventId(),
                        request.getSeatsBooked());
            }
            else
            {
                int difference =
                        request.getSeatsBooked() - booking.getSeatsBooked();

                if (difference > 0)
                {
                    eventClient.bookSeats(
                            request.getEventId(),
                            difference);
                }
                else if (difference < 0)
                {
                    eventClient.releaseSeats(
                            request.getEventId(),
                            -difference);
                }
            }
        }

        booking.setUserId(request.getUserId());
        booking.setEventId(request.getEventId());
        booking.setSeatsBooked(request.getSeatsBooked());

        Booking updatedBooking = repository.save(booking);

        return mapToResponse(updatedBooking);
    }

    @Override
    public void deleteBooking(Long id)
    {
        Booking booking = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id : " + id));

        repository.delete(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByUserId(Long userId)
    {
        return repository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> cancelBooking(Long id)
    {
        Booking booking = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id : " + id));

        if ("CONFIRMED".equals(booking.getBookingStatus()))
        {
            eventClient.releaseSeats(
                    booking.getEventId(),
                    booking.getSeatsBooked());

            booking.setBookingStatus("CANCELLED");

            repository.save(booking);
        }

        return ResponseEntity.ok(mapToResponse(booking));
    }

    private BookingResponse mapToResponse(Booking booking)
    {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId())
                .eventId(booking.getEventId())
                .seatsBooked(booking.getSeatsBooked())
                .bookingStatus(booking.getBookingStatus())
                .build();
    }
}
 
