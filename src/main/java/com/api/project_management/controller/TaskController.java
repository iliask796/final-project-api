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

@CrossOrigin
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
    public ResponseEntity<DataResponse<?>> getTasksOfTasklist(@PathVariable int id){
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

    @GetMapping("/tasks")
    public ResponseEntity<ListTaskResponse> getAllTasks() {
        List<Task> tasksToReturn;
        tasksToReturn = tasks.findAll();
        tasksToReturn.sort((t1,t2) -> t1.getPosition().compareTo(t2.getPosition()));
        ListTaskResponse response = new ListTaskResponse();
        response.set(tasksToReturn);
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
                Task taskToSwap = this.tasks.findByPositionAndTasklist(swapPos,taskToUpdate.getTasklist());
                if (taskToSwap != null) {
                    taskToSwap.setPosition(currPos);
                    this.tasks.save(taskToSwap);
                }
                taskToUpdate.setPosition(swapPos);
            }
            taskToUpdate = this.tasks.save(taskToUpdate);
        } catch (Exception e){
            ErrorResponse error = new ErrorResponse();
            error.set("Task update failed. Please check the required fields."+e);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        TaskResponse response = new TaskResponse();
        response.set(taskToUpdate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}/move/{listId}")
    public ResponseEntity<DataResponse<?>> moveTask(@PathVariable int id, @PathVariable int listId){
        Task taskToMove = this.tasks.findById(id).orElse(null);
        if (taskToMove == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Task with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            Tasklist tasklistAcquiresTask = this.tasklists.findById(listId).orElseThrow(NullPointerException::new);
            taskToMove.setTasklist(tasklistAcquiresTask);
            List<Task> targetTasklist = tasklistAcquiresTask.getTasks();
            targetTasklist.sort((t1,t2) -> t1.getPosition().compareTo(t2.getPosition()));
            int newPosition;
            if (targetTasklist.size() > 0) {
                newPosition = targetTasklist.get(targetTasklist.size()-1).getPosition() + 1;
            } else {
                newPosition = 1;
            }
            taskToMove.setPosition(newPosition);
            taskToMove = tasks.save(taskToMove);
        } catch (NullPointerException e){
            ErrorResponse error = new ErrorResponse();
            error.set("Tasklist with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        TaskResponse response = new TaskResponse();
        response.set(taskToMove);
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
