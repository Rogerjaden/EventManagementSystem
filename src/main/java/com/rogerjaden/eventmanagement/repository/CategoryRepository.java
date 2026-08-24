package com.rogerjaden.eventmanagement.repository;

import com.rogerjaden.eventmanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
