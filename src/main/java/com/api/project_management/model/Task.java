package com.api.project_management.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer position;
    private String description;
    @ManyToOne
    @JsonIncludeProperties({"tasklistId"})
    @JoinColumn(name = "tasklistId", referencedColumnName = "tasklistId")
    Tasklist tasklist;
}
