package com.klef.sdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.sdp.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}