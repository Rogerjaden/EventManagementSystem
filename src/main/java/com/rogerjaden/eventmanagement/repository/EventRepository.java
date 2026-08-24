package com.rogerjaden.eventmanagement.repository;

import com.rogerjaden.eventmanagement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

}
