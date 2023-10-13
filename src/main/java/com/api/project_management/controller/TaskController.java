package com.api.project_management.controller;

import com.api.project_management.model.Task;
import com.api.project_management.model.Tasklist;
import com.api.project_management.repository.TaskRepository;
import com.api.project_management.repository.TasklistRepository;
import com.api.project_management.utility.DataResponse;
import com.api.project_management.utility.ErrorResponse;
import com.api.project_management.utility.responses.ListTaskResponse;
import com.api.project_management.utility.responses.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository tasks;
    @Autowired
    private TasklistRepository tasklists;

    @PostMapping("/tasklists/{id}/tasks")
    public ResponseEntity<DataResponse<?>> createTask(@PathVariable int id, @RequestBody Task task){
        Task taskToCreate;
        try {
            Tasklist tasklistHasTasks = this.tasklists.findById(id).orElseThrow(NullPointerException::new);
            task.setTasklist(tasklistHasTasks);
            taskToCreate = this.tasks.save(task);
        } catch (NullPointerException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Tasklist with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Task creation failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        TaskResponse response = new TaskResponse();
        response.set(taskToCreate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/tasklists/{id}/tasks")
    public ResponseEntity<DataResponse<?>> getAllTasks(@PathVariable int id){
        List<Task> taskList;
        try {
            Tasklist tasklistHasTasks = this.tasklists.findById(id).orElseThrow(NullPointerException::new);
            taskList = tasklistHasTasks.getTasks();
            taskList.sort((t1,t2) -> t1.getPosition().compareTo(t2.getPosition()));
        } catch (NullPointerException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Tasklist with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        ListTaskResponse response = new ListTaskResponse();
        response.set(taskList);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<DataResponse<?>> updateTask(@PathVariable int id, @RequestBody Task task){
        Task taskToUpdate = this.tasks.findById(id).orElse(null);
        if (taskToUpdate == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Task with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            taskToUpdate.setName(task.getName());
            taskToUpdate.setDescription(task.getDescription());
            int currPos = taskToUpdate.getPosition();
            int swapPos = task.getPosition();
            if (currPos != swapPos) {
                Task taskToSwap = this.tasks.findByPosition(swapPos);
                if (taskToSwap != null) {
                    taskToSwap.setPosition(currPos);
                    this.tasks.save(taskToSwap);
                }
                taskToUpdate.setPosition(swapPos);
            }
            taskToUpdate = this.tasks.save(taskToUpdate);
        } catch (Exception e){
            ErrorResponse error = new ErrorResponse();
            error.set("Task update failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        TaskResponse response = new TaskResponse();
        response.set(taskToUpdate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<DataResponse<?>> deleteTask(@PathVariable int id){
        Task taskToDelete = this.tasks.findById(id).orElse(null);
        if (taskToDelete == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Task with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.tasks.delete(taskToDelete);
        TaskResponse response = new TaskResponse();
        response.set(taskToDelete);
        return ResponseEntity.ok(response);
    }
}
