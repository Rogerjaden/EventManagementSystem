package com.rogerjaden.eventmanagement.repository;

import com.rogerjaden.eventmanagement.entity.Event;
import com.rogerjaden.eventmanagement.entity.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>, JpaSpecificationExecutor<Event> {
    Page<Event> findByIsDeletedFalseAndEventStatus(
            EventStatus eventStatus,
            Pageable pageable
    );
    Optional<Event> findByIdAndIsDeletedFalse(Long id);
}
