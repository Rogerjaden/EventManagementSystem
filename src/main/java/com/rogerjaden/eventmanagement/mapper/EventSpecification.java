package com.rogerjaden.eventmanagement.mapper;

import com.rogerjaden.eventmanagement.dto.request.EventSearchRequest;
import com.rogerjaden.eventmanagement.entity.Event;
import com.rogerjaden.eventmanagement.entity.enums.EventStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> withFilters(EventSearchRequest request) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Only show non-deleted events
            predicates.add(
                    cb.isFalse(root.get("isDeleted"))
            );

            // Only show active events
            predicates.add(
                    cb.equal(
                            root.get("eventStatus"),
                            EventStatus.ACTIVE
                    )
            );

            // Search by title
            if (StringUtils.hasText(request.title())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + request.title().trim().toLowerCase() + "%"
                        )
                );
            }

            // Filter by category
            if (request.categoryId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                request.categoryId()
                        )
                );
            }

            // Filter events occurring on a particular date
            if (request.date() != null) {

                LocalDateTime startOfDay =
                        request.date().atStartOfDay();

                LocalDateTime startOfNextDay =
                        request.date().plusDays(1).atStartOfDay();

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("startTime"),
                                startOfDay
                        )
                );

                predicates.add(
                        cb.lessThan(
                                root.get("startTime"),
                                startOfNextDay
                        )
                );
            }

            // Search by location
            if (StringUtils.hasText(request.location())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("location")),
                                "%" + request.location().trim().toLowerCase() + "%"
                        )
                );
            }

            // Minimum price
            if (request.minPrice() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("price"),
                                request.minPrice()
                        )
                );
            }

            // Maximum price
            if (request.maxPrice() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("price"),
                                request.maxPrice()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}