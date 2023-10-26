package com.api.project_management.controller;

import com.api.project_management.model.User;
import com.api.project_management.model.Workspace;
import com.api.project_management.repository.UserRepository;
import com.api.project_management.repository.WorkspaceRepository;
import com.api.project_management.utility.DataResponse;
import com.api.project_management.utility.ErrorResponse;
import com.api.project_management.utility.responses.ListWorkspaceResponse;
import com.api.project_management.utility.responses.WorkspaceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class WorkspaceController {
    @Autowired
    private WorkspaceRepository workspaces;
    @Autowired
    private UserRepository users;

    @PostMapping("/users/{id}/workspaces")
    public ResponseEntity<DataResponse<?>> createWorkspace(@PathVariable int id, @RequestBody Workspace workspace){
        Workspace workspaceToCreate;
        try {
            User userHasWorkspace = this.users.findById(id).orElseThrow(NullPointerException::new);
            workspace.setUser(userHasWorkspace);
            workspaceToCreate = this.workspaces.save(workspace);
        } catch (NullPointerException e) {
            ErrorResponse error = new ErrorResponse();
            error.set("User with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Workspace creation failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        WorkspaceResponse response = new WorkspaceResponse();
        response.set(workspaceToCreate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}/workspaces")
    public ResponseEntity<DataResponse<?>> getUserWorkspaces(@PathVariable int id){
        User userHasWorkspaces = this.users.findById(id).orElse(null);
        if (userHasWorkspaces == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        List<Workspace> workspacesOfUser = this.workspaces.findByUser(userHasWorkspaces);
        ListWorkspaceResponse response = new ListWorkspaceResponse();
        response.set(workspacesOfUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workspaces/{id}")
    public ResponseEntity<DataResponse<?>> getWorkspace(@PathVariable int id){
        Workspace workspaceToFind = this.workspaces.findById(id).orElse(null);
        if (workspaceToFind == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Workspace with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        WorkspaceResponse response = new WorkspaceResponse();
        response.set(workspaceToFind);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/workspaces/{id}")
    public ResponseEntity<DataResponse<?>> updateWorkspace(@PathVariable int id, @RequestBody Workspace workspace){
        Workspace workspaceToUpdate = this.workspaces.findById(id).orElse(null);
        if (workspaceToUpdate == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Workspace with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            workspaceToUpdate.setName(workspace.getName());
            if (workspace.getDueDate() != null){
                workspaceToUpdate.setDueDate(workspace.getDueDate());
            }
            workspaceToUpdate = this.workspaces.save(workspaceToUpdate);
        } catch (DataIntegrityViolationException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Workspace update failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        WorkspaceResponse response = new WorkspaceResponse();
        response.set(workspaceToUpdate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/workspaces/{id}")
    public ResponseEntity<DataResponse<?>> deleteWorkspace(@PathVariable int id){
        Workspace workspaceToDelete = this.workspaces.findById(id).orElse(null);
        if (workspaceToDelete == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Workspace with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.workspaces.delete(workspaceToDelete);
        WorkspaceResponse response = new WorkspaceResponse();
        response.set(workspaceToDelete);
        return ResponseEntity.ok(response);
    }
}
