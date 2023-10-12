package com.api.project_management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "workspaces")
@JsonIgnoreProperties({"user","tasklists"})
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workspaceId;
    @Column(nullable = false)
    private String name;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate dueDate;
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;
    @OneToMany(mappedBy = "workspace", orphanRemoval = true)
    List<Tasklist> tasklists;
}
