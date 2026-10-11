package com.rogerjaden.eventmanagement.dto.request;

import java.time.LocalDate;

public record EventSearchRequest(String title,
                                Long categoryId,
                                LocalDate date,
                                String location,
                                Double minPrice,
                                Double maxPrice)
{}
