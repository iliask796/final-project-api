package com.api.project_management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tasklists")
@JsonIgnoreProperties({"workspace"})
public class Tasklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tasklistId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer position;
    @ManyToOne
    @JoinColumn(name = "workspaceId", referencedColumnName = "workspaceId")
    Workspace workspace;
}
