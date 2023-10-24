package com.api.project_management.repository;

import com.api.project_management.model.Task;
import com.api.project_management.model.Tasklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findByPositionAndTasklist(int position, Tasklist tasklist);
}
