package com.api.project_management.repository;

import com.api.project_management.model.User;
import com.api.project_management.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {
    List<Workspace> findByUser(User user);
}
