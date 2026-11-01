package com.klef.ms.test;

import com.klef.sdp.BookingServiceApplication;
import com.klef.sdp.entity.Booking;
import com.klef.sdp.repo.BookingRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = BookingServiceApplication.class)
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository repository;

    @Test
    public void testSaveBooking() {
        Booking booking = new Booking();
        booking.setUserId(1L);
        booking.setEventId(101L);
        booking.setSeatsBooked(2);
        booking.setBookingStatus("CONFIRMED");

        Booking saved = repository.save(booking);

        assertNotNull(saved.getBookingId());
        assertEquals(1L, saved.getUserId());
        assertEquals(2, saved.getSeatsBooked());
    }

    @Test
    public void testFindBookingById() {
        Booking booking = new Booking();
        booking.setUserId(2L);
        booking.setEventId(102L);
        booking.setSeatsBooked(3);
        booking.setBookingStatus("CONFIRMED");

        Booking saved = repository.save(booking);

        Optional<Booking> result =
                repository.findById(saved.getBookingId());

        assertTrue(result.isPresent());
        assertEquals(102L, result.get().getEventId());
    }

    @Test
    public void testUpdateBooking() {
        Booking booking = new Booking();
        booking.setUserId(3L);
        booking.setEventId(103L);
        booking.setSeatsBooked(2);
        booking.setBookingStatus("CONFIRMED");

        Booking saved = repository.save(booking);

        saved.setSeatsBooked(5);
        Booking updated = repository.save(saved);

        assertEquals(5, updated.getSeatsBooked());
    }

    @Test
    public void testDeleteBooking() {
        Booking booking = new Booking();
        booking.setUserId(4L);
        booking.setEventId(104L);
        booking.setSeatsBooked(1);
        booking.setBookingStatus("CONFIRMED");

        Booking saved = repository.save(booking);

        repository.deleteById(saved.getBookingId());

        assertFalse(repository.findById(saved.getBookingId()).isPresent());
    }

    @Test
    public void testFindAllBookings() {
        Booking booking = new Booking();
        booking.setUserId(5L);
        booking.setEventId(105L);
        booking.setSeatsBooked(2);
        booking.setBookingStatus("CONFIRMED");

        repository.save(booking);

        assertFalse(repository.findAll().isEmpty());
    }

    @Test
    public void testFindByUserId() {
        Booking booking = new Booking();
        booking.setUserId(10L);
        booking.setEventId(110L);
        booking.setSeatsBooked(2);
        booking.setBookingStatus("CONFIRMED");

        repository.save(booking);

        List<Booking> result = repository.findByUserId(10L);

        assertFalse(result.isEmpty());
        assertEquals(10L, result.get(0).getUserId());
    }
}