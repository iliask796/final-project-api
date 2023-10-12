package com.api.project_management.repository;

import com.api.project_management.model.Tasklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasklistRepository extends JpaRepository<Tasklist, Integer> {
    Tasklist findByPosition(Integer position);
}
