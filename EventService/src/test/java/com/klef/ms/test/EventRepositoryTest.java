package com.klef.ms.test;

import com.klef.sdp.EventServiceApplication;
import com.klef.sdp.entity.Event;
import com.klef.sdp.repository.EventRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = EventServiceApplication.class)
public class EventRepositoryTest {

    @Autowired
    private EventRepository repository;

    // Keep your existing test methods here

    @Test
    public void testSaveEvent() {
        Event event = new Event();
        event.setEventName("Tech Fest");
        event.setLocation("Vijayawada");
        event.setEventDate(LocalDate.now());
        event.setTotalSeats(100);
        event.setAvailableSeats(100);

        Event saved = repository.save(event);

        assertNotNull(saved.getEventId());
        assertEquals("Tech Fest", saved.getEventName());
        assertEquals(100, saved.getAvailableSeats());
    }

    @Test
    public void testFindEventById() {
        Event event = new Event();
        event.setEventName("Music Fest");
        event.setLocation("Guntur");
        event.setTotalSeats(50);
        event.setAvailableSeats(50);

        Event saved = repository.save(event);

        Optional<Event> result =
                repository.findById(saved.getEventId());

        assertTrue(result.isPresent());
        assertEquals("Music Fest", result.get().getEventName());
    }

    @Test
    public void testUpdateAvailableSeats() {
        Event event = new Event();
        event.setEventName("Sports Meet");
        event.setLocation("Hyderabad");
        event.setTotalSeats(80);
        event.setAvailableSeats(80);

        Event saved = repository.save(event);

        saved.setAvailableSeats(60);
        Event updated = repository.save(saved);

        assertEquals(60, updated.getAvailableSeats());
    }

    @Test
    public void testDeleteEvent() {
        Event event = new Event();
        event.setEventName("Cultural Fest");
        event.setLocation("Chennai");
        event.setTotalSeats(40);
        event.setAvailableSeats(40);

        Event saved = repository.save(event);

        repository.deleteById(saved.getEventId());

        assertFalse(repository.findById(saved.getEventId()).isPresent());
    }

    @Test
    public void testFindAllEvents() {
        Event event = new Event();
        event.setEventName("Annual Fest");
        event.setLocation("Vijayawada");
        event.setTotalSeats(100);
        event.setAvailableSeats(100);

        repository.save(event);

        assertFalse(repository.findAll().isEmpty());
    }
}