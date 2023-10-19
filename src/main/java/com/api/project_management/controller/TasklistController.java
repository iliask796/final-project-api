package com.api.project_management.controller;

import com.api.project_management.model.Tasklist;
import com.api.project_management.model.Workspace;
import com.api.project_management.repository.TasklistRepository;
import com.api.project_management.repository.WorkspaceRepository;
import com.api.project_management.utility.DataResponse;
import com.api.project_management.utility.ErrorResponse;
import com.api.project_management.utility.responses.ListTasklistResponse;
import com.api.project_management.utility.responses.TasklistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class TasklistController {
    @Autowired
    TasklistRepository tasklists;
    @Autowired
    WorkspaceRepository workspaces;

    @PostMapping("/workspaces/{id}/tasklists")
    public ResponseEntity<DataResponse<?>> createTasklist(@PathVariable int id, @RequestBody Tasklist tasklist){
        Tasklist tasklistToCreate;
        try {
            Workspace workspaceHasTasklists = this.workspaces.findById(id).orElseThrow(NullPointerException::new);
            tasklist.setWorkspace(workspaceHasTasklists);
            tasklistToCreate = this.tasklists.saveAndFlush(tasklist);
        } catch (NullPointerException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Workspace with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Tasklist creation failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        TasklistResponse response = new TasklistResponse();
        response.set(tasklistToCreate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/workspaces/{id}/tasklists")
    public ResponseEntity<DataResponse<?>> getAllTasklists(@PathVariable int id){
        List<Tasklist> tasklistList;
        try {
            Workspace workspaceHasTasklists = this.workspaces.findById(id).orElseThrow(NullPointerException::new);
            tasklistList = workspaceHasTasklists.getTasklists();
            tasklistList.sort((tl1, tl2) -> tl1.getPosition().compareTo(tl2.getPosition()));
        } catch (NullPointerException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Workspace with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        ListTasklistResponse response = new ListTasklistResponse();
        response.set(tasklistList);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/tasklists/{id}")
    public ResponseEntity<DataResponse<?>> updateTasklist(@PathVariable int id, @RequestBody Tasklist tasklist){
        Tasklist tasklistToUpdate = this.tasklists.findById(id).orElse(null);
        if (tasklistToUpdate == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Tasklist with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            tasklistToUpdate.setName(tasklist.getName());
            int currPos = tasklistToUpdate.getPosition();
            int swapPos = tasklist.getPosition();
            if (currPos != swapPos){
                Tasklist tasklistToSwap = this.tasklists.findByPosition(swapPos);
                if (tasklistToSwap != null){
                    tasklistToSwap.setPosition(currPos);
                    this.tasklists.save(tasklistToSwap);
                }
                tasklistToUpdate.setPosition(swapPos);
            }
            tasklistToUpdate = this.tasklists.save(tasklistToUpdate);
        } catch (Exception e){
            ErrorResponse error = new ErrorResponse();
            error.set("Tasklist update failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        TasklistResponse response = new TasklistResponse();
        response.set(tasklistToUpdate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasklists/{id}")
    public ResponseEntity<DataResponse<?>> deleteTasklist(@PathVariable int id){
        Tasklist tasklistToDelete = this.tasklists.findById(id).orElse(null);
        if (tasklistToDelete == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Tasklist with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.tasklists.delete(tasklistToDelete);
        TasklistResponse response = new TasklistResponse();
        response.set(tasklistToDelete);
        return ResponseEntity.ok(response);
    }
}
